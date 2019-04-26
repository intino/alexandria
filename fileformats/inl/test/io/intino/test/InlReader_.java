package io.intino.test;

import io.intino.alexandria.Resource;
import io.intino.alexandria.ResourceStore;
import io.intino.alexandria.inl.MessageBuilder;
import io.intino.alexandria.inl.MessageReader;
import io.intino.test.schemas.AlertModified;
import io.intino.test.schemas.Document;
import io.intino.test.schemas.DocumentArray;
import io.intino.test.schemas.DocumentList;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static io.intino.alexandria.inl.MessageCast.cast;
import static org.assertj.core.api.Assertions.assertThat;

public class InlReader_ {
    @Test
    public void should_read_inl_as_object() throws IllegalAccessException {
        String inl =
                "[AlertModified]\n" +
                "alert: Alerts#e4a80d88-7bd5-4948-bd1d-7b38f47c40c7\n" +
                "active: true\n" +
                "mailingList: jbelizon@monentia.es\n" +
                "applyToAllStations: false\n";
        AlertModified object = cast(new MessageReader(inl).next()).as(AlertModified.class);

        assertThat(object.mailingList().size()).isEqualTo(1);
        assertThat(object).isNotNull();
        assertThat(MessageBuilder.toMessage(object).toString()).isEqualTo(inl);
    }

    @Test
    public void should_read_message_with_resource() throws IllegalAccessException {
        String inl =
                "[Document]\n" +
                "ts: 2017-03-21T07:39:00Z\n" +
                "attachment: @4444-444-44-44444.png\n";
        Document document = cast(new MessageReader(inl).next()).as(Document.class);
        assertThat(document.ts().toString()).isEqualTo("2017-03-21T07:39:00Z");
        assertThat(document.attachment().id()).isEqualTo("4444-444-44-44444.png");
        assertThat(toString(document.attachment().data()).length()).isEqualTo("4444-444-44-44444.png".length());
    }

    @Test
    public void should_read_message_with_resource_list() throws IllegalAccessException {
        String inl =
                "[DocumentList]\n" +
                "ts: 2017-03-21T07:39:00Z\n" +
                "files:\n" +
                "\t@4444-444-44-44444.png\n" +
                "\t@5555-555-55.jpeg\n";
        DocumentList documentList = cast(new MessageReader(inl).next()).as(DocumentList.class);
        assertThat(documentList.ts().toString()).isEqualTo("2017-03-21T07:39:00Z");
        assertThat(documentList.files().size()).isEqualTo(2);
        assertThat(documentList.files().get(0).id()).isEqualTo("4444-444-44-44444.png");
        assertThat(documentList.files().get(1).id()).isEqualTo("5555-555-55.jpeg");
        assertThat(toString(documentList.files().get(0).data())).isEqualTo("4444-444-44-44444.png");
        assertThat(toString(documentList.files().get(1).data())).isEqualTo("5555-555-55.jpeg");
    }

    private String toString(InputStream data) {
        return new Scanner(data).next();
    }

    @Test
    public void should_read_message_with_resource_array() throws IllegalAccessException {
        String inl =
                "[DocumentArray]\n" +
                "ts: 2017-03-21T07:39:00Z\n" +
                "files:\n" +
                "\t@4444-444-44-44444.png\n" +
                "\t@5555-555-55.jpeg\n";
        DocumentArray documentArray = cast(new MessageReader(inl).next()).as(DocumentArray.class);
        assertThat(documentArray.ts().toString()).isEqualTo("2017-03-21T07:39:00Z");
        assertThat(documentArray.files().length).isEqualTo(2);
        assertThat(documentArray.files()[0].id()).isEqualTo("4444-444-44-44444.png");
        assertThat(documentArray.files()[1].id()).isEqualTo("5555-555-55.jpeg");
        assertThat(toString(documentArray.files()[0].data()).length()).isEqualTo("4444-444-44-44444.png".length());
        assertThat(toString(documentArray.files()[1].data()).length()).isEqualTo("5555-555-55.jpeg".length());
    }


    private ResourceStore resourceStore() {
        return new ResourceStore() {
            @Override
            public List<Resource> resources() {
                return Collections.emptyList();
            }

            @Override
            public Resource get(String id) {
                return new Resource(id){
                    @Override
                    public InputStream data() {
                        return new ByteArrayInputStream(id.getBytes());
                    }
                };
            }

            @Override
            public void put(Resource resource) {

            }
        };
    }

}
