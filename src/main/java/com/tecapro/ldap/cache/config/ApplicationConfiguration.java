package com.tecapro.ldap.cache.config;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.config.UrlXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.tecapro.ldap.cache.CacheFactory;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by chai65 on 12/20/2016.
 */
@Configuration
public class ApplicationConfiguration {


    @Autowired
    private ApplicationProperties appProps;


    @Bean
    public HazelcastInstance hazelcastInstance(
            @Value("${ls.hazelcast.config:classpath:hazelcast.xml}") String hazelcastPath) throws IOException {
        Config config = new UrlXmlConfig(hazelcastPath);
        return Hazelcast.newHazelcastInstance(config);
    }


    @Bean
    public LDAPConnectionPool ldapConnectionPool() throws LDAPException {
        ApplicationProperties.LdapProperties ldap = appProps.getLdap();
        LDAPConnectionPool pool = new LDAPConnectionPool(
                new LDAPConnection(
                        ldap.getHost(),
                        ldap.getPort(),
                        ldap.getBindDn(),
                        ldap.getPassword()
                ),
                ldap.getInitCon(),
                ldap.getMaxCon()
        );
        return pool;
    }


    @Bean
    @Autowired
    public CacheFactory cacheFactory(HazelcastInstance hazelcastInstance, LDAPConnectionPool pool){
        return new CacheFactory(hazelcastInstance, pool);
    }


}
