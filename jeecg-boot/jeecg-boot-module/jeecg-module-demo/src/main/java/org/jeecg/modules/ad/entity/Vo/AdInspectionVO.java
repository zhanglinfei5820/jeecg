package org.jeecg.modules.ad.entity.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 广告年检表VO
 */
@Data
public class AdInspectionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    private String id;
    
    /**车辆类型*/
    private Integer vehicleType;
    
    /**车牌号码*/
    private String plateNumber;
    
    /**品牌*/
    private String brand;
    
    /**型号*/
    private String model;
    
    /**所属公司名称*/
    private String companyName;
    
    /**广告名称*/
    private String adName;
    
    /**广告投放结束时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date launchEndTime;
    
    /**车辆状态*/
    private Integer status;
} 