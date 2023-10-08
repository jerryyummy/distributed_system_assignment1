package org.example;

import java.io.BufferedWriter;
import java.util.concurrent.CountDownLatch;

public class MainThread extends Thread{

    int threadGroupSize;
    int numThreadGroups;
    int delaySeconds;
    String IPAddr;

    int id;

    CountDownLatch latch;

    BufferedWriter out;


    public MainThread(int threadGroupSize, int delaySeconds, String IPAddr,int id,CountDownLatch latch,BufferedWriter out) {
        this.threadGroupSize = threadGroupSize;
        this.delaySeconds = delaySeconds;
        this.IPAddr = IPAddr;
        this.id = id;
        this.latch = latch;
        this.out = out;
    }


    public void run(){
        long groupStartTime = System.currentTimeMillis();
        CountDownLatch threadGroupSizeLatch = new CountDownLatch(threadGroupSize); // 每线程组有多少线程 -- 10
        System.out.println("Thread Group:" + id + " is running");

        for (int j = 0; j < threadGroupSize; j++) {                 // 每线程组有多少线程 -- 10
            new Thread(new ClientAPI(IPAddr, threadGroupSizeLatch, id, j + 1)).start();
        }


        try {
            threadGroupSizeLatch.await();    // 等待threadGroupSize个线程(10)跑完，到这里
            long groupEndTime = System.currentTimeMillis();
//            out.write("group" + id + ": " + (groupEndTime - groupStartTime) + " ms\n");   // 写每个线程组时间
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        latch.countDown();     // 一个线程组完成
    }
}
