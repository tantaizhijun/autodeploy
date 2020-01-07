package com.asiainfo.casebase.repository.casebase;


import com.asiainfo.casebase.entity.casebase.WebOperLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebOperDao extends JpaRepository<WebOperLog,Integer> {
}
