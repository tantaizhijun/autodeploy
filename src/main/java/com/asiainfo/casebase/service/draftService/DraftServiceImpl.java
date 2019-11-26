package com.asiainfo.casebase.service.draftService;

import com.asiainfo.casebase.entity.casUser.CasUser;
import com.asiainfo.casebase.entity.casebase.NmCaseLibraryDraftVal;
import com.asiainfo.casebase.repository.casebase.CaseLibraryDraftRepository;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.service.UserService;
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
        draftRepository.save(draftVal);

        return new ResultData(200,"保存成功",true);
    }

    /**
     * @Desc 删除
     **/
    @Override
    public void delete(Long draftId) {
        draftRepository.deleteById(draftId);
    }
}
