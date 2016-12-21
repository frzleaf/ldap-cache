package com.tecapro.ldap.cache.web;

import com.tecapro.ldap.cache.AttributeToDnCache;
import com.tecapro.ldap.cache.CacheFactory;
import com.tecapro.ldap.cache.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.websocket.server.PathParam;

/**
 * Created by chai65 on 12/20/2016.
 */

@Controller
public class Api {

    @Autowired
    private CacheFactory cacheFactory;

    @Autowired
    ApplicationProperties applicationProperties;

    @RequestMapping(value = "/{objectClass}/{attribute}")
    public WebAsyncTask<ResponseEntity<String>> getDnFromAttribute(
            @PathParam("objectClass") String objectClass,
            @PathParam("attribute") String attribute,
            @RequestParam("att") String attValue,
            @RequestParam("base") String baseDn
    ) {
        return new WebAsyncTask<ResponseEntity<String>>(applicationProperties.getApiTimeout(),
                () -> {
                    AttributeToDnCache a2dn = cacheFactory.a2d(objectClass, attribute, baseDn);
                    String foundDn = a2dn.getDn(attribute);
                    if (foundDn != null && !"".equals(foundDn)) {
                        return ResponseEntity.ok(foundDn);
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
                });
    }

    @RequestMapping(value = "/{objectClass}/{attribute}")
    public ResponseEntity updateA2d(
            @PathParam("objectClass") String objectClass,
            @PathParam("attribute") String attribute,
            @RequestParam("base") String baseDn
    ) {
       cacheFactory.createA2D()
    }


}


