package io.intino.alexandria.movv;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class Movv implements Iterable<Mov> {
    private final Index index;
    private final RandomAccessFile raf;
    private Access access;

    public Movv(File file) throws IOException {
        this.index = Index.load(indexOf(file));
        this.raf = new RandomAccessFile(file, "r");
        this.access = access();
    }

    static File indexOf(File file) {
        return new File(file.getAbsolutePath() + ".i");
    }

    public Mov get(long id) {
        return new Mov(index, access).of(id);
    }

    public boolean contains(long id) {
        return index.contains(id);
    }

    private Access access() {
        return Access.of(raf, index.dataSize());
    }

    @Override
    public Iterator<Mov> iterator() {
        return new Iterator<Mov>() {
            Iterator<Long> iterator = index.iterator();
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
