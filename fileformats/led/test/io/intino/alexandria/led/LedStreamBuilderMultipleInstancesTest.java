package io.intino.alexandria.led;

import io.intino.alexandria.logger.Logger;
import io.intino.test.transactions.TestTransaction;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

public class LedStreamBuilderMultipleInstancesTest {

    private static final int NUM_ELEMENTS = 500_000_000;
    private static final int NUM_ITERATIONS = 100_000;
    private static final int BLOCK_SIZE = 100_000;
    private static final int NUM_DISTINCT_TRANSACTIONS = 10;

    @Test
    public void test() {
        System.out.println(">> Testing " + TestTransaction.SIZE);

        Random random = new Random();

        List<LocalDate> months = LocalDate.of(2019, 1, 1).datesUntil(LocalDate.of(2020, 1, 1))
                .collect(Collectors.toList());

        List<Map<LocalDate, LedStream.Builder<TestTransaction>>> builders = new ArrayList<>();
        for(int i = 0;i < NUM_DISTINCT_TRANSACTIONS;i++) {
            builders.add(new ConcurrentHashMap<>());
        }

        final double start = System.currentTimeMillis();

        AtomicInteger count = new AtomicInteger();

        IntStream.range(0, NUM_ELEMENTS).unordered().sequential().forEach(i -> {
            final LocalDate month = months.get(random.nextInt(months.size()));
            LedStream.Builder<TestTransaction> builder = builders.get(random.nextInt(builders.size()))
                    .computeIfAbsent(month,
                    k -> LedStream.builder(TestTransaction.class, BLOCK_SIZE, new File("temp")));
            synchronized (builder) {
                builder.append(t -> t.id(random.nextInt()));
            }
            if(count.incrementAndGet() % 1_000_000 == 0) {
                double time = (System.currentTimeMillis() - start) / 1000.0;
                System.out.println(">> Created " + count.get() + " elements (" + (count.get() / (float)NUM_ELEMENTS)*100 + "% in "+ time + " seconds)");
            }
        });

        System.out.println(">> Simulation finished");

        System.out.println(">> Starting iteration/serialization...");

        final double start2 = System.currentTimeMillis();

        for(var map : builders) {

            for(Map.Entry<LocalDate, LedStream.Builder<TestTransaction>> entry : map.entrySet()) {

                System.out.println(">> Iterating/Serializing " + entry.getKey() + "...");

                LedStream.Builder<TestTransaction> builder = entry.getValue();

                try(LedStream<TestTransaction> ledStream = builder.build()) {

                    AtomicLong lastId = new AtomicLong(Long.MIN_VALUE);
                    AtomicInteger i = new AtomicInteger();

                    ledStream.peek(item -> {
                        final long id = item.id();
                        assertTrue(i.get() + " => " + id + " < " + lastId, lastId.get() <= id);
                        lastId.set(id);
                        i.incrementAndGet();
                    }).serialize(new File("temp/ledstreambuilder_full_led_" + entry.getKey() + "_" + NUM_ELEMENTS +".led"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        double time = (System.currentTimeMillis() - start2) / 1000.0;

        System.out.println(">> Iteration finished in " + time + " seconds");

    }

}