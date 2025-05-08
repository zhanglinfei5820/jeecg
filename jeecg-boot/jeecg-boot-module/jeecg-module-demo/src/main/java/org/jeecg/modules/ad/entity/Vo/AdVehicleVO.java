package org.jeecg.modules.ad.entity.Vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 车辆表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="车辆表")
public class AdVehicleVO implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
    @Schema(description = "主键")
    private String id;
	/**创建人*/
    @Schema(description = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private String sysOrgCode;
    /**安装时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "安装时间")
    private Date installationTime;
	/**广告ID*/
	@Excel(name = "广告ID", width = 60)
    @Schema(description = "广告ID")
    private String adId;
	/**广告名称*/
	@Excel(name = "广告名称", width = 60)
    @Schema(description = "广告名称")
    private String adName;
	/**所属公司ID*/
	@Excel(name = "所属公司ID", width = 60)
    @Schema(description = "所属公司ID")
    private String companyId;
	/**所属公司名称*/
	@Excel(name = "所属公司名称", width = 60)
    @Schema(description = "所属公司名称")
    private String companyName;
	/**所属司机ID*/
	@Excel(name = "所属司机ID", width = 60)
    @Schema(description = "所属司机ID")
    private String driverId;
	/**所属司机名称*/
	@Excel(name = "所属司机名称", width = 60)
    @Schema(description = "所属司机名称")
    private String driverName;
	/**车牌号码*/
	@Excel(name = "车牌号码", width = 15)
    @Schema(description = "车牌号码")
    private String plateNumber;
	/**车辆类型*/
	@Excel(name = "车辆类型", width = 15)
    @Schema(description = "车辆类型")
    @Dict(dicCode = "ad_vehicle_type")
    private Integer vehicleType;
	/**品牌*/
	@Excel(name = "品牌", width = 15)
    @Schema(description = "品牌")
    private String brand;
	/**型号*/
	@Excel(name = "型号", width = 15)
    @Schema(description = "型号")
    private String model;
	/**颜色*/
	@Excel(name = "颜色", width = 15)
    @Schema(description = "颜色")
    private String color;
	/**行驶证号*/
	@Excel(name = "行驶证号", width = 15)
    @Schema(description = "行驶证号")
    private String vehicleLicense;
	/**行驶证图片*/
	@Excel(name = "行驶证图片", width = 15)
    @Schema(description = "行驶证图片")
    private String licenseImage;
	/**状态(0禁用,1启用)*/
	@Excel(name = "状态", width = 15)
    @Schema(description = "状态")
    @Dict(dicCode = "ad_vehicle_status")
    private Integer status;
	/**车窗可用面积(m²)*/
	@Excel(name = "车窗可用面积(m²)", width = 15)
    @Schema(description = "车窗可用面积(m²)")
    private BigDecimal windowArea;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private String remark;
	/**安装图片*/
	@Excel(name = "安装图片", width = 15)
    @Schema(description = "安装图片")
    private String installationImage;
	/**维护金次数*/
	@Excel(name = "维护金次数", width = 15)
    @Schema(description = "维护金次数")
    private Integer maintenanceCount;
    /**到期日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "到期日期")
    private Date expireTime;
    /**查询条件*/
    private String keyword;
    /**车辆图片*/
    @Excel(name = "车辆图片", width = 15)
    @Schema(description = "车辆图片")
    private java.lang.String vehicleImage;
    /**排序*/
    private java.lang.String orderBy;
    /**手机号*/
    private java.lang.String phone;
}
