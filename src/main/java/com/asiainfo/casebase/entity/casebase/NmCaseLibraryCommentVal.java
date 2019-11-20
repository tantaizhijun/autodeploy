package com.asiainfo.casebase.entity.casebase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
/**
 * @Desc  案例库--评论表
 */
@Data
@Entity
@Table(name = "nm_case_library_comment_val")
public class NmCaseLibraryCommentVal {
    @ApiModelProperty(value="主键Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,length = 11)
    private long id;

    //案例库id
    @ApiModelProperty(value="案例库id")
    @Column(name = "case_library_id",length = 11)
    private long caseLibraryId;

    //评论id(默认-1)
    @ApiModelProperty(value="评论id(默认-1)")
    @Column(name = "case_id",length = 11)
    private long caseId;

    //评论内容
    @ApiModelProperty(value="评论内容")
    @Column(name = "case_content")
    private String caseContent;

    //评论人用户id
    @ApiModelProperty(value="评论人用户id")
    @Column(name = "created_by",length = 30)
    private String createdBy;

    //评论人中文名
    @ApiModelProperty(value="评论人中文名")
    @Column(name = "created_by_cn",length = 10)
    private String createdByCn;

    //评论时间
    @ApiModelProperty(value="评论时间")
    @Column(name = "created_time")
    private Date createdTime;

}
