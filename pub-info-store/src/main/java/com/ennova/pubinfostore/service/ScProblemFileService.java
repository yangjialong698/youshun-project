package com.ennova.pubinfostore.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.utils.FileUtils;
import com.ennova.pubinfocommon.utils.JWTUtil;
import com.ennova.pubinfocommon.vo.UserVO;
import com.ennova.pubinfostore.dao.ScProblemFileMapper;
import com.ennova.pubinfostore.entity.ScProblemFile;
import com.ennova.pubinfostore.vo.FileVO;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ScProblemFileService {

    private final HttpServletRequest request;
    private final ScProblemFileMapper scProblemFileMapper;

    /**
     * 本地路径
     */
    @Value("${spring.upload.local.path}")
    private String localPath;

    /**
     * 访问url
     */
    @Value("${spring.upload.local.url}")
    private String localUrl;

    /**
     * 支持文件
     */
    @Value("${file.suffix}")
    private String[] fileSuffix;

    public Callback<FileVO> selectImageFile(MultipartFile file) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            return Callback.error(2, "上传文件不能为空!");
        }
        HashMap<String, String> map = FileUtils.uploadFile(file, localPath, fileSuffix);
        if (StringUtils.isNotBlank(map.get("error"))) {
            return Callback.error(2, map.get("error"));
        }
        String subname = map.get("year") + "/" + map.get("month") + "/" + map.get("newfileName");
        String newName =  map.get("year") + "/" + map.get("month") + "/" + "idx_" + map.get("newfileName") ;

        String oldPath = localPath + "/" + subname;
        String newPath = localPath + "/" + newName;

        try {
            IOUtils.copy(new FileInputStream(oldPath), new FileOutputStream(newPath));
            doWithPhoto(newPath);
        } catch (IOException e) {
            log.info("生成缩略图失败");
            e.printStackTrace();
        }

        ScProblemFile scProblemFile = ScProblemFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).ysFileUrl(localUrl + "/file/" + newName).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(new Date()).build();
        int count = scProblemFileMapper.insertSelective(scProblemFile);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(scProblemFile.getId()).fileName(map.get("fileName")).newfileName(subname).ysFileUrl(localUrl + "/file/" + newName).build();
            return Callback.success(fileVo);
        }
        return Callback.error(2, "选择文件失败!");
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

    /*public Callback<FileVO> selectVideoFile(MultipartFile file) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        assert userVo != null;
        if (ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            return Callback.error(2, "上传文件不能为空!");
        }
        HashMap<String, String> map = FileUtils.uploadFile(file, localPath, fileSuffix);
        if (StringUtils.isNotBlank(map.get("error"))) {
            return Callback.error(2, map.get("error"));
        }
        String subname = map.get("year") + "/" + map.get("month") + "/" + map.get("newfileName");
        String newName = map.get("year") + "/" + map.get("month") + "/" + "idx_" + map.get("newfileName");

        String oldPath = localPath + "/" + subname;
        String newPath = localPath + "/" + newName;

        try {
            IOUtils.copy(new FileInputStream(oldPath), new FileOutputStream(newPath));
            doWithVideo(oldPath, newPath);
        } catch (IOException e) {
            log.info("压缩视频失败");
            e.printStackTrace();
        }

        ScProblemFile scProblemFile = ScProblemFile.builder().fileMd5(subname).fileUrl(localUrl + "/file/" + subname).ysFileUrl(localUrl + "/file/" + newName).name(map.get("fileName"))
                .fileSize(map.get("fileSize")).openFile(0).delFlag(0).userId(userVo.getId()).createTime(new Date()).build();
        int count = scProblemFileMapper.insertSelective(scProblemFile);
        if (count > 0) {
            FileVO fileVo = FileVO.builder().id(scProblemFile.getId()).fileName(map.get("fileName")).newfileName(subname).build();
            return Callback.success(fileVo);
        }
        return Callback.error(2, "选择文件失败!");
    }

    private static void doWithVideo(String oldPath, String newPath) {

        File newfile = new File(newPath);
        File oldfile = new File(oldPath);
        if (!newfile.exists()) {
            return;
        }
        try {
            MultimediaObject object = new MultimediaObject(oldfile);
            AudioInfo audioInfo = object.getInfo().getAudio();
            // 根据视频⼤⼩来判断是否需要进⾏压缩,
            int maxSize = 5;
            double mb = Math.ceil(oldfile.length() / 1048576);
            int second = (int) object.getInfo().getDuration() / 1000;
            BigDecimal bd = new BigDecimal(String.format("%.4f", mb / second));
            System.out.println("开始压缩视频了--> 视频每秒平均 " + bd + " MB ");
            // 视频 > 5MB, 或者每秒 > 0.5 MB 才做压缩，不需要的话可以把判断去掉
            boolean temp = mb > maxSize || bd.compareTo(new BigDecimal(0.5)) > 0;
            if (temp) {
                long time = System.currentTimeMillis();
                // TODO 视频属性设置
                int maxBitRate = 128000;
                int maxSamplingRate = 44100;
                int bitRate = 800000;
                int maxFrameRate = 20;
                int maxWidth = 1280;
                AudioAttributes audio = new AudioAttributes();
                // 设置通⽤编码格式10 audio.setCodec("aac");
                // 设置最⼤值：⽐特率越⾼，清晰度/⾳质越好
                // 设置⾳频⽐特率,单位:b (⽐特率越⾼，清晰度/⾳质越好，当然⽂件也就越⼤ 128000 = 182kb)
                if (audioInfo.getBitRate() > maxBitRate) {
                    audio.setBitRate(new Integer(maxBitRate));
                }
                // 设置重新编码的⾳频流中使⽤的声道数（1 =单声道，2 = 双声道（⽴体声））。如果未设置任何声道值，则编码器将选择默认值 0。
                audio.setChannels(audioInfo.getChannels());
                // 采样率越⾼声⾳的还原度越好，⽂件越⼤
                // 设置⾳频采样率，单位：赫兹 hz
                // 设置编码时候的⾳量值，未设置为0,如果256，则⾳量值不会改变
                // audio.setVolume(256);
                if (audioInfo.getSamplingRate() > maxSamplingRate) {
                    audio.setSamplingRate(maxSamplingRate);
                }
                // TODO 视频编码属性配置
                VideoInfo videoInfo = object.getInfo().getVideo();
                VideoAttributes video = new VideoAttributes();
                video.setCodec("h264");
                // 设置⾳频⽐特率,单位:b (⽐特率越⾼，清晰度/⾳质越好，当然⽂件也就越⼤ 800000 = 800kb)
                if (videoInfo.getBitRate() > bitRate) {
                    video.setBitRate(bitRate);
                }
                // 视频帧率：15 f / s 帧率越低，效果越差
                // 设置视频帧率（帧率越低，视频会出现断层，越⾼让⼈感觉越连续），视频帧率（Frame
                // rate）是⽤于测量显⽰帧数的量度。所谓的测量单位为每秒显⽰帧数(Frames per Second，简：FPS）或“赫兹”（Hz）。
                if (videoInfo.getFrameRate() > maxFrameRate) {
                    video.setFrameRate(maxFrameRate);
                }
                // 限制视频宽⾼
                int width = videoInfo.getSize().getWidth();
                int height = videoInfo.getSize().getHeight();
                if (width > maxWidth) {
                    float rat = (float) width / maxWidth;
                    video.setSize(new VideoSize(maxWidth, (int) (height / rat)));
                }
                EncodingAttributes attr = new EncodingAttributes();
                // attr.setFormat("mp4");
                attr.setAudioAttributes(audio);
                attr.setVideoAttributes(video);
                // 速度最快的压缩⽅式，压缩速度从快到慢： ultrafast, superfast, veryfast, faster, fast, medium,
                // slow, slower, veryslow and placebo.
                // attr.setPreset(PresetUtil.VERYFAST);
                // attr.setCrf(27);
                // 设置线程数
                // attr.setEncodingThreads(Runtime.getRuntime().availableProcessors() / 2);
                Encoder encoder = new Encoder();
                encoder.encode(new MultimediaObject(oldfile), newfile, attr);
                System.out.println("压缩总耗时：" + (System.currentTimeMillis() - time) / 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    public void netDownLoadFile(String netAddress, String filename, HttpServletResponse response) throws Exception {
        URL url;
        URLConnection conn;
        InputStream inputStream = null;
        try {
            // 这里填文件的url地址
            url = new URL(netAddress);
            conn = url.openConnection();
            inputStream = conn.getInputStream();
            response.setContentType(conn.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename="
                    + URLEncoder.encode(filename, "UTF-8"));
            byte[] buffer = new byte[1024];
            int len;
            OutputStream outputStream = response.getOutputStream();
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Callback deleteFile(FileVO fileDelDTO) {
        String token = request.getHeader("Authorization");
        UserVO userVo = JWTUtil.getUserVOByToken(token);
        String path = localPath + "/" + fileDelDTO.getNewfileName();
        String ysPath = localPath + "/" + fileDelDTO.getYsFileUrl().substring(fileDelDTO.getYsFileUrl().indexOf("/file") + 6);
        assert userVo != null;
        List<ScProblemFile> files = scProblemFileMapper.selectAllByFileMd5(fileDelDTO.getNewfileName());
        if (files != null && !files.isEmpty()) {
            File file = new File(path);
            File ysFile = new File(ysPath);
            if (file.exists()) {
                //查看是否唯一
                int count = scProblemFileMapper.selectByFileMd5(fileDelDTO.getNewfileName());
                    if (count == 1) {
                        file.delete();
                        ysFile.delete();
                    }
                scProblemFileMapper.deleteByPrimaryKey(fileDelDTO.getId());
                }
            }
        return Callback.success("附件删除成功");
    }
}
