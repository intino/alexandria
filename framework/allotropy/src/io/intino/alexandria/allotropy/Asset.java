package io.intino.alexandria.allotropy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Asset {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private final String path;
    private final File root;

    public Asset(File root, String path) {
        this.root = root;
        this.path = path;
    }

    public File get(Date date) {
        return new File(root, path.replace("*", dateFormat.format(date)));
    }

    public void put(Date date, File file) {
        //zip
    }
}