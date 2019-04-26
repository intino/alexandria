package io.intino.alexandria.movv;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;

public class Index_ {

    @After
	public void tearDown() {
        new File("index").delete();
    }

    @Test
    public void should_store_and_load_from_file() throws IOException {
        Index index = new Index.BulkIndex(32);
        index.put(4,0);
        index.put(2,1);
        index.put(5,2);
        index.put(3,3);
        index.put(8,4);
        index.put(9,5);
        index.put(0,6);
        index.put(1,7);
        index.put(7,8);
        index.put(6,9);
        index.store(new File("index"));
        assertThat(index).isEqualTo(Index.load(new File("index")));
    }

    @Test
    public void should_be_sorted_and_without_duplicates() {
        for (int i = 0; i < 100; i++)
            assertThat(isSorted(index().ids)).isTrue();
    }

    private Index.RandomIndex index() {
        Index.RandomIndex index = new Index.RandomIndex(32, new long[0], new int[0]);
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
