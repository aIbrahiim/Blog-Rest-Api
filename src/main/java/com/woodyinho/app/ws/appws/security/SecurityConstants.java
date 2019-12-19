package com.woodyinho.app.ws.appws.security;

import com.woodyinho.app.ws.appws.SpringApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000;
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/auth/signup";






    public static String getTokenSecret(){
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}
