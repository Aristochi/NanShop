
package com.NanShop.mall.controller.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.google.code.kaptcha.util.Config;

import java.util.Properties;

@Component
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        com.google.code.kaptcha.impl.DefaultKaptcha defaultKaptcha = new com.google.code.kaptcha.impl.DefaultKaptcha();
        Properties properties = new Properties();
        // 是否使用边框
        properties.put("kaptcha.border", "no");
        // 验证码字体颜色
        properties.put("kaptcha.textproducer.font.color", "red");
        // 验证码图片大小
        properties.put("kaptcha.image.width", "160");
        properties.put("kaptcha.image.height", "40");
        // 验证码字体的大小
        properties.put("kaptcha.textproducer.font.size", "40");
        // 验证码保存在session的key
        properties.put("kaptcha.session.key", "verifyCode");
        // 验证码的字体设置
        properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        properties.put("kaptcha.textproducer.char.space", "4");
//        properties.put("kaptcha.textproducer.char.string","0123456789ABCEFGHIJKLMNOPQRSTUVWXYZ");
        properties.put("kaptcha.noise.color","black");
        properties.put("kaptcha.background.clear.to","white");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}