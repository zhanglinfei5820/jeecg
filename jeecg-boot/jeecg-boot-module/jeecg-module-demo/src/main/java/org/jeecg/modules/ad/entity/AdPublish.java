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
import javax.validation.constraints.Min;

/**
 * @Description: 广告发布表
 * @Author: jeecg-boot
 * @Date:   2025-04-15
 * @Version: V1.0
 */
@Data
@TableName("ad_publish")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="广告发布表")
public class AdPublish implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
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
	/**商户ID*/
	@Excel(name = "商户名称", width = 15, dictTable = "ad_merchant", dicText = "name", dicCode = "id")
	@Dict(dictTable = "ad_merchant", dicText = "name", dicCode = "id")
    @Schema(description = "商户ID")
    private String merchantId;
	/**物料ID*/
	@Excel(name = "物料名称", width = 15, dictTable = "ad_material", dicText = "name", dicCode = "id")
	@Dict(dictTable = "ad_material", dicText = "name", dicCode = "id")
    @Schema(description = "物料ID")
    private String materialId;
	/**广告标题*/
	@Excel(name = "广告标题", width = 15)
    @Schema(description = "广告标题")
    private String name;
	/**开始报名时间*/
	@Excel(name = "开始报名时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始报名时间")
    private Date startTime;
	/**报名结束时间*/
	@Excel(name = "报名结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "报名结束时间")
    private Date endTime;
	/**投放开始时间*/
	@Excel(name = "投放开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "投放开始时间")
    private Date launchStartTime;
	/**投放结束时间*/
	@Excel(name = "投放结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "投放结束时间")
    private Date launchEndTime;
	/**预算金额*/
	@Excel(name = "预算金额", width = 15)
    @Schema(description = "预算金额")
    @Min(value = 0, message = "预算金额不能为负数")
    private BigDecimal budget;
	/**状态(0待审核,1已发布,2已结束,3已取消)*/
	@Excel(name = "状态", width = 15, dicCode = "ad_publish_status")
	@Dict(dicCode = "ad_publish_status")
    @Schema(description = "状态(0待审核,1已发布,2已结束,3已取消4已满员)")
    private Integer status;
	/**安装时间*/
	@Excel(name = "安装时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "安装时间")
    private Date installationTime;
	/**安装地址*/
	@Excel(name = "安装地址", width = 15)
    @Schema(description = "安装地址")
    private String installationAddress;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private String remark;
	/**广告位置(左窗/右窗/后窗等)*/
	@Excel(name = "广告位置", width = 15, dicCode = "ad_position")
	@Dict(dicCode = "ad_position")
    @Schema(description = "广告位置(左窗/右窗/后窗等)")
    private String position;
	/**司机数*/
	@Excel(name = "司机数", width = 15)
    @Schema(description = "司机数")
    @Min(value = 0, message = "司机数不能为负数")
    private Integer drivers;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @Schema(description = "单价")
    @Min(value = 0, message = "单价不能为负数")
    private BigDecimal price;
	/**实际司机数*/
	@Excel(name = "实际司机数", width = 15)
    @Schema(description = "实际司机数")
    private BigDecimal actualDrivers;
}
