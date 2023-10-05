package org.example;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadGroupTest {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        long totalRequests = 0;

        int threadGroupSize = input.nextInt();
        int numThreadGroups = input.nextInt();
        int delaySeconds = input.nextInt();
        String IPAddr = "http://54.87.245.13:8082/AlbumStore/albums";
        Client client = new Client(IPAddr);

        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(100000);

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
                                client.get();
                                System.out.println("get count: "+j);
                                break;
                            }catch (Exception e){
                                System.out.println("crowded");
                            }
                        }
                        for (int retry = 0; retry < 5; retry++) {
                            try {
                                client.post();
                                System.out.println("post count: "+j);
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
            System.out.println("cururent finished request:"+totalRequests);
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

