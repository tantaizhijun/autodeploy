package com.autodeploy.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CommonController {


    @RequestMapping(value = "/user/login")
    public String userLogin() {
        return "user/index";
    }


}
