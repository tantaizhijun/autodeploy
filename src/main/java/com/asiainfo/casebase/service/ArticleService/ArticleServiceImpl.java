package com.asiainfo.casebase.service.ArticleService;

import com.asiainfo.casebase.entity.casUser.CasUser;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCollectedVal;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCurVal;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryHisVal;
import com.asiainfo.casebase.repository.casebase.CaseLibraryCollectedRepository;
import com.asiainfo.casebase.repository.casebase.CaseLibraryCurRepository;
import com.asiainfo.casebase.repository.casebase.CaseLibraryHisRepository;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private CaseLibraryCollectedRepository libraryCollectedRepository;

    @Autowired
    private UserService userService;


    @Transactional(value = "transactionManagerCaseBase")
    @Override
    public ResultData saveOrUpdate(NmCaseLibraryCurVal caseLibraryCur) {

        CasUser casUser = userService.findUserFromCas();
        if (casUser == null) {
            return new ResultData(-1, "获取用户信息失败", false);
        }

        Date date = new Date();
        caseLibraryCur.setCreated(date);
        caseLibraryCur.setUpdateTime(date);
        caseLibraryCur.setPublisher(casUser.getId());
        caseLibraryCur.setPublisherCn(casUser.getName());
        caseLibraryCur.setUpdateBy(casUser.getId());
        caseLibraryCur.setUpdateByCn(casUser.getName());

        NmCaseLibraryHisVal libraryHisVal = new NmCaseLibraryHisVal();
        BeanUtils.copyProperties(caseLibraryCur, libraryHisVal);
        libraryHisVal.setId(null);
        if (caseLibraryCur.getId() == null) {
            libraryHisVal.setState("保存");
        } else {
            libraryHisVal.setState("更新");
        }

        NmCaseLibraryCurVal savedCur = libraryCurRepository.save(caseLibraryCur);
        libraryHisVal.setCaseLibraryId(savedCur.getId());

        libraryHisRepository.save(libraryHisVal);

        String msg = "";
        if (caseLibraryCur.getId() == null) {
            msg = "发帖";
        } else {
            msg = "修改";
        }
        return new ResultData(200, msg + "成功", true);
    }

    @Override
    public ResultData delete(Long caseLibraryCurId) {

        Optional<NmCaseLibraryCurVal> optional = libraryCurRepository.findById(caseLibraryCurId);
        if (!optional.isPresent()) {
            log.error("未查询到该条信息,id:{}", caseLibraryCurId);
            return new ResultData(-1, "删除失败", false);
        }
        CasUser casUser = userService.findUserFromCas();
        if (casUser == null) {
            return new ResultData(-1, "获取用户信息失败", false);
        }

        NmCaseLibraryCurVal libraryCurVal = optional.get();

        NmCaseLibraryHisVal libraryHisVal = new NmCaseLibraryHisVal();
        BeanUtils.copyProperties(libraryCurVal, libraryHisVal);
        libraryHisVal.setCaseLibraryId(caseLibraryCurId);
        libraryHisVal.setId(null);
        libraryHisVal.setState("删除");
        libraryHisVal.setUpdateBy(casUser.getId());
        libraryHisVal.setUpdateByCn(casUser.getName());

        libraryHisRepository.save(libraryHisVal);
        libraryCurRepository.deleteById(caseLibraryCurId);
        return new ResultData(200, "删除成功", true);
    }


    /**
     * @Desc 收藏/取消收藏
     * @param type boolean  收藏/取消收藏
     *
     **/
    @Override
    public ResultData collect(Long caseLibraryCurId,Boolean type) {
        CasUser casUser = userService.findUserFromCas();
        if (casUser == null) {
            return new ResultData(-1, "获取用户信息失败", false);
        }
        List<NmCaseLibraryCollectedVal> collectedValList = libraryCollectedRepository.findByCaseLibraryIdAndCreatedBy(casUser.getId(), caseLibraryCurId);
        if(collectedValList == null || collectedValList.size() == 0) {
            NmCaseLibraryCollectedVal collectedVal = new NmCaseLibraryCollectedVal();
            collectedVal.setCaseLibraryId(caseLibraryCurId);
            collectedVal.setCreatedBy(casUser.getId());
            collectedVal.setCreatedByCn(casUser.getName());
            collectedVal.setCreatedTime(new Date());
            libraryCollectedRepository.save(collectedVal);
            return new ResultData(200, "收藏成功", true);
        } else {
            libraryCollectedRepository.deleteAll(collectedValList);
            return new ResultData(200,"取消成功",true);
        }
    }
}
