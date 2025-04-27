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
import javax.persistence.Column;

/**
 * @Description: 广告物料表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@TableName("ad_material")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="广告物料表")
public class AdMaterial implements Serializable {
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
	/**分类ID*/
	@Excel(name = "分类ID", width = 15)
    @Schema(description = "分类ID")
    private java.lang.String categoryId;
    /**分类名称*/
    @Excel(name = "分类名称", width = 15)
    @Schema(description = "分类名称")
    private java.lang.String categoryName;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @Schema(description = "物料名称")
    private java.lang.String name;
	/**广告内容(文字/图片URL)*/
	@Excel(name = "广告内容(文字/图片URL)", width = 15)
    @Schema(description = "广告内容(文字/图片URL)")
    @Column(columnDefinition = "text")
    private java.lang.String content;
    
    /**视频URL*/
    @Excel(name = "文件URL", width = 15)
    @Schema(description = "文件URL")
    private java.lang.String fileUrl;
    
	/**类型(1文字,2图片,3视频)*/
	@Excel(name = "类型(1文字,2图片,3视频)", width = 15)
    @Schema(description = "类型(1文字,2图片,3视频)")
    @Dict(dicCode = "ad_material_type")
    private java.lang.Integer type;
	/**尺寸规格*/
	@Excel(name = "尺寸规格", width = 15)
    @Schema(description = "尺寸规格")
    private java.lang.String size;
	/**播放时长(秒)*/
	@Excel(name = "播放时长(秒)", width = 15)
    @Schema(description = "播放时长(秒)")
    private java.lang.Integer duration;
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
