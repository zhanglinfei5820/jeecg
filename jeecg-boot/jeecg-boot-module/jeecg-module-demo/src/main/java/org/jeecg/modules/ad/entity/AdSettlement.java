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
 * @Description: 结算表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@TableName("ad_settlement")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="结算表")
public class AdSettlement implements Serializable {
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
	/**商户ID*/
	@Excel(name = "商户ID", width = 15)
    @Schema(description = "商户ID")
    private java.lang.String merchantId;
	/**结算月份(YYYY-MM)*/
	@Excel(name = "结算月份(YYYY-MM)", width = 15)
    @Schema(description = "结算月份(YYYY-MM)")
    private java.lang.String month;
	/**广告数量*/
	@Excel(name = "广告数量", width = 15)
    @Schema(description = "广告数量")
    private java.lang.Integer adCount;
	/**总金额*/
	@Excel(name = "总金额", width = 15)
    @Schema(description = "总金额")
    private java.math.BigDecimal totalAmount;
	/**已付金额*/
	@Excel(name = "已付金额", width = 15)
    @Schema(description = "已付金额")
    private java.math.BigDecimal paidAmount;
	/**未付金额*/
	@Excel(name = "未付金额", width = 15)
    @Schema(description = "未付金额")
    private java.math.BigDecimal unpaidAmount;
	/**状态(0未结算,1部分结算,2已结算)*/
	@Excel(name = "状态(0未结算,1部分结算,2已结算)", width = 15)
    @Schema(description = "状态(0未结算,1部分结算,2已结算)")
    @Dict(dicCode = "ad_settlement_status")
    private java.lang.Integer status;
	/**结算时间*/
	@Excel(name = "结算时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结算时间")
    private java.util.Date settlementTime;
	/**结算人*/
	@Excel(name = "结算人", width = 15)
    @Schema(description = "结算人")
    private java.lang.String settlementBy;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private java.lang.String remark;
}
