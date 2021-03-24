package io.intino.alexandria.led;

import io.intino.alexandria.logger.Logger;
import io.intino.test.schemas.TestSchema;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class NotSortedLedStreamBuilderTest {

    public static final int NUM_ELEMENTS = 10_000_000;
    public static final File OUT_FILE = new File("temp/u_ledstreambuilder_full_led_" + NUM_ELEMENTS + ".led");

    static {
        OUT_FILE.getParentFile().mkdirs();
    }

    @Test
    public void test() {
        System.out.println(TestSchema.SIZE);
        LedStream.Builder<TestSchema> builder = new UnsortedLedStreamBuilder<>(TestSchema.class, OUT_FILE);
        Random random = new Random();
        double start = System.currentTimeMillis();
        for(int i = 0;i < NUM_ELEMENTS;i++) {
            long id = i;
            builder.append(t -> t.id(random.nextInt()));
            if(i % NUM_ELEMENTS / 10 == 0) {
                double time = (System.currentTimeMillis() - start) / 1000.0;
                System.out.println(">> Created " + i + " elements (" + time + " seconds)");
            }
        }

        System.out.println("Build finish");

        start = System.currentTimeMillis();

        try(LedStream<TestSchema> ledStream = builder.build()) {
            ledStream.serialize(OUT_FILE);
        } catch (Exception e) {
            Logger.error(e);
        }

        double time = (System.currentTimeMillis() - start) / 1000.0;

        System.out.println("iteration in " + time + " seconds");

    }

    @AfterClass
    public static void afterClass() throws Exception {
        OUT_FILE.delete();
    }
}