package com.asiainfo.casebase.service.logsService;

import com.alibaba.fastjson.JSONObject;

import com.asiainfo.casebase.config.ApplicationContextProvider;
import com.asiainfo.casebase.entity.casebase.WebOperLog;
import com.asiainfo.casebase.utils.AESUtils;
import com.asiainfo.casebase.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Desc 远程保存日志的线程类
 **/
@Slf4j
public class RemoteLogThread implements Runnable {

    private List<WebOperLog> logs;

    public RemoteLogThread(List<WebOperLog> logs) {
        this.logs = logs;
    }

    @Override
    public void run() {
        try {
            log.info("保存远程日");
            String encryptLogs = AESUtils.encrypt(JsonUtils.toJson(logs));
            
            RestTemplate restTemplate = ApplicationContextProvider.getBean(RestTemplate.class);
            Environment environment = ApplicationContextProvider.getBean(Environment.class);
            String data = restTemplate.postForObject(environment.getProperty("logservice"), encryptLogs, String.class);
            
            String s = AESUtils.desEncrypt(data);
            JSONObject jsonObject = JSONObject.parseObject(s);
        } catch (Exception e){
            log.error("远程保存日志异常:{}",e);
        }
    }
}
