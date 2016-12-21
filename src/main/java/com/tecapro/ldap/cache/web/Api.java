package com.tecapro.ldap.cache.web;

import com.tecapro.ldap.cache.AttributeToDnCache;
import com.tecapro.ldap.cache.CacheFactory;
import com.tecapro.ldap.cache.config.ApplicationProperties;
import com.unboundid.ldap.sdk.LDAPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * Created by chai65 on 12/20/2016.
 */

@Controller
public class Api {


    @Autowired
    private CacheFactory cacheFactory;

    @Autowired
    ApplicationProperties applicationProperties;

    @RequestMapping(value = "/{objectClass}/{attribute}", method = RequestMethod.GET)
    public WebAsyncTask<ResponseEntity<String>> getDnFromAttribute(
            @PathVariable("objectClass") String objectClass,
            @PathVariable("attribute") String attribute,
            @RequestParam("att") String attValue,
            @RequestParam(value = "base", required = false) String baseDn) {

        return new WebAsyncTask<ResponseEntity<String>>(applicationProperties.getApiTimeout(),
                () -> {
                    AttributeToDnCache a2dn = cacheFactory.a2d(objectClass, attribute, baseDn);
                    if (a2dn == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Cache " + AttributeToDnCache.a2dName(objectClass, attribute, baseDn) + " might not have been initialized");
                    }
                    String foundDn = a2dn.getDn(attValue);
                    if (foundDn != null && !"".equals(foundDn)) {
                        return ResponseEntity.ok(foundDn);
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
                });
    }


    @RequestMapping(value = "/{objectClass}/{attribute}", method = RequestMethod.POST)
    public ResponseEntity updateA2d(
            @PathVariable("objectClass") String objectClass,
            @PathVariable("attribute") String attribute,
            @RequestParam(value = "base", required = false) String baseDn,
            @RequestParam(value = "force", defaultValue = "false", required = false) boolean force
    ) {
        AttributeToDnCache cache = cacheFactory.createA2D()
                .objClass(objectClass)
                .attr(attribute)
                .baseDn(baseDn)
                .get();
        try {
            cache.update(force);
            return ResponseEntity.ok(cache.size() + " entries have been updated");
        } catch (LDAPException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }


}


