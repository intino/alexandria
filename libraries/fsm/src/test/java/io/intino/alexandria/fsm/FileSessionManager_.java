package io.intino.alexandria.fsm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.*;

public class FileSessionManager_ {

    public static void main(String[] args) throws Exception {

        replaceMonths();

        FileSessionManager fsm1 = createFileSessionManager("A", "mailbox_1", "mailbox_2");
        FileSessionManager fsm2 = createFileSessionManager("B", "mailbox_2", "mailbox_1");

        fsm1.start();
        fsm2.start();

        ExecutorService threadPool = Executors.newCachedThreadPool();

        //threadPool.submit(() -> writeMessages(fsm1));
        threadPool.submit(() -> writeMessages(fsm2));

        while(true) {
            Thread.sleep(10000);
            Future<Instant> pause = fsm1.pause();
            Instant ts = pause.get();
//            if(ts == null) {
//                System.out.println("FSM could not be completely paused. State = " + fsm1.state());
//            } else {
//                System.out.println(fsm1.id() + " went fully paused: " + ts);
//            }
            Thread.sleep(10000);
            fsm1.resume();
        }

//        threadPool.shutdown();
//        threadPool.awaitTermination(1, DAYS);
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
                if(iterations % 100 == 0)
                    System.out.println(fsm.id() + " wrote " + limit + " messages in " + fsm.outputMailbox().root().getName());
            }
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(++iterations == 20000) break;
        }
    }

    private static FileSessionManager createFileSessionManager(String id, String input, String output) {
        return new FileSessionManager.Builder()
                .id(id)
                .readsFrom(new File("temp", input))
                .writesTo(new File("temp", output))
                .atFixedRate(5, SECONDS)
                .maxBytesPerSession(1024 * 1024) // 1MB
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

    private static void replaceMonths() throws IOException {
        File[] files = new File("C:\\Users\\naits\\Desktop\\MonentiaDev\\alexandria\\temp\\mailbox_2\\02_processed").listFiles();
        if(files == null) return;
        String[] months = {"202201", "202202"};
        Random r = new Random();
        for(File file : files) {
            Files.move(file.toPath(), new File(file.getAbsolutePath().replace("202203", months[r.nextInt(months.length)])).toPath());
        }
    }
}
