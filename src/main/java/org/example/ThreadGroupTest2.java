package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.opencsv.CSVWriter;

public class ThreadGroupTest2 {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        long totalRequests = 0;

        int threadGroupSize = input.nextInt();
        int numThreadGroups = input.nextInt();
        int delaySeconds = input.nextInt();
        String IPAddr = "http://54.80.72.233:8080/assignment1_war/AlbumStore/albums";
        Client client = new Client(IPAddr);
        FileWriter fileWriter = new FileWriter("/Users/youyun/Documents/java project/client/src/main/resources/java3.csv");
        CSVWriter csvWriter = new CSVWriter(fileWriter);

        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(10000);

//        for (int i = 0; i < 10; i++) {
//            executor.submit(() -> {
//                for (int j = 0; j < 100; j++) {
//                    try {
//                        client.get();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                try {
//                    client.post();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//
//            });
//        }

        System.out.println("test");

        for (int group = 1; group <= numThreadGroups; group++) {
            long groupStartTime = System.currentTimeMillis();
            CountDownLatch latch = new CountDownLatch(threadGroupSize);

            for (int i = 0; i < threadGroupSize; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < 1000; j++) {
                        // Wrap the POST and GET operations with retry logic
                        for (int retry = 0; retry < 5; retry++) {
                            try {
                                long start = System.currentTimeMillis();
                                client.get();
                                List<String[]> data = new ArrayList<String[]>();
                                long end = System.currentTimeMillis();
                                data.add(new String[] {String.valueOf(start),"get", String.valueOf(end-start),"200"});
                                csvWriter.writeAll(data);
                                break;
                            }catch (Exception e){
                                System.out.println("crowded");
                            }
                        }
                        for (int retry = 0; retry < 5; retry++) {
                            try {
                                long start = System.currentTimeMillis();
                                client.post();
                                List<String[]> data = new ArrayList<String[]>();
                                long end = System.currentTimeMillis();
                                data.add(new String[] {String.valueOf(start),"post", String.valueOf(end-start),"200"});
                                csvWriter.writeAll(data);
                                break;
                            }catch (Exception e){
                                System.out.println("crowded");
                            }
                        }
                    }
                    latch.countDown();
                });
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long groupEndTime = System.currentTimeMillis();
            long groupElapsedTime = groupEndTime - groupStartTime;

            if (group < numThreadGroups) {
                try {
                    Thread.sleep(delaySeconds * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            // Increment the total number of requests for this group
            totalRequests += threadGroupSize * 2000;
        }
        executor.shutdown();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        // Calculate and print requests per second (RPS)
        double rps = (double) totalRequests / (totalTime / 1000.0);
        System.out.println("Total Requests: " + totalRequests);
        System.out.println("Total Time (ms): " + totalTime);
        System.out.println("Requests Per Second (RPS): " + rps);
    }
}

