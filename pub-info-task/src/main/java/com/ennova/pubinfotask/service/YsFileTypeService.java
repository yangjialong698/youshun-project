package com.ennova.pubinfotask.service;

import com.ennova.pubinfocommon.entity.Callback;
import com.ennova.pubinfocommon.vo.BaseVO;
import com.ennova.pubinfocommon.vo.PageUtil;
import com.ennova.pubinfotask.dao.YsFileTypeMapper;
import com.ennova.pubinfotask.dao.YsMasterFileMapper;
import com.ennova.pubinfotask.entity.YsFileType;
import com.ennova.pubinfotask.entity.YsMasterFile;
import com.ennova.pubinfotask.vo.YsFileTypeVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/22
 * @Description: com.ennova.pubinfotask.service
 * @Version: 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class YsFileTypeService {

    private final YsFileTypeMapper ysFileTypeMapper;
    private final YsMasterFileMapper ysMasterFileMapper;

    public Callback insertOrUpdate(YsFileTypeVO vo) {

        List<YsFileType> prefixs = ysFileTypeMapper.selectAllByFilePrefixAndName(vo.getFilePrefix(), null);
        List<YsFileType> names = ysFileTypeMapper.selectAllByFilePrefixAndName(null, vo.getName());


        YsFileType ysFileType = new YsFileType();
        BeanUtils.copyProperties(vo, ysFileType);
        ysFileType.setDelFlag(0);
        if (vo.getId() != null) {
            ysFileType.setUpdateTime(LocalDateTime.now());
            ysFileType.setName(null); // 只更新状态
            ysFileType.setFilePrefix(null);
            int i = ysFileTypeMapper.updateByPrimaryKeySelective(ysFileType);
            if (i > 0) {
                return Callback.success(true);
            }
        } else {
            if (prefixs != null && !prefixs.isEmpty()){
                return Callback.error(2,"不能重复添加此前缀!");
            }
            if (names != null && !names.isEmpty()){
                return Callback.error(2,"不允许重复添加此类型名称!");
            }
            ysFileType.setCreateTime(LocalDateTime.now());
            int i = ysFileTypeMapper.insert(ysFileType);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2,"新增或修改,操作失败!");
    }

    public Callback updateByPrimaryKey(Integer id) {
        if (id != null) {
            List<YsMasterFile> fileList = ysMasterFileMapper.selectByYsFileTypeId(id);
            if(CollectionUtils.isNotEmpty(fileList)){
                return Callback.error(2,"该类型正在使用，不能删除");
            }
            YsFileType ysFileType = new YsFileType();
            ysFileType.setId(id);
            ysFileType.setDelFlag(1);
            int i = ysFileTypeMapper.updateByPrimaryKeySelective(ysFileType);
            if (i > 0) {
                return Callback.success(true);
            }
        }
        return Callback.error(2,"操作失败！");
    }
    
    public Callback<BaseVO<YsFileType>> selectByNameList(Integer page, Integer pageSize, String name){
        Page<YsFileType> startPage = PageMethod.startPage(page, pageSize);
        List<YsFileType> list = ysFileTypeMapper.selectByNameList(name);
        BaseVO<YsFileType> baseVO = new BaseVO<>(list, new PageUtil(pageSize, (int) startPage.getTotal(), page));
        return Callback.success(baseVO);
    }

    public Callback<List<YsFileType>> selectAll(){
        List<YsFileType> ysFileTypes = ysFileTypeMapper.selectAll();
        return Callback.success(ysFileTypes);
    }

}

