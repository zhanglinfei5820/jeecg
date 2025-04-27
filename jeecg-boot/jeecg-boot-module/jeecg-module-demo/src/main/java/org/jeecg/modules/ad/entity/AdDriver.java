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
 * @Description: 司机表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@TableName("ad_driver")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="司机表")
public class AdDriver implements Serializable {
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
	/**司机姓名*/
	@Excel(name = "司机姓名", width = 15)
    @Schema(description = "司机姓名")
    private java.lang.String name;
	/**手机号码*/
	@Excel(name = "手机号码", width = 15)
    @Schema(description = "手机号码")
    private java.lang.String phone;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
    @Schema(description = "身份证号")
    private java.lang.String idCard;
	/**身份证正面照*/
	@Excel(name = "身份证正面照", width = 15)
    @Schema(description = "身份证正面照")
    private java.lang.String idCardFront;
	/**身份证反面照*/
	@Excel(name = "身份证反面照", width = 15)
    @Schema(description = "身份证反面照")
    private java.lang.String idCardBack;
	/**驾驶证号*/
	@Excel(name = "驾驶证号", width = 15)
    @Schema(description = "驾驶证号")
    private java.lang.String driverLicense;
	/**驾驶证图片*/
	@Excel(name = "驾驶证图片", width = 15)
    @Schema(description = "驾驶证图片")
    private java.lang.String licenseImage;
	/**状态(0禁用,1启用)*/
	@Excel(name = "状态", width = 15, dicCode = "ad_status")
	@Dict(dicCode = "ad_status")
    @Schema(description = "状态(0禁用,1启用)")
    private java.lang.Integer status;
	/**账户余额*/
	@Excel(name = "账户余额", width = 15)
    @Schema(description = "账户余额")
    private java.math.BigDecimal balance;
	/**累计收入*/
	@Excel(name = "累计收入", width = 15)
    @Schema(description = "累计收入")
    private java.math.BigDecimal totalIncome;
	/**评分(1-5)*/
	@Excel(name = "评分", width = 15, dicCode = "driver_rating")
	@Dict(dicCode = "driver_rating")
    @Schema(description = "评分(1-5)")
    private java.lang.Integer rating;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private java.lang.String remark;
}
