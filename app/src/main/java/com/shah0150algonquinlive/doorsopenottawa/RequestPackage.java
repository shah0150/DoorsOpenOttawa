package com.shah0150algonquinlive.doorsopenottawa;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adeshshah on 2016-12-12.
 */

public class RequestPackage {


    private String uri;
    private HttpMethod method = HttpMethod.GET;
    private Map<String, String> params = new HashMap<>();

    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public HttpMethod getMethod() {
        return method;
    }
    public void setMethod(HttpMethod method) {
        this.method = method;
    }
    public Map<String, String> getParams() {
        return params;
    }
    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    private Map<String, File> imageParams = new HashMap<>();
    public void setParam(String key, String value) {
        params.put(key, value);
    }

    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(params.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key + "=" + value);
        }
        return sb.toString();
    }

    public void setImageParams(String key,File bm)
    {
        imageParams.put(key,bm);
    }

    public File getImage()
    {
        return imageParams.get("image");
    }


}
