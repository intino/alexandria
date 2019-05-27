package io.intino.alexandria.movv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Iterator;

import static io.intino.alexandria.movv.Movv.Mode.Disk;
import static io.intino.alexandria.movv.Movv.Mode.Memory;

public class Movv implements Iterable<Mov> {
    private final ChainIndex chainIndex;
	private final ChainReader chainReader;

    public Movv(File file) throws IOException {
    	this(file, Memory);
    }

    public Movv(File file, Mode mode) throws IOException {
	    this.chainIndex = ChainIndex.load(file);
        this.chainReader = mode == Disk ?
				ChainReader.load(rafOf(file), chainIndex.dataSize()) :
				ChainReader.load(contentOf(file), chainIndex.dataSize());
    }

	private RandomAccessFile rafOf(File file) throws FileNotFoundException {
		return new RandomAccessFile(chainFileOf(file), "r");
	}

	private byte[] contentOf(File file) throws IOException {
		return Files.readAllBytes(chainFileOf(file).toPath());
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

	public enum Mode {
    	Memory, Disk
	}
}
