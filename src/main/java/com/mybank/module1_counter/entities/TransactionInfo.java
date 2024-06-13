package com.mybank.module1_counter.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@TableName("transaction")
public class TransactionInfo {
    @TableId(type = IdType.AUTO)
    private Integer transactionId;              // 交易id

    private String cardId;                      // 交易银行卡id
    private String cardType;                    // 银行卡类型

    private LocalDateTime transactionTime;      // 交易时间
    private Double transactionAmount;           // 交易金额
    private String transactionType;             // 交易类型（活期存款，活期取款，定期取款，转账）
    private String transactionChannel;          // 交易渠道（ATM、网银、柜台）

    private String currency;                    // 交易货币类型
    private String moneySource;                 // 交易资金来源
    private String moneyGoes;                   // 交易资金去向
    // 只有当type是转账时 moneySource 和 moneyGoes 都非空
    // 转账的时候，为了方便检索自己的交易记录，会存两条交易记录
}
