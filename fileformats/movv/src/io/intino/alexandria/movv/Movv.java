package io.intino.alexandria.movv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class Movv implements Iterable<Mov> {
    private final ChainIndex chainIndex;
	private ChainReader chainReader;

    public Movv(File file) throws IOException {
	    this.chainIndex = ChainIndex.load(file);
        this.chainReader = ChainReader.load(rafOf(file), chainIndex.dataSize());
    }

	private RandomAccessFile rafOf(File file) throws FileNotFoundException {
		return new RandomAccessFile(chainFileOf(file), "r");
	}

	static File chainFileOf(File file) {
        return new File(file.getAbsolutePath() + ".chain");
    }

    public Mov get(long id) {
        return new Mov(chainIndex, chainReader).of(id);
    }

    public boolean contains(long id) {
	    return chainIndex.contains(id);
    }

    @Override
    public Iterator<Mov> iterator() {
        return new Iterator<Mov>() {
            Iterator<Long> iterator = chainIndex.iterator();
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Mov next() {
                return get(iterator.next());
            }
        };
    }
}
