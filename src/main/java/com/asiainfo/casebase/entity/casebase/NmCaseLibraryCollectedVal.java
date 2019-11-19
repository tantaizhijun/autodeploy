package com.sample;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Desc  案例库  收藏
 */
@Data
@Entity
@Table(name = "nm_case_library_collected_val")
public class NmCaseLibraryCollectedVal {

    @ApiModelProperty(value="主键Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,length = 11)
    private long id;

    //案例库id
    @ApiModelProperty(value="案例库id")
    @Column(name = "case_library_id",length = 11)
    private long caseLibraryId;

    //收藏人用户id
    @ApiModelProperty(value="收藏人用户id")
    @Column(name = "created_by",length = 30)
    private String createdBy;

    //收藏人中文名
    @ApiModelProperty(value="收藏人中文名")
    @Column(name = "created_by_cn",length = 10)
    private String createdByCn;

    //收藏时间
    @ApiModelProperty(value="收藏时间")
    @Column(name = "created_time")
    private Date createdTime;


}