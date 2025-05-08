package org.jeecg.modules.ad.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Description: 广告发布明细表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@TableName("ad_publish_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="广告发布明细表")
public class AdPublishDetail implements Serializable {
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
	/**发布ID*/
	@Excel(name = "发布ID", width = 15)
    @Schema(description = "发布ID")
    private java.lang.String publishId;
	/**车辆ID*/
	@Excel(name = "车辆ID", width = 15)
    @Schema(description = "车辆ID")
    private java.lang.String vehicleId;
	/**司机ID*/
	@Excel(name = "司机ID", width = 15)
    @Schema(description = "司机ID")
    private java.lang.String driverId;
	/**司机名称*/
	@Excel(name = "司机名称", width = 15)
    @Schema(description = "司机名称")
    private java.lang.String driverName;
	/**广告位置(左窗/右窗/后窗等)*/
	@Excel(name = "广告位置(左窗/右窗/后窗等)", width = 15)
    @Schema(description = "广告位置(左窗/右窗/后窗等)")
    @Dict(dicCode = "ad_position")
    private java.lang.String position;
	/**安装图片*/
	@Excel(name = "安装图片", width = 15)
    @Schema(description = "安装图片")
    private java.lang.String images;
	/**广告标题*/
	@Excel(name = "广告标题", width = 15)
    @Schema(description = "广告标题")
    private java.lang.String name;
	/**单价(元/天)*/
	@Excel(name = "单价(元/天)", width = 15)
    @Schema(description = "单价(元/天)")
    private java.math.BigDecimal price;
	/**状态(0禁用,1启用)*/
	@Excel(name = "状态(0禁用,1启用)", width = 15)
    @Schema(description = "状态(0禁用,1启用)")
    @Dict(dicCode = "ad_publish_detail_status")
    private java.lang.Integer status;
	/**公司id*/
	@Excel(name = "公司id", width = 15)
    @Schema(description = "公司id")
    private java.lang.String companyId;
	/**公司名称*/
	@Excel(name = "公司名称", width = 15)
    @Schema(description = "公司名称")
    private java.lang.String companyName;
	/**车牌号*/
	@Excel(name = "车牌号", width = 15)
    @Schema(description = "车牌号")
    @TableField(exist = false)
    private java.lang.String plateNumber;
	/**类型（0公司1司机）*/
	@Excel(name = "类型（0公司1司机）", width = 15)
    @Schema(description = "类型（0公司1司机）")
    @Dict(dicCode = "ad_publish_detail_type")
    private java.lang.Integer type;
	/**抽成*/
	@Excel(name = "抽成", width = 15)
    @Schema(description = "抽成")
    private java.math.BigDecimal percentage;
    
    /**司机数*/
    @Excel(name = "司机数", width = 15)
    @Schema(description = "司机数")
    private java.lang.Integer drivers;
    
    /**实际司机数*/
    @Excel(name = "实际司机数", width = 15)
    @Schema(description = "实际司机数")
    private java.lang.Integer actualDrivers;
}
