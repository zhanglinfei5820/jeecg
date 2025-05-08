package org.jeecg.modules.ad.entity.Vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 司机提交年检信息VO
 * @Author: jeecg-boot
 * @Date:   2025-04-15
 * @Version: V1.0
 */
@Data
public class DriverInspectionAddVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**司机ID*/
    private String relatedId;
    
    /**车牌号*/
    private String plateNumber;
    
    /**广告ID*/
    private String adId;
    
    /**类型*/
    @Dict(dicCode = "ad_inspection_type")
    private Integer type;
    
    /**破损描述*/
    private String damageDesc;
    
    /**检查图片(多张)*/
    private String images;

    private String inspectionTime;
}