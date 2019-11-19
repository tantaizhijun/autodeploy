package com.asiainfo.casebase.repository.casUser;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCommentVal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc NmCaseLibraryCommentVal表操作
 **/
@Repository
public interface CaseLibraryCommentRepository extends JpaRepository<NmCaseLibraryCommentVal,Long> {




}