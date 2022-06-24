package com.ennova.pubinfotask.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/4/29
 * @Description: com.ennova.pubinfotask.vo
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserVO {

    private Integer userId;

    private String username;

    private String roleName;

    private String roleCode;

}
