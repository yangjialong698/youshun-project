package com.ennova.pubinfocommon.utils;

import cn.hutool.core.date.DateUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Mr.Lou
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/21
 * @Description: com.ennova.pubinfocommon.utils
 * @Version: 1.0
 */
@Slf4j
public class FileUtils {

    @SneakyThrows
    public static HashMap<String, String> uploadFile(MultipartFile file, String localPath, String[] fileSuffix) {
        // List<String> suffixList = Arrays.asList(fileSuffix);
        HashMap<String, String> map = new HashMap();
        //获取文件md5值
        String fileMd5 = DigestUtils.md5DigestAsHex(file.getInputStream());
        //获取文件扩展名        名称
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String originalName = fileName.substring(0, fileName.lastIndexOf("."));
        // 后缀
        String expandedName = fileName.substring(fileName.lastIndexOf("."));

        String newfileName = fileMd5 + expandedName;

        String dayStr = DateUtil.format(new Date(), "yyyy-MM-dd");
        String[] dayArr = dayStr.split("-");
        String year = dayArr[0];
        String month = dayArr[1];
        String newPath = localPath + File.separator + year + "/" + month;

        File monthFile = new File(newPath);
        if (!monthFile.exists() && !monthFile.isDirectory()) {
            monthFile.mkdirs();
        }
        File outFile = new File(monthFile + "/" + newfileName);

        FileOutputStream writeFile = new FileOutputStream(outFile);
        IOUtils.copy(file.getInputStream(), writeFile);
        String fileSize = FileUtils.formatFileSize(outFile.length());

        map.put("originalName", originalName);
        map.put("newfileName", newfileName);
        map.put("fileMd5", fileMd5);
        map.put("fileName", fileName);
        map.put("year", year);
        map.put("month", month);
        map.put("fileSize", fileSize);
        return map;
    }


    public static String formatFileSize(Long fileLength) {
        String fileSizeString = "";
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileLength != null) {
            if (fileLength < 1024) {
                fileSizeString = df.format((double) fileLength) + "B";
            } else if (fileLength < 1048576) {
                fileSizeString = df.format((double) fileLength / 1024) + "K";
            } else if (fileLength < 1073741824) {
                fileSizeString = df.format((double) fileLength / 1048576) + "M";
            } else {
                fileSizeString = df.format((double) fileLength / 1073741824) + "G";
            }
        }
        return fileSizeString;
    }

    @SneakyThrows
    public static String getFileMd5(MultipartFile file, String localPath) {
        //获取文件md5值
        String fileMd5 = DigestUtils.md5DigestAsHex(file.getInputStream());
        //获取文件扩展名
        String fileName = file.getOriginalFilename();
        // 后缀
        assert fileName != null;
        String expandedName = fileName.substring(fileName.lastIndexOf("."));
        String dayStr = DateUtil.format(new Date(), "yyyy-MM-dd");
        String[] dayArr = dayStr.split("-");
        String year = dayArr[0];
        String month = dayArr[1];
        return year + "/" + month + "/" + fileMd5 + expandedName;
    }


}
