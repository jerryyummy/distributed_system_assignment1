package org.example;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ClientAPI extends Thread{
    String IPAddr;
    CountDownLatch latch;

    int threadGroupId;  // 第几个线程组

    int threadId;    // 第几个线程

    public ClientAPI(String IPAddr,CountDownLatch latch,int threadGroupId, int threadId){
        this.IPAddr = IPAddr;
        this.latch = latch;
        this.threadGroupId = threadGroupId;
        this.threadId = threadId;
    }

    public boolean get() throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(IPAddr);
        long start = System.currentTimeMillis();
        try (CloseableHttpResponse response = httpclient.execute(httpget)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            long end = System.currentTimeMillis();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/Users/youyun/Desktop/client/src/main/resources/result/java2.csv",true));
            bufferedWriter.append(String.valueOf(start)).append(" Get ").append(String.valueOf(end - start)).append(" ms status:200\n");
            bufferedWriter.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean post() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(IPAddr);
        long start = System.currentTimeMillis();
//        JSONObject param = new JSONObject();
//        param.put("albumID", "11");
//        httpPost.setEntity(new StringEntity(param.toString(), "UTF-8"));
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            long end = System.currentTimeMillis();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/Users/youyun/Desktop/client/src/main/resources/result/java2.csv",true));
            bufferedWriter.append(String.valueOf(start)).append(" Post ").append(String.valueOf(end - start)).append(" ms status:200\n");
            bufferedWriter.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void run(){
        for (int i = 0; i < 1000; i++) {
            System.out.println("thread group " + threadGroupId + " 中的第 " + threadId + " 个thread "+(i+1)+" 个请求");
            for (int retry = 0; retry < 5; retry++) {
                try {
                    get();
                    break;
                }catch (Exception e){
                    System.out.println("crowded");
                }
            }
            for (int retry = 0; retry < 5; retry++) {
                try {
                    post();
                    break;
                }catch (Exception e){
                    System.out.println("crowded");
                }
            }
        }
        latch.countDown();
    }
}
