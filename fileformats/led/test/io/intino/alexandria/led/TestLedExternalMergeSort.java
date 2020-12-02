package io.intino.alexandria.led;

import io.intino.alexandria.led.util.LedUtils;
import io.intino.alexandria.logger.Logger;
import io.intino.test.transactions.TestTransaction;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import static org.junit.Assert.assertTrue;

@Ignore
public class TestLedExternalMergeSort {

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

    @Test
    public void testSuperLarge() {
        createLed(srcFile, 50_000_001);
        mergeSort();
    }

    private void mergeSort() {
        System.out.println(">> Merging...");
        LedUtils.sort(srcFile, destFile);
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

    private void createLed(File file, int numTransactions) {
        System.out.println("Creating unsorted led of " + numTransactions + ", each transaction of " + TestTransaction.SIZE + " bytes...");
        LedStream.Builder<TestTransaction> builder = new UnsortedLedStreamBuilder<>(TestTransaction.class, file);
        Random random = new Random();
        double start = System.currentTimeMillis();
        for(int i = 0;i < numTransactions;i++) {
            builder.append(t -> t.id(random.nextInt())
                    .a((short) random.nextInt())
                    .b(random.nextInt())
                    .c(random.nextFloat()));
            if(i % 1_000_000 == 0) {
                double time = (System.currentTimeMillis() - start) / 1000.0;
                System.out.println(">> Created " + i + " elements (" + time + " seconds)");
            }
        }
        builder.build();
        System.out.println("Build finish");
    }
}
