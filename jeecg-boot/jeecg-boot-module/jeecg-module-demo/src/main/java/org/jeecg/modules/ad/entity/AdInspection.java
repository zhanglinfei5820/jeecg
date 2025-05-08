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
 * @Description: 广告年检表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@TableName("ad_inspection")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="广告年检表")
public class AdInspection implements Serializable {
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
	/**发布明细ID*/
	@Excel(name = "发布明细ID", width = 15)
    @Schema(description = "发布明细ID")
    private java.lang.String publishDetailId;
	/**年检时间*/
	@Excel(name = "年检时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "年检时间")
    private java.util.Date inspectionTime;
	/**检查人*/
	@Excel(name = "检查人", width = 15)
    @Schema(description = "检查人")
    private java.lang.String inspector;
	/**检查结果(0不合格,1合格)*/
	@Excel(name = "检查结果(0不合格,1合格)", width = 15, dicCode = "ad_report_result")
    @Schema(description = "检查结果(0不合格,1合格)")
    @Dict(dicCode = "ad_report_result")
    private java.lang.Integer result;
	/**破损程度(0无,1轻微,2中等,3严重)*/
	@Excel(name = "破损程度(0无,1轻微,2中等,3严重)", width = 15, dicCode = "ad_damage_level")
    @Schema(description = "破损程度(0无,1轻微,2中等,3严重)")
    @Dict(dicCode = "ad_damage_level")
    private java.lang.Integer damageLevel;
	/**破损描述*/
	@Excel(name = "破损描述", width = 15)
    @Schema(description = "破损描述")
    private java.lang.String damageDesc;
	/**检查图片(多张)*/
	@Excel(name = "检查图片(多张)", width = 15)
    @Schema(description = "检查图片(多张)")
    private java.lang.String images;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private java.lang.String remark;
	/**类型*/
	@Excel(name = "类型", width = 15)
    @Schema(description = "类型")
    @Dict(dicCode = "ad_inspection_type")
    private java.lang.Integer type;
	/**车辆id*/
	@Excel(name = "车辆id", width = 15)
    @Schema(description = "车辆id")
    private java.lang.String vehicleId;
	/**车牌号*/
	@Excel(name = "车牌号", width = 15)
    @Schema(description = "车牌号")
    @TableField(exist = false)
    private java.lang.String plateNumber;
	/**司机id*/
	@Excel(name = "司机id", width = 15)
    @Schema(description = "司机id")
    private java.lang.String driveId;
	/**司机名称*/
	@Excel(name = "司机名称", width = 15)
    @Schema(description = "司机名称")
    @TableField(exist = false)
    private java.lang.String driverName;
	/**广告id*/
	@Excel(name = "广告id", width = 15)
    @Schema(description = "广告id")
    private java.lang.String adId;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @Schema(description = "状态-0待审核1已完成")
    @Dict(dicCode = "ad_inspection_status")
    private java.lang.Integer status;
}
