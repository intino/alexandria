package io.intino.alexandria.led;

import io.intino.alexandria.led.util.sorting.LedExternalMergeSort;
import io.intino.alexandria.logger.Logger;
import io.intino.test.schemas.TestSchema;
import org.junit.*;

import java.io.File;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestLedExternalMergeSort {

    private static final int NUM_SCHEMAS_IN_MEMORY = 100_000;

    private static final File SRC = new File("temp/unsorted_led.led");
    private static final File DST = new File("temp/sorted_led.led");

    @BeforeClass
    public static void beforeClass() throws Exception {
        SRC.delete();
        DST.delete();
    }

    @Before
    public void setUp() throws Exception {
        SRC.getParentFile().mkdirs();
        DST.getParentFile().mkdirs();
    }

    @After
    public void tearDown() throws Exception {
        SRC.delete();
        DST.delete();
    }

    @Test
    public void testEmpty() {
        createLed(SRC, 0);
        mergeSort();
    }

    @Test
    public void testOne() {
        createLed(SRC, 1);
        mergeSort();
    }

    @Test
    public void testFew() {
        createLed(SRC, 11);
        mergeSort();
    }

    @Test
    public void testNormal() {
        createLed(SRC, 2_501_017);
        mergeSort();
    }

    @Ignore
    @Test
    public void testLarge() {
        createLed(SRC, 10_486_926);
        mergeSort();
    }

    @Ignore
    @Test
    public void testSuperLarge() {
        createLed(SRC, 50_000_001);
        mergeSort();
    }

    @Ignore
    @Test
    public void testMegaLarge() {
        createLed(SRC, 100_000_001);
        mergeSort();
    }

    private void mergeSort() {
        System.out.println(">> Testing " + SRC + "(" +
                SRC.length() / 1024.0 / 1024.0 + " MB)...");
        LedHeader sourceHeader = LedHeader.from(SRC);
        new LedExternalMergeSort(SRC, DST)
                .numTransactionsInMemory(NUM_SCHEMAS_IN_MEMORY)
                .checkChunkSorting(true)
                .sort();
        System.out.println("	>> Validating result led...");
        LedHeader destHeader = LedHeader.from(DST);
        assertEquals("Sorting did not maintain of information: " + sourceHeader.elementCount() + " != " + destHeader.elementCount(),
                sourceHeader.elementCount(), destHeader.elementCount());

        System.out.println(">> Checking sorting...");

        try(LedStream<TestSchema> ledStream = new LedReader(DST).read(TestSchema.class)) {

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
        System.out.println("Creating unsorted led of " + numTransactions + ", each schema of " + TestSchema.SIZE + " bytes...");
        LedStream.Builder<TestSchema> builder = new UnsortedLedStreamBuilder<>(TestSchema.class, file);
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
