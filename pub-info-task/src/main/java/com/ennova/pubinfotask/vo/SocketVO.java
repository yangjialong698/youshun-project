package com.ennova.pubinfotask.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @className: SocketVO
 * @Description: socketVO返回对象
 * @author: shibingyang1990@gmail.com
 * @date: 2022/6/22 11:04:30
 */
@Builder
@Data
public class SocketVO<T> {

    //来源：0 公告 1 供应商认证
    private int sourceType;

    //类型 0 - 新增  1- 审核通过 2-审核不通过  3-修改
    private int type;

    //内容
    private T content;

}