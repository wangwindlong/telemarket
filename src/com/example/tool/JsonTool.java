package com.example.tool;

import com.example.telemarket.bean.ModelResult;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonTool {
    public static JSONObject doGet(String url) {
        try {
            String result = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            result = EntityUtils.toString(response.getEntity());
            JSONObject object = new JSONObject(result);
            return object;
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> ModelResult<T> doPostResult(String url, Map<String, String> map_params) {
        HttpClient httpClient = null;
        ModelResult<T> modelResult = new ModelResult<T>();
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> paramPair = new ArrayList<NameValuePair>();
            for (String mapParam : map_params.keySet()) {
                paramPair.add(new BasicNameValuePair(mapParam, map_params.get(mapParam)));
            }
            //http://www.open-open.com/lib/view/open1327556868217.html
            httpPost.setEntity(new UrlEncodedFormEntity(paramPair, HTTP.UTF_8));
            HttpParams httpParameters = new BasicHttpParams();
                    /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(httpParameters, 15000);
            // 连接超时
            HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
            //// 超时设置
            HttpConnectionParams.setSoTimeout(httpParameters, 15000);
            httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse response = httpClient.execute(httpPost);
            modelResult.setCode(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                JSONObject object = new JSONObject(result);
                modelResult.setReturnResult(true);
                modelResult.setJsonObject(object);
            } else {
                modelResult.setReturnResult(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelResult.setReturnResult(false);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return modelResult;
    }

    public static JSONObject doPost(String url, Map<String, String> map_params) {

        HttpClient httpClient = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> paramPair = new ArrayList<NameValuePair>();
            for (String mapParam : map_params.keySet()) {
                paramPair.add(new BasicNameValuePair(mapParam, map_params.get(mapParam)));
            }
            //http://www.open-open.com/lib/view/open1327556868217.html
            httpPost.setEntity(new UrlEncodedFormEntity(paramPair, HTTP.UTF_8));
            HttpParams httpParameters = new BasicHttpParams();
                    /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(httpParameters, 30000);
            // 连接超时
            HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
            //// 超时设置
            HttpConnectionParams.setSoTimeout(httpParameters, 30000);
            httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                JSONObject object = new JSONObject(result);
                return object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    public static String doPostString(String url, Map<String, String> map_params) {

        HttpClient httpClient = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> paramPair = new ArrayList<NameValuePair>();
            for (String mapParam : map_params.keySet()) {
                paramPair.add(new BasicNameValuePair(mapParam, map_params.get(mapParam)));
            }
            //http://www.open-open.com/lib/view/open1327556868217.html
            httpPost.setEntity(new UrlEncodedFormEntity(paramPair, HTTP.UTF_8));
            HttpParams httpParameters = new BasicHttpParams();
                    /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(httpParameters, 30000);
            // 连接超时
            HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
            //// 超时设置
            HttpConnectionParams.setSoTimeout(httpParameters, 30000);
            httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    public static <T> ModelResult<T> doLongPostResult(String url, Map<String, String> map_params) {
        HttpClient httpClient = null;
        ModelResult<T> modelResult = new ModelResult<T>();
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> paramPair = new ArrayList<NameValuePair>();
            for (String mapParam : map_params.keySet()) {
                paramPair.add(new BasicNameValuePair(mapParam, map_params.get(mapParam)));
            }
            //http://www.open-open.com/lib/view/open1327556868217.html
            httpPost.setEntity(new UrlEncodedFormEntity(paramPair, HTTP.UTF_8));
            HttpParams httpParameters = new BasicHttpParams();
                    /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(httpParameters, 60000);
            // 连接超时
            HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);
            //// 超时设置
            HttpConnectionParams.setSoTimeout(httpParameters, 60000);
            httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse response = httpClient.execute(httpPost);
            modelResult.setCode(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                JSONObject object = new JSONObject(result);
                modelResult.setReturnResult(true);
                modelResult.setJsonObject(object);
            } else {
                modelResult.setReturnResult(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelResult.setReturnResult(false);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return modelResult;
    }
}
