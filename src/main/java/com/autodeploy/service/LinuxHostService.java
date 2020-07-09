package com.autodeploy.service;

import com.autodeploy.entity.LinuxHost;
import com.autodeploy.repository.LinuxHostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LinuxHostService {
    @Autowired
    private LinuxHostRepo linuxHostRepo;

    public Boolean save(LinuxHost linuxHost) {
        LinuxHost save = linuxHostRepo.save(linuxHost);
        return true;
    }
}
