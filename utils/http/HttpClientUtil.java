package com.sheca.unitrust.common.util.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.*;


/**
 * Created by CHAR on 2019/1/16.
 */
//@Slf4j
public class HttpClientUtil {
    private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(15000)
            .setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000)
            .build();

    private static HttpClientUtil instance = null;

    private HttpClientUtil() {
    }

    public static HttpClientUtil getInstance() {
        if (instance == null) {
            instance = new HttpClientUtil();
        }
        return instance;
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public String sendHttpPost(String httpUrl,String params) {

        return sendHttpPost(httpUrl,params,"application/json", "UTF-8");
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl
     * @param params
     * @param contentType 參考：application/json, application/x-www-form-urlencoded
     * @param charset     參考：UTF-8, GBK
     * @return
     */
    public String sendHttpPost(String httpUrl, String params, String contentType, String charset) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            //设置参数
            StringEntity stringEntity = new StringEntity(params, StringUtils.isEmpty(charset) ? "UTF-8" : charset);
            stringEntity.setContentType(StringUtils.isEmpty(contentType) ? "application/x-www-form-urlencoded" : contentType);
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    public String sendHttpsPost(String httpUrl, String params, String contentType, String charset) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            //设置参数
            StringEntity stringEntity = new StringEntity(params, StringUtils.isEmpty(charset) ? "UTF-8" : charset);
            stringEntity.setContentType(StringUtils.isEmpty(contentType) ? "application/x-www-form-urlencoded" : contentType);
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpsPost(httpPost);
    }


    /**
     * 带headers信息post请求方法
     * @param httpUrl
     * @param param
     * @param headerMap
     * @param charset 默认 UTF-8
     * @param contentType  默认 application/json
     * @return
     */
    public String sendHttpPostWithHeaders(String httpUrl, String param, Map<String, String> headerMap, String charset, String contentType) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            //设置参数
            Set<Map.Entry<String, String>> set = headerMap.entrySet();
            Iterator<Map.Entry<String, String>> iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
            StringEntity reqEntity = new StringEntity(param, StringUtils.isEmpty(charset) ? "UTF-8" : charset);
            reqEntity.setContentType(StringUtils.isEmpty(contentType) ? "application/json" : contentType);
            httpPost.setEntity(reqEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }


    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private String sendHttpPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = SslUtil.SslHttpClientBuild();
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
            } else {
                System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));
//                log.error("call http clinet error ,the status is :{}, response: {}", response.getStatusLine(),
//                        EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
        } catch (Exception e) {
//            log.error("httpclient error url:{} ,exception: {}", httpPost.getURI(), e.getMessage());
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }
    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public String sendHttpsPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        return sendHttpsPost(httpPost);
    }

    private String sendHttpsPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = new SSLClient();
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200){
                entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
            }else{
//                log.error("call http clinet error ,the status is :{}", response.getStatusLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public String sendHttpGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpGet(httpGet);
    }

    public InputStream getFile(String httpUrl,Map<String,String> headerMap) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        Set<Map.Entry<String, String>> set = headerMap.entrySet();
        Iterator<Map.Entry<String, String>> iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            httpGet.addHeader(entry.getKey(), entry
                    .getValue());
        }
        return httpGetInputStream(httpGet);
    }


    /**
     * 发送 get请求Https
     *
     * @param httpUrl
     */
    public String sendHttpsGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return httpsGet(httpGet);
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @return
     */
    private String sendHttpGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            //e.printStackTrace();
//            log.error("sendHttpGet ,the exception is :{}", e);
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                //e.printStackTrace();
//                log.error("sendHttpGet ,the exception is :{}", e);
            }
        }
        return responseContent;
    }

    private InputStream httpGetInputStream(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                InputStream is = response.getEntity().getContent();
                return is;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送Get请求Https
     *
     * @param httpGet
     * @return
     */
    private String sendHttpsGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送Get请求Https
     *
     * @param httpGet
     * @return
     */
    private String httpsGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = SslUtil.SslHttpClientBuild();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    //获取proxy
    public Proxy needProxy(){
        //添加proxy校验
        //查询当前是否有proxy
        Proxy proxy = null;
        String proxySet = System.getProperty("java.net.useSystemProxies");
        if (null != proxySet && "true".equals(proxySet)){
            String proxyHost = System.getProperty("http.proxyHost");
            String proxyPort = System.getProperty("http.proxyPort");

            if (null != proxyHost && !"".equals(proxyHost)
                    && null != proxyPort && !"".equals(proxyPort)){

                Integer port = Integer.parseInt(proxyPort);
//                log.info("port is :{}",port);
                return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
            }
            else {
//                log.error("http.proxyHost 或者 http.proxyPort vm 参数设置错误");
                return null;
            }

        }
        else {
            return null;
        }
    }
}
