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
 * @Description: 流水记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@TableName("ad_transaction")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="流水记录表")
public class AdTransaction implements Serializable {
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
	/**抽成*/
	@Excel(name = "抽成", width = 15)
    @Schema(description = "抽成")
    private BigDecimal percentage;
	/**金额*/
	@Excel(name = "金额", width = 15)
    @Schema(description = "金额")
    private java.math.BigDecimal amount;
	/**类型(1收入,2支出)*/
	@Excel(name = "类型(1收入,2支出)", width = 15)
    @Schema(description = "类型(1收入,2支出)")
    @Dict(dicCode = "ad_stream_type")
    private java.lang.Integer type;
	/**交易类型(1广告收益,2维护金,3提现)*/
	@Excel(name = "交易类型(1广告收益,2维护金,3提现)", width = 15)
    @Schema(description = "交易类型(1广告收益,2维护金,3提现)")
    @Dict(dicCode = "ad_transaction_type")
    private java.lang.Integer transactionType;
	/**关联ID(发布明细ID/提现ID等)*/
	@Excel(name = "关联ID(发布明细ID/提现ID等)", width = 15)
    @Schema(description = "关联ID(发布明细ID/提现ID等)")
    private java.lang.String relatedId;
	/**交易后余额*/
	@Excel(name = "交易后余额", width = 15)
    @Schema(description = "交易后余额")
    private java.math.BigDecimal balance;
	/**状态(0失败,1成功)*/
	@Excel(name = "状态(0失败,1成功)", width = 15)
    @Schema(description = "状态(0失败,1成功)")
    @Dict(dicCode = "ad_s_status")
    private java.lang.Integer status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private java.lang.String remark;
	/**上报id*/
	@Excel(name = "上报id", width = 15)
    @Schema(description = "上报id")
    private java.lang.String reportId;
}
