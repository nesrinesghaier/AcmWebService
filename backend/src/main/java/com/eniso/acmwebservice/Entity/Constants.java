package com.eniso.acmwebservice.Entity;

public class Constants {

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
    public static final long REFRESH_DATA_DELAY_SECONDS = 10 * 60;
    public static final String SIGNING_KEY = "acm123webservice";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORITY_PREFIX = "ROLE_";
    public static final String HEADER_STRING = "Authorization";
}