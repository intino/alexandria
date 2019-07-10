package io.intino.test;

import io.intino.alexandria.inl.Message;
import io.intino.alexandria.inl.MessageBuilder;
import io.intino.alexandria.inl.MessageReader;
import io.intino.test.schemas.*;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.intino.alexandria.inl.MessageCast.cast;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MessageCast_ {

    @Test
    public void should_cast_null_objects() throws IllegalAccessException {
		Teacher teacher = cast(null).as(Teacher.class);
        assertThat(teacher).isNull();
    }

    @Test
    public void should_cast_message_of_a_class_with_parent() throws IllegalAccessException {
        String str =
                "[Teacher]\n" +
                "name: Jose\n" +
                "gender: Male\n" +
                "money: 50.0\n" +
                "birthDate: 2016-10-04T20:10:12Z\n" +
                "university: ULPGC\n" +
                "\n" +
                "[Teacher.Country]\n" +
                "name: Spain\n";

        MessageReader messages = new MessageReader(str);
        assertThat(messages.hasNext()).isTrue();
        Message message = messages.next();
        assertThat(messages.hasNext()).isFalse();
		Teacher teacher = cast(message).as(Teacher.class);
        assertThat(teacher.name).isEqualTo("Jose");
        assertThat(teacher.gender.toString()).isEqualTo("Male");
        assertThat(teacher.money).isEqualTo(50.0);
        assertThat(teacher.birthDate).isEqualTo(instant(2016, 10, 4, 20, 10, 12));
        assertThat(teacher.university).isEqualTo("ULPGC");
        assertThat(teacher.country.name).isEqualTo("Spain");
        assertThat(teacher.country.continent).isNull();

    }
    @Test
    public void should_cast_message_with_empty_attributes() throws IllegalAccessException {
        String str =
                "[Teacher]\n" +
                "name: Jose\n" +
                "gender: Male\n" +
                "money: 50.0\n" +
                "birthDate: 2016-10-04T20:10:11Z\n" +
                "\n" +
                "[Person.Country]\n" +
                "name: Spain\n";

		Teacher teacher = cast(new MessageReader(str).next()).as(Teacher.class);
        assertThat(teacher.name).isEqualTo("Jose");
        assertThat(teacher.gender.toString()).isEqualTo("Male");
        assertThat(teacher.money).isEqualTo(50.0);
        assertThat(teacher.birthDate).isEqualTo(instant(2016, 10, 4, 20, 10, 11));
        assertThat(teacher.university).isNull();
        assertThat(teacher.country.name).isEqualTo("Spain");
        assertThat(teacher.country.continent).isNull();
    }

    @Test
    public void should_cast_messages_with_array_attributes() throws IllegalAccessException {
        String str =
                "[Menu]\n" +
                "meals:\n" +
                "\tSoup\n" +
                "\tLobster\n" +
                "\tMussels\n" +
                "\tCake\n" +
                "prices:\n" +
                "\t5.0\n" +
                "\t24.5\n" +
                "\t8.0\n" +
                "\t7.0\n" +
                "availability:\n" +
                "\ttrue\n" +
                "\tfalse\n";
		Menu menu = cast(new MessageReader(str).next()).as(Menu.class);
        assertThat(menu.meals.length).isEqualTo(4);
        assertThat(menu.prices.length).isEqualTo(4);
        assertThat(menu.availability.length).isEqualTo(2);
        assertThat(menu.meals[0]).isEqualTo("Soup");
        assertThat(menu.meals[1]).isEqualTo("Lobster");
        assertThat(menu.prices[0]).isEqualTo(5.0);
        assertThat(menu.prices[1]).isEqualTo(24.5);
        assertThat(menu.prices[2]).isEqualTo(8.0);
        assertThat(menu.availability[0]).isEqualTo(true);
        assertThat(menu.availability[1]).isEqualTo(false);
    }

    @Test
    public void should_cast_message_with_array_attributes_of_size_1() throws IllegalAccessException {
        String str =
                "[Menu]\n" +
                "meals:\n" +
                "\tSoup\n" +
                "\tLobster\n" +
                "\tMussels\n" +
                "\tCake\n" +
                "prices:\n" +
                "\t7.0\n" +
                "availability:\n" +
                "\ttrue\n" +
                "\tfalse\n";
		Menu menu = cast(new MessageReader(str).next()).as(Menu.class);
        assertThat(menu.prices.length).isEqualTo(1);
        assertThat(menu.prices[0]).isEqualTo(7.0);
    }

    @Test
    public void should_cast_empty_array_attributes() throws IllegalAccessException {
        String str =
                "[Menu]\n" +
                "availability:\n" +
                "\ttrue\n" +
                "\tfalse\n";
		Menu menu = cast(new MessageReader(str).next()).as(Menu.class);
        assertThat(menu.meals.length).isEqualTo(0);
        assertThat(menu.prices.length).isEqualTo(0);
        assertThat(menu.availability.length).isEqualTo(2);
        assertThat(menu.availability[0]).isEqualTo(true);
        assertThat(menu.availability[1]).isEqualTo(false);
    }

    @Test
    public void should_cast_array_attributes_with_null_values() throws IllegalAccessException {
        String str =
                "[Menu]\n" +
                "meals:\n" +
                "\tSoup\n" +
                "\t\0\n" +
                "\tMussels\n" +
                "\tCake\n" +
                "prices:\n" +
                "\t5.0\n" +
                "\t\0\n" +
                "\t8.0\n" +
                "\t7.0\n" +
                "availability:\n" +
                "\ttrue\n" +
                "\tfalse\n";
		Menu menu = cast(new MessageReader(str).next()).as(Menu.class);
        assertThat(menu.meals.length).isEqualTo(4);
        assertThat(menu.prices.length).isEqualTo(4);
        assertThat(menu.availability.length).isEqualTo(2);
        assertThat(menu.meals[0]).isEqualTo("Soup");
        assertThat(menu.meals[1]).isNull();
        assertThat(menu.prices[0]).isEqualTo(5.0);
        assertThat(menu.prices[1]).isNull();
        assertThat(menu.prices[2]).isEqualTo(8.0);
        assertThat(menu.availability[0]).isEqualTo(true);
        assertThat(menu.availability[1]).isEqualTo(false);
    }

    @Test
    public void should_cast_message_with_multi_line_attributes() throws IllegalAccessException {
        String stack =
            "java.lang.NullPointerException: Attempt to invoke interface method 'java.lang.Object java.util.List.get(int)' on a null object reference\n" +
                    "    at io.intino.consul.AppService$5.run(AppService.java:154)\n" +
                    "    at android.os.Handler.handleCallback(Handler.java:815)\n" +
                    "    at android.os.Handler.dispatchMessage(Handler.java:104)\n" +
                    "    at android.os.Looper.loop(Looper.java:194)\n" +
                    "    at android.app.ActivityThread.main(ActivityThread.java:5666)\n" +
                    "    at java.lang.reflect.Method.invoke(Native Method)\n" +
                    "    at java.lang.reflect.Method.invoke(Method.java:372)\n" +
                    "\n" +
                    "    at com.android.compiler.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:959)\n" +
                    "    at com.android.compiler.os.ZygoteInit.main(ZygoteInit.java:754)\n\n";

        String str =
            "[Crash]\n" +
            "instant: 2017-03-21T07:39:00Z\n" +
            "app: io.intino.consul\n" +
            "deviceId: b367172b0c6fe726\n" +
            "stack:\n" + indent(stack) + "\n";

        MessageReader messages = new MessageReader(str);
        Message message = messages.next();

        assertThat(messages.hasNext()).isFalse();
        assertThat(message.get("stack").as(String.class)).isEqualTo(stack.trim());

        Crash crash = cast(message).as(Crash.class);
        assertThat(crash.instant.toString()).isEqualTo("2017-03-21T07:39:00Z");
        assertThat(crash.app).isEqualTo("io.intino.consul");
        assertThat(crash.deviceId).isEqualTo("b367172b0c6fe726");
        assertThat(crash.stack).isEqualTo(stack.trim());
    }

    @Test
    public void should_cast_message_with_multiple_components() throws IllegalAccessException {
        String str =
            "[Teacher]\n" +
            "name: Jose\n" +
            "gender: Male\n" +
            "money: 50.0\n" +
            "birthDate: 2016-10-04T20:10:11Z\n" +
            "university: ULPGC\n" +
            "\n" +
            "[Teacher.Country]\n" +
            "name: Spain\n" +
            "\n" +
            "[Teacher.Phone]\n" +
            "value: +150512101402\n" +
            "\n" +
            "[Teacher.Phone.Country]\n" +
            "name: USA\n" +
            "\n" +
            "[Teacher.Phone]\n" +
            "value: +521005101402\n" +
            "\n" +
            "[Teacher.Phone.Country]\n" +
            "name: Mexico\n";
		Teacher teacher = cast(new MessageReader(str).next()).as(Teacher.class);
        assertThat(teacher.name).isEqualTo("Jose");
        assertThat(teacher.gender.toString()).isEqualTo("Male");
        assertThat(teacher.money).isEqualTo(50.0);
        assertThat(teacher.birthDate).isEqualTo(instant(2016, 10, 4, 20, 10, 11));
        assertThat(teacher.country.name).isEqualTo("Spain");
        assertThat(teacher.phones.size()).isEqualTo(2);
        assertThat(teacher.phones.get(0).value).isEqualTo("+150512101402");
        assertThat(teacher.phones.get(0).country.name).isEqualTo("USA");
        assertThat(teacher.phones.get(1).value).isEqualTo("+521005101402");
        assertThat(teacher.phones.get(1).country.name).isEqualTo("Mexico");
    }



    @Test
    public void should_cast_schema_3() throws IllegalAccessException {
        String str =
                "[CredentialLogin]\n" +
                "authentication: open\n" +
                "\n" +
                "[CredentialLogin.Parameter]\n" +
                "name: username\n" +
                "value: mcaballero\n" +
                "\n" +
                "[CredentialLogin.Parameter]\n" +
                "name: rememberMe\n" +
                "value: true\n" +
                "\n" +
                "[CredentialLogin.Parameter]\n" +
                "name: token\n" +
                "value: abcdedgrd\n";
		CredentialLogin t = cast(new MessageReader(str).next()).as(CredentialLogin.class);
        assertThat(t.authentication()).isEqualTo("open");
        assertThat(t.parameterList().size()).isEqualTo(3);
        assertThat(t.parameterList().get(0).name()).isEqualTo("username");
        assertThat(t.parameterList().get(0).value()).isEqualTo("mcaballero");
    }

    @Test
    public void should_cast_schema_4() throws IllegalAccessException {
        String str =
                "[InfrastructureOperation]\n" +
                "ts: 2018-05-22T11:17:20.895Z\n" +
                "operation: Add\n" +
                "user: cesar\n" +
                "objectType: Responsible\n" +
                "objectID: josejuanhernandez\n" +
                "parameters:\n" +
                "\tjosejuanhernandez\n" +
                "\tU0CU1BD7E\n" +
                "\tjosejuanhernandez@siani.es\n";
		InfrastructureOperation op = cast(new MessageReader(str).next()).as(InfrastructureOperation.class);
        assertThat(op.operation()).isEqualTo("Add");
        assertThat(op.user()).isEqualTo("cesar");
        assertThat(op.objectType()).isEqualTo("Responsible");
        assertThat(op.objectID()).isEqualTo("josejuanhernandez");
        assertThat(op.parameters().size()).isEqualTo(3);
        assertThat(op.parameters().get(0)).isEqualTo("josejuanhernandez");
        assertThat(op.parameters().get(1)).isEqualTo("U0CU1BD7E");
        assertThat(op.parameters().get(2)).isEqualTo("josejuanhernandez@siani.es");
    }

    @Test
    public void should_cast_inl_as_object() throws IllegalAccessException {
        String inl =
                "[AlertModified]\n" +
                "alert: Alerts#e4a80d88-7bd5-4948-bd1d-7b38f47c40c7\n" +
                "active: true\n" +
                "mailingList: cambuyonero@yahoo.es\n" +
                "applyToAllStations: false\n";

        AlertModified object = cast(new MessageReader(inl).next()).as(AlertModified.class);
        assertThat(object.mailingList().size()).isEqualTo(1);
        assertThat(object).isNotNull();
        assertThat(MessageBuilder.toMessage(object).toString()).isEqualTo(inl);
    }

    @Test
    public void should_cast_message_with_attachment() throws IllegalAccessException {
        String inl =
                "[Document]\n" +
                "ts: 2017-03-21T07:39:00Z\n" +
                "attachment: cv.pdf@002eb31f-f3b3-4ba2-adfa-d758c4a55abe\n" +
                "\n" +
                "[&]\n" +
                "002eb31f-f3b3-4ba2-adfa-d758c4a55abe:744\nTG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4gVXQgc2VtcGVyIG1ldHVzIG1hbGVzdWFkYSBlbGl0IHZlc3RpYnVsdW0gZGFwaWJ1cy4gVXQgcHJldGl1bSBtYWxlc3VhZGEgc2VtcGVyLiBTZWQgdmVsIG9kaW8gdmVzdGlidWx1bSwgcnV0cnVtIHF1YW0gZXQsIHBvcnRhIGFudGUuIE51bGxhbSBmcmluZ2lsbGEgbmliaCBhdCBudW5jIGNvbnNlcXVhdCBpbXBlcmRpZXQuIFBoYXNlbGx1cyBzZWQgcGVsbGVudGVzcXVlIG51bGxhLiBQcmFlc2VudCBmYWNpbGlzaXMgbGVjdHVzIGVmZmljaXR1ciwgYmliZW5kdW0gZXggZWdldCwgdmFyaXVzIG5pYmguIEFsaXF1YW0gdHJpc3RpcXVlIGVuaW0gZXQgZmV1Z2lhdCBlZ2VzdGFzLiBOdWxsYW0gbm9uIHZlbGl0IHBvcnRhLCB2aXZlcnJhIG1hZ25hIHNlZCwgdWx0cmljZXMgbWkuIERvbmVjIGV1IGxlY3R1cyBhdWN0b3IsIGltcGVyZGlldCBtZXR1cyBxdWlzLCBibGFuZGl0IGR1aS4gQWVuZWFuIHZpdmVycmEganVzdG8gZmVsaXMsIGlkIGxhb3JlZXQgcHVydXMgdGluY2lkdW50IGEu\n\n";

        Document document = cast(new MessageReader(inl).next()).as(Document.class);
        assertThat(document.ts().toString()).isEqualTo("2017-03-21T07:39:00Z");
        assertThat(document.attachment().name()).isEqualTo("cv.pdf");
        assertThat(document.attachment().bytes().length).isEqualTo(558);
        assertThat(document.attachment().bytes()).isEqualTo("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut semper metus malesuada elit vestibulum dapibus. Ut pretium malesuada semper. Sed vel odio vestibulum, rutrum quam et, porta ante. Nullam fringilla nibh at nunc consequat imperdiet. Phasellus sed pellentesque nulla. Praesent facilisis lectus efficitur, bibendum ex eget, varius nibh. Aliquam tristique enim et feugiat egestas. Nullam non velit porta, viverra magna sed, ultrices mi. Donec eu lectus auctor, imperdiet metus quis, blandit dui. Aenean viverra justo felis, id laoreet purus tincidunt a.".getBytes());
    }

    @Test
    public void should_cast_message_with_attachments() throws IllegalAccessException {
        String inl =
                "[DocumentList]\n" +
                "ts: 2017-03-21T07:39:00Z\n" +
                "files:\n" +
                "\tcv.pdf@002eb31f-f3b3-4ba2-adfa-d758c4a55abe\n" +
                "\txx.png@885d35f3-2811-42c2-a202-b6a7e4b03465\n" +
                "\tyy.png@b112828f-d7d0-4694-9880-5657f570ee04\n" +
                "\n" +
                "[&]\n" +
                "002eb31f-f3b3-4ba2-adfa-d758c4a55abe:744\nTG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4gVXQgc2VtcGVyIG1ldHVzIG1hbGVzdWFkYSBlbGl0IHZlc3RpYnVsdW0gZGFwaWJ1cy4gVXQgcHJldGl1bSBtYWxlc3VhZGEgc2VtcGVyLiBTZWQgdmVsIG9kaW8gdmVzdGlidWx1bSwgcnV0cnVtIHF1YW0gZXQsIHBvcnRhIGFudGUuIE51bGxhbSBmcmluZ2lsbGEgbmliaCBhdCBudW5jIGNvbnNlcXVhdCBpbXBlcmRpZXQuIFBoYXNlbGx1cyBzZWQgcGVsbGVudGVzcXVlIG51bGxhLiBQcmFlc2VudCBmYWNpbGlzaXMgbGVjdHVzIGVmZmljaXR1ciwgYmliZW5kdW0gZXggZWdldCwgdmFyaXVzIG5pYmguIEFsaXF1YW0gdHJpc3RpcXVlIGVuaW0gZXQgZmV1Z2lhdCBlZ2VzdGFzLiBOdWxsYW0gbm9uIHZlbGl0IHBvcnRhLCB2aXZlcnJhIG1hZ25hIHNlZCwgdWx0cmljZXMgbWkuIERvbmVjIGV1IGxlY3R1cyBhdWN0b3IsIGltcGVyZGlldCBtZXR1cyBxdWlzLCBibGFuZGl0IGR1aS4gQWVuZWFuIHZpdmVycmEganVzdG8gZmVsaXMsIGlkIGxhb3JlZXQgcHVydXMgdGluY2lkdW50IGEu\n\n" +
                "885d35f3-2811-42c2-a202-b6a7e4b03465:928\nTWF1cmlzIG9ybmFyZSBuaWJoIGFjIGxpYmVybyBwbGFjZXJhdCwgZWdldCBlbGVpZmVuZCByaXN1cyB1bGxhbWNvcnBlci4gTWFlY2VuYXMgcG9zdWVyZSBjb21tb2RvIGR1aSwgdml0YWUgZWxlaWZlbmQgZXN0LiBEdWlzIG5vbiBhdWd1ZSBuZWMgaXBzdW0gY3Vyc3VzIHNhZ2l0dGlzIHF1aXMgdml0YWUgbGVvLiBOdW5jIGV1IHBoYXJldHJhIGVuaW0uIFN1c3BlbmRpc3NlIHBvdGVudGkuIFBoYXNlbGx1cyBudW5jIGVuaW0sIGJsYW5kaXQgdmVsIGxlbyBhYywgYWxpcXVhbSBsdWN0dXMgZXJvcy4gU2VkIGV0IHF1YW0gZXVpc21vZCwgZnJpbmdpbGxhIGR1aSBldSwgcnV0cnVtIGxhY3VzLiBDdXJhYml0dXIgbGFjaW5pYSB2dWxwdXRhdGUgdG9ydG9yIHZpdGFlIHNlbXBlci4gVmVzdGlidWx1bSBhbnRlIGlwc3VtIHByaW1pcyBpbiBmYXVjaWJ1cyBvcmNpIGx1Y3R1cyBldCB1bHRyaWNlcyBwb3N1ZXJlIGN1YmlsaWEgQ3VyYWU7IFV0IG9ybmFyZSBldSB0ZWxsdXMgaW4gZmV1Z2lhdC4gUHJhZXNlbnQgcXVpcyB0cmlzdGlxdWUgdGVsbHVzLCBub24gY29uZ3VlIG5pc2wuIE5hbSBuZWMgZW5pbSBldCBwdXJ1cyB2dWxwdXRhdGUgdWx0cmljZXMgcXVpcyBldSBqdXN0by4gVXQgaWQgdmVzdGlidWx1bSBwdXJ1cy4gUGhhc2VsbHVzIHNlZCBmZWxpcyBvcm5hcmUsIHBoYXJldHJhIGR1aSBlZ2V0LCBkYXBpYnVzIGVuaW0u\n\n" +
                "b112828f-d7d0-4694-9880-5657f570ee04:940\nU3VzcGVuZGlzc2UgdmFyaXVzIGF1Y3RvciBleCwgc2l0IGFtZXQgZnJpbmdpbGxhIGVyYXQgb3JuYXJlIGFjLiBGdXNjZSBvcmNpIGV4LCBmcmluZ2lsbGEgYWMgaWFjdWxpcyBxdWlzLCBkaWN0dW0gbHVjdHVzIGVyYXQuIFZpdmFtdXMgZXggbWFzc2EsIHZlbmVuYXRpcyBuZWMgZXN0IHZlbCwgdmVoaWN1bGEgaW1wZXJkaWV0IGF1Z3VlLiBBZW5lYW4gc2VtIG9yY2ksIHBsYWNlcmF0IHZpdGFlIGN1cnN1cyBldSwgc2FnaXR0aXMgcXVpcyB0dXJwaXMuIFNlZCB1dCBlcm9zIHZlbCBhcmN1IGRpY3R1bSB2YXJpdXMgYXQgdml0YWUgdG9ydG9yLiBQaGFzZWxsdXMgY29uc2VxdWF0IHVsdHJpY2llcyBsYW9yZWV0LiBJbnRlZ2VyIHRyaXN0aXF1ZSwgbGVjdHVzIGluIHVsdHJpY2llcyBlZ2VzdGFzLCBhcmN1IHB1cnVzIG1vbGVzdGllIG1hc3NhLCBpbiB2aXZlcnJhIGVzdCBudW5jIGV1IG1hZ25hLiBTZWQgYWNjdW1zYW4gZXUgdHVycGlzIGluIHBvcnRhLiBNb3JiaSB0aW5jaWR1bnQgc2FnaXR0aXMgdm9sdXRwYXQuIEluIGNvbnZhbGxpcyB0dXJwaXMgbWkuIFNlZCBhIG5pc2wgdXQgbGlndWxhIGNvbmRpbWVudHVtIGFsaXF1ZXQgc2l0IGFtZXQgaWQgcmlzdXMuIERvbmVjIGEgaWFjdWxpcyBtYXVyaXMuIFByYWVzZW50IHVsbGFtY29ycGVyIGxlbyBlZ2V0IG51bmMgYmliZW5kdW0sIGFjIGVmZmljaXR1ciBsZWN0dXMgdGVtcHVzLg==\n\n";

        DocumentList documentList = cast(new MessageReader(inl).next()).as(DocumentList.class);
        assertThat(documentList.ts().toString()).isEqualTo("2017-03-21T07:39:00Z");
        assertThat(documentList.files().size()).isEqualTo(3);
        assertThat(documentList.files().get(0).name()).isEqualTo("cv.pdf");
        assertThat(documentList.files().get(1).name()).isEqualTo("xx.png");
        assertThat(documentList.files().get(2).name()).isEqualTo("yy.png");
        assertThat(documentList.files().get(0).bytes().length).isEqualTo(558);
        assertThat(documentList.files().get(1).bytes().length).isEqualTo(696);
        assertThat(documentList.files().get(2).bytes().length).isEqualTo(703);
        assertThat(documentList.files().get(0).bytes()).isEqualTo("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut semper metus malesuada elit vestibulum dapibus. Ut pretium malesuada semper. Sed vel odio vestibulum, rutrum quam et, porta ante. Nullam fringilla nibh at nunc consequat imperdiet. Phasellus sed pellentesque nulla. Praesent facilisis lectus efficitur, bibendum ex eget, varius nibh. Aliquam tristique enim et feugiat egestas. Nullam non velit porta, viverra magna sed, ultrices mi. Donec eu lectus auctor, imperdiet metus quis, blandit dui. Aenean viverra justo felis, id laoreet purus tincidunt a.".getBytes());
        assertThat(documentList.files().get(1).bytes()).isEqualTo("Mauris ornare nibh ac libero placerat, eget eleifend risus ullamcorper. Maecenas posuere commodo dui, vitae eleifend est. Duis non augue nec ipsum cursus sagittis quis vitae leo. Nunc eu pharetra enim. Suspendisse potenti. Phasellus nunc enim, blandit vel leo ac, aliquam luctus eros. Sed et quam euismod, fringilla dui eu, rutrum lacus. Curabitur lacinia vulputate tortor vitae semper. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Ut ornare eu tellus in feugiat. Praesent quis tristique tellus, non congue nisl. Nam nec enim et purus vulputate ultrices quis eu justo. Ut id vestibulum purus. Phasellus sed felis ornare, pharetra dui eget, dapibus enim.".getBytes());
        assertThat(documentList.files().get(2).bytes()).isEqualTo("Suspendisse varius auctor ex, sit amet fringilla erat ornare ac. Fusce orci ex, fringilla ac iaculis quis, dictum luctus erat. Vivamus ex massa, venenatis nec est vel, vehicula imperdiet augue. Aenean sem orci, placerat vitae cursus eu, sagittis quis turpis. Sed ut eros vel arcu dictum varius at vitae tortor. Phasellus consequat ultricies laoreet. Integer tristique, lectus in ultricies egestas, arcu purus molestie massa, in viverra est nunc eu magna. Sed accumsan eu turpis in porta. Morbi tincidunt sagittis volutpat. In convallis turpis mi. Sed a nisl ut ligula condimentum aliquet sit amet id risus. Donec a iaculis mauris. Praesent ullamcorper leo eget nunc bibendum, ac efficitur lectus tempus.".getBytes());
    }

    @SuppressWarnings("SameParameterValue")
    private Instant instant(int y, int m, int d, int h, int mn, int s) {
        return LocalDateTime.of(y, m, d, h, mn, s).atZone(ZoneId.of("UTC")).toInstant();
    }

    private static String indent(String text) {
        return "\t" + text.replaceAll("\\n", "\n\t");
    }


}
