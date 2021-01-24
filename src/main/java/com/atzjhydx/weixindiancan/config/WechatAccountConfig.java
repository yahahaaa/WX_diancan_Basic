package com.atzjhydx.weixindiancan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    //公众平台id
    private String mpAppId;

    //公众平台密钥
    private String mpAppSecret;

    //商户号
    private String mchId;

    //商户key
    private String mchKey;

    //商户证书路径
    private String KeyPath;

    //异步通知路径
    private String notifyUrl;

    //开放平台id
    private String openAppId;

    //开放平台密钥
    private String openAppSecret;

    //微信模板id
    private Map<String,String> templateId;
}
