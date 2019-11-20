package com.asiainfo.casebase.web;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCurVal;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.service.ArticleService.ArticleService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Desc 帖子相关接口
 **/
@Slf4j
@ApiModel(value = "帖子相关接口")
@RestController
@RequestMapping("/article")
public class ArticleWeb {


    @Autowired
    private ArticleService articleService;

    /**
     * @Desc 发布帖子
     **/
    @ApiOperation(value="发布帖子",notes="发布帖子",httpMethod = "POST")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultData save(@RequestBody NmCaseLibraryCurVal libraryCurVal) {
        try{
            ResultData resultData = articleService.saveOrUpdate(libraryCurVal);
            return resultData;
        }catch (Exception e){
            log.error("保存帖子异常",e);
            return new ResultData(-1,"保存失败",false);
        }
    }











}
