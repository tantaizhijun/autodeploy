package com.asiainfo.casebase.repository.casebase;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryDraftVal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc NmCaseLibraryHisVal表操作
 **/
@Repository
public interface CaseLibraryDraftRepository extends JpaRepository<NmCaseLibraryDraftVal,Long> {


}
