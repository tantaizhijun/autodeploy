package com.asiainfo.casebase.repository.casUser;



import com.asiainfo.casebase.entity.casUser.CasUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasUserRepository extends JpaRepository<CasUser,String> {

    @Query("from CasUser cu where cu.id = :id and status = '0'" )
    List<CasUser> queryWithId(@Param("id") String id);


}
