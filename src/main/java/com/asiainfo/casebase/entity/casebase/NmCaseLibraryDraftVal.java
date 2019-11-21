package com.asiainfo.casebase.entity.casebase;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
/**
 * @Desc  案例库--草稿表
 */
@Data
@Entity
@Table(name = "nm_case_library_draft_val")
public class NmCaseLibraryDraftVal {

    @ApiModelProperty(value="主键Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,length = 11)
    private Long id;

    //所属领域：专家池大类名-专家池小类名
    @ApiModelProperty(value="所属领域：专家池大类名-专家池小类名")
    @Column(name = "case_library_field",length = 50)
    private String caseLibraryField;

    //帖子题目
    @ApiModelProperty(value="帖子题目")
    @Column(name = "case_library_title",length = 500)
    private String caseLibraryTitle;

    //帖子内容
    @ApiModelProperty(value="帖子内容")
    @Column(name = "case_library_content")
    private String caseLibraryContent;

    //附件
    @ApiModelProperty(value="附件")
    @Column(name = "enclosure",length=2000)
    private String enclosure;

    //发布人员用户id
    @ApiModelProperty(value="发布人员用户id")
    @Column(name = "publisher",length=30)
    private String publisher;

    //发布人员中文名
    @ApiModelProperty(value="发布人员中文名")
    @Column(name = "publisher_cn",length=20)
    private String publisherCn;

    //插入时间
    @ApiModelProperty(value="插入时间")
    @Column(name = "created")
    private Date created;

    //修改人用户id
    @ApiModelProperty(value="修改人用户id")
    @Column(name = "update_by",length=30)
    private String updateBy;

    //修改人中文名
    @ApiModelProperty(value="修改人中文名")
    @Column(name = "update_by_cn",length=10)
    private String updateByCn;

    //修改时间
    @ApiModelProperty(value="修改时间")
    @Column(name = "update_time")
    private Date updateTime;

}