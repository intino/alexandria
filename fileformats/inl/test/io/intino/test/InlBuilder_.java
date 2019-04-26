package io.intino.test;

import io.intino.alexandria.inl.MessageBuilder;
import io.intino.test.schemas.*;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class InlBuilder_ {
    @Test
    public void should_build_attributes_and_component_of_a_class() {
        Person person = new Person(
                "Jose",
                50,
                instant(2016, 10, 4, 10, 10, 11),
                new Country("Spain")
        );
        assertThat(MessageBuilder.toMessage(person)).isEqualTo(
                "[Person]\n" +
                "name: Jose\n" +
                "money: 50.0\n" +
                "birthDate: 2016-10-04T10:10:11Z\n" +
                "\n" +
                "[Person.Country]\n" +
                "name: Spain\n"
        );
    }

    @Test
    public void should_build_array_attributes_of_a_class() {
        Menu menu = new Menu(
                new String[]{"Soup", "Lobster", "Mussels", "Cake"},
                new Double[]{5.0, 24.5, 8.0, 7.0},
                new Boolean[]{true, false}
        );

        assertThat(MessageBuilder.toMessage(menu)).isEqualTo(
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
                        "\tfalse\n"
        );
    }

    @Test
    public void should_build_empty_array_attributes_of_a_class() {
        Menu menu = new Menu(new String[]{}, new Double[]{}, new Boolean[]{true, false});
        assertThat(MessageBuilder.toMessage(menu)).isEqualTo(
                "[Menu]\n" +
                        "availability:\n" +
                        "\ttrue\n" +
                        "\tfalse\n");
    }

    @Test
    public void should_build_schema() {
        AlertModified alert = new AlertModified()
                .alert("Alerts#bbc15556-244b-45af-97b9-c0f18b1e42be")
                .active(true)
                .mailingList(asList("cambullonero@monentia.es", "locuaz@gmail.com"))
                .applyToAllStations(false);

        assertThat(MessageBuilder.toMessage(alert)).isEqualTo(
                "[AlertModified]\n" +
                        "alert: Alerts#bbc15556-244b-45af-97b9-c0f18b1e42be\n" +
                        "active: true\n" +
                        "mailingList:\n\tcambullonero@monentia.es\n\tlocuaz@gmail.com\n" +
                        "applyToAllStations: false\n");
    }

    @Test
    public void should_serialize_empty_array_attributes_of_a_class() {
        Menu menu = new Menu(new String[]{}, new Double[]{}, new Boolean[]{true, false});
        assertThat(MessageBuilder.toMessage(menu)).isEqualTo(
                "[Menu]\n" +
                        "availability:\n" +
                        "\ttrue\n" +
                        "\tfalse\n");
    }

    @Test
    public void should_build_array_attribute_with_null_values_of_a_class() {
        Menu menu = new Menu(new String[]{"Soup", null, "Mussels", "Cake"}, new Double[]{5.0, null, 8.0, 7.0}, new Boolean[]{true, false});
        assertThat(MessageBuilder.toMessage(menu)).isEqualTo(
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
                        "\tfalse\n");
    }

    @Test
    public void should_not_serialize_null_attributes_of_a_class() {
        Person person = new Person("Jose", 50, null, null);
        assertThat(MessageBuilder.toMessage(person)).isEqualTo(
                "[Person]\n" +
                        "name: Jose\n" +
                        "money: 50.0\n");
    }

    @Test
    public void should_build_message_with_multiple_components() {
        Teacher teacher = new Teacher("Jose", 50, instant(2016, 10, 4, 20, 10, 11), new Country("Spain"), "ULPGC");
        teacher.add(new Phone("+150512101402", new Country("USA")));
        teacher.add(new Phone("+521005101402", new Country("Mexico")));
        assertThat(MessageBuilder.toMessage(teacher)).isEqualTo(
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
                        "name: Mexico\n");
    }

    @Test
    public void should_build_message_with_multi_line() {
        Teacher teacher = new Teacher("Jose\nHernandez", 50, instant(2016, 10, 4, 20, 10, 11), new Country("Spain"), "ULPGC");
        teacher.add(new Phone("+150512101402", new Country("USA")));
        teacher.add(new Phone("+521005101402", new Country("Mexico")));
        assertThat(MessageBuilder.toMessage(teacher)).isEqualTo(
                "[Teacher]\n" +
                        "name:\n" +
                        "\tJose\n" +
                        "\tHernandez\n" +
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
                        "name: Mexico\n");
    }

    @Test
    public void should_build_crash() {
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
        Crash crash = new Crash();
        crash.instant = instant(2017, 3, 21, 7, 39, 0);
        crash.app = "io.intino.consul";
        crash.deviceId = "b367172b0c6fe726";
        crash.stack = stack;
        assertThat(MessageBuilder.toMessage(crash)).isEqualTo(
                "[Crash]\n" +
                        "instant: 2017-03-21T07:39:00Z\n" +
                        "app: io.intino.consul\n" +
                        "deviceId: b367172b0c6fe726\n" +
                        "stack:\n" + indent(stack) + "\n");
    }



    @SuppressWarnings("SameParameterValue")
    private static Instant instant(int y, int m, int d, int h, int mn, int s) {
        return LocalDateTime.of(y, m, d, h, mn, s).atZone(ZoneId.of("UTC")).toInstant();
    }

    private static String indent(String text) {
        return "\t" + text.replaceAll("\\n", "\n\t");
    }


}
