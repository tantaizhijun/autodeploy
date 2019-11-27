package com.asiainfo.casebase.service.ArticleService;

import com.asiainfo.casebase.entity.casebase.NmCaseLibraryCurVal;
import com.asiainfo.casebase.responseEntity.ResultData;

public interface ArticleService {

    /**
     * @Desc 保存或更新文章
     **/
    ResultData saveOrUpdate(NmCaseLibraryCurVal caseLibraryCur);


    /**
     * @Desc 删除文章
     **/
    ResultData delete(Long caseLibraryCurId);



    /**
     * @Desc 收藏文章
     **/
    ResultData collect(Long caseLibraryCurId,Boolean type);



}
