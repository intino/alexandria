package io.intino.alexandria.inl;

import java.io.IOException;
import java.io.OutputStream;

public class InlWriter {
    private OutputStream os;

    public InlWriter(OutputStream os) {
        this.os = os;
    }

    public void write(Object object) throws IOException {
        os.write(InlBuilder.build(object).toInl().getBytes());
    }

    public void close() throws IOException {
        os.close();
    }



}
