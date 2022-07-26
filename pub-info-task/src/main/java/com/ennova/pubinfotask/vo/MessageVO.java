package com.ennova.pubinfotask.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessageVO<T> {
    //来源：0 公告 1 供应商认证  2 日报  3 经验建议
    private int sourceType;

    //类型 0 - 新增  1- 审核通过 2-审核不通过  3-修改  4.日报反馈  5.经验建议评论
    private int type;

    //内容
    private T content;

    //创建用户
    private String userId;

    //源Id
    private Integer backId;
}
