package com.tecapro.ldap.cache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by chai65 on 12/20/2016.
 */
@Component
@ConfigurationProperties(prefix = "lc")
public class ApplicationProperties {

    private LdapProperties ldap;

    @Value("${api.timeout:5000}")
    private long apiTimeout;

    public LdapProperties getLdap() {
        return ldap;
    }

    public void setLdap(LdapProperties ldap) {
        this.ldap = ldap;
    }



    public static class LdapProperties {
        private String bindDn;
        private String password;
        private String host;
        private int port;
        private int timeOut = 100;
        private int maxResult = 300000;
        private int initCon = 2;
        private int maxCon = 10;
        private String baseDn;

        public String getBindDn() {
            return bindDn;
        }

        public void setBindDn(String bindDn) {
            this.bindDn = bindDn;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
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

        public int getTimeOut() {
            return timeOut;
        }

        public void setTimeOut(int timeOut) {
            this.timeOut = timeOut;
        }

        public int getMaxResult() {
            return maxResult;
        }

        public void setMaxResult(int maxResult) {
            this.maxResult = maxResult;
        }

        public int getInitCon() {
            return initCon;
        }

        public void setInitCon(int initCon) {
            this.initCon = initCon;
        }

        public int getMaxCon() {
            return maxCon;
        }

        public void setMaxCon(int maxCon) {
            this.maxCon = maxCon;
        }

        public String getBaseDn() {
            return baseDn;
        }

        public void setBaseDn(String baseDn) {
            this.baseDn = baseDn;
        }
    }


    public long getApiTimeout() {
        return apiTimeout;
    }

    public void setApiTimeout(long apiTimeout) {
        this.apiTimeout = apiTimeout;
    }
}
