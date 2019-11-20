package com.asiainfo.casebase.repository.casebase;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCurVal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc NmCaseLibraryCurVal表操作
 **/
@Repository
public interface CaseLibraryCurRepository extends JpaRepository<NmCaseLibraryCurVal,Long> {




}