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
     * @Desc 保存或更新帖子
     **/
    @ApiOperation(value="保存或更新帖子",notes="保存或更新帖子",httpMethod = "POST")
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
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
     * @Desc 收藏帖子/取消收藏
     * @param type true 收藏; false 取消收藏
     **/
    @ApiOperation(value="收藏帖子/取消收藏",notes="收藏帖子/取消收藏",httpMethod = "GET")
    @RequestMapping(value = "/collect",method = RequestMethod.GET)
    public ResultData collect(@RequestParam Long caseLibraryCurId,
                              @RequestParam Boolean type) {
        String typeStr = type ? "收藏" : "取消收藏";
        try{
            ResultData resultData = articleService.collect(caseLibraryCurId,type);
            return resultData;
        }catch (Exception e){
            log.error(typeStr + "帖子异常",e);
            return new ResultData(-1,typeStr + "失败",false);
        }
    }



}
