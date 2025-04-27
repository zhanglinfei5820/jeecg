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
 * @Description: 广告分类表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@TableName("ad_category")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="广告分类表")
public class AdCategory implements Serializable {
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
	/**分类名称*/
	@Excel(name = "分类名称", width = 15)
    @Schema(description = "分类名称")
    private java.lang.String name;
	/**分类编码*/
	@Excel(name = "分类编码", width = 15)
    @Schema(description = "分类编码")
    private java.lang.String code;
	/**排序值*/
	@Excel(name = "排序值", width = 15)
    @Schema(description = "排序值")
    private java.lang.Integer sort;
	/**状态(0禁用,1启用)*/
	@Excel(name = "状态(0禁用,1启用)", width = 15)
    @Schema(description = "状态(0禁用,1启用)")
    @Dict(dicCode = "ad_status")
    private java.lang.Integer status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @Schema(description = "备注")
    private java.lang.String remark;
}
