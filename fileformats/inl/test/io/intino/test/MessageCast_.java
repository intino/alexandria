package io.intino.test;

import io.intino.alexandria.inl.MessageCast;
import io.intino.alexandria.inl.MessageReader;
import io.intino.test.schemas.*;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MessageCast_ {

    @Test
    public void should_cast_null_objects() throws IllegalAccessException {
        Teacher teacher = MessageCast.cast(null).as(Teacher.class);
        assertThat(teacher).isNull();
    }

    @Test
    public void should_cast_message_of_a_class_with_parent() throws IllegalAccessException {
        String str =
                "[Teacher]\n" +
                "name: Jose\n" +
                "money: 50.0\n" +
                "birthDate: 2016-10-04T20:10:12Z\n" +
                "university: ULPGC\n" +
                "\n" +
                "[Teacher.Country]\n" +
                "name: Spain\n";
        Teacher teacher = MessageCast.cast(new MessageReader(str).next()).as(Teacher.class);
        assertThat(teacher.name).isEqualTo("Jose");
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
                "money: 50.0\n" +
                "birthDate: 2016-10-04T20:10:11Z\n" +
                "\n" +
                "[Person.Country]\n" +
                "name: Spain\n";

        Teacher teacher = MessageCast.cast(new MessageReader(str).next()).as(Teacher.class);
        assertThat(teacher.name).isEqualTo("Jose");
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
        Menu menu = MessageCast.cast(new MessageReader(str).next()).as(Menu.class);
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
        Menu menu = MessageCast.cast(new MessageReader(str).next()).as(Menu.class);
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
        Menu menu = MessageCast.cast(new MessageReader(str).next()).as(Menu.class);
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
        Menu menu = MessageCast.cast(new MessageReader(str).next()).as(Menu.class);
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
                    "    at com.android.compiler.os.ZygoteInit.main(ZygoteInit.java:754)" +
                    "\n" +
                    "\n";
        String str =
            "[Crash]\n" +
            "instant: 2017-03-21T07:39:00Z\n" +
            "app: io.intino.consul\n" +
            "deviceId: b367172b0c6fe726\n" +
            "stack:\n" + indent(stack) + "\n";
        Crash crash = MessageCast.cast(new MessageReader(str).next()).as(Crash.class);
        assertThat(crash.instant.toString()).isEqualTo("2017-03-21T07:39:00Z");
        assertThat(crash.app).isEqualTo("io.intino.consul");
        assertThat(crash.deviceId).isEqualTo("b367172b0c6fe726");
        assertThat(crash.stack).isEqualTo(stack);
    }

    @Test
    public void should_cast_message_with_multiple_components() throws IllegalAccessException {
        String str =
            "[Teacher]\n" +
            "name: Jose\n" +
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
        Teacher teacher = MessageCast.cast(new MessageReader(str).next()).as(Teacher.class);
        assertThat(teacher.name).isEqualTo("Jose");
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
        final CredentialLogin t = MessageCast.cast(new MessageReader(str).next()).as(CredentialLogin.class);
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
        InfrastructureOperation op = MessageCast.cast(new MessageReader(str).next()).as(InfrastructureOperation.class);
        assertThat(op.operation()).isEqualTo("Add");
        assertThat(op.user()).isEqualTo("cesar");
        assertThat(op.objectType()).isEqualTo("Responsible");
        assertThat(op.objectID()).isEqualTo("josejuanhernandez");
        assertThat(op.parameters().size()).isEqualTo(3);
        assertThat(op.parameters().get(0)).isEqualTo("josejuanhernandez");
        assertThat(op.parameters().get(1)).isEqualTo("U0CU1BD7E");
        assertThat(op.parameters().get(2)).isEqualTo("josejuanhernandez@siani.es");
    }


    @SuppressWarnings("SameParameterValue")
    private Instant instant(int y, int m, int d, int h, int mn, int s) {
        return LocalDateTime.of(y, m, d, h, mn, s).atZone(ZoneId.of("UTC")).toInstant();
    }

    private static String indent(String text) {
        return "\t" + text.replaceAll("\\n", "\n\t");
    }


}
