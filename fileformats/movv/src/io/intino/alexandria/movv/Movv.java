package io.intino.alexandria.movv;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Movv {
    private final Index index;
    private final RandomAccessFile raf;

    public Movv(File file) throws IOException {
        this.index = Index.load(indexOf(file));
        this.raf = new RandomAccessFile(file, "r");
    }

    static File indexOf(File file) {
        return new File(file.getAbsolutePath() + ".i");
    }

    public Mov get(long id) {
        return new Mov(index, access()).of(id);
    }

    public boolean contains(long id) {
        return index.indexOf(id) != -1;
    }

    private Access access() {
        return Access.of(raf, index.dataSize());
    }

}
