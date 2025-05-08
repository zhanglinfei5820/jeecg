package org.jeecg.modules.ad.entity.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 司机查询广告VO
 * @Author: jeecg-boot
 * @Date:   2025-04-15
 * @Version: V1.0
 */
@Data
public class DriverAdVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**广告ID*/
    private String id;
    
    /**广告名称*/
    private String name;
    
    /**物料名称*/
    private String materialName;
    
    /**商户名称*/
    private String merchantName;
    
    /**投放开始时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date launchStartTime;
    
    /**投放结束时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date launchEndTime;
    
    /**安装时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date installationTime;
    
    /**安装地址*/
    private String installationAddress;
    
    /**状态(0待审核,1已发布,2已结束,3已取消)*/
    @Dict(dicCode = "ad_publish_status")
    private Integer status;
    
    /**车牌号*/
    private String plateNumber;
    
    /**广告位置(左窗/右窗/后窗等)*/
    @Dict(dicCode = "ad_position")
    private String position;
    
    /**单价*/
    private BigDecimal price;
    
    /**广告图片*/
    private String materialImage;
} 