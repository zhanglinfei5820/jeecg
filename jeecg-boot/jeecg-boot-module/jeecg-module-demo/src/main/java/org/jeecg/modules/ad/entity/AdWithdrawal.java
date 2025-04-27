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
 * @Description: 提现申请表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@TableName("ad_withdrawal")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="提现申请表")
public class AdWithdrawal implements Serializable {
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
	/**司机ID*/
	@Excel(name = "司机ID", width = 15)
    @Schema(description = "司机ID")
    private java.lang.String driverId;
	/**提现金额*/
	@Excel(name = "提现金额", width = 15)
    @Schema(description = "提现金额")
    private java.math.BigDecimal amount;
	/**银行名称*/
	@Excel(name = "银行名称", width = 15)
    @Schema(description = "银行名称")
    private java.lang.String bankName;
	/**银行账号*/
	@Excel(name = "银行账号", width = 15)
    @Schema(description = "银行账号")
    private java.lang.String bankAccount;
	/**开户名*/
	@Excel(name = "开户名", width = 15)
    @Schema(description = "开户名")
    private java.lang.String accountName;
	/**状态(0待处理,1处理中,2已完成,3已拒绝)*/
	@Excel(name = "状态(0待处理,1处理中,2已完成,3已拒绝)", width = 15)
    @Schema(description = "状态(0待处理,1处理中,2已完成,3已拒绝)")
    @Dict(dicCode = "ad_withdrawal_status")
    private java.lang.Integer status;
	/**审核时间*/
	@Excel(name = "审核时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "审核时间")
    private java.util.Date auditTime;
	/**审核人*/
	@Excel(name = "审核人", width = 15)
    @Schema(description = "审核人")
    private java.lang.String auditBy;
	/**审核备注*/
	@Excel(name = "审核备注", width = 15)
    @Schema(description = "审核备注")
    private java.lang.String auditRemark;
}
