package org.jeecg.modules.ad.entity.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 司机查询广告年检VO
 * @Author: jeecg-boot
 * @Date:   2025-04-15
 * @Version: V1.0
 */
@Data
public class DriverInspectionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    private String id;
    
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**年检时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date inspectionTime;
    
    /**检查人*/
    private String inspector;
    
    /**检查结果(0不合格,1合格)*/
    @Dict(dicCode = "ad_report_result")
    private Integer result;
    
    /**破损程度(0无,1轻微,2中等,3严重)*/
    @Dict(dicCode = "ad_damage_level")
    private Integer damageLevel;
    
    /**破损描述*/
    private String damageDesc;
    
    /**检查图片(多张)*/
    private String images;
    
    /**备注*/
    private String remark;
    
    /**类型*/
    @Dict(dicCode = "ad_inspection_type")
    private String type;
    
    /**车辆id*/
    private String vehicleId;
    
    /**车牌号*/
    private String plateNumber;
    
    /**状态-0待审核1已完成*/
    private Integer status;
    
    /**广告名称*/
    private String adName;

    /**车子图片*/
    private String vehicleImage;
}