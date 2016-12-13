package com.shah0150algonquinlive.doorsopenottawa;

/**
 * Created by adeshshah on 2016-11-08.
 */
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.shah0150algonquinlive.doorsopenottawa.HttpMethod;
import com.shah0150algonquinlive.doorsopenottawa.RequestPackage;
import static android.content.ContentValues.TAG;

public class HttpManager {


//    public static String getData(String uri) {
//
//        BufferedReader reader = null;
//
//
//        try {
//            // open the URI
//            URL url = new URL(uri);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//            // make a buffered reader
//            StringBuilder sb = new StringBuilder();
//            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//            // read the HTTP response from URI one-line-at-a-time
//            String line;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//
//            // return the HTTP response
//            return sb.toString();
//            // exception handling: a) print stack-trace, b) return null
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//        }
//    }
//
//    public static String getData(String uri, String userName, String password) {
//
//        BufferedReader reader = null;
//        HttpURLConnection con = null;
//
//        byte[] loginBytes = (userName + ":" + password).getBytes();
//        StringBuilder loginBuilder = new StringBuilder()
//                .append("Basic ")
//                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));
//
//        try {
//            URL url = new URL(uri);
//            con = (HttpURLConnection) url.openConnection();
//
//            con.addRequestProperty("Authorization", loginBuilder.toString());
//
//            StringBuilder sb = new StringBuilder();
//            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//
//            return sb.toString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                int status = con.getResponseCode();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            return null;
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//        }
//    }
private  static int  statuscode;
    /**
     * Return the HTTP response from uri
     *
     * @param p RequestPackage
     * @return String the response; null when exception
     */
    public static String getData(RequestPackage p) {
        HttpURLConnection con = null;
        BufferedReader reader = null;
        int status;
        String uri = p.getUri();
        if (p.getMethod() == HttpMethod.GET) {
            uri += "?" + p.getEncodedParams();
        }
        byte[] loginBytes = ("shah0150" + ":" + "password").getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));
        try {
            URL url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod().toString());
            con.addRequestProperty("Authorization", loginBuilder.toString());
            JSONObject json = new JSONObject(p.getParams());
            String params = json.toString();
            Log.d(TAG, "JSON : "+params);
            if (p.getMethod() == HttpMethod.POST || p.getMethod() == HttpMethod.PUT) {
                con.addRequestProperty("Accept", "application/json");
                con.addRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(params);
                writer.flush();
            }

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                status = con.getResponseCode();
                statuscode=status;
                Log.d(TAG, "getData: "+status);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }


        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }



    public static String getDataX(String uri, String userName, String password) {

        BufferedReader reader = null;
        HttpURLConnection con = null;

        byte[] loginBytes = (userName + ":" + password).getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            URL url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();

            con.addRequestProperty("Authorization", loginBuilder.toString());

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                int status = con.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }



}

