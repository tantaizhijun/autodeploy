package com.sample;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Desc  案例库  sql
 */
@Data
@Entity
@Table(name = "case_base_sql")
public class CaseBaseSql {

  @ApiModelProperty(value="sqlId")
  @Id
  @Column(name = "sql_id",nullable = false,length = 255)
  private String sqlId;

  //sql名称
  @ApiModelProperty(value="sql名称")
  @Column(name = "sql_name",length = 255)
  private String sqlName;

  //sql说明
  @ApiModelProperty(value="sql说明")
  @Column(name = "sql_desc",length = 255)
  private String sqlDesc;

  //sql类型
  @ApiModelProperty(value="sql类型")
  @Column(name = "sql_type",length = 255)
  private String sqlType;

  //sql内容
  @ApiModelProperty(value="sql内容")
  @Column(name = "sql",nullable = false)
  private String sql;

  //参数
  @ApiModelProperty(value="参数")
  @Column(name = "params",length = 1000)
  private String params;

  //数据源配置名称
  @ApiModelProperty(value="数据源配置名称")
  @Column(name = "dbstr",length = 64)
  private String dbstr;

  //状态
  @ApiModelProperty(value="状态")
  @Column(name = "status",length = 16)
  private String status;

  //更新时间
  @ApiModelProperty(value="更新时间")
  @Column(name = "oper_time")
  private Date operTime;

  //创建人
  @ApiModelProperty(value="创建人")
  @Column(name = "oper_by",length=30)
  private String operBy;

  //创建时间
  @ApiModelProperty(value="创建时间")
  @Column(name = "create_time")
  private Date createTime;


}
