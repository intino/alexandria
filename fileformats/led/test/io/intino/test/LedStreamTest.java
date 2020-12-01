package io.intino.test;

import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.allocators.TransactionAllocator;
import io.intino.alexandria.led.allocators.stack.StackAllocators;
import io.intino.alexandria.led.leds.IteratorLedStream;
import io.intino.test.transactions.TestTransaction;
import io.intino.test.transactions.Venta;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@Ignore
public class LedStreamTest {

    private LedStream<TestTransaction> ledStream;
    private final int numElements;

    public LedStreamTest() {
        numElements = 521_331;
    }

    @Before
    public void setUp()  {
        ledStream = createTestTransactionsLedStream(Function.identity());
    }

    private LedStream<TestTransaction> createTestTransactionsLedStream(Function<Integer, Integer> indexMapper) {
        TransactionAllocator<TestTransaction> allocator = StackAllocators.newManaged(TestTransaction.SIZE, numElements, TestTransaction::new);
        return IteratorLedStream.fromStream(TestTransaction.SIZE,
                IntStream.range(1, numElements).map(indexMapper::apply).mapToObj(i ->
                        allocator.calloc().id(i).a((short) (i + 1)).b(i * 2)));
    }

    @Test
    public void testFilter() {
        Iterator<TestTransaction> TestTransactions = ledStream.filter(c -> c.id() % 2 == 0);
        while(TestTransactions.hasNext()) {
            assertEquals(0, TestTransactions.next().id() % 2);
        }
    }

    @Test
    public void testPeek() {
        Iterator<TestTransaction> TestTransactions = ledStream.peek(c -> c.id(c.id() * -1));
        int i = 1;
        while(TestTransactions.hasNext()) {
            assertEquals(-i, TestTransactions.next().id());
            ++i;
        }
    }

    @Test
    public void testMap() {

        Queue<TestTransaction> testTransactions = new ArrayDeque<>(numElements);

        Iterator<Venta> ventas = ledStream.map(
                Venta.SIZE, Venta::new, (c, ca) -> {
                    ca.id(c.id()).kwh(c.a()).importe(c.b());
                    testTransactions.add(c);
                });

        while(ventas.hasNext()) {
            Venta venta = ventas.next();
            TestTransaction testTransaction = testTransactions.poll();
            assertEquals(testTransaction.id(), venta.id());
            assertEquals(testTransaction.a(),venta.kwh(), 0.0001f);
            assertEquals(testTransaction.b(),venta.importe(), 0.0001f);
        }
    }

    @Test
    public void testMerge() {

        Queue<TestTransaction> TestTransactionsQueue = new PriorityQueue<>();

        LedStream<TestTransaction> a = createTestTransactionsLedStream(Function.identity()).peek(TestTransactionsQueue::add);
        LedStream<TestTransaction> b = createTestTransactionsLedStream(i -> i + numElements).peek(TestTransactionsQueue::add);

        LedStream<TestTransaction> merge = a.merge(b);

        while(merge.hasNext()) {
            TestTransaction actual = merge.next();
            TestTransaction expected = TestTransactionsQueue.poll();
            assertEquals(expected.id(), actual.id());
        }
    }

    @Test
    public void testRemoveAllNoRemoving() {

        Queue<TestTransaction> TestTransactions = new ArrayDeque<>(numElements);

        LedStream<TestTransaction> a = createTestTransactionsLedStream(Function.identity()).peek(TestTransactions::add);
        LedStream<TestTransaction> b = createTestTransactionsLedStream(i -> i + numElements);

        LedStream<TestTransaction> complementAB = a.removeAll(b);

        assertTrue(complementAB.hasNext());

        while(complementAB.hasNext()) {
            TestTransaction actual = complementAB.next();
            TestTransaction expected = TestTransactions.poll();
            assertEquals(expected.id(), actual.id());
        }
    }

    @Test
    public void testRemoveAllResultEmpty() {

        LedStream<TestTransaction> a = createTestTransactionsLedStream(Function.identity());
        LedStream<TestTransaction> b = createTestTransactionsLedStream(Function.identity());

        LedStream<TestTransaction> complementAB = a.removeAll(b);

        assertFalse(complementAB.hasNext());
    }

    @Test
    public void testRemoveAllEvenids() {

        Queue<TestTransaction> TestTransactionsOddid = new ArrayDeque<>(numElements);

        LedStream<TestTransaction> a = createTestTransactionsLedStream(Function.identity()).peek(c -> {
            if(c.id() % 2 != 0) {
                TestTransactionsOddid.add(c);
            }
        });
        LedStream<TestTransaction> b = createTestTransactionsLedStream(i -> i * 2);

        LedStream<TestTransaction> complementAB = a.removeAll(b);

        assertTrue(complementAB.hasNext());

        while(complementAB.hasNext()) {
            TestTransaction actual = complementAB.next();
            assertNotEquals(0, actual.id() % 2);
            TestTransaction expected = TestTransactionsOddid.poll();
            assertEquals(expected.id(), actual.id());
        }
    }

    @Test
    public void testRetainAllEmptyResult() {
        LedStream<TestTransaction> a = createTestTransactionsLedStream(Function.identity());
        LedStream<TestTransaction> b = createTestTransactionsLedStream(i -> -i);

        LedStream<TestTransaction> intersectionAB = a.retainAll(b);

        assertFalse(intersectionAB.hasNext());
    }

    @Test
    public void testRetainAllSameLedStream() {

        Queue<TestTransaction> TestTransactions = new ArrayDeque<>(numElements);

        LedStream<TestTransaction> a = createTestTransactionsLedStream(Function.identity()).peek(TestTransactions::add);
        LedStream<TestTransaction> b = createTestTransactionsLedStream(Function.identity());

        LedStream<TestTransaction> intersectionAB = a.retainAll(b);

        assertTrue(intersectionAB.hasNext());

        while(intersectionAB.hasNext()) {
            TestTransaction actual = intersectionAB.next();
            TestTransaction expected = TestTransactions.poll();
            assertEquals(expected.id(), actual.id());
        }
    }

    @Test
    public void testRetainAllEvenids() {

        Queue<TestTransaction> TestTransactionsEvenid = new ArrayDeque<>(numElements);

        LedStream<TestTransaction> a = createTestTransactionsLedStream(Function.identity()).peek(c -> {
            if(c.id() % 2 == 0) {
                TestTransactionsEvenid.add(c);
            }
        });
        LedStream<TestTransaction> b = createTestTransactionsLedStream(i -> i * 2);

        LedStream<TestTransaction> intersectionAB = a.retainAll(b);

        assertTrue(intersectionAB.hasNext());

        while(intersectionAB.hasNext()) {
            TestTransaction actual = intersectionAB.next();
            assertEquals(0, actual.id() % 2);
            TestTransaction expected = TestTransactionsEvenid.poll();
            assertEquals(expected.id(), actual.id());
        }
    }
}