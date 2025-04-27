package org.jeecg.modules.ad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.ad.entity.AdPublish;
import org.jeecg.modules.ad.entity.AdPublishDetail;
import org.jeecg.modules.ad.mapper.AdPublishDetailMapper;
import org.jeecg.modules.ad.service.IAdPublishDetailService;
import org.jeecg.modules.ad.service.IAdPublishService;
import org.jeecg.modules.ad.util.AdPublishDetailValidator;
import org.jeecg.modules.ad.utils.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description: 广告发布明细表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Slf4j
@Service
public class AdPublishDetailServiceImpl extends ServiceImpl<AdPublishDetailMapper, AdPublishDetail> implements IAdPublishDetailService {
    @Autowired
    private IAdPublishService adPublishService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String distribute(AdPublishDetail adPublishDetail) {
        String publishId = adPublishDetail.getPublishId();
        try {
            // 0. 检查明细状态
            if (adPublishDetail.getStatus() != 0) {
                log.error("当前明细状态不是待审核，publishDetailId: {}, 当前状态: {}", 
                        adPublishDetail.getId(), adPublishDetail.getStatus());
                throw new RuntimeException("当前明细状态不是待审核");
            }

            // 1. 参数校验
            AdPublishDetailValidator.validateDistribute(adPublishDetail);
            log.info("开始分发广告，publishId: {}", publishId);

            // 2. 获取发布广告信息
            AdPublish adPublish = getAdPublish(publishId);
            log.info("获取发布广告信息成功，publishId: {}, 总司机数: {}", publishId, adPublish.getDrivers());

            // 3. 获取已分发司机总数
            int totalDistributed = getTotalDistributedDrivers(publishId);
            log.info("当前已分发司机数: {}, publishId: {}", totalDistributed, publishId);

            // 4. 检查是否超过总司机数
            validateTotalDrivers(adPublish, totalDistributed);

            // 5. 计算并设置实际分发数量
            int actualDrivers = calculateActualDrivers(adPublish.getDrivers(), totalDistributed, adPublishDetail.getDrivers());
            adPublishDetail.setActualDrivers(actualDrivers);
            adPublishDetail.setStatus(1);
            log.info("计算实际分发数量: {}, publishId: {}", actualDrivers, publishId);

            // 6. 更新分发记录
            updateDistributeRecord(adPublishDetail);
            log.info("更新分发记录成功，publishDetailId: {}", adPublishDetail.getId());

            // 7. 处理满员情况
            if (isFull(totalDistributed, actualDrivers, adPublish.getDrivers())) {
                log.info("广告已满员，开始更新其他未分发记录状态，publishId: {}", publishId);
                updateRemainingRecords(publishId);
            }

            // 8. 记录分发结果
            logDistributeResult(adPublishDetail, actualDrivers, totalDistributed);

            return "分发成功！已分发总数：" + (totalDistributed + actualDrivers);

        } catch (JeecgBootException e) {
            log.error("参数校验失败，publishId: " + publishId, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("分发广告失败，publishId: " + publishId, e);
            throw e;
        } catch (Exception e) {
            log.error("分发广告发生未知异常，publishId: " + publishId, e);
            throw new RuntimeException("分发广告失败：" + e.getMessage());
        }
    }

    private AdPublish getAdPublish(String publishId) {
        AdPublish adPublish = adPublishService.getById(publishId);
        if (adPublish == null) {
            log.error("未找到对应的发布广告信息，publishId: {}", publishId);
            throw new RuntimeException("未找到对应的发布广告信息");
        }
        
        // 判断广告状态是否为发布状态
        if (adPublish.getStatus() != 1) {
            log.error("广告状态不是发布状态，publishId: {}, 当前状态: {}", publishId, adPublish.getStatus());
            throw new RuntimeException("广告状态不是发布状态，请认真核实！");
        }
        
        return adPublish;
    }

    private int getTotalDistributedDrivers(String publishId) {
        QueryWrapper<AdPublishDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("SUM(actual_drivers) as total")
                .eq("publish_id", publishId)
                .eq("status", 1);
        Map<String, Object> result = getMap(queryWrapper);
        return result != null && result.get("total") != null ?
                ((Number)result.get("total")).intValue() : 0;
    }

    private void validateTotalDrivers(AdPublish adPublish, int totalDistributed) {
        if (totalDistributed >= adPublish.getDrivers()) {
            log.info("广告司机数已达标，publishId: {}, 已分发: {}, 总数: {}",
                    adPublish.getId(), totalDistributed, adPublish.getDrivers());
            throw new RuntimeException("广告司机数已达标，不能再分发");
        }
    }

    private int calculateActualDrivers(int totalDrivers, int totalDistributed, int currentDrivers) {
        int remainingDrivers = totalDrivers - totalDistributed;
        return Math.min(currentDrivers, remainingDrivers);
    }

    private void updateDistributeRecord(AdPublishDetail adPublishDetail) {
        boolean updateResult = updateById(adPublishDetail);
        if (!updateResult) {
            log.error("更新分发记录失败，publishDetailId: {}", adPublishDetail.getId());
            throw new RuntimeException("分发失败，请重试");
        }
    }

    private boolean isFull(int totalDistributed, int actualDrivers, int totalDrivers) {
        return totalDistributed + actualDrivers >= totalDrivers;
    }

    private void updateRemainingRecords(String publishId) {
        try {
            // 1. 先查询需要更新的记录数量
            QueryWrapper<AdPublishDetail> countWrapper = new QueryWrapper<>();
            countWrapper.eq("publish_id", publishId)
                    .eq("status", 0);
            long count = count(countWrapper);
            
            if (count == 0) {
                log.info("没有需要更新的未分发记录，publishId: {}", publishId);
                return;
            }
            
            log.info("开始批量更新未分发记录状态，publishId: {}, 待更新数量: {}", publishId, count);
            
            // 2. 执行批量更新
            QueryWrapper<AdPublishDetail> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq("publish_id", publishId)
                    .eq("status", 0);
            
            AdPublishDetail updateEntity = new AdPublishDetail();
            updateEntity.setStatus(4);
            
            boolean batchUpdateResult = update(updateEntity, updateWrapper);
            
            if (!batchUpdateResult) {
                // 3. 如果批量更新失败，尝试单条更新
                log.warn("批量更新失败，尝试单条更新，publishId: {}", publishId);
                List<AdPublishDetail> toUpdateList = list(updateWrapper);
                int successCount = 0;
                
                for (AdPublishDetail detail : toUpdateList) {
                    try {
                        detail.setStatus(4);
                        if (updateById(detail)) {
                            successCount++;
                        } else {
                            log.error("单条更新失败，publishDetailId: {}", detail.getId());
                        }
                    } catch (Exception e) {
                        log.error("单条更新异常，publishDetailId: " + detail.getId(), e);
                    }
                }
                
                if (successCount == 0) {
                    throw new RuntimeException("所有更新操作都失败，publishId: " + publishId);
                }
                
                log.info("单条更新完成，publishId: {}, 成功更新数量: {}", publishId, successCount);
            } else {
                log.info("批量更新成功，publishId: {}, 更新数量: {}", publishId, count);
            }

            // 4. 更新发布广告信息状态为已满员
            AdPublish adPublish = new AdPublish();
            adPublish.setId(publishId);
            adPublish.setStatus(4);
            boolean updatePublishResult = adPublishService.updateById(adPublish);
            if (!updatePublishResult) {
                log.error("更新发布广告信息状态失败，publishId: {}", publishId);
                throw new RuntimeException("更新发布广告信息状态失败");
            }
            log.info("更新发布广告信息状态成功，publishId: {}, 新状态: 4", publishId);
            
        } catch (Exception e) {
            log.error("更新未分发记录状态失败，publishId: " + publishId, e);
            throw new RuntimeException("更新未分发记录状态失败: " + e.getMessage());
        }
    }

    private void logDistributeResult(AdPublishDetail adPublishDetail, int actualDrivers, int totalDistributed) {
        log.info("分发成功，publishId: {}, 本次分发: {}, 已分发总数: {}",
                adPublishDetail.getPublishId(), actualDrivers, totalDistributed + actualDrivers);
    }
}
