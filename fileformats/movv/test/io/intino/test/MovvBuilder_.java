package io.intino.test;

import io.intino.alexandria.movv.Mov;
import io.intino.alexandria.movv.Movv;
import io.intino.alexandria.movv.MovvBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Iterator;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MovvBuilder_ {

    @Before
    public void setUp() {
        new File("movvs").mkdirs();
    }

    @After
    public void tearDown() throws Exception {
        Files.walk(new File("movvs").toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void should_create_an_empty_movv() throws IOException {
        buildEmptyMovv();

        Movv movv = new Movv(new File("movvs/empty.movv"));
        assertThat(movv.get(1000).at(instant(2018,2,10))).isNull();
        assertThat(movv.get(2000).at(instant(2018,2,10))).isNull();
    }

    @Test
    public void should_create_a_movv() throws IOException {
        build();

        Movv movv = new Movv(new File("movvs/single.movv"));
        assertThat(movv.get(1000).length()).isEqualTo(1);
        assertThat(movv.get(1000).last()).isEqualTo("1");
        assertThat(movv.get(1000).iterator().hasNext()).isTrue();
        assertThat(movv.get(1000).iterator().next().data).isEqualTo("1");
        assertThat(movv.get(1000).iterator().next().instant).isEqualTo(instant(2018,1,2));
        assertThat(movv.get(1000).at(instant(2018,1,1))).isNull();
        assertThat(movv.get(1000).at(instant(2018,1,2))).isEqualTo("1");
        assertThat(movv.get(1000).at(instant(2019,1,1))).isEqualTo("1");
        assertThat(movv.get(2000).at(instant(2019,1,1))).isNull();

    }

    @Test
    public void should_create_a_movv_using_non_sorted_stage() throws IOException {
        buildWithNonSortedStage();

        Movv movv = new Movv(new File("movvs/single.movv"));
        assertThat(movv.get(1000).length()).isEqualTo(2);
        assertThat(movv.get(1000).last()).isEqualTo("2");
        assertThat(movv.get(1000).iterator().hasNext()).isTrue();
        assertThat(movv.get(1000).iterator().next().data).isEqualTo("1");
        assertThat(movv.get(1000).iterator().next().instant).isEqualTo(instant(2018,1,1));
        assertThat(movv.get(1000).at(instant(2010,1,1))).isNull();
        assertThat(movv.get(1000).at(instant(2018,2,10))).isEqualTo("1");
        assertThat(movv.get(1000).at(instant(2018,2,20))).isEqualTo("2");
        assertThat(movv.get(1000).at(instant(2019,1,1))).isEqualTo("2");
        assertThat(movv.get(2000).at(instant(2019,1,1))).isNull();

    }

    @Test
    public void should_update_a_movv() throws IOException {
        buildWithNonSortedStage();
        update();

        Movv movv = new Movv(new File("movvs/single.movv"));
        assertThat(movv.get(1000).length()).isEqualTo(4);
        assertThat(movv.get(1000).at(instant(2010,1,1))).isNull();
        assertThat(movv.get(1000).at(instant(2018,2,10))).isEqualTo("1");
        assertThat(movv.get(1000).at(instant(2018,2,25))).isEqualTo("2");
        assertThat(movv.get(1000).at(instant(2018,2,25))).isEqualTo("2");
        assertThat(movv.get(1000).at(instant(2019,1,1))).isEqualTo("4");
        assertThat(movv.get(1000).at(instant(2019,2,15))).isEqualTo("5");
        assertThat(movv.get(1000).at(Instant.now())).isEqualTo("5");
    }

    @Test
    public void should_update_a_movv_using_non_sorted_stage() throws IOException {
        buildWithNonSortedStage();
        updateWithNonSortedStages();

        Movv movv = new Movv(new File("movvs/single.movv"));
        assertThat(movv.get(1000).length()).isEqualTo(4);
        assertThat(movv.get(1000).at(instant(2010,1,1))).isNull();
        assertThat(movv.get(1000).at(instant(2018,2,10))).isEqualTo("1");
        assertThat(movv.get(1000).at(instant(2018,2,25))).isEqualTo("2");
        assertThat(movv.get(1000).at(instant(2018,2,25))).isEqualTo("2");
        assertThat(movv.get(1000).at(instant(2019,1,1))).isEqualTo("4");
        assertThat(movv.get(1000).at(instant(2019,2,15))).isEqualTo("5");
        assertThat(movv.get(1000).at(Instant.now())).isEqualTo("5");
    }
    private static final int TimeOffset = 5000;

    @Test
    public void should_create_a_movv_with_many_items() throws IOException {
        int size = 1000;
        int updates = 4;
        buildMultiple(size);
        updateMultiple(size,updates);

        Iterator<Mov.Entry> iterator;
        Movv movv = new Movv(new File("movvs/multiple.movv"));
        for (int k = 0; k < size; k++) {
            assertThat(movv.get(k).length()).isEqualTo(updates);
            iterator = movv.get(k).iterator();
            for (int i = 0; i < updates; i++)
                assertThat(iterator.next().data).isEqualTo(String.valueOf(i*size+k));
            iterator = movv.get(k).iterator();
            iterator.next();
            Instant instant = instant(2018,2,1).plusMillis((k+size)*TimeOffset);
            for (int i = 1; i < updates; i++)
                assertThat(iterator.next().instant.toEpochMilli()-instant.toEpochMilli()).isEqualTo(((i-1)*size*TimeOffset));

        }
    }

    private void buildEmptyMovv() throws IOException {
        MovvBuilder.create(new File("movvs/empty.movv"), 32)
                .close();
    }

    private void build() throws IOException {
        MovvBuilder.create(new File("movvs/single.movv"), 32)
                .add(1000, instant(2018, 1, 2), "1")
                .close();
    }

    private void buildWithNonSortedStage() throws IOException {
        MovvBuilder.create(new File("movvs/single.movv"), 32)
                .stageOf(1000)
                    .add(instant(2018, 2, 24), "2")
                    .add(instant(2018, 1, 1), "1")
                    .add(instant(2018, 2, 20), "2")
                .commit()
                .close();
    }

    private void update() throws IOException {
        MovvBuilder.update(new File("movvs/single.movv"))
                .add(1000, instant(2018, 3, 20), "2")
                .add(1000, instant(2018, 3, 24), "2")
                .add(1000, instant(2018, 4, 13), "2")
                .add(1000, instant(2018, 8, 18), "4")
                .add(1000, instant(2019, 1, 6), "4")
                .close();

        MovvBuilder.update(new File("movvs/single.movv"))
                .add(1000, instant(2019,2,1), "4")
                .add(1000, instant(2019,2,10), "5")
                .close();
    }

    private void updateWithNonSortedStages() throws IOException {
        MovvBuilder.update(new File("movvs/single.movv"))
                .stageOf(1000)
                    .add(instant(2018, 3, 20), "2")
                    .add(instant(2018, 3, 24), "2")
                    .commit()
                .stageOf(1000)
                    .add(instant(2018, 8, 18), "4")
                    .add(instant(2018, 4, 13), "2")
                    .commit()
                .add(1000, instant(2019, 1, 6), "4")
                .close();

        MovvBuilder.update(new File("movvs/single.movv"))
                .add(1000, instant(2019,2,1), "4")
                .add(1000, instant(2019,2,10), "5")
                .close();
    }

    private void buildMultiple(int size) throws IOException {
        MovvBuilder builder = MovvBuilder.create(new File("movvs/multiple.movv"), 32);
        Instant instant = instant(2018,1,1);
        for (int i = 0; i < size; i++) {
            builder.add(i, instant, String.valueOf(i));
            instant = instant.plusMillis(TimeOffset);
        }
        builder.close();
    }

    private void updateMultiple(int size, int updates) throws IOException {
        MovvBuilder builder = MovvBuilder.update(new File("movvs/multiple.movv"));
        Instant instant = instant(2018,2,1);
        for (int i = 0; i < size * updates; i++) {
            builder.add(i % size, instant, String.valueOf(i));
            instant = instant.plusMillis(TimeOffset);
        }
        builder.close();
    }

    private static Instant instant(int year, int month, int day) {
        return LocalDateTime.of(year,month,day,0,0,0).toInstant(ZoneOffset.UTC);
    }


}
