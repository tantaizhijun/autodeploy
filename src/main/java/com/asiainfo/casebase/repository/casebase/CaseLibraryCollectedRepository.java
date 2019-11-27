package com.asiainfo.casebase.repository.casebase;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCollectedVal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Desc NmCaseLibraryCollectedVal表操作
 **/
@Repository
public interface CaseLibraryCollectedRepository extends JpaRepository<NmCaseLibraryCollectedVal,Long> {



    @Query(value = "select t from NmCaseLibraryCollectedVal t where t.caseLibraryId = ?2 and t.createdBy = ?1")
    List<NmCaseLibraryCollectedVal> findByCaseLibraryIdAndCreatedBy(String createdBy,Long caseLibraryId);

}