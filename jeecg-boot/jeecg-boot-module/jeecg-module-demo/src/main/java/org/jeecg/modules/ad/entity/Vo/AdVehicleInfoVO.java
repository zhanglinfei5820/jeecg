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
 * @Description: 小程序车辆信息VO
 * @Author: jeecg-boot
 * @Date: 2025-04-15
 * @Version: V1.0
 */
@Data
@Schema(description="小程序车辆信息VO")
public class AdVehicleInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**车辆ID*/
    @Schema(description = "车辆ID")
    private String id;
    
    /**车牌号码*/
    @Schema(description = "车牌号码")
    private String plateNumber;
    
    /**车辆类型*/
    @Schema(description = "车辆类型")
    @Dict(dicCode = "ad_vehicle_type")
    private Integer vehicleType;
    
    /**车辆状态*/
    @Schema(description = "车辆状态")
    @Dict(dicCode = "ad_vehicle_status")
    private Integer status;
    
    /**到期时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "到期时间")
    private Date expireTime;
    
    /**品牌*/
    @Schema(description = "品牌")
    private String brand;
    
    /**安装时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "安装时间")
    private Date installationTime;
    
    /**安装地点*/
    @Schema(description = "安装地点")
    private String installationAddress;
    
    /**广告名称*/
    @Schema(description = "广告名称")
    private String adName;
    
    /**广告商名称*/
    @Schema(description = "广告商名称")
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
    
    /**单价*/
    @Schema(description = "单价")
    private BigDecimal price;
    
    /**车辆图片*/
    @Schema(description = "车辆图片")
    private String vehicleImage;
    
    /**是否到期*/
    @Schema(description = "是否到期")
    private Boolean isExpired;
} 