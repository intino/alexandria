package io.intino.alexandria.fsm;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.*;

public class FileSessionManager_ {

    public static void main(String[] args) throws InterruptedException {

        FileSessionManager fsm1 = createFileSessionManager("A", "mailbox_1", "mailbox_2");
        FileSessionManager fsm2 = createFileSessionManager("B", "mailbox_2", "mailbox_1");

        fsm1.start();
        fsm2.start();

        ExecutorService threadPool = Executors.newCachedThreadPool();

        threadPool.submit(() -> writeMessages(fsm1));
        threadPool.submit(() -> writeMessages(fsm2));

//        while(true) {
//            Thread.sleep(10000);
//            fsm1.pause();
//            Thread.sleep(10000);
//            fsm1.resume();
//        }

        threadPool.shutdown();
        threadPool.awaitTermination(1, DAYS);
    }

    private static void writeMessages(FileSessionManager fsm) {
        long count = 0;
        int iterations = 0;
        while(true) {
            Random random = new Random(System.nanoTime());
            if(fsm.state() == StatefulScheduledService.State.Running) {
                int limit = random.nextInt(10_000);
                for(int i = 0;i < limit;i++) {
                    fsm.publish(fsm.id() + ": " + count++);
                }
                System.out.println(fsm.id() + " wrote " + limit + " messages in " + fsm.outputMailbox().root().getName());
            }
            try {
                Thread.sleep(random.nextInt(500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(++iterations == 2000) break;
        }
    }

    private static FileSessionManager createFileSessionManager(String id, String input, String output) {
        return new FileSessionManager.Builder()
                .id(id)
                .readsFrom(new File("temp", input))
                .writesTo(new File("temp", output))
                .atFixedRate(5, SECONDS)
                .maxBytesPerSession(1024 * 1024 * 1024) // 1MB
                .sessionTimeout(5, SECONDS)
                .lockTimeout(2, MINUTES)
                .onMessageProcess(FileSessionManager_::processMessage)
                .build();
    }

    private static long x = 0;

    private static void processMessage(String message) {
        if(Math.random() >= 0.99) {
            throw new RuntimeException(message + " error");
        }
        for(int i = 0;i < message.length();i++) {
            x += (int) message.charAt(i);
        }
        if(x % 10_000_000 == 0) {
            System.out.println(x);
        }
    }
}
