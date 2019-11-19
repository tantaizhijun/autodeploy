package com.asiainfo.casebase.repository.casUser;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCollectedVal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc NmCaseLibraryCollectedVal表操作
 **/
@Repository
public interface CaseLibraryCollectedRepository extends JpaRepository<NmCaseLibraryCollectedVal,Long> {




}