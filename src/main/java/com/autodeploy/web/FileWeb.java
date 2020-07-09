package com.autodeploy.web;

import com.autodeploy.responseEntity.ResultData;
import com.autodeploy.utils.commonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Desc 文件上传下载
 **/

@Slf4j
@RestController
@RequestMapping("/file")
public class FileWeb {

    @Autowired
    private Environment env;


    @Value("${image_upload}")
    private String image_upload;

    @Value("${file_upload}")
    private String file_upload;

    @Value("${image_relative_path_prefix}")
    private String imageRelativePrefix;


    @Value("${file_relative_path_prefix}")
    private String fileRelativePrefix;


    /**
     * 实现文件上传
     */
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    public ResultData upload(@RequestParam("file") MultipartFile file,
                             @RequestParam("type") String type) {
        String fileName = file.getOriginalFilename();
        if(!checkFileExt(fileName)) {
            return new ResultData(-1,"不允许的文件格式",false);
        }

        String uploadPath = "";
        String relativePathPrefix = "";
        if ("image".equals(type)) {
            uploadPath = env.getProperty("image_upload");
            relativePathPrefix = imageRelativePrefix;
        } else {
            uploadPath = env.getProperty("file_upload");
            relativePathPrefix = fileRelativePrefix;
        }

        //为了防止文件重复
        String uuid = commonUtil.getUUID();
        String newFileName = uuid + "uuid_" + fileName;

        String subPath = "/" + newFileName;
        String destPath = uploadPath + subPath;

        File dest = new File(destPath);

        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);

            HashMap<String, String> map = new HashMap<>();
            map.put("fileName", fileName);
            map.put("filePath", relativePathPrefix + subPath);
            return new ResultData(200, "上传成功", true, map);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return new ResultData(-1, "上传失败", false);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultData(-1, "上传失败", false);
        }
    }


    /**
     * 实现多文件上传
     */
    @RequestMapping(value = "/multiUpload", method = RequestMethod.POST)
    public ResultData multiUpload(@RequestParam(value = "type") String type,
                                  @RequestBody List<MultipartFile> files,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        if (files.isEmpty()) {
            return new ResultData(-1, "上传文件为空", false);
        }
        //文件类型验证
        Object[] fileNames = files.stream().map(file -> file.getOriginalFilename()).toArray();
        for (int i = 0; i < fileNames.length; i++) {
            if(!checkFileExt(fileNames[i].toString())) {
                return new ResultData(-1,"存在不允许的文件格式",false);
            }
        }

        int i = 0;
        ArrayList<Object> retList = new ArrayList<>();
        for (MultipartFile file : files) {
            i++;
            try {
                String fileName = file.getOriginalFilename();

                String uploadPath = "";
                String relativePathPrefix = "";
                if ("image".equals(type)) {
                    uploadPath = env.getProperty("image_upload");
                    relativePathPrefix = imageRelativePrefix;
                } else {
                    uploadPath = env.getProperty("file_upload");
                    relativePathPrefix = fileRelativePrefix;
                }

                //为了防止文件重复
                String uuid = commonUtil.getUUID();
                String newFileName = uuid + "uuid_" + fileName;

                String subPath = "/" + newFileName;
                String destPath = uploadPath + subPath;

                File dest = new File(destPath);

                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);
                HashMap<String, Object> resultMap = new HashMap<>();

                resultMap.put("fileName", fileName);
                resultMap.put("filePath", relativePathPrefix + subPath);
                resultMap.put("success", true);
                retList.add(resultMap);
            } catch (IOException e) {
                log.error("上传异常:{}", e);
                HashMap<String, Object> resultMap = new HashMap<>();
                resultMap.put("success", false);
                resultMap.put("fileName", file.getName());
                retList.add(resultMap);
                e.printStackTrace();
//                    continue;
                return new ResultData(-1, "上传失败", false);
            }
        }
        return new ResultData(200, "上传成功", true, retList);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public String downLoad(HttpServletResponse response, @RequestParam(value = "filePath") String filePath) {
        try {

            String fileName = filePath.substring(filePath.indexOf("uuid_") + 5);
            if(fileName.contains("../") || fileName.contains("./") || fileName.contains("..\\")||fileName.contains(".\\")) {
                return "文件名参数存在非法字符";
            }
            if(!checkFileExt(fileName)) {
                return "不被允许下载的文件格式";
            }
            String uploadPath = env.getProperty("downloadPath");
            String absPath = uploadPath + filePath;
            File file = new File(absPath);
            if (file.exists()) {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Length",String.valueOf(file.length()));
                response.setContentType("application/force-download");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream os = null; //输出流
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
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
                } finally {
                    fis.close();
                }
            }
            return null;
        } catch (Exception e) {
            log.error("下载异常", e);
            return "下载异常";
        }
    }

    private boolean checkFileExt(String fileName) {
        String allowedFileExt = env.getProperty("allowedFileExt");
        allowedFileExt = allowedFileExt.toLowerCase();

        String[] allowedExtArr = allowedFileExt.split(",");
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        ext = ext.toLowerCase();

        if(Arrays.asList(allowedExtArr).contains(ext)) {
            return true;
        }
        return false;
    }
}
