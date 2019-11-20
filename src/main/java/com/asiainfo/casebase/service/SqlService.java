package com.asiainfo.casebase.service;


import com.asiainfo.casebase.config.db.DataSourceHolder;

import com.asiainfo.casebase.entity.casebase.CaseBaseSql;
import com.asiainfo.casebase.repository.casebase.CaseBaseSqlRepository;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.utils.DbUtil;
import com.asiainfo.casebase.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Desc sql 表业务
 **/
@Service
@Slf4j
public class SqlService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CaseBaseSqlRepository sqlRepository;


    //    public List<Map<String, Object>> executeSql(Map map) {
    public ResultData executeSql(Map map) {
        try {
//        EncryptResponseBodyAdvice.setEncryptStatus(false);
            DataSourceHolder.setDataSource("default");

            List<Map<String, Object>> result = new ArrayList<>();
            String id = (String) map.get("id");
            if (StringUtils.isEmpty(id)) {
                log.error("id为空，找不到对应sql");
                return new ResultData(-1, "id为空, 无法匹配sql!", false);
            }
            Optional<CaseBaseSql> optional = sqlRepository.findById(id);

            if (!optional.isPresent()) {
                log.error("根据id匹配sql失败");
                return new ResultData(-1, "根据id匹配sql失败", false);
            }
            CaseBaseSql onlineexamSql = optional.get();
            String sql = onlineexamSql.getSql();
            String params = onlineexamSql.getParams();

            //如果传进来的dbstr有值，则设置数据源
            String dbstr = (String) map.get("dbstr");
            if (dbstr != null) {
                dbstr = dbstr.replaceAll("@", "-");
                log.info("切换数据源为：" + dbstr);
                DataSourceHolder.setDataSource(dbstr);
            } else {
                //根据查询的字段设置数据源
                String dbstr1 = onlineexamSql.getDbstr();
                log.info("切换数据源为：" + dbstr1);
                DataSourceHolder.setDataSource(dbstr1);
            }

            log.info("数据源切换完毕！准备向sql中替换参数并执行");
            //向sql中替换参数并执行
            String realSql = DbUtil.replaceParam(sql, map);


            //如果表里的params字段有值，则先将入参的值替换params字段，再用字段替换sql中的占位符
            //将params字段用入参替换为 String["value1","value2",...] 形式的字符数组
            String[] realparams = DbUtil.replaceVar(map, params);
            log.info("replaceVar执行完毕，准备调用replaceSqlConstant获取sql");

            //用字符数组里的值去替换sql语句中包含的‘&’。
            // 返回值：替换了‘&’的sql（这个sql只剩‘？’没换）和没用来替换‘&’而剩下的参数（用来替换‘’？）
            Map<String, Object> m = DbUtil.replaceSqlConstant(realSql, realparams);
            log.info("replaceSqlConstant执行完毕，准备执行sql");

            Object params1[] = (String[]) m.get("params");
            if (params1 == null) {
                params1 = new Object[]{};
            }
            realSql = (String) m.get("sql");
            //log.info("最终sql：" + realSql);
            result = jdbcTemplate.queryForList(realSql, params1);
            return new ResultData(200,"查询成功",true,DbUtil.getStringListMap(result));
        } catch (Exception e) {
            log.error("查询异常" + e);
            return new ResultData(-1, "查询异常", false);
        }
    }


    public ResultData executeSql(String str) {
        Map hashMap = JsonUtils.JsonToMap(str);

        return executeSql(hashMap);
    }

}
