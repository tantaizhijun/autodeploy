package com.asiainfo.casebase.service.logsService;


import com.asiainfo.casebase.entity.casebase.WebOperLog;
import com.asiainfo.casebase.repository.casebase.WebOperDao;
import com.asiainfo.casebase.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 系统日志 service
 **/
@Slf4j
@Service
public class LogService {

    public final static String add = "新增";
    public final static String modify = "修改";
    public final static String delete = "删除";
    public final static String select = "查询";

    @Autowired
    private WebOperDao webOperDao;


    /**
     * @Desc 批量日志保存
     **/
    public void doSaveBatch(List<WebOperLog> logs) {
        webOperDao.saveAll(logs);

//        //异步保存到远程日志服务
        ThreadPoolUtils.submit(new RemoteLogThread(logs));
    }


    /**
     * @Desc 单个日志保存
     **/
    public void save(String oper_tab,String oper_tab_seq,String oper_type,String descr,String oper_result,String oper_msg,Integer success_cnt,Integer fail_cnt,String oper_by){
        WebOperLog logEntity = new WebOperLog(oper_tab,oper_tab_seq,oper_type,descr,oper_result,oper_msg,success_cnt,fail_cnt,oper_by);
        logEntity.setOper_time(new Date());
        webOperDao.save(logEntity);

        //保存到远程日志服务
        ArrayList<WebOperLog> logList = new ArrayList<>();
        logList.add(logEntity);
        ThreadPoolUtils.submit(new RemoteLogThread(logList));
    }

}
