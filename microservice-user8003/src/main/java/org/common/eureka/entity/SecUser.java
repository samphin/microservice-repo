package org.common.eureka.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class SecUser {

    private String userId;

    private String userName;

    private String password;

    private String realName;

    private BigDecimal sex;

    private String address;

    private BigDecimal age;

    private Date birthday;

    private Date createTime;

    private String createUserId;

    private String credentialsSalt;

    private String email;

    private Date enabledTime;

    private String idCard;

    private Date invalidTime;

    private BigDecimal isSuperAdmin;

    private Date lastUpdateTime;

    private String lastUpdateUserId;

    private String place;

    private String remark;

    private BigDecimal status;

    private String telephone;
}