package io.intino.test;

import io.intino.alexandria.tabb.ColumnStream;
import io.intino.alexandria.tabb.ColumnStream.Type;
import io.intino.alexandria.tabb.TabbBuilder;
import io.intino.alexandria.tabb.streamers. ObjectStreamer;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectStreamer_ {

    private List<Person> persons;

    @Before
    public void setUp() {
        persons = new ArrayList<>();
        persons.add(new Person("PM", Gender.Male, Instant.now(), 3, 3800.5));
        persons.add(new Person("María", Gender.Female, Instant.now(), 1, 4000.));
        persons.add(new Person("Ezequiel", Gender.Male, Instant.now(), 0, 2600.8));
    }

    @Test
    public void should_create_streams_from_list() throws IOException {
        ColumnStream[] streams = createStreams();
        assertThat(streams[0].name()).isEqualTo("name");
        assertThat(streams[1].name()).isEqualTo("gender");
        assertThat(streams[2].name()).isEqualTo("children");
        assertThat(streams[3].name()).isEqualTo("salary");
        assertThat(streams[0].mode().features.length).isEqualTo(0);
        assertThat(streams[1].mode().features.length).isEqualTo(2);
        assertThat(streams[1].mode().features[0]).isEqualTo("Male");
        assertThat(streams[1].mode().features[1]).isEqualTo("Female");
        assertThat(streams[2].mode().features.length).isEqualTo(0);
        assertThat(streams[3].mode().features.length).isEqualTo(0);
        assertThat(streams[0].type()).isEqualTo(Type.String);
        assertThat(streams[1].type()).isEqualTo(Type.Nominal);
        assertThat(streams[2].type()).isEqualTo(Type.Integer);
        assertThat(streams[3].type()).isEqualTo(Type.Double);

        Object[] data = dataOf(streams);
        assertThat(data[0]).isEqualTo(new String[] {"PM","María", "Ezequiel"});
        assertThat(data[1]).isEqualTo(new int[] {0,1,0});
        assertThat(data[2]).isEqualTo(new int[] {3,1,0});
        assertThat(data[3]).isEqualTo(new double[] {3800.5, 4000.0, 2600.8});
        assertThat(streams[0].hasNext()).isFalse();
        assertThat(streams[1].hasNext()).isFalse();
        assertThat(streams[2].hasNext()).isFalse();
        assertThat(streams[3].hasNext()).isFalse();
    }

    @Test
    public void should_be_source_for_tabb_builder() throws IOException {
        ColumnStream[] streams = createStreams();
        new TabbBuilder().add(streams).save(new File("x.tabb"));
    }

    private ColumnStream[] createStreams() {
        return new ObjectStreamer<>(persons.iterator())
                .add(nameSelector())
                .add(genderSelector())
                .add(childrenSelector())
                .add(salarySelector()).get();
    }

    private Object[] dataOf(ColumnStream[] streams) {
        Object[][] objects = new Object[streams.length][persons.size()];
        for (int i = 0; i < persons.size(); i++) {
            streams[0].next();
            for (int j = 0; j < streams.length; j++) {
                objects[j][i] = streams[j].value();
            }
        }
        return objects;
    }

    private ObjectStreamer.Selector<Person> nameSelector() {
        return new ObjectStreamer.Selector<Person>() {
            @Override
            public String name() {
                return "name";
            }

            @Override
            public Type type() {
                return Type.String;
            }

            @Override
            public ColumnStream.Mode mode() {
                return new ColumnStream.Mode();
            }

            @Override
            public Object select(Person person) {
                return person.name;
            }
        };
    }

    private ObjectStreamer.Selector<Person> genderSelector() {
        return new ObjectStreamer.Selector<Person>() {
            @Override
            public String name() {
                return "gender";
            }

            @Override
            public Type type() {
                return Type.Nominal;
            }

            @Override
            public ColumnStream.Mode mode() {
                return new ColumnStream.Mode("Male","Female");
            }

            @Override
            public Object select(Person person) {
                return person.gender.ordinal();
            }
        };
    }

    private ObjectStreamer.Selector<Person> childrenSelector() {
        return new ObjectStreamer.Selector<Person>() {
            @Override
            public String name() {
                return "children";
            }

            @Override
            public Type type() {
                return Type.Integer;
            }

            @Override
            public ColumnStream.Mode mode() {
                return new ColumnStream.Mode();
            }

            @Override
            public Object select(Person person) {
                return person.children;
            }
        };
    }

    private ObjectStreamer.Selector<Person> salarySelector() {
        return new ObjectStreamer.Selector<Person>() {
            @Override
            public String name() {
                return "salary";
            }

            @Override
            public Type type() {
                return Type.Double;
            }

            @Override
            public ColumnStream.Mode mode() {
                return new ColumnStream.Mode();
            }

            @Override
            public Object select(Person person) {
                return person.salary;
            }
        };
    }

    private static class Person {
        final String name;
        final Gender gender;
        final Instant birthDate;
        final int children;
        final double salary;

        Person(String name, Gender gender, Instant birthDate, int children, double salary) {
            this.name = name;
            this.gender = gender;
            this.birthDate = birthDate;
            this.children = children;
            this.salary = salary;
        }
    }

    private enum Gender {
        Male, Female
    }
}
