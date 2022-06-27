package com.ennova.pubinfotask.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @BelongsProject: public-info
 * @BelongsPackage: com.ennova.pubinfotask.dto
 * @Author: shibingyang1990@gmail.com
 * @CreateTime: 2022-05-25  13:59
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@Builder
public class UserMasterDTO {
     private Set<Integer> userList;
     private Set<Integer> masterIds;
}
