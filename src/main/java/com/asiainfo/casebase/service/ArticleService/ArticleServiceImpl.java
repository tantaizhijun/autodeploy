package com.asiainfo.casebase.service.ArticleService;

import com.asiainfo.casebase.entity.casUser.CasUser;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCollectedVal;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCurVal;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryHisVal;
import com.asiainfo.casebase.entity.casebase.WebOperLog;
import com.asiainfo.casebase.repository.casebase.CaseLibraryCollectedRepository;
import com.asiainfo.casebase.repository.casebase.CaseLibraryCurRepository;
import com.asiainfo.casebase.repository.casebase.CaseLibraryHisRepository;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.service.UserService;
import com.asiainfo.casebase.service.logsService.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private LogService logService;


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
        NmCaseLibraryHisVal hisVal = libraryHisRepository.save(libraryHisVal);
        String operType = "";
        if (caseLibraryCur.getId() == null) {
            operType = "发帖";
        } else {
            operType = "修改";
        }
        //日志
        ArrayList<WebOperLog> logs = new ArrayList<>();
        logs.add(new WebOperLog("CASEBASE:nm_case_library_cur_val",savedCur.getId().toString(),operType,
                operType+"帖子","成功","保存案例库帖子",1,0,casUser.getId()));
        logs.add(new WebOperLog("CASEBASE:nm_case_library_his_val",hisVal.getId().toString(),operType,
                operType+"历史表","成功","保存案例库发帖历史表",1,0,casUser.getId()));
        logService.doSaveBatch(logs);

        return new ResultData(200, operType + "成功", true);
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

        NmCaseLibraryHisVal hisVal = libraryHisRepository.save(libraryHisVal);
        libraryCurRepository.deleteById(caseLibraryCurId);

        //日志
        ArrayList<WebOperLog> logs = new ArrayList<>();
        logs.add(new WebOperLog("CASEBASE:nm_case_library_his_val",hisVal.getId().toString(),"新增",
                "删除帖子新增删除历史","成功","删除帖子新增删除历史",1,0,casUser.getId()));

        logs.add(new WebOperLog("CASEBASE:nm_case_library_cur_val",caseLibraryCurId.toString(),"删除",
                "删除帖子","成功","删除帖子",1,0,casUser.getId()));
        logService.doSaveBatch(logs);

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
            logService.save("CASEBASE:nm_case_library_collected_val",caseLibraryCurId.toString(),"新增","添加收藏",
                    "成功","收藏帖子",1,0,casUser.getId());
            return new ResultData(200, "收藏成功", true);
        } else {
            libraryCollectedRepository.deleteAll(collectedValList);
            logService.save("CASEBASE:nm_case_library_collected_val",caseLibraryCurId.toString(),"删除","取消收藏",
                    "成功","取消帖子收藏",1,0,casUser.getId());
            return new ResultData(200,"取消成功",true);
        }
    }
}
