package com.asiainfo.casebase.entity.casUser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "CAS_USER")
public class CasUser<T> {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    private String password;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private int status;

    @Column(name = "p_status")
    private int pStatus;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Column(name = "createtime")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Column(name = "password_lastupdate")
    private Date passwordLastupdate;

    @Column(name = "password_history")
    private String passwordHistory;

    @Column(name = "oid")
    private int oid;

    @Column(name = "login_count")
    private String loginCount;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @Column(name = "round")
    private int round;

    @Column(name = "itdept")
    private  String itdept;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "areacode")
    private String areaCode;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Column(name = "updatetime")
    private Date updateTime;

    @Transient
    private T auth;

}
