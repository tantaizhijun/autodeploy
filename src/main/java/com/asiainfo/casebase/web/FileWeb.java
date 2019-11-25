package com.asiainfo.casebase.web;

import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.utils.commonUtil;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Desc 文件上传下载
 **/

@Slf4j
@ApiModel(value = "文件上传下载相关接口")
@RestController
@RequestMapping("/file")
public class FileWeb {

    @Autowired
    private Environment env;

    /**
     * 实现文件上传
     * */
    @RequestMapping(value = "upload")
    public ResultData upload(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return new ResultData(-1,"请选择文件",false);
        }
        String fileName = file.getOriginalFilename();
        String uploadPath = env.getProperty("file_upload");

        //为了防止文件重复
        String uuid = commonUtil.getUUID();
        String newFileName = fileName + "." + uuid;
        String destPath = uploadPath + "/" + newFileName;

        File dest = new File(destPath);

        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);

            HashMap<String, String> map = new HashMap<>();
            map.put("fileName",fileName);
            map.put("uuid",uuid);
            map.put("filePath",uploadPath);
            map.put("absPath",destPath);

            return new ResultData(200,"上传成功", true,map);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return new ResultData(-1,"上传失败",false);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultData(-1,"上传失败",false);
        }
    }


    /**
     * 实现多文件上传
     * */
    @RequestMapping(value="/multiUpload",method= RequestMethod.POST)
    public ResultData multiUpload(HttpServletRequest request){

        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        if(files.isEmpty()){
            return new ResultData(-1,"上传文件为空",false);
        }
        String uploadPath = env.getProperty("file_upload");
        int i = 0;
        ArrayList<Object> retList = new ArrayList<>();
        for(MultipartFile file:files){
            i++;
            if(file.isEmpty()){
                //todo 有文件为空的处理
                return new ResultData(-1,"第" + i +"个文件文件为空,上传失败",false);
            }else{
                String fileName = file.getOriginalFilename();
                //为了防止文件重复
                String uuid = commonUtil.getUUID();
                String newFileName = fileName + "." + uuid;
                String destPath = uploadPath + "/" + newFileName;

                File dest = new File(destPath);
                if(!dest.getParentFile().exists()){
                    dest.getParentFile().mkdirs();
                }
                try {
                    file.transferTo(dest);
                    HashMap<String, String> resultMap = new HashMap<>();

                    resultMap.put("fileName",fileName);
                    resultMap.put("uuid",uuid);
                    resultMap.put("filePath",uploadPath);
                    resultMap.put("absPath",destPath);
                    retList.add(resultMap);
                }catch (Exception e) {
                    e.printStackTrace();
                    return  new ResultData(-1,"上传失败",false);
                }
            }
        }
        return new ResultData(200,"上传成功", true,retList);
    }


    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public String downLoad(HttpServletResponse response, @RequestParam(value = "fileName") String fileName,
                           @RequestParam(value = "uuid") String uuid) {
        try {
            String uploadPath = env.getProperty("file_upload");
            String absPath = uploadPath + "/" + fileName + "." + uuid;
            File file = new File(absPath);
            if(file.exists()){
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName,"UTF-8"));

                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream os = null; //输出流
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while(i != -1){
                        os.write(buffer);
                        i = bis.read(buffer);
                    }
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    fis.close();
                }
            }
            return null;
        }catch (Exception e){
            log.error("下载异常",e);
            return "下载异常";
        }
    }

}
