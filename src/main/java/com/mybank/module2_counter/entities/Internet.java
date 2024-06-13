package com.mybank.module2_counter.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("internet")
public class Internet {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String customerAccountId;
    private Integer customerId;
    private Boolean isInBlackList;
    private String password;
    private Date createTime;
}
