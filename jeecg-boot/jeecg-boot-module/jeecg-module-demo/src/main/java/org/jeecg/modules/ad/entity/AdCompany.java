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
 * @Description: 广告公司
 * @Author: jeecg-boot
 * @Date:   2025-04-26
 * @Version: V1.0
 */
@Data
@TableName("ad_company")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="广告公司")
public class AdCompany implements Serializable {
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
	/**广告公司名称*/
	@Excel(name = "广告公司名称", width = 15)
    @Schema(description = "广告公司名称")
    private String name;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @Schema(description = "联系人")
    private String contact;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @Schema(description = "联系电话")
    private String phone;
	/**公司地址*/
	@Excel(name = "公司地址", width = 15)
    @Schema(description = "公司地址")
    private String address;
	/**营业执照号*/
	@Excel(name = "营业执照号", width = 15)
    @Schema(description = "营业执照号")
    private String businessLicense;
	/**营业执照图片*/
	@Excel(name = "营业执照图片", width = 15)
    @Schema(description = "营业执照图片")
    private String licenseImage;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @Schema(description = "状态")
    private Integer status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private String remark;
}
