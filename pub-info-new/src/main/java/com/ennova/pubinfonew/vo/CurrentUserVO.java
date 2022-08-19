package com.ennova.pubinfonew.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserVO {

    private Integer userId;

    private String username;

    private String jobNum;

}
