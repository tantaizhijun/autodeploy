package com.asiainfo.casebase.repository.casUser;

import com.asiainfo.casebase.entity.casebase.CaseBaseSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc CaseBaseSql表操作
 **/
@Repository
public interface CaseBaseSqlRepository extends JpaRepository<CaseBaseSql,String> {




}