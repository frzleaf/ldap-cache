package com.tecapro.ldap.cache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chai65 on 12/21/2016.
 */
public class CacheFactory {

    private LDAPConnectionPool pool;
    private HazelcastInstance hazelcastInstance;
    private Map<Integer, AttributeToDnCache> a2dCaches = new HashMap<>();


    /**
     * @return
     */
    public CacheFactory(HazelcastInstance instance, LDAPConnectionPool pool) {
        this.pool = pool;
        this.hazelcastInstance = instance;
    }


    public CrashA2DCache createA2D(String name) {
        return new CrashA2DCache(hazelcastInstance, pool, this);
    }


    public static class CrashA2DCache {

        private AttributeToDnCache instance;
        private CacheFactory cacheFactory;
        private HazelcastInstance hazelcastInstance;

        public CrashA2DCache(HazelcastInstance hazelcastInstance, LDAPConnectionPool pool, CacheFactory cacheFactory) {
            this.cacheFactory = cacheFactory;
            this.hazelcastInstance = hazelcastInstance;
            this.instance = new AttributeToDnCache(pool);
        }

        public CrashA2DCache baseDn(String baseDn) {
            instance.setBaseDn(baseDn == null ? "" : baseDn);
            return this;
        }

        public CrashA2DCache attr(String attr) {
            Assert.notNull(attr);
            instance.setAttributeName(attr);
            return this;
        }


        public CrashA2DCache objClass(String obj) {
            Assert.notNull(obj);
            instance.setObjectClass(obj);
            return this;
        }

        public AttributeToDnCache get() {
            String mapName = String.join(">", instance.getObjectClass(), instance.get)
            hazelcastInstance.getMap()
            cacheFactory.getA2dCaches()
                    .put(AttributeToDnCache.insensitiveHash(instance.getObjectClass(),
                            instance.getAttributeName(),
                            instance.getBaseDn()),
                        instance);
            return instance;
        }

    }


    public Map<Integer, AttributeToDnCache> getA2dCaches() {
        return a2dCaches;
    }


    public AttributeToDnCache a2d(String objectClass, String attributeName, String baseDn){
        return a2dCaches.get(AttributeToDnCache.insensitiveHash(objectClass,attributeName, baseDn));
    }
}
