package com.ennova.pubinfotask.vo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: shibingyang1990@gmail.com
 * @Date: 2022/5/13
 * @Description: 子任务添加修改报工，前端入参
 * @Version: 1.0
 */
@Data
public class BaseVO<T>{
    public List<T> list;

}
