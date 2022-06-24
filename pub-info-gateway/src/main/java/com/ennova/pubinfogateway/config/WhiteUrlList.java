package com.ennova.pubinfogateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@SuppressWarnings("ConfigurationProperties")
@Data
@Component
@ConfigurationProperties(prefix="white-url")
public class WhiteUrlList {
    private List<String> list;
}
