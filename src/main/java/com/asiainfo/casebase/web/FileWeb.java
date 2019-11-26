package com.asiainfo.casebase.web;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.casebase.responseEntity.ResultData;
import com.asiainfo.casebase.utils.DateUtils;
import com.asiainfo.casebase.utils.commonUtil;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
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


    @Value("image_upload")
    private String image_upload;

    @Value("file_upload")
    private String file_upload;


    @RequestMapping("/upfile")
    @ResponseBody
    public void umeditorUpload(@RequestParam("upfile") MultipartFile file,
                               @RequestParam("type") String type,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IllegalStateException, IOException {


        String uploadPath = "";
        if ("image".equals(type)) {
            uploadPath = env.getProperty("image_upload");
        } else {
            uploadPath = env.getProperty("file_upload");
        }
        String date = DateUtils.dateToStringFormat(new Date(), "yyyy-MM-dd");
        uploadPath = uploadPath + "/" + date;


        String newFileName = commonUtil.getUUID() + "_" + file.getOriginalFilename();

        File dir = new File(uploadPath, newFileName);

        String src = uploadPath + "/" + newFileName;

        JSONObject json = new JSONObject();
        if (!dir.exists()) {
            dir.mkdirs();
            json.put("state", "SUCCESS");
            json.put("original", file.getOriginalFilename());
            json.put("size", file.getSize());
            json.put("url", src);
            json.put("title", newFileName);
        } else {
            json.put("state", "FALSE");
        }
        file.transferTo(dir);
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.write(json.toString());
        writer.close();
    }



    /**
     * 实现文件上传
     * */
    @RequestMapping(value = "upload")
    public ResultData upload(@RequestParam("file") MultipartFile file,
                             @RequestParam("type") String type){
        if(file.isEmpty()){
            return new ResultData(-1,"请选择文件",false);
        }
        String fileName = file.getOriginalFilename();


        String uploadPath = "";
        if ("image".equals(type)) {
            uploadPath = env.getProperty("image_upload");
        } else {
            uploadPath = env.getProperty("file_upload");
        }
        String date = DateUtils.dateToStringFormat(new Date(), "yyyy-MM-dd");
        uploadPath = uploadPath + "/" + date;

        //为了防止文件重复
        String uuid = commonUtil.getUUID();
        String newFileName = uuid + "_" + fileName;
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
    public ResultData multiUpload(@RequestParam(value = "type") String type,
                                  @RequestBody List<MultipartFile> files,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        if(files == null || files.size() == 0) {
            files = ((MultipartHttpServletRequest)request).getFiles("files");
        }

        if(files.isEmpty()){
            return new ResultData(-1,"上传文件为空",false);
        }

        String uploadPath = "";
        if ("image".equals(type)) {
            uploadPath = env.getProperty("image_upload");
        } else {
            uploadPath = env.getProperty("file_upload");
        }
        String date = DateUtils.dateToStringFormat(new Date(), "yyyy-MM-dd");
        uploadPath = uploadPath + "/" + date;

        int i = 0;
        ArrayList<Object> retList = new ArrayList<>();
        for(MultipartFile file:files){
            i++;
            if(file.isEmpty()){
                //todo 有文件为空的处理
                //return new ResultData(-1,"第" + i +"个文件文件为空,上传失败",false);
                HashMap<String, Object> resultMap = new HashMap<>();
                resultMap.put("success",false);
                resultMap.put("fileName",file.getName());

                retList.add(resultMap);
                continue;
            }else{
                try {
                    String fileName = file.getOriginalFilename();
                    //为了防止文件重复
                    String uuid = commonUtil.getUUID();
                    String newFileName = uuid + "_" + fileName;
                    String destPath = uploadPath + "/" + newFileName;

                    File dest = new File(destPath);
                    if(!dest.getParentFile().exists()){
                        dest.getParentFile().mkdirs();
                    }

                    file.transferTo(dest);
                    HashMap<String, Object> resultMap = new HashMap<>();

                    resultMap.put("fileName",fileName);
                    resultMap.put("uuid",uuid);
                    resultMap.put("filePath",uploadPath);
                    resultMap.put("absPath",destPath);
                    resultMap.put("success",true);
                    retList.add(resultMap);
                }catch (Exception e) {
                    HashMap<String, Object> resultMap = new HashMap<>();
                    resultMap.put("success",false);
                    resultMap.put("fileName",file.getName());
                    retList.add(resultMap);
                    e.printStackTrace();
                    continue;
//                    return  new ResultData(-1,"上传失败",false);
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
