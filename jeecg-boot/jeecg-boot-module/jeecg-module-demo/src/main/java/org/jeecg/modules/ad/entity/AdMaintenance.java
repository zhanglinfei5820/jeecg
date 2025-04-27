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
 * @Description: 破损维护记录表
 * @Author: jeecg-boot
 * @Date:   2025-04-18
 * @Version: V1.0
 */
@Data
@TableName("ad_maintenance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="破损维护记录表")
public class AdMaintenance implements Serializable {
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
	/**广告年检表id*/
	@Excel(name = "广告年检表id", width = 15)
    @Schema(description = "广告年检表id")
    private String adInspectionId;
	/**发布明细ID*/
	@Excel(name = "发布明细ID", width = 15)
    @Schema(description = "发布明细ID")
    private String publishDetailId;
	/**破损等级*/
	@Excel(name = "破损等级", width = 15)
    @Schema(description = "破损等级")
    private Integer damageSeverity;
	/**破损具体情况描述*/
	@Excel(name = "破损具体情况描述", width = 15)
    @Schema(description = "破损具体情况描述")
    private String damageDescription;
	/**破损首次被上报的日期时间*/
	@Excel(name = "破损首次被上报的日期时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "破损首次被上报的日期时间")
    private Date firstReportTime;
	/**最近一次维护的日期时间*/
	@Excel(name = "最近一次维护的日期时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最近一次维护的日期时间")
    private Date lastMaintenanceTime;
	/**状态枚举*/
	@Excel(name = "状态枚举", width = 15)
    @Schema(description = "状态枚举")
    private Integer status;
	/**维护处理结果说明*/
	@Excel(name = "维护处理结果说明", width = 15)
    @Schema(description = "维护处理结果说明")
    private String processingResult;
	/**负责处理的人员姓名或工号*/
	@Excel(name = "负责处理的人员姓名或工号", width = 15)
    @Schema(description = "负责处理的人员姓名或工号")
    private String processorName;
	/**处理操作完成的日期时间*/
	@Excel(name = "处理操作完成的日期时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "处理操作完成的日期时间")
    private Date processingTime;
	/**破损现场照片存储路径*/
	@Excel(name = "破损现场照片存储路径", width = 15)
    @Schema(description = "破损现场照片存储路径")
    private String damageImages;
	/**维修工单等文件存储路径*/
	@Excel(name = "维修工单等文件存储路径", width = 15)
    @Schema(description = "维修工单等文件存储路径")
    private String maintenanceAttachments;
}
