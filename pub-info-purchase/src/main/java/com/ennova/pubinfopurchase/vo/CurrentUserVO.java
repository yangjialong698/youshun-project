package com.ennova.pubinfopurchase.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2022/7/12
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
