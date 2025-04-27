package org.jeecg.modules.ad.entity.Vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import javax.validation.constraints.NotNull;

/**
 * 处理上报实体表
 */
@Data
public class ReportProcessVo {
    /**司机ID*/
    @Excel(name = "司机ID", width = 15)
    @Schema(description = "司机ID")
    @NotNull(message = "司机ID不能为空")
    private java.lang.String driverId;
    /**司机ID*/
    @Excel(name = "上报ID", width = 15)
    @Schema(description = "上报ID")
    @NotNull(message = "上报ID不能为空")
    private java.lang.String reportId;
    /**发布明细ID*/
    @Excel(name = "发布明细ID", width = 15)
    @Schema(description = "发布明细ID")
    @NotNull(message = "发布明细ID不能为空")
    private java.lang.String publishDetailId;
    /**交易类型*/
    @Excel(name = "交易类型", width = 15)
    @Schema(description = "交易类型")
    @NotNull(message = "交易类型不能为空")
    private java.lang.Integer transactionType;
    /**上报处理状态*/
    @Excel(name = "上报处理状态", width = 15)
    @Schema(description = "上报处理状态")
    @NotNull(message = "上报处理状态不能为空")
    private java.lang.Integer status;
    /**处理结果*/
    @Excel(name = "处理结果", width = 15)
    @Schema(description = "处理结果")
    private java.lang.String processResult;
}
