package com.asiainfo.casebase.config.cas;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName CasUtil
 **/
@Slf4j
public class CasUtil {

    /**
     * 从cas中获取用户名
     * @param request
     * @return
     */
    public static String getAccountNameFromCas(HttpServletRequest request) {

        Object attribute = request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        if(attribute != null) {
            Assertion assertion = (Assertion)attribute;
            String id = assertion.getPrincipal().getName();
            if(StringUtils.isEmpty(id)){
                log.error("从cas获取用户id为空");
            }
            return id;
        }
        log.error("从cas获取用户id为空");
        return null;

    }

    /**
     * 从cas中获取用户名 无参
     */
    public static String getUserNameFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String username = getAccountNameFromCas(request);
        return username;

    }
}
