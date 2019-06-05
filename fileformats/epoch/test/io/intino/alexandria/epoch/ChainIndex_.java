package io.intino.alexandria.epoch;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;

public class ChainIndex_ {

    @After
	public void tearDown() {
        new File("index").delete();
        new File("index.chain").delete();
    }

    @Test
    public void should_store_and_load_from_file() throws IOException {
        ChainIndex chainIndex = new ChainIndex.BulkChainIndex(new File("index"), 32);
        chainIndex.put(4,0);
        chainIndex.put(2,1);
        chainIndex.put(5,2);
        chainIndex.put(3,3);
        chainIndex.put(8,4);
        chainIndex.put(9,5);
        chainIndex.put(0,6);
        chainIndex.put(1,7);
        chainIndex.put(7,8);
        chainIndex.put(6,9);
        chainIndex.close();
        assertThat(chainIndex).isEqualTo(ChainIndex.load(new File("index")));
    }

    @Test
    public void should_be_sorted_and_without_duplicates() {
        for (int i = 0; i < 100; i++)
            assertThat(isSorted(index().ids)).isTrue();
    }

    private ChainIndex.RandomChainIndex index() {
        ChainIndex.RandomChainIndex index = new ChainIndex.RandomChainIndex(new File("index"), 32, new long[0], new int[0]);
        long id;
        for (int i = 0; i < 1000; i++) {
            do {
                id = abs(new Random().nextInt());
            } while (index.indexOf(id) >= 0);
            index.put(id,-1);
        }
        return index;
    }

    private boolean isSorted(long[] ids) {
        for (int i = 1; i < ids.length - 1; i++)
            if (ids[i] <= ids[i - 1]) return false;
        return true;
    }



}
