package com.asiainfo.casebase.web;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCurVal;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryDraftVal;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.service.draftService.DraftService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Desc 草稿箱
 **/
@Slf4j
@ApiModel(value = "帖子相关接口")
@RestController
@RequestMapping("/draft")
public class DraftWeb {

    @Autowired
    private DraftService draftService;

    /**
     * @Desc 保存或更新草稿箱
     **/
    @ApiOperation(value="保存或更新草稿箱",notes="保存或更新草稿箱",httpMethod = "POST")
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    public ResultData save(@RequestBody NmCaseLibraryDraftVal draftVal) {
        try{
            draftService.saveOrUpdate(draftVal);
            return new ResultData(200,"保存成功",true);
        }catch (Exception e){
            log.error("保存草稿异常",e);
            return new ResultData(-1,"保存失败",false);
        }
    }

    /**
     * @Desc 删除草稿
     **/
    @ApiOperation(value="删除草稿",notes="删除草稿",httpMethod = "GET")
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ResultData delete(@RequestParam Long draftId) {
        try{
            draftService.delete(draftId);
            return new ResultData(200,"删除成功",true);
        }catch (Exception e){
            log.error("删除草稿异常",e);
            return new ResultData(-1,"删除失败",false);
        }
    }


}
