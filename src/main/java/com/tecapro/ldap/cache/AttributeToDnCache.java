package com.tecapro.ldap.cache;

import com.hazelcast.core.IMap;
import com.unboundid.ldap.sdk.*;
import org.springframework.util.Assert;

/**
 * Created by chai65 on 12/20/2016.
 */
public class AttributeToDnCache {

    private String attributeName;
    private String objectClass;
    private String baseDn = "";
    private IMap<String, String> store;
    private LDAPConnectionPool pool;


    public AttributeToDnCache(LDAPConnectionPool pool) {
        this.pool = pool;
    }


    public void init(IMap<String, String> store){
        this.store = store;
    }


    /**
     * @param clear
     * @throws LDAPException
     */
    public synchronized void update(boolean clear) throws LDAPException {
        Assert.notNull(store, "Store cache might not have been initialized");
        if (clear) {
            clear();
        }
        LDAPConnection connection = this.pool.getConnection();
        SearchResult searchResult = connection.search(this.baseDn, SearchScope.SUB, "objectClass=" + objectClass, attributeName);
        for (SearchResultEntry entry : searchResult.getSearchEntries()) {
            Attribute attr = entry.getAttribute(attributeName);
            String key = attr.getValue();
            if ( null != key && !"".equals(key)){
                store.put(key, entry.getDN());
            }
        }
    }

    public String getDn(String attribute){
        return this.store.get(attribute);
    }


    public void clear() {
        store.clear();
    }


    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public String getBaseDn() {
        return baseDn;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }


    /**
     * @param objectClass
     * @param attributeName
     * @param baseDn
     * @return
     */
    public static int hash(String objectClass, String attributeName, String baseDn){
        if ( objectClass == null ){
            objectClass = "";
        }
        if ( attributeName == null ){
            objectClass = "";
        }

        if ( baseDn == null ){
            baseDn = "";
        }
        return String.join(",", objectClass, attributeName, baseDn).hashCode();
    }
    public static int insensitiveHash(String objectClass, String attributeName, String baseDn){
        if ( objectClass == null ){
            objectClass = "";
        }
        if ( attributeName == null ){
            objectClass = "";
        }
        if ( baseDn == null ){
            baseDn = "";
        }
        return hash(objectClass.toLowerCase(), attributeName.toLowerCase(), baseDn.toLowerCase());
    }

}
