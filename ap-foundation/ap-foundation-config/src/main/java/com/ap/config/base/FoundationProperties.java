package com.ap.config.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import lombok.extern.slf4j.Slf4j;

@ConfigurationProperties(prefix = "ap-foundation", ignoreUnknownFields = false)
@Component
@Slf4j
public class FoundationProperties {

	@Value("${spring.application.name}")
	private String applicationName;
	
	private final AxonCluster axonCluster = new AxonCluster(applicationName);

    private final Async async = new Async();

    private final Http http = new Http();

    private final Datasource datasource = new Datasource();
    
    private final Redis redis = new Redis();
    
    private final Metrics metrics = new Metrics();

    private final CorsConfiguration cors = new CorsConfiguration();
    
    private final Security security = new Security();
    
    private final Swagger swagger = new Swagger();
    
    public Redis getRedis() {
        return redis;
    }
    
    public AxonCluster getaxonCluster() {
        return axonCluster;
    }    
    
    public Metrics getMetrics() {
        return metrics;
    }

    public Async getAsync() {
        return async;
    }

    public Http getHttp() {
        return http;
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public CorsConfiguration getCors() {
        return cors;
    }
    
    public Security getSecurity() {
        return security;
    }
    
    public Swagger getSwagger() {
		return swagger;
	}

	public static class AxonCluster {
    	private String clusterName;
    	private String eventExchange;
    	private String eventDeadLetterExchange;
    	private String eventRetryExchange;
    	//--
    	private String queueSagaDeadLetter;
    	private String queueQueryDeadLetter;
    	//--
    	private String queueSaga;
    	private String queueQuery;
    	//--
    	private String routingKey = "com.ap.*";
    	
    	public void changeClusterName(String appName) {
    		this.eventExchange = "ap.eventbus.exchange";
    		this.eventDeadLetterExchange = appName+".eventbus.deadletter.exchange";
    		this.eventRetryExchange = appName+".eventbus.retry.exchange";
    		this.queueSagaDeadLetter = appName+".dead.saga";
    		this.queueQueryDeadLetter = appName+".dead.querymodel";
    		this.queueSaga = appName+".queueSaga";
    		this.queueQuery = appName+".queueQuerymodel";
    	}    	
    	
    	public String getClusterName() {
			return clusterName;
		}

		public void setClusterName(String clusterName) {
			this.changeClusterName(clusterName);
			this.clusterName = clusterName;
		}
    	
    	public AxonCluster(String appName) {
    		this.changeClusterName(appName);
    	}
    	
    	public String getEventExchange() {
			return eventExchange;
		}
		public String getEventDeadLetterExchange() {
			return eventDeadLetterExchange;
		}
		public String getEventRetryExchange() {
			return eventRetryExchange;
		}
		public String getQueueSagaDeadLetter() {
			return queueSagaDeadLetter;
		}
		public String getQueueQueryDeadLetter() {
			return queueQueryDeadLetter;
		}
		public String getQueueSaga() {
			return queueSaga;
		}
		public String getQueueQuery() {
			return queueQuery;
		}
		public String getRoutingKey() {
			return routingKey;
		}
		public void setRoutingKey(String routingKey) {
			this.routingKey = routingKey;
		}		
    	
    }
    

    public static class Async {

        private int corePoolSize = 4;

        private int maxPoolSize = 50;
        
        private int scheduledPoolSize = 10;

        public int getScheduledPoolSize() {
			return scheduledPoolSize;
		}

		private int queueCapacity = 10000;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }
        
        public void setScheduledPoolSize(int scheduledPoolSize) {
            this.scheduledPoolSize = scheduledPoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    public static class Http {

        private final Cache cache = new Cache();

        public Cache getCache() {
            return cache;
        }

        public static class Cache {

            private int timeToLiveInDays = 31;

            public int getTimeToLiveInDays() {
                return timeToLiveInDays;
            }

            public void setTimeToLiveInDays(int timeToLiveInDays) {
                this.timeToLiveInDays = timeToLiveInDays;
            }
        }
    }
    
    public static class Redis {
    	private String host = "localhost";
    	private String password = "";
    	private int port = 6379;
    	private boolean usepool = true;
    	private int maxActive = 1000;
    	private int maxIdle = 1000;
    	private long maxWaitMillis = 30000;
    	
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public boolean isUsepool() {
			return usepool;
		}
		public void setUsepool(boolean usepool) {
			this.usepool = usepool;
		}
		public int getMaxActive() {
			return maxActive;
		}
		public void setMaxActive(int maxActive) {
			this.maxActive = maxActive;
		}
		public int getMaxIdle() {
			return maxIdle;
		}
		public void setMaxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
		}
		public long getMaxWaitMillis() {
			return maxWaitMillis;
		}
		public void setMaxWaitMillis(long maxWaitMillis) {
			this.maxWaitMillis = maxWaitMillis;
		}
    	
    }

    public static class Datasource {

        private boolean cachePrepStmts = true;

        private int prepStmtCacheSize = 250;

        private int prepStmtCacheSqlLimit = 2048;

        private boolean useServerPrepStmts = true;

        private String serverName = "";

        private String portNumber = "";

        private String databaseName = "";

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getPortNumber() {
            return portNumber;
        }

        public void setPortNumber(String portNumber) {
            this.portNumber = portNumber;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        public boolean isCachePrepStmts() {
            return cachePrepStmts;
        }

        public void setCachePrepStmts(boolean cachePrepStmts) {
            this.cachePrepStmts = cachePrepStmts;
        }

        public int getPrepStmtCacheSize() {
            return prepStmtCacheSize;
        }

        public void setPrepStmtCacheSize(int prepStmtCacheSize) {
            this.prepStmtCacheSize = prepStmtCacheSize;
        }

        public int getPrepStmtCacheSqlLimit() {
            return prepStmtCacheSqlLimit;
        }

        public void setPrepStmtCacheSqlLimit(int prepStmtCacheSqlLimit) {
            this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
        }

        public boolean isUseServerPrepStmts() {
            return useServerPrepStmts;
        }

        public void setUseServerPrepStmts(boolean useServerPrepStmts) {
            this.useServerPrepStmts = useServerPrepStmts;
        }
    }
    
    public static class Metrics {

        private final Jmx jmx = new Jmx();

        private final Spark spark = new Spark();

        private final Graphite graphite = new Graphite();
        
        private final Logs logs = new Logs();

        public Jmx getJmx() {
            return jmx;
        }

        public Spark getSpark() {
            return spark;
        }

        public Graphite getGraphite() {
            return graphite;
        }
        
        public Logs getLogs() {
            return logs;
        }

        public static class Jmx {

            private boolean enabled = true;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }

        public static class Spark {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 9999;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }
        }

        public static class Graphite {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 2003;

            private String prefix = "ap";

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public String getPrefix() {
                return prefix;
            }

            public void setPrefix(String prefix) {
                this.prefix = prefix;
            }
        }
        
        public static  class Logs {

            private boolean enabled = false;

            private long reportFrequency = 60;

            public long getReportFrequency() {
                return reportFrequency;
            }

            public void setReportFrequency(int reportFrequency) {
                this.reportFrequency = reportFrequency;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }
    
    public static class Swagger {
    	private String title = "AP API";

        private String description = "**A API spefication for version 2**";

        private String version = "2.0.0";

        private String termsOfServiceUrl;

        private String contact;

        private String license;

        private String licenseUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTermsOfServiceUrl() {
            return termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }
    }
    
    private final Logging logging = new Logging();

    public Logging getLogging() { return logging; }

    public static class Logging {

        private final Logstash logstash = new Logstash();

        public Logstash getLogstash() { return logstash; }

        public static class Logstash {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 5000;

            private int queueSize = 512;

            public boolean isEnabled() { return enabled; }

            public void setEnabled(boolean enabled) { this.enabled = enabled; }

            public String getHost() { return host; }

            public void setHost(String host) { this.host = host; }

            public int getPort() { return port; }

            public void setPort(int port) { this.port = port; }

            public int getQueueSize() { return queueSize; }

            public void setQueueSize(int queueSize) { this.queueSize = queueSize; }
        }
    }
    
    
    
    public static class Security {

        private final Authentication authentication = new Authentication();

        public Authentication getAuthentication() {
            return authentication;
        }

        public static class Authentication {

            private final Jwt jwt = new Jwt();

            public Jwt getJwt() {
                return jwt;
            }

            public static class Jwt {

                private String secret;

                private long tokenValidityInSeconds = 1800;
                private long tokenValidityInSecondsForRememberMe = 2592000;

                public String getSecret() {
                    return secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public long getTokenValidityInSeconds() {
                    return tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
                }
            }
        }
    }

}
