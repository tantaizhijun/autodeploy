package com.asiainfo.casebase.service.draftService;

import com.asiainfo.casebase.entity.casUser.CasUser;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryDraftVal;
import com.asiainfo.casebase.repository.casebase.CaseLibraryDraftRepository;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.service.UserService;
import com.asiainfo.casebase.service.logsService.LogService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Desc 草稿箱业务
 **/

@Service
public class DraftServiceImpl implements DraftService {

    @Autowired
    private UserService userService;

    @Autowired
    private CaseLibraryDraftRepository draftRepository;

    @Autowired
    private LogService logService;

    /**
     * @Desc 保存或更新
     **/
    @Override
    public ResultData saveOrUpdate(NmCaseLibraryDraftVal draftVal) {

        CasUser casUser = userService.findUserFromCas();
        if (casUser == null) {
            return new ResultData(-1, "获取用户信息失败", false);
        }
        draftVal.setPublisher(casUser.getId());
        draftVal.setPublisherCn(casUser.getName());

        Date date = new Date();
        draftVal.setCreated(date);
        draftVal.setUpdateBy(casUser.getId());
        draftVal.setUpdateByCn(casUser.getName());
        draftVal.setUpdateTime(date);
        NmCaseLibraryDraftVal val = draftRepository.save(draftVal);
        String operType = draftVal.getId() ==null ? "新增" : "修改";
        logService.save("CASEBASE:nm_case_library_draft_val",val.getId().toString(),operType,operType+"草稿","成功",
                operType+"草稿",1,0,casUser.getId());

        return new ResultData(200,"保存成功",true);
    }

    /**
     * @Desc 删除
     **/
    @Override
    public void delete(Long draftId) {
        draftRepository.deleteById(draftId);

        CasUser casUser = userService.findUserFromCas();
        logService.save("CASEBASE:nm_case_library_draft_val", draftId.toString(),"删除","删除草稿","成功",
                "删除草稿",1,0,casUser.getId());
    }
}
