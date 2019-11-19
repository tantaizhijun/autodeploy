package com.asiainfo.casebase.repository.casUser;


import com.asiainfo.casebase.entity.casUser.CasUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Desc casUser表操作
 **/
@Repository
public interface CasUserRepository extends JpaRepository<CasUser,String> {




}
