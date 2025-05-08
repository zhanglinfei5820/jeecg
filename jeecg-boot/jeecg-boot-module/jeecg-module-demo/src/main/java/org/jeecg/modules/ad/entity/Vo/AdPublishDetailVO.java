package org.jeecg.modules.ad.entity.Vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 广告发布明细表
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="广告发布明细表")
public class AdPublishDetailVO implements Serializable {
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
	/**发布ID*/
	@Excel(name = "发布ID", width = 15)
    @Schema(description = "发布ID")
    private String publishId;
	/**广告名称*/
	@Excel(name = "广告名称", width = 15)
    @Schema(description = "广告名称")
    private String adName;
	/**车辆ID*/
	@Excel(name = "车辆ID", width = 15)
    @Schema(description = "车辆ID")
    private String vehicleId;
	/**司机ID*/
	@Excel(name = "司机ID", width = 15)
    @Schema(description = "司机ID")
    private String driverId;
	/**司机名称*/
	@Excel(name = "司机名称", width = 15)
    @Schema(description = "司机名称")
    private String driverName;
	/**广告位置(左窗/右窗/后窗等)*/
	@Excel(name = "广告位置(左窗/右窗/后窗等)", width = 15)
    @Schema(description = "广告位置(左窗/右窗/后窗等)")
    @Dict(dicCode = "ad_position")
    private String position;
	/**安装图片*/
	@Excel(name = "安装图片", width = 15)
    @Schema(description = "安装图片")
    private String images;
	/**广告标题*/
	@Excel(name = "广告标题", width = 15)
    @Schema(description = "广告标题")
    private String name;
	/**单价(元/天)*/
	@Excel(name = "单价(元/天)", width = 15)
    @Schema(description = "单价(元/天)")
    private BigDecimal price;
	/**状态(0禁用,1启用)*/
	@Excel(name = "状态(0禁用,1启用)", width = 15)
    @Schema(description = "状态(0禁用,1启用)")
    @Dict(dicCode = "ad_publish_detail_status")
    private Integer status;
	/**公司id*/
	@Excel(name = "公司id", width = 15)
    @Schema(description = "公司id")
    private String companyId;
	/**公司名称*/
	@Excel(name = "公司名称", width = 15)
    @Schema(description = "公司名称")
    private Integer companyName;
	/**类型（0公司1司机）*/
	@Excel(name = "类型（0公司1司机）", width = 15)
    @Schema(description = "类型（0公司1司机）")
    @Dict(dicCode = "ad_publish_detail_type")
    private Integer type;
	/**抽成*/
	@Excel(name = "抽成", width = 15)
    @Schema(description = "抽成")
    private BigDecimal percentage;
    
    /**司机数*/
    @Excel(name = "司机数", width = 15)
    @Schema(description = "司机数")
    private Integer drivers;
    
    /**实际司机数*/
    @Excel(name = "实际司机数", width = 15)
    @Schema(description = "实际司机数")
    private Integer actualDrivers;

    /**安装时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "安装时间")
    private java.util.Date installationTime;

    /**安装图片*/
    @Excel(name = "安装图片", width = 15)
    @Schema(description = "安装图片")
    private java.lang.String installationImage;
}
