package com.shah0150algonquinlive.doorsopenottawa;

/**
 * Created by adeshshah on 2016-11-08.
 */
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

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

    public static String uploadFile(RequestPackage pkg) {
        final String boundary;
        final String LINE_FEED = "\r\n";
        HttpURLConnection httpConn;
        String charset;
        OutputStream outputStream;
        PrintWriter writer;
        byte[] loginBytes = ("shah0150" + ":" + "password").getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            boundary = "===" + System.currentTimeMillis() + "===";
            URL url = new URL(pkg.getUri());
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.addRequestProperty("Authorization", loginBuilder.toString());
            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"),
                    true);
            String fileName = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + pkg.getImage().getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append(
                    "Content-Disposition: form-data; name=\"" + "photoImage"
                            + "\"; filename=\"" + fileName + "\"")
                    .append(LINE_FEED);
            writer.append(
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
                    .append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);

            writer.append(LINE_FEED);
            writer.flush();


            FileInputStream inputStream = new FileInputStream(pkg.getImage());
            byte[] buffer = new byte[6000];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();

            List<String> response = new ArrayList<String>();

            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();

            // checks server's status code first
            int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        httpConn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response.add(line);
                }
                reader.close();
                httpConn.disconnect();
            } else {
                throw new IOException("Server returned non-OK status: " + status);
            }

            return response.toString();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }



}

