package io.intino.test;

import io.intino.alexandria.inl.Message;
import io.intino.alexandria.inl.MessageReader;
import io.intino.alexandria.inl.MessageWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageWriter_ {
    private Message message;

    @Before
    public void setUp() throws Exception {
        message = readMessage();
    }

    @Test
    public void show_write_and_read_messages() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MessageWriter writer = new MessageWriter(os);
        writer.write(message);
        writer.write(message);
        writer.write(message);
        writer.close();
        MessageReader messages = new MessageReader(new String(os.toByteArray()));
        assertThat(messages.hasNext()).isTrue();
        assertThat(messages.next()).isEqualTo(message);
        assertThat(messages.hasNext()).isTrue();
        assertThat(messages.next()).isEqualTo(message);
        assertThat(messages.hasNext()).isTrue();
        assertThat(messages.next()).isEqualTo(message);
        assertThat(messages.hasNext()).isFalse();
    }

    private Message readMessage() {
        String inl =
                "[Document]\n" +
                "name: my attachment\n" +
                "file1: photo.png@002eb31f-f3b3-4ba2-adfa-d758c4a55abe\n" +
                "file2:\n" +
                "\tcv.pdf@885d35f3-2811-42c2-a202-b6a7e4b03465\n" +
                "\txx.png@b112828f-d7d0-4694-9880-5657f570ee04\n" +
                "\tyy.png@2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8\n" +
                "\n" +
                "[&]\n" +
                "002eb31f-f3b3-4ba2-adfa-d758c4a55abe:744\nTG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4gVXQgc2VtcGVyIG1ldHVzIG1hbGVzdWFkYSBlbGl0IHZlc3RpYnVsdW0gZGFwaWJ1cy4gVXQgcHJldGl1bSBtYWxlc3VhZGEgc2VtcGVyLiBTZWQgdmVsIG9kaW8gdmVzdGlidWx1bSwgcnV0cnVtIHF1YW0gZXQsIHBvcnRhIGFudGUuIE51bGxhbSBmcmluZ2lsbGEgbmliaCBhdCBudW5jIGNvbnNlcXVhdCBpbXBlcmRpZXQuIFBoYXNlbGx1cyBzZWQgcGVsbGVudGVzcXVlIG51bGxhLiBQcmFlc2VudCBmYWNpbGlzaXMgbGVjdHVzIGVmZmljaXR1ciwgYmliZW5kdW0gZXggZWdldCwgdmFyaXVzIG5pYmguIEFsaXF1YW0gdHJpc3RpcXVlIGVuaW0gZXQgZmV1Z2lhdCBlZ2VzdGFzLiBOdWxsYW0gbm9uIHZlbGl0IHBvcnRhLCB2aXZlcnJhIG1hZ25hIHNlZCwgdWx0cmljZXMgbWkuIERvbmVjIGV1IGxlY3R1cyBhdWN0b3IsIGltcGVyZGlldCBtZXR1cyBxdWlzLCBibGFuZGl0IGR1aS4gQWVuZWFuIHZpdmVycmEganVzdG8gZmVsaXMsIGlkIGxhb3JlZXQgcHVydXMgdGluY2lkdW50IGEu\n\n" +
                "885d35f3-2811-42c2-a202-b6a7e4b03465:928\nTWF1cmlzIG9ybmFyZSBuaWJoIGFjIGxpYmVybyBwbGFjZXJhdCwgZWdldCBlbGVpZmVuZCByaXN1cyB1bGxhbWNvcnBlci4gTWFlY2VuYXMgcG9zdWVyZSBjb21tb2RvIGR1aSwgdml0YWUgZWxlaWZlbmQgZXN0LiBEdWlzIG5vbiBhdWd1ZSBuZWMgaXBzdW0gY3Vyc3VzIHNhZ2l0dGlzIHF1aXMgdml0YWUgbGVvLiBOdW5jIGV1IHBoYXJldHJhIGVuaW0uIFN1c3BlbmRpc3NlIHBvdGVudGkuIFBoYXNlbGx1cyBudW5jIGVuaW0sIGJsYW5kaXQgdmVsIGxlbyBhYywgYWxpcXVhbSBsdWN0dXMgZXJvcy4gU2VkIGV0IHF1YW0gZXVpc21vZCwgZnJpbmdpbGxhIGR1aSBldSwgcnV0cnVtIGxhY3VzLiBDdXJhYml0dXIgbGFjaW5pYSB2dWxwdXRhdGUgdG9ydG9yIHZpdGFlIHNlbXBlci4gVmVzdGlidWx1bSBhbnRlIGlwc3VtIHByaW1pcyBpbiBmYXVjaWJ1cyBvcmNpIGx1Y3R1cyBldCB1bHRyaWNlcyBwb3N1ZXJlIGN1YmlsaWEgQ3VyYWU7IFV0IG9ybmFyZSBldSB0ZWxsdXMgaW4gZmV1Z2lhdC4gUHJhZXNlbnQgcXVpcyB0cmlzdGlxdWUgdGVsbHVzLCBub24gY29uZ3VlIG5pc2wuIE5hbSBuZWMgZW5pbSBldCBwdXJ1cyB2dWxwdXRhdGUgdWx0cmljZXMgcXVpcyBldSBqdXN0by4gVXQgaWQgdmVzdGlidWx1bSBwdXJ1cy4gUGhhc2VsbHVzIHNlZCBmZWxpcyBvcm5hcmUsIHBoYXJldHJhIGR1aSBlZ2V0LCBkYXBpYnVzIGVuaW0u\n\n" +
                "b112828f-d7d0-4694-9880-5657f570ee04:940\nU3VzcGVuZGlzc2UgdmFyaXVzIGF1Y3RvciBleCwgc2l0IGFtZXQgZnJpbmdpbGxhIGVyYXQgb3JuYXJlIGFjLiBGdXNjZSBvcmNpIGV4LCBmcmluZ2lsbGEgYWMgaWFjdWxpcyBxdWlzLCBkaWN0dW0gbHVjdHVzIGVyYXQuIFZpdmFtdXMgZXggbWFzc2EsIHZlbmVuYXRpcyBuZWMgZXN0IHZlbCwgdmVoaWN1bGEgaW1wZXJkaWV0IGF1Z3VlLiBBZW5lYW4gc2VtIG9yY2ksIHBsYWNlcmF0IHZpdGFlIGN1cnN1cyBldSwgc2FnaXR0aXMgcXVpcyB0dXJwaXMuIFNlZCB1dCBlcm9zIHZlbCBhcmN1IGRpY3R1bSB2YXJpdXMgYXQgdml0YWUgdG9ydG9yLiBQaGFzZWxsdXMgY29uc2VxdWF0IHVsdHJpY2llcyBsYW9yZWV0LiBJbnRlZ2VyIHRyaXN0aXF1ZSwgbGVjdHVzIGluIHVsdHJpY2llcyBlZ2VzdGFzLCBhcmN1IHB1cnVzIG1vbGVzdGllIG1hc3NhLCBpbiB2aXZlcnJhIGVzdCBudW5jIGV1IG1hZ25hLiBTZWQgYWNjdW1zYW4gZXUgdHVycGlzIGluIHBvcnRhLiBNb3JiaSB0aW5jaWR1bnQgc2FnaXR0aXMgdm9sdXRwYXQuIEluIGNvbnZhbGxpcyB0dXJwaXMgbWkuIFNlZCBhIG5pc2wgdXQgbGlndWxhIGNvbmRpbWVudHVtIGFsaXF1ZXQgc2l0IGFtZXQgaWQgcmlzdXMuIERvbmVjIGEgaWFjdWxpcyBtYXVyaXMuIFByYWVzZW50IHVsbGFtY29ycGVyIGxlbyBlZ2V0IG51bmMgYmliZW5kdW0sIGFjIGVmZmljaXR1ciBsZWN0dXMgdGVtcHVzLg==\n\n" +
                "2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8:428\nRXRpYW0gYmliZW5kdW0gc2VtcGVyIGltcGVyZGlldC4gU3VzcGVuZGlzc2UgYXQgb2RpbyBsaWJlcm8uIERvbmVjIHZlbCBwbGFjZXJhdCBtYXVyaXMuIFByYWVzZW50IGRvbG9yIGVyb3MsIGNvbnNlcXVhdCBzZWQgdmVoaWN1bGEgdml0YWUsIGFsaXF1ZXQgYWMgbWFnbmEuIE1hdXJpcyBpYWN1bGlzIG51bmMgYWMgZnJpbmdpbGxhIGFsaXF1YW0uIFZpdmFtdXMgc2l0IGFtZXQgdHJpc3RpcXVlIG5lcXVlLCB2aXRhZSBjb25zZXF1YXQgbmlzaS4gUHJhZXNlbnQgZnJpbmdpbGxhIGltcGVyZGlldCBsZWN0dXMsIHNpdCBhbWV0IHBlbGxlbnRlc3F1ZSBsYWN1cy4=\n";
        System.out.println(inl);
        MessageReader messages = new MessageReader(inl);
        return messages.next();
    }
}
