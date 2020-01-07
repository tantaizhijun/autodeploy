package com.asiainfo.casebase.entity.casebase;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(name = "nm_web_oper_log")
public class WebOperLog {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String oper_tab;
    private String oper_tab_seq;
    private String oper_type;
    private String descr;
    private String oper_result;
    private String oper_msg;
    private Integer success_cnt = 1;
    private Integer fail_cnt = 0;
    private String oper_by;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyyMMdd HH:mm")
    private Date oper_time=new Date();

    public WebOperLog(String oper_tab, String oper_tab_seq, String oper_type, String descr, String oper_result, String oper_msg, Integer success_cnt, Integer fail_cnt, String oper_by){
        this.oper_tab = oper_tab;
        this.oper_tab_seq = oper_tab_seq;
        this.oper_type = oper_type;
        this.descr = descr;
        this.oper_result = oper_result;
        this.oper_msg = oper_msg;
        this.success_cnt = success_cnt;
        this.fail_cnt = fail_cnt;
        this.oper_by = oper_by;
    }

}