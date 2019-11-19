package com.asiainfo.casebase.web;


import com.asiainfo.casebase.responseEntity.ResultData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Desc 考试竞赛相关接口
 **/
@Slf4j
@ApiModel(value = "试卷相关接口")
@RestController
@RequestMapping("/exam")
public class ExamWeb {


    /**
     * @Desc 创建考试,录入试卷信息和题目
     **/
    @ApiOperation(value="录入考试试卷",notes="录入试卷",httpMethod = "POST")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultData save() {
        try{
            return new ResultData(-1,"录入试卷失败",false);
        }catch (Exception e){
            log.error("录入试卷异常",e);
            return new ResultData(-1,"录入试卷失败",false);
        }
    }




}
