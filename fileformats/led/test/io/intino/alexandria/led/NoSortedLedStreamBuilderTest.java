package io.intino.alexandria.led;

import io.intino.alexandria.logger.Logger;
import io.intino.test.transactions.TestTransaction;
import org.junit.Test;

import java.io.File;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertTrue;

public class NoSortedLedStreamBuilderTest {

    @Test
    public void test() {
        System.out.println(TestTransaction.SIZE);
        LedStream.Builder<TestTransaction> builder = new ExtSortLedStreamBuilder<>(TestTransaction.class, new File("temp"));
        Random random = new Random();
        double start = System.currentTimeMillis();
        final int numElements = 100_000_000;
        for(int i = 0;i < numElements;i++) {
            long id = i;
            builder.append(t -> t.id(random.nextInt()));
            if(i % 1_000_000 == 0) {
                double time = (System.currentTimeMillis() - start) / 1000.0;
                System.out.println(">> Created " + i + " elements (" + time + " seconds)");
            }
        }

        System.out.println("Build finish");

        start = System.currentTimeMillis();

        try(LedStream<TestTransaction> ledStream = builder.build()) {
            ledStream.serialize(new File("temp/u_ledstreambuilder_full_led_" + numElements +".led"));
        } catch (Exception e) {
            Logger.error(e);
        }

        double time = (System.currentTimeMillis() - start) / 1000.0;

        System.out.println("iteration in " + time + " seconds");

    }

}