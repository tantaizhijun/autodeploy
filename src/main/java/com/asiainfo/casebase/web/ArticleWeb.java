package com.asiainfo.casebase.web;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCurVal;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.service.ArticleService.ArticleService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * @Desc 删除帖子
     **/
    @ApiOperation(value="删除帖子",notes="删除帖子",httpMethod = "GET")
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ResultData delete(@RequestParam Long caseLibraryCurId) {
        try{
            ResultData resultData = articleService.delete(caseLibraryCurId);
            return resultData;
        }catch (Exception e){
            log.error("删除帖子异常",e);
            return new ResultData(-1,"删除失败",false);
        }
    }


    /**
     * @Desc 收藏帖子
     **/
    @ApiOperation(value="收藏帖子",notes="收藏帖子",httpMethod = "GET")
    @RequestMapping(value = "/collect",method = RequestMethod.GET)
    public ResultData collect(@RequestParam Long caseLibraryCurId) {
        try{
            ResultData resultData = articleService.collect(caseLibraryCurId);
            return resultData;
        }catch (Exception e){
            log.error("收藏帖子异常",e);
            return new ResultData(-1,"收藏失败",false);
        }
    }





}
