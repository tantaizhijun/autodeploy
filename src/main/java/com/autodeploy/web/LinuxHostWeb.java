package com.autodeploy.web;

import com.autodeploy.entity.LinuxHost;
import com.autodeploy.responseEntity.ResultData;
import com.autodeploy.service.LinuxHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/linux")
public class LinuxHostWeb {

    @Autowired
    private LinuxHostService linuxHostService;


    @PostMapping
    public ResultData save(@RequestBody LinuxHost linuxHost){
        try {
            linuxHostService.save(linuxHost);
            return new ResultData(200,"保存成功",true);
        }catch (Exception e){
            return new ResultData(-1,"保存失败",false);
        }
    }

}
