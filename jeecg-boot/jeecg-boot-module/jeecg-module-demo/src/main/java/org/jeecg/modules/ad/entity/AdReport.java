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
 * @Description: 广告上报记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@TableName("ad_report")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="广告上报记录表")
public class AdReport implements Serializable {
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
	/**司机ID*/
	@Excel(name = "司机ID", width = 15)
    @Schema(description = "司机ID")
    private java.lang.String driverId;
	/**上报时间*/
	@Excel(name = "上报时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "上报时间")
    private java.util.Date reportTime;
	/**上报类型(1日常上报,2问题上报)*/
	@Excel(name = "上报类型(1日常上报,2问题上报)", width = 15)
    @Schema(description = "上报类型(1日常上报,2问题上报)")
    @Dict(dicCode = "ad_report_type")
    private java.lang.Integer reportType;
	/**状态(0待处理,1已处理)*/
	@Excel(name = "状态(0待处理,1已处理)", width = 15)
    @Schema(description = "状态(0待处理,1已处理)")
    @Dict(dicCode = "ad_report_process_status")
    private java.lang.Integer status;
	/**上报内容*/
	@Excel(name = "上报内容", width = 15)
    @Schema(description = "上报内容")
    private java.lang.String content;
	/**上报图片(多张)*/
	@Excel(name = "上报图片(多张)", width = 15)
    @Schema(description = "上报图片(多张)")
    private java.lang.String images;
	/**处理结果*/
	@Excel(name = "处理结果", width = 15)
    @Schema(description = "处理结果")
    private java.lang.String processResult;
	/**处理时间*/
	@Excel(name = "处理时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "处理时间")
    private java.util.Date processTime;
	/**处理人*/
	@Excel(name = "处理人", width = 15)
    @Schema(description = "处理人")
    private java.lang.String processBy;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private java.lang.String remark;
}
