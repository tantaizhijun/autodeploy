package com.autodeploy.repository;

import com.autodeploy.entity.LinuxHost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinuxHostRepo extends JpaRepository<LinuxHost,Long> {
}
