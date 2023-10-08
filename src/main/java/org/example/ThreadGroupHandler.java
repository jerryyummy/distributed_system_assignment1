package org.example;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;

public class ThreadGroupHandler {

    int threadGroupSize;
    int numThreadGroups;
    int delaySeconds;
    String IPAddr;

    public ThreadGroupHandler(int threadGroupSize, int numThreadGroups, int delaySeconds, String IPAddr) {
        this.threadGroupSize = threadGroupSize;
        this.numThreadGroups = numThreadGroups;
        this.delaySeconds = delaySeconds;
        this.IPAddr = IPAddr;
    }

    public void request() throws Exception {
        String fileName = "java.txt";
        long totalRequests = 0;
        CountDownLatch threadGroupLatch = new CountDownLatch(numThreadGroups);  // 线程组总数 10
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
        long startTime = System.currentTimeMillis();     // 线程组运行前时间

        for (int i = 0; i <numThreadGroups; i++) {
            new Thread(new MainThread(threadGroupSize, delaySeconds, IPAddr, i + 1, threadGroupLatch, out)).start();
            Thread.sleep(delaySeconds * 1000L);
            totalRequests += 2000;
        }

        threadGroupLatch.await();
        System.out.println("threadGroup all Done");
        long endTime = System.currentTimeMillis();       //  线程组运行完时间
        out.write("thread group run all time: " + (endTime - startTime) + " ms\n");
        out.write("throughput: " + (double) totalRequests / ((endTime - startTime) / 1000.0) + "\n");
        out.close();
    }

    public static void main(String[] args) throws Exception {
        ThreadGroupHandler threadGroupHandler = new ThreadGroupHandler(10, 10, 2, "http://54.89.65.236:8080/assignment1_war/AlbumStore/albums");
        threadGroupHandler.request();
        ThreadGroupHandler threadGroupHandler2 = new ThreadGroupHandler(10, 20, 2, "http://54.89.65.236:8080/assignment1_war/AlbumStore/albums");
        threadGroupHandler2.request();
        ThreadGroupHandler threadGroupHandler1 = new ThreadGroupHandler(10, 30, 2, "http://54.89.65.236:8080/assignment1_war/AlbumStore/albums");
        threadGroupHandler1.request();
    }

}
