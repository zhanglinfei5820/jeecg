package org.jeecg.modules.ad.service;

import org.jeecg.modules.ad.entity.AdVehicle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 车辆表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
public interface IAdVehicleService extends IService<AdVehicle> {
    Map<String, Object> getVehicleStatistics();

    List<?> queryVxList(AdVehicle adVehicle);
}
