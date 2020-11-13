package io.intino.alexandria.led;

import io.intino.alexandria.logger.Logger;
import io.intino.test.transactions.TestTransaction;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.junit.Assert.*;

public class LedStreamBuilderTest {

    @Test
    public void test() {
        System.out.println(TestTransaction.SIZE);
        LedStream.Builder<TestTransaction> builder = LedStream.builder(TestTransaction.class);
        Random random = new Random();
        double start = System.currentTimeMillis();
        for(int i = 0;i < 10_000_000;i++) {
            long id = i;
            builder.create(t -> t.id(random.nextInt()));
            if(i % 1_000_000 == 0) {
                double time = (System.currentTimeMillis() - start) / 1000.0;
                System.out.println(">> Created " + i + " elements (" + time + " seconds)");
            }
        }

        System.out.println("Build finish");

        start = System.currentTimeMillis();

        try(LedStream<TestTransaction> ledStream = builder.build()) {
            long lastId = Long.MIN_VALUE;
            int i = 0;
            while(ledStream.hasNext()) {
                final long id = ledStream.next().id();
                assertTrue(i + " => " + id + " < " + lastId, lastId <= id);
                lastId = id;
                ++i;
            }
        } catch (Exception e) {
            Logger.error(e);
        }

        double time = (System.currentTimeMillis() - start) / 1000.0;

        System.out.println("iteration in " + time + " seconds");

    }

}