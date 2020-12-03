package io.intino.alexandria.led;

import io.intino.alexandria.led.util.LedUtils;
import io.intino.alexandria.led.util.sorting.LedExternalMergeSort;
import io.intino.alexandria.logger.Logger;
import io.intino.test.transactions.TestTransaction;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestLedExternalMergeSort {

    private static final int NUM_TRANSACTIONS_IN_MEMORY = 100_000;

    private final File srcFile = new File("temp/unsorted_led.led");
    private final File destFile = new File("temp/sorted_led.led");

    @Before
    public void setUp() throws Exception {
        srcFile.delete();
        destFile.delete();
    }

    @Test
    public void testEmpty() {
        createLed(srcFile, 0);
        mergeSort();
    }

    @Test
    public void testOne() {
        createLed(srcFile, 1);
        mergeSort();
    }

    @Test
    public void testFew() {
        createLed(srcFile, 11);
        mergeSort();
    }

    @Test
    public void testNormal() {
        createLed(srcFile, 1_501_017);
        mergeSort();
    }

    @Test
    public void testLarge() {
        createLed(srcFile, 10_486_926);
        mergeSort();
    }

    @Ignore
    @Test
    public void testSuperLarge() {
        createLed(srcFile, 50_000_001);
        mergeSort();
    }

    @Ignore
    @Test
    public void testMegaLarge() {
        createLed(srcFile, 100_000_001);
        mergeSort();
    }

    private void mergeSort() {
        System.out.println(">> Testing " + srcFile + "(" +
                srcFile.length() / 1024.0 / 1024.0 + " MB)...");
        LedHeader sourceHeader = LedHeader.from(srcFile);
        new LedExternalMergeSort(srcFile, destFile)
                .numTransactionsInMemory(NUM_TRANSACTIONS_IN_MEMORY)
                .checkChunkSorting(true)
                .sort();
        System.out.println("	>> Validating result led...");
        LedHeader destHeader = LedHeader.from(destFile);
        assertEquals("Sorting did not maintain of information: " + sourceHeader.elementCount() + " != " + destHeader.elementCount(),
                sourceHeader.elementCount(), destHeader.elementCount());

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

    private void createLed(File file, int numTransactions) {
        System.out.println("Creating unsorted led of " + numTransactions + ", each transaction of " + TestTransaction.SIZE + " bytes...");
        LedStream.Builder<TestTransaction> builder = new UnsortedLedStreamBuilder<>(TestTransaction.class, file);
        Random random = new Random();
        double start = System.currentTimeMillis();
        for(int i = 0;i < numTransactions;i++) {
            final long id = random.nextInt(100000000);
            builder.append(t -> t.id(id));
            if(i % 1_000_000 == 0) {
                double time = (System.currentTimeMillis() - start) / 1000.0;
                System.out.println(">> Created " + i + " elements (" + time + " seconds)");
            }
        }
        builder.build();
        System.out.println("Build finish");
    }
}
