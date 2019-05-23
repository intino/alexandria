package io.intino.test;

import io.intino.alexandria.movv.Mov;
import io.intino.alexandria.movv.Movv;
import io.intino.alexandria.movv.MovvBuilder;
import io.intino.alexandria.movv.MovvBuilder.Stage;
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
import java.util.Arrays;
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
        assertThat(movv.get(1000).at(instant(2018,2,10)).data).isEqualTo(bytes());
        assertThat(movv.get(2000).at(instant(2018,2,10)).data).isEqualTo(bytes());
    }

    @Test
    public void should_create_a_movv() throws IOException {
        build();

        for (Movv.Mode mode : Movv.Mode.values()) {
            Movv movv = new Movv(new File("movvs/single.movv"), mode);
            assertThat(movv.get(1000).length()).isEqualTo(1);
            assertThat(movv.get(1000).first().instant.toString()).isEqualTo("2018-01-02T00:00:00Z");
            assertThat(movv.get(1000).first().data).isEqualTo(bytes(49, 0));
            assertThat(movv.get(1000).last().instant.toString()).isEqualTo("2018-01-02T00:00:00Z");
            assertThat(movv.get(1000).last().data).isEqualTo(bytes(49, 0));
            assertThat(movv.get(1000).iterator().hasNext()).isTrue();
            assertThat(movv.get(1000).iterator().next().data).isEqualTo(bytes(49, 0));
            assertThat(movv.get(1000).iterator().next().instant).isEqualTo(instant(2018,1,2));
            assertThat(movv.get(1000).at(instant(2018,1,1)).data).isEqualTo(bytes());
            assertThat(movv.get(1000).at(instant(2018,1,2)).data).isEqualTo(bytes(49,0));
            assertThat(movv.get(1000).at(instant(2019,1,1)).data).isEqualTo(bytes(49,0));
            assertThat(movv.get(2000).at(instant(2019,1,1)).data).isEqualTo(bytes());
        }

    }

    private byte[] bytes(int... data) {
        byte[] bytes = new byte[data.length];
        for (int i = 0; i < data.length; i++)
            bytes[i] = (byte) data[i];
        return bytes;
    }

    @Test
    public void should_create_a_movv_using_non_sorted_stage() throws IOException {
        buildWithNonSortedStage();

        for (Movv.Mode mode : Movv.Mode.values()) {
            Movv movv = new Movv(new File("movvs/single.movv"), mode);
            assertThat(movv.get(1000).length()).isEqualTo(2);
            assertThat(movv.get(1000).last().instant.toString()).isEqualTo("2018-02-20T00:00:00Z");
            assertThat(movv.get(1000).last().data).isEqualTo(bytes(50, 0));
            assertThat(movv.get(1000).iterator().hasNext()).isTrue();
            assertThat(movv.get(1000).iterator().next().data).isEqualTo(bytes(49, 0));
            assertThat(movv.get(1000).iterator().next().instant).isEqualTo(instant(2018, 1, 1));
            assertThat(movv.get(1000).at(instant(2010, 1, 1)).data).isEqualTo(bytes());
            assertThat(movv.get(1000).at(instant(2018, 2, 10)).data).isEqualTo(bytes(49, 0));
            assertThat(movv.get(1000).at(instant(2018, 2, 20)).data).isEqualTo(bytes(50, 0));
            assertThat(movv.get(1000).at(instant(2019, 1, 1)).data).isEqualTo(bytes(50, 0));
            assertThat(movv.get(2000).at(instant(2019, 1, 1)).data).isEqualTo(bytes());
        }
    }

    @Test
    public void should_update_a_movv() throws IOException {
        buildWithNonSortedStage();
        update();

        for (Movv.Mode mode : Movv.Mode.values()) {
            Movv movv = new Movv(new File("movvs/single.movv"));
            assertThat(movv.get(1000).length()).isEqualTo(4);
            assertThat(movv.get(1000).at(instant(2010, 1, 1)).data).isEqualTo(bytes());
            assertThat(movv.get(1000).at(instant(2018, 2, 10)).data).isEqualTo(bytes(49, 0));
            assertThat(movv.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(movv.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(movv.get(1000).at(instant(2019, 1, 1)).data).isEqualTo(bytes(52, 0));
            assertThat(movv.get(1000).at(instant(2019, 2, 15)).data).isEqualTo(bytes(48, 53));
            assertThat(movv.get(1000).at(Instant.now()).data).isEqualTo(bytes(48, 53));
        }
    }


    @Test
    public void should_not_update_a_movv_if_new_record_instant_equals_to_last_instant() throws IOException {
        buildWithNonSortedStage();
        updateWithSameInstant();

        for (Movv.Mode mode : Movv.Mode.values()) {
            Movv movv = new Movv(new File("movvs/single.movv"));
            assertThat(movv.get(1000).length()).isEqualTo(2);
            assertThat(movv.get(1000).at(instant(2010, 1, 1)).data).isEqualTo(bytes());
            assertThat(movv.get(1000).at(instant(2018, 2, 10)).data).isEqualTo(bytes(49, 0));
            assertThat(movv.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(movv.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(movv.get(1000).at(Instant.now()).data).isEqualTo(bytes(50, 0));
        }
    }

    @Test
    public void should_update_a_movv_using_non_sorted_stage() throws IOException {
        buildWithNonSortedStage();
        updateWithNonSortedStages();

        for (Movv.Mode mode : Movv.Mode.values()) {
            Movv movv = new Movv(new File("movvs/single.movv"));
            assertThat(movv.get(1000).length()).isEqualTo(4);
            assertThat(movv.get(1000).at(instant(2010, 1, 1)).data).isEqualTo(bytes());
            assertThat(movv.get(1000).at(instant(2018, 2, 10)).data).isEqualTo(bytes(49, 0));
            assertThat(movv.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(movv.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(movv.get(1000).at(instant(2019, 1, 1)).data).isEqualTo(bytes(52, 0));
            assertThat(movv.get(1000).at(instant(2019, 2, 15)).data).isEqualTo(bytes(53, 0));
            assertThat(movv.get(1000).at(Instant.now()).data).isEqualTo(bytes(53, 0));
            assertThat(movv.get(1000).last().instant.toString()).isEqualTo("2019-02-10T00:00:00Z");
            assertThat(movv.get(1000).last().data).isEqualTo(bytes(53, 0));
        }
    }
    private final int TimeOffset = 5000;
    private final int size = 1000;
    private final int updates = 4;

    @Test
    public void should_create_a_movv_with_many_items_with_stage() throws IOException {
        buildMultipleWithStage();
        updateMultipleWithStage();

        for (Movv.Mode mode : Movv.Mode.values()) {
            Movv movv = new Movv(new File("movvs/multiple.movv"));
            checkMultiple(movv);
        }
    }

    @Test
    public void should_create_a_movv_with_many_items() throws IOException {
        buildMultiple();
        updateMultiple();

        for (Movv.Mode mode : Movv.Mode.values()) {
            Movv movv = new Movv(new File("movvs/multiple.movv"));
            checkMultiple(movv);
        }
    }

    private void checkMultiple(Movv movv) {
        Iterator<Mov.Item> iterator;
        for (int k = 0; k < size; k++) {
            assertThat(movv.get(k).length()).isEqualTo(updates);
            iterator = movv.get(k).iterator();
            for (int i = 0; i < updates; i++)
                assertThat(iterator.next().data).isEqualTo(resize(String.valueOf(i*size+k).getBytes(), 32));
            iterator = movv.get(k).iterator();
            iterator.next();
            Instant instant = instant(2018,2,1).plusMillis((k+size)*TimeOffset);
            for (int i = 1; i < updates; i++)
                assertThat(iterator.next().instant.toEpochMilli()-instant.toEpochMilli()).isEqualTo(((i-1)*size*TimeOffset));

        }
    }

    private Object resize(byte[] bytes, int dataSize) {
        return Arrays.copyOf(bytes, dataSize);
    }

    private void buildEmptyMovv() throws IOException {
        MovvBuilder.create(new File("movvs/empty.movv"), 1)
                .close();
    }

    private void build() throws IOException {
        MovvBuilder.create(new File("movvs/single.movv"), 2)
                .add(1000, instant(2018, 1, 2), bytes(49,0))
                .close();
    }

    private void buildWithNonSortedStage() throws IOException {
        MovvBuilder.create(new File("movvs/single.movv"), 2)
                .stageOf(1000)
                    .add(instant(2018, 2, 24), bytes(50,0))
                    .add(instant(2018, 1, 1), bytes(49,0))
                    .add(instant(2018, 1, 1), bytes(51,0))
                    .add(instant(2018, 2, 20), bytes(50,0))
                .commit()
                .close();
    }

    private void update() throws IOException {
        MovvBuilder.update(new File("movvs/single.movv"))
                .add(1000, instant(2018, 3, 20), bytes(50,0))
                .add(1000, instant(2018, 3, 24), bytes(50,0))
                .add(1000, instant(2018, 4, 13), bytes(50,0))
                .add(1000, instant(2018, 8, 18), bytes(52,0))
                .add(1000, instant(2019, 1, 6), bytes(52,0))
                .close();

        MovvBuilder.update(new File("movvs/single.movv"))
                .add(1000, instant(2019,2,1), bytes(52,0))
                .add(1000, instant(2019,2,10), bytes(48,53))
                .close();
    }

    private void updateWithSameInstant() throws IOException {
        MovvBuilder.update(new File("movvs/single.movv"))
                .add(1000, instant(2018, 2, 20), bytes(53,0))
                .close();
    }

    private void updateWithNonSortedStages() throws IOException {
        MovvBuilder builder = MovvBuilder.update(new File("movvs/single.movv"))
                .stageOf(1000)
                .add(instant(2018, 3, 20), bytes(50,0))
                .add(instant(2018, 3, 24), bytes(50,0))
                .commit();
        builder.stageOf(1000)
                .add(instant(2018, 8, 18), bytes(52,0))
                .add(instant(2018, 4, 13), bytes(50,0))
                .add(instant(2019, 1, 6), bytes(52,0));
        builder.stages().forEach(Stage::commit);
        builder.close();

        MovvBuilder.update(new File("movvs/single.movv"))
                .add(1000, instant(2019,2,1), bytes(52,0))
                .add(1000, instant(2019,2,10), bytes(53,0))
                .close();
    }

    private void buildMultiple() throws IOException {
        MovvBuilder builder = MovvBuilder.create(new File("movvs/multiple.movv"), 32);
        Instant instant = instant(2018,1,1);
        for (int i = 0; i < size; i++) {
            builder.add(i, instant, String.valueOf(i).getBytes());
            instant = instant.plusMillis(TimeOffset);
        }
        builder.close();
    }

    private void buildMultipleWithStage() throws IOException {
        MovvBuilder builder = MovvBuilder.create(new File("movvs/multiple.movv"), 32);
        Instant instant = instant(2018,1,1);
        for (int i = 0; i < size; i++) {
            builder.stageOf(i).add(instant, String.valueOf(i).getBytes());
            instant = instant.plusMillis(TimeOffset);
        }
        builder.stages().forEach(Stage::commit);
        builder.close();
    }

    private void updateMultiple() throws IOException {
        MovvBuilder builder = MovvBuilder.update(new File("movvs/multiple.movv"));
        Instant instant = instant(2018,2,1);
        for (int i = 0; i < size * updates; i++) {
            builder.add(i % size, instant, String.valueOf(i).getBytes());
            instant = instant.plusMillis(TimeOffset);
        }
        builder.close();
    }

    private void updateMultipleWithStage() throws IOException {
        MovvBuilder builder = MovvBuilder.update(new File("movvs/multiple.movv"));
        Instant instant = instant(2018,2,1);
        for (int i = 0; i < size * updates; i++) {
            builder.stageOf(i % size).add(instant, String.valueOf(i).getBytes());
            instant = instant.plusMillis(TimeOffset);
        }
        builder.stages().forEach(Stage::commit);
        builder.close();
    }

    private static Instant instant(int year, int month, int day) {
        return LocalDateTime.of(year,month,day,0,0,0).toInstant(ZoneOffset.UTC);
    }


}
