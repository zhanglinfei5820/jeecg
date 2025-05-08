package org.jeecg.modules.ad.entity.Vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: 广告上报任务VO
 * @Author: jeecg-boot
 * @Date: 2025-04-15
 * @Version: V1.0
 */
@Data
@Schema(description="广告上报任务VO")
public class AdReportTaskVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**ID*/
    @Schema(description = "ID")
    private String id;
    
    /**商户名称*/
    @Schema(description = "商户名称")
    private String merchantName;
    
    /**广告投放开始时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "广告投放开始时间")
    private Date launchStartTime;
    
    /**广告投放结束时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "广告投放结束时间")
    private Date launchEndTime;
    
    /**上次上报时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "上次上报时间")
    private Date lastReportTime;
    
    /**上报次数*/
    @Schema(description = "上报次数")
    private Integer maintenanceCount;

    /**上报次数*/
    @Schema(description = "上报次数")
    private Integer rmaintenanceCount;

    /**上报时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "上报时间")
    private Date reportTime;
    
    /**状态*/
    @Schema(description = "状态")
    @Dict(dicCode = "ad_report_process_status")
    private Integer status;

    /**车辆状态*/
    @Schema(description = "车辆状态")
    private Integer vehicleStatus;

    /**单价*/
    @Schema(description = "单价")
    private BigDecimal price;

    /**安装地点*/
    @Schema(description = "安装地点")
    private String installationAddress;
}