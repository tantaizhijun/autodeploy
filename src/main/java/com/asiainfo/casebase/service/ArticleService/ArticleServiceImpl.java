package com.asiainfo.casebase.service.ArticleService;

import com.asiainfo.casebase.entity.casUser.CasUser;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCurVal;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryHisVal;
import com.asiainfo.casebase.repository.casebase.CaseLibraryCurRepository;
import com.asiainfo.casebase.repository.casebase.CaseLibraryHisRepository;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Desc 文章(帖子)业务操作类
 **/
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private CaseLibraryCurRepository libraryCurRepository;

    @Autowired
    private CaseLibraryHisRepository libraryHisRepository;

    @Autowired
    private UserService userService;


    @Override
    public ResultData saveOrUpdate(NmCaseLibraryCurVal caseLibraryCur) {
        try {

            CasUser casUser = userService.findUserFromCas();

            Date date = new Date();
            caseLibraryCur.setCreated(date);
            caseLibraryCur.setUpdateTime(date);




            NmCaseLibraryHisVal libraryHisVal = new NmCaseLibraryHisVal();
            BeanUtils.copyProperties(caseLibraryCur,libraryHisVal);
            if(StringUtils.isEmpty(String.valueOf(caseLibraryCur.getId()))) {
                libraryHisVal.set("保存");
            } else {
                libraryHisVal.set("更新");
            }
            libraryCurRepository.save(caseLibraryCur);
            libraryHisRepository.save(libraryHisVal);

            return new ResultData(200,"保存成功",true);
        }catch (Exception e){
            log.error("帖子保存异常",e);
            return new ResultData(-1,"保存失败",false);
        }
    }

    @Override
    public ResultData delete(Long caseLibraryCurId) {
        libraryCurRepository.deleteById(caseLibraryCurId);
        return null;
    }

    @Override
    public ResultData collect(Long caseLibraryCurId) {

        return null;
    }
}
