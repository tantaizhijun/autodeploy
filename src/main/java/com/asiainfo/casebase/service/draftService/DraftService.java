package com.asiainfo.casebase.service.draftService;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryDraftVal;
import com.asiainfo.casebase.responseEntity.ResultData;
/***
 * @Desc 草稿箱接口
 **/
public interface DraftService {

    /**
     * @Desc 保存或更新草稿
     **/
    ResultData saveOrUpdate(NmCaseLibraryDraftVal draftVal);


    /**
     * @Desc 删除草稿
     **/
    void delete(Long draftId);



}
