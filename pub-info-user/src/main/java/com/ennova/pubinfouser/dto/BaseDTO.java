package com.ennova.pubinfouser.dto;

import com.ennova.pubinfouser.entity.DeptEntity;
import com.ennova.pubinfouser.entity.RoleEntity;
import com.ennova.pubinfouser.entity.UserEntity;
import org.springframework.beans.BeanUtils;

/**
 * @author WangWei
 * @version 1.0
 * @CreateTime: 2022-04-20
 */
public class BaseDTO {
    public static RoleEntity convertBean(RoleDTO dto){
        RoleEntity entity = new RoleEntity();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static UserEntity convertBean(UserDTO dto){
        UserEntity entity = new UserEntity();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static DeptEntity convertBean(DeptDTO dto){
        DeptEntity entity = new DeptEntity();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

}
