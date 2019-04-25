package io.intino.alexandria.zet;

import java.io.*;

public class ZFile {
    private File file;

    public ZFile(String name) {
        this(new File(name));
    }

    public ZFile(File file) {
        this.file = file;
    }

    public long size() throws IOException {
        RandomAccessFile file = new RandomAccessFile(this.file, "r");
        file.seek(file.length() - 8);
        long size = file.readLong();
        file.close();
        return size;
    }


}
