package com.ap.config.base;

public class Constants {

	public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
	public static final String SPRING_PROFILE_PRODUCTION = "prod";
	public static final String SPRING_PROFILE_FAST = "fast";
	
	public static final String DEFAULT_APP = "AP";
    public static final String DEFAULT_TENANT = "AP";
    public static final String DEFAULT_USER = "GUEST";
    
    public static final String ROLE_SYSTEM_ADMINISTRATOR = "ROLE_SYSTEM_ADMINISTRATOR";
    public static final String ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR";
    public static final String ROLE_PUBLIC_USER = "ROLE_PUBLIC_USER";
    
    public static final String V1_0 = "application/vnd.ap.v1+json";
    public static final String V2_0 = "application/vnd.ap.v2+json";
    
    public static class UserRole{
    	public static final String ROLE_SYSADMIN[] = {"ROLE_SYSTEM_ADMINISTRATOR","ROLE_ADMINISTRATOR", "ROLE_PUBLIC_USER"};
    	//specific to admin
    	public static final String[] ROLE_ADMINISTRATOR = {"ROLE_ADMINISTRATOR", "ROLE_PUBLIC_USER"};
    	//public user
    	public static final String ROLE_PUBLICUSER = "ROLE_PUBLIC_USER";
    	//anonymous login / guest
    	public static final String ROLE_GUEST = "ROLE_GUEST";
    }

}
