package io.intino.alexandria.led;

import io.intino.alexandria.led.util.LedUtils;
import io.intino.alexandria.logger.Logger;
import io.intino.test.transactions.TestTransaction;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class TestLedExternalMergeSort {

    @Ignore
    @Test
    public void test() {

        File file = new File("temp/unsorted_led.led");
        File destFile = new File("temp/sorted_led.led");

        // createLed(file);

        System.out.println(">> Merging...");
        LedUtils.mergeSort(file, destFile);
        System.out.println(">> Files merged!");

        System.out.println(">> Checking sorting...");

        try(LedStream<TestTransaction> ledStream = new LedReader(destFile).read(TestTransaction::new)) {

            long lastId = Long.MIN_VALUE;

            int count = 0;
            while(ledStream.hasNext()) {
                long id = ledStream.next().id();
                assertTrue((count) + " => Not sorted: " + id  + " < " + lastId, id >= lastId);
                lastId = id;
                ++count;
            }

        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private void createLed(File file) {
        System.out.println(TestTransaction.SIZE);
        LedStream.Builder<TestTransaction> builder = new UnsortedLedStreamBuilder<>(TestTransaction.class, new File("temp"));
        Random random = new Random();
        double start = System.currentTimeMillis();
        final int numElements = 100_000_000;
        for(int i = 0;i < numElements;i++) {
            long id = i;
            builder.append(t -> t.id(random.nextInt()).a((short) random.nextInt()).b(random.nextInt()).c(random.nextFloat()));
            if(i % 1_000_000 == 0) {
                double time = (System.currentTimeMillis() - start) / 1000.0;
                System.out.println(">> Created " + i + " elements (" + time + " seconds)");
            }
        }

        builder.build();
        System.out.println("Build finish");
    }
}
