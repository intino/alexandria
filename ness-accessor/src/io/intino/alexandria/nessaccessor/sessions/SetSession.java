package io.intino.alexandria.nessaccessor.sessions;

import io.intino.alexandria.nessaccessor.NessAccessor;

import java.io.*;
import java.util.stream.Stream;

public class SetSession {

    private OutputStream data;

    public SetSession(OutputStream data) {
        this.data = new BufferedOutputStream(data);
    }

    public Segment segment(String tank, NessAccessor.SetStore.Timetag timetag) {
        return new Segment(tank, timetag);
    }

    public class Segment {
        private ByteArrayOutputStream os;
        private DataOutputStream data;

        public Segment(String tank, NessAccessor.SetStore.Timetag timetag) {
            os = new ByteArrayOutputStream();
            data = new DataOutputStream(os);
            //TODO
        }

        public void put(Stream<Long> ids) {
            ids.forEach(this::write);
        }

        public void put(long... ids)  {
            for (long id : ids) write(id);
        }

        private void write(Long id) {
            try { data.writeLong(id); } catch (IOException ignored) { }
        }

        public void close() throws IOException {
            os.writeTo(data);
        }

    }
    public void close() throws IOException {
        data.close();
    }
}
