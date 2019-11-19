package com.asiainfo.casebase.repository.casUser;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryHisVal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc NmCaseLibraryHisVal表操作
 **/
@Repository
public interface CaseLibraryHisRepository extends JpaRepository<NmCaseLibraryHisVal,Long> {




}
