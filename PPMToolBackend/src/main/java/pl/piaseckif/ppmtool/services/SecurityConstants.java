package pl.piaseckif.ppmtool.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class SecurityConstants {

    public static final String SIGN_UP_URL = "/api/users/**";
    public static final String H2_URL =  "h2-console/**";
    public static final String SECRET_KEY = "SecretKey";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING  = "Authorization";
    public static final long EXPIRATION_TIME = 300_000L;

}
