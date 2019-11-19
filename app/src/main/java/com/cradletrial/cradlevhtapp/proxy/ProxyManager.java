package com.cradletrial.cradlevhtapp.proxy;

import android.app.Activity;

import com.cradletrial.cradlevhtapp.R;


/**
 * The ProxyManager class deals with providing the
 * application with the proxy to generate HTTP Requests.
 */

public class ProxyManager {

    private static WGServerProxy proxy;
    private static String token;


    public static WGServerProxy getProxy(Activity activity) {

        generateProxy(activity);
        return proxy;
    }

    private static void generateProxy(Activity activity) {

        if(token == null) {
            proxy = ProxyBuilder.getProxy("test");
        } else {
            proxy = ProxyBuilder.getProxy("test", token);
        }
    }

    public static String setToken(String token) {
        ProxyManager.token = token;
        return token;
    }

    public static String getToken() {
        return token;
    }

}