package io.intino.concurrency;

import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.UnsortedLedStreamBuilder;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.buffers.store.ByteStore;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UnsortedLedStreamBuilderMultithreading {

    private static final int NUM_THREADS = 1;
    private static final long NUM_TRANSACTIONS = 100_000;

    @Test
    public void test() throws InterruptedException {
        final File tempFile = new File("test_unsorted.led");
        tempFile.delete();
        UnsortedLedStreamBuilder<Transaction> builder = new UnsortedLedStreamBuilder<>(Transaction.class, tempFile);
        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
        for(long i = 1;i <= NUM_TRANSACTIONS;i++) {
            final long index = i;
            threadPool.submit(() -> {
                builder.append(t -> {
                    t.id(index);
                    Assert.assertEquals(index, t.id());
                });
            });
        }

        threadPool.shutdown();
        threadPool.awaitTermination(10, TimeUnit.DAYS);

        LedStream<Transaction> stream = builder.build();
        Set<Long> ids = new LinkedHashSet<>();

        stream.forEach(t -> {
            Assert.assertTrue("Id out of range: " + t.id(), t.id() > 0 && t.id() <= NUM_TRANSACTIONS);
            Assert.assertTrue("Repeated id: " + t.id(), ids.add(t.id()));
        });
    }

    private static class Transaction extends Schema {

        public static final int SIZE = Long.BYTES;
        public static final UUID SERIAL_UUID = UUID.randomUUID();
        public static final SchemaFactory<Transaction> FACTORY = new SchemaFactory<>(Transaction.class) {
            @Override
            public Transaction newInstance(ByteStore store) {
                return new Transaction(store);
            }
        };

        public Transaction(ByteStore store) {
            super(store);
        }

        @Override
        public long id() {
            return bitBuffer.getAlignedLong(0);
        }

        public Transaction id(long id) {
            bitBuffer.setAlignedLong(0, id);
            return this;
        }

        @Override
        public int size() {
            return SIZE;
        }

        @Override
        public UUID serialUUID() {
            return SERIAL_UUID;
        }
    }
}
