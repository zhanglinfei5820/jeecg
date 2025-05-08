package org.jeecg.modules.ad.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 车辆表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@TableName("ad_vehicle")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="车辆表")
public class AdVehicle implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private java.lang.String id;
	/**创建人*/
    @Schema(description = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private java.lang.String sysOrgCode;
    /**安装时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "安装时间")
    private java.util.Date installationTime;
	/**广告ID*/
	@Excel(name = "广告ID", width = 60)
    @Schema(description = "广告ID")
    private java.lang.String adId;
	/**所属公司ID*/
	@Excel(name = "所属公司ID", width = 60)
    @Schema(description = "所属公司ID")
    private java.lang.String companyId;
	/**所属司机ID*/
	@Excel(name = "所属司机ID", width = 60)
    @Schema(description = "所属司机ID")
    private java.lang.String driverId;
	/**车牌号码*/
	@Excel(name = "车牌号码", width = 15)
    @Schema(description = "车牌号码")
    private java.lang.String plateNumber;
	/**车辆类型*/
	@Excel(name = "车辆类型", width = 15)
    @Schema(description = "车辆类型")
    @Dict(dicCode = "ad_vehicle_type")
    private java.lang.Integer vehicleType;
	/**品牌*/
	@Excel(name = "品牌", width = 15)
    @Schema(description = "品牌")
    private java.lang.String brand;
	/**型号*/
	@Excel(name = "型号", width = 15)
    @Schema(description = "型号")
    private java.lang.String model;
	/**颜色*/
	@Excel(name = "颜色", width = 15)
    @Schema(description = "颜色")
    private java.lang.String color;
	/**行驶证号*/
	@Excel(name = "行驶证号", width = 15)
    @Schema(description = "行驶证号")
    private java.lang.String vehicleLicense;
	/**行驶证图片*/
	@Excel(name = "行驶证图片", width = 15)
    @Schema(description = "行驶证图片")
    private java.lang.String licenseImage;
	/**状态(0禁用,1启用)*/
	@Excel(name = "状态", width = 15)
    @Schema(description = "状态-0启用,1已安装2禁止3已分配4待安装")
    @Dict(dicCode = "ad_vehicle_status")
    private java.lang.Integer status;
	/**车窗可用面积(m²)*/
	@Excel(name = "车窗可用面积(m²)", width = 15)
    @Schema(description = "车窗可用面积(m²)")
    private java.math.BigDecimal windowArea;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private java.lang.String remark;
	/**安装图片*/
	@Excel(name = "安装图片", width = 15)
    @Schema(description = "安装图片")
    private java.lang.String installationImage;
	/**维护金次数*/
	@Excel(name = "维护金次数", width = 15)
    @Schema(description = "维护金次数")
    private java.lang.Integer maintenanceCount;
    /**到期日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "到期日期")
    private java.util.Date expireTime;
	/**车辆图片*/
	@Excel(name = "车辆图片", width = 15)
    @Schema(description = "车辆图片")
    private java.lang.String vehicleImage;
}
