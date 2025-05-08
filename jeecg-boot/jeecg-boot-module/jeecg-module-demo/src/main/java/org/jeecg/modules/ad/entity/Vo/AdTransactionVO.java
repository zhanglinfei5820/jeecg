package org.jeecg.modules.ad.entity.Vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Description: 流水记录表视图对象
 * @Author: jeecg-boot
 * @Date:   2025-04-14
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="流水记录表视图对象")
public class AdTransactionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @Schema(description = "主键")
    private String id;
    
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private Date createTime;
    
    /**金额*/
    @Schema(description = "金额")
    private BigDecimal amount;
    
    /**类型(1收入,2支出)*/
    @Schema(description = "类型(1收入,2支出)")
    @Dict(dicCode = "ad_stream_type")
    private Integer type;
    
    /**交易类型(0维护金,1提现)*/
    @Schema(description = "交易类型(0维护金,1提现)")
    @Dict(dicCode = "ad_transaction_type")
    private Integer transactionType;
    
    /**关联ID(发布明细ID/提现ID等)*/
    @Schema(description = "关联ID(发布明细ID/提现ID等)")
    private String relatedId;
    
    /**交易后余额*/
    @Schema(description = "交易后余额")
    private BigDecimal balance;
    
    /**状态(0失败,1成功)*/
    @Schema(description = "状态(0失败,1成功)")
    @Dict(dicCode = "ad_s_status")
    private Integer status;
    
    /**备注*/
    @Schema(description = "备注")
    private String remark;
    
    /**广告名称*/
    @Schema(description = "广告名称")
    private String adName;

    /**上报时间*/
    @Schema(description = "上报时间")
    private Date reportTime;
}