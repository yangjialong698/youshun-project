package com.ennova.pubinfotask.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfotask.dao.GwMessageMapper;
import com.ennova.pubinfotask.dao.OpinionBoxFileMapper;
import com.ennova.pubinfotask.dao.OpinionBoxMapper;
import com.ennova.pubinfotask.dao.UserMapper;
import com.ennova.pubinfotask.entity.GwMessage;
import com.ennova.pubinfotask.entity.OpinionBox;
import com.ennova.pubinfotask.entity.OpinionBoxFile;
import com.ennova.pubinfotask.utils.BeanConvertUtils;
import com.ennova.pubinfotask.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class OpinionBoxService{

    private final OpinionBoxMapper boxMapper;
    private final OpinionBoxFileMapper boxFileMapper;
    private final UserMapper userMapper;
    private final HttpServletRequest req;

    // 在线留言
    private final GwMessageMapper gwMessageMapper;

    /**
     * 访问url
     */
    @Value("${spring.upload.local.url}")
    private String localUrl;
    /**
     * 本地路径
     */
    @Value("${spring.upload.local.path}")
    private String localPath;
    /**
     * 支持文件
     */
    @Value("${file.suffix}")
    private String[] fileSuffix;

    //上传附件
    public Callback<FileVO> uploadFile(MultipartFile file) {
//        String token = req.getHeader("Authorization");
//        UserVO userVo = JWTUtil.getUserVOByToken(token);
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            log.info("上传文件大小为空!");
            return Callback.error("上传文件不能为空!");
        }

        HashMap<String, String> map = FileUtils.uploadFile(file, localPath, fileSuffix);
        if (StringUtils.isNotBlank(map.get("error"))){
            return Callback.error( map.get("error"));
        }

        String subname =  map.get("year") + "/" + map.get("month") + "/" + map.get("newfileName");
        String newName =  map.get("year") + "/" + map.get("month") + "/" + "idx_" + map.get("newfileName") ;

        String oldPath = localPath + "/" + subname;
        String newPath = localPath + "/" + newName;

        try {
            //Thumbnails.of(oldPath).scale(0.2f).outputQuality(0.001f).toFile(newPath);
            // 生成缩略图
            //Thumbnails.of(oldPath).scale(0.4f).toFile(newPath);
            IOUtils.copy(new FileInputStream(oldPath), new FileOutputStream(newPath));
            doWithPhoto(newPath);
        } catch (IOException e) {
            log.info("生成缩略图失败");
            e.printStackTrace();
        }

        OpinionBoxFile fileVO = OpinionBoxFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).name(map.get("fileName")) //.userId(userVo.getId())
                .fileSize(map.get("fileSize")).delFlag(0).createTime(new Date()).build();
        int count = boxFileMapper.insertSelective(fileVO);
        if (count > 0) {
            FileVO resultVO = FileVO.builder().id(fileVO.getId()).newfileName(localUrl + "/file/" + subname).build();
            return Callback.success(resultVO);
        }
        return Callback.error("附件上传失败!");
    }


    //删除附件
    public Callback deleteFile(Integer id, String filePath) {
        if (ObjectUtils.isEmpty(filePath)) {
            log.info("路径不能为空!");
            return Callback.error("上传路径不能为空!");
        }
        String md5 = filePath.substring(filePath.indexOf("/file"));
        int md5Count = boxFileMapper.selectCountByFileMd5(md5);
        boxFileMapper.deleteByPrimaryKey(id);
        if (md5Count <= 1) {
            String path = localPath + md5;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        return Callback.success();
    }



    // 新增意见箱
    public Callback addOpinionBox(BaseOpinionBoxVO baseOpinionBoxVO) {
//        String token = req.getHeader("Authorization");
//        UserVO userVo = JWTUtil.getUserVOByToken(token);
//        assert userVo != null;

        OpinionBox opinionBox = BeanConvertUtils.convertTo(baseOpinionBoxVO, OpinionBox::new);
        opinionBox.setCreateTime(new Date());
        opinionBox.setDelFlag(0);
//        opinionBox.setUserId(userVo.getId());

        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        opinionBox.setName("游客"+ uuidStr.substring(0, 8));
        String fileIds = opinionBox.getYsOpinionFileId();

        List<Integer> ids = boxFileMapper.selectIdByIdList(Arrays.asList(fileIds.split(",")));

        // 如需校验是不是当前用户上传的附件
//        StringBuffer sb = new StringBuffer();
//        if (StringUtils.isNotBlank(fileIds)) {
//            String[] split = fileIds.split(",");
//            for (int i = 0; i < split.length; i++) {
//                OpinionBoxFile file = boxFileMapper.selectByPrimaryKey(Integer.parseInt(split[i]));
//                if (file != null && file.getUserId().equals(userVo.getId())) {
//                    if (i == split.length - 1) {
//                        sb.append(split[i]);
//                    } else {
//                        sb.append(split[i]).append(",");
//                    }
//                }
//            }
//        }
//        opinionBox.setYsOpinionFileId(sb.toString());

        // 不校验是不是当前用户上传的附件
        opinionBox.setYsOpinionFileId(StringUtils.join(ids, ","));
        int count = boxMapper.insertSelective(opinionBox);
        if (count > 0) {
            return Callback.success("新增意见箱成功!");
        }
        return Callback.error("新增意见箱失败!");
    }


    // 删除意见箱
    public Callback deleteOpinionBox(Integer id) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        // 只有高层才能删除附件
        OpinionBox opinionBox = boxMapper.selectByPrimaryKey(id);
        log.info("删除意见箱:{}", opinionBox != null ? opinionBox.toString() : "");
//        if (opinionBox != null && currentUserVO.getRoleCode().equals("check_person")) {
            //opinionBox.setDelFlag(1);
            //int count = boxMapper.updateByPrimaryKeySelective(opinionBox);
            int count = boxMapper.deleteByPrimaryKey(id);
            if (null != opinionBox){
                // 删除附件
                if (StringUtils.isNotBlank(opinionBox.getYsOpinionFileId())) {
                    String[] split = opinionBox.getYsOpinionFileId().split(",");
                    for (String s : split) {
                        //OpinionBoxFile file = boxFileMapper.selectByPrimaryKey(Integer.parseInt(s));
                        //file.setDelFlag(1);
                        //boxFileMapper.updateByPrimaryKeySelective(file);
                        log.info("删除附件:{}", s);
                        OpinionBoxFile boxFile = boxFileMapper.selectByPrimaryKey(Integer.parseInt(s));
                        if (boxFile != null) {
                            String path = localPath + "/" + boxFile.getFileMd5();
                            //数据库查询是否有其他人使用该附件
                            int existCount = boxFileMapper.selectCountByFileMd5(boxFile.getFileMd5());
                            if (existCount == 1) {
                                // 删除文件
                                File file = new File(path);
                                if (file.exists()) {
                                    file.delete();
                                }
                            }
                            boxFileMapper.deleteByPrimaryKey(Integer.parseInt(s));
                        }
                    }
                }
            }
            if (count > 0) {
                return Callback.success("删除意见箱成功!");
            }
//        }
        return Callback.error("无此数据!");
    }

    // 意见箱列表: 暂无查询条件
    public Callback<BaseVO<OpinionBoxVO>> opinionBoxList(Integer page, Integer pageSize) {
        String token = req.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        CurrentUserVO currentUserVO = userMapper.selectCurrentUser(userVo.getId());
        // 高层才能查询
//        if (currentUserVO.getRoleCode().equals("check_person")) {
            Page<OpinionBox> startPage = PageHelper.startPage(page, pageSize);
            List<OpinionBox> list = boxMapper.selectList();
            List<OpinionBoxVO> resultList = new ArrayList<>();
            list.forEach(opinionBox -> {
                OpinionBoxVO opinionBoxVO = BeanConvertUtils.convertTo(opinionBox, OpinionBoxVO::new);
                if (StringUtils.isNotBlank(opinionBox.getYsOpinionFileId())) {
                    String[] split = opinionBox.getYsOpinionFileId().split(",");
                    List<OpinionBoxFileVO> fileList = new ArrayList<>();
                    for (String s : split) {
                        OpinionBoxFile file = boxFileMapper.selectByPrimaryKey(Integer.parseInt(s));
                        if (file != null) {
                            OpinionBoxFileVO opinionBoxFileVO = BeanConvertUtils.convertTo(file, OpinionBoxFileVO::new);
                            String fileUrl = opinionBoxFileVO.getFileUrl();
                            if (StringUtils.isNotBlank(fileUrl)) {
                                // 从最后一个点开始截取到前面一个/的位置
                                String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
                                String prefixUrl = fileUrl.substring(0, fileUrl.lastIndexOf("/") + 1);
                                String newFileUrl = prefixUrl + "idx_" + fileName;
                                opinionBoxFileVO.setIdxFileUrl(newFileUrl);
                            }
                            fileList.add(opinionBoxFileVO);
                        }
                    }
                    opinionBoxVO.setFileVOList(fileList);
                }
                resultList.add(opinionBoxVO);
            });
            BaseVO<OpinionBoxVO> baseVO = new BaseVO<>(resultList, new PageUtil(pageSize, (int) startPage.getTotal(), page));
            return Callback.success(baseVO);
//        }
        //return Callback.error("无权限!");
    }

    /********************************************************  在线留言列表   *******************************************************************************/

    // 在线留言列表
    public Callback<BaseVO<GwMessage>> messageList(Integer page, Integer pageSize, String content){
        Page<GwMessage> startPage = PageHelper.startPage(page, pageSize);
        List<GwMessage> list = gwMessageMapper.selectByRemarkLike(content);
        BaseVO<GwMessage> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }


    private static void doWithPhoto(String path) {

        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        BufferedImage image = null;
        FileOutputStream os = null;
        try {
            image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage bfImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bfImage.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            os = new FileOutputStream(path);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
            encoder.encode(bfImage);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
