package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class Client {
    String IPAddr;

    public Client(String IPAddr){
        this.IPAddr = IPAddr;
    }


    public boolean get() throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(IPAddr+"//11");
        try (CloseableHttpResponse response = httpclient.execute(httpget)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            System.out.println(result);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean post() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(IPAddr);
//        JSONObject param = new JSONObject();
//        param.put("albumID", "11");
//        httpPost.setEntity(new StringEntity(param.toString(), "UTF-8"));
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            System.out.println(result);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
