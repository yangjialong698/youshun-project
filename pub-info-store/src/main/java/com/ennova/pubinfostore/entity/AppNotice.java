package com.ennova.pubinfostore.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AppNotice {
    public String id;

    public String title;

    public String content;

    public Byte platform;

    public String userid;

    public String cid;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createTime;
}