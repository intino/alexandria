package io.intino.test;

import io.intino.alexandria.epoch.Epoch;
import io.intino.alexandria.epoch.EpochBuilder;
import io.intino.alexandria.epoch.EpochBuilder.Stage;
import io.intino.alexandria.epoch.Timeline;
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
public class EpochBuilder_ {

    private final int TimeOffset = 5000;
    private final int size = 1000;
    private final int updates = 4;

    private static Instant instant(int year, int month, int day) {
        return LocalDateTime.of(year,month,day,0,0,0).toInstant(ZoneOffset.UTC);
    }

    @Before
    public void setUp() {
        new File("epochs").mkdirs();
    }

    @After
    public void tearDown() throws Exception {
        Files.walk(new File("epochs").toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void should_create_an_empty_epoch() throws IOException {
        buildEmptyepoch();

        Epoch epoch = new Epoch(new File("epochs/empty.epoch"));
        assertThat(epoch.get(1000).at(instant(2018, 2, 10)).data).isEqualTo(bytes());
        assertThat(epoch.get(2000).at(instant(2018, 2, 10)).data).isEqualTo(bytes());
    }

    @Test
    public void should_create_a_epoch() throws IOException {
        build();

        for (Epoch.Mode mode : Epoch.Mode.values()) {
            Epoch epoch = new Epoch(new File("epochs/single.epoch"), mode);
            assertThat(epoch.get(1000).length()).isEqualTo(1);
            assertThat(epoch.get(1000).first().instant.toString()).isEqualTo("2018-01-02T00:00:00Z");
            assertThat(epoch.get(1000).first().data).isEqualTo(bytes(49, 0));
            assertThat(epoch.get(1000).last().instant.toString()).isEqualTo("2018-01-02T00:00:00Z");
            assertThat(epoch.get(1000).last().data).isEqualTo(bytes(49, 0));
            assertThat(epoch.get(1000).iterator().hasNext()).isTrue();
            assertThat(epoch.get(1000).iterator().next().data).isEqualTo(bytes(49, 0));
            assertThat(epoch.get(1000).iterator().next().instant).isEqualTo(instant(2018, 1, 2));
            assertThat(epoch.get(1000).at(instant(2018, 1, 1)).data).isEqualTo(bytes());
            assertThat(epoch.get(1000).at(instant(2018, 1, 2)).data).isEqualTo(bytes(49, 0));
            assertThat(epoch.get(1000).at(instant(2019, 1, 1)).data).isEqualTo(bytes(49, 0));
            assertThat(epoch.get(2000).at(instant(2019, 1, 1)).data).isEqualTo(bytes());
        }

    }

    private byte[] bytes(int... data) {
        byte[] bytes = new byte[data.length];
        for (int i = 0; i < data.length; i++)
            bytes[i] = (byte) data[i];
        return bytes;
    }

    @Test
    public void should_create_a_epoch_using_non_sorted_stage() throws IOException {
        buildWithNonSortedStage();

        for (Epoch.Mode mode : Epoch.Mode.values()) {
            Epoch epoch = new Epoch(new File("epochs/single.epoch"), mode);
            assertThat(epoch.get(1000).length()).isEqualTo(2);
            assertThat(epoch.get(1000).last().instant.toString()).isEqualTo("2018-02-20T00:00:00Z");
            assertThat(epoch.get(1000).last().data).isEqualTo(bytes(50, 0));
            assertThat(epoch.get(1000).iterator().hasNext()).isTrue();
            assertThat(epoch.get(1000).iterator().next().data).isEqualTo(bytes(49, 0));
            assertThat(epoch.get(1000).iterator().next().instant).isEqualTo(instant(2018, 1, 1));
            assertThat(epoch.get(1000).at(instant(2010, 1, 1)).data).isEqualTo(bytes());
            assertThat(epoch.get(1000).at(instant(2018, 2, 10)).data).isEqualTo(bytes(49, 0));
            assertThat(epoch.get(1000).at(instant(2018, 2, 20)).data).isEqualTo(bytes(50, 0));
            assertThat(epoch.get(1000).at(instant(2019, 1, 1)).data).isEqualTo(bytes(50, 0));
            assertThat(epoch.get(2000).at(instant(2019, 1, 1)).data).isEqualTo(bytes());
        }
    }

    @Test
    public void should_update_a_epoch() throws IOException {
        buildWithNonSortedStage();
        update();

        for (Epoch.Mode mode : Epoch.Mode.values()) {
            Epoch epoch = new Epoch(new File("epochs/single.epoch"));
            assertThat(epoch.get(1000).length()).isEqualTo(4);
            assertThat(epoch.get(1000).at(instant(2010, 1, 1)).data).isEqualTo(bytes());
            assertThat(epoch.get(1000).at(instant(2018, 2, 10)).data).isEqualTo(bytes(49, 0));
            assertThat(epoch.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(epoch.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(epoch.get(1000).at(instant(2019, 1, 1)).data).isEqualTo(bytes(52, 0));
            assertThat(epoch.get(1000).at(instant(2019, 2, 15)).data).isEqualTo(bytes(48, 53));
            assertThat(epoch.get(1000).at(Instant.now()).data).isEqualTo(bytes(48, 53));
        }
    }

    @Test
    public void should_not_update_a_epoch_if_new_record_instant_equals_to_last_instant() throws IOException {
        buildWithNonSortedStage();
        updateWithSameInstant();

        for (Epoch.Mode mode : Epoch.Mode.values()) {
            Epoch epoch = new Epoch(new File("epochs/single.epoch"));
            assertThat(epoch.get(1000).length()).isEqualTo(2);
            assertThat(epoch.get(1000).at(instant(2010, 1, 1)).data).isEqualTo(bytes());
            assertThat(epoch.get(1000).at(instant(2018, 2, 10)).data).isEqualTo(bytes(49, 0));
            assertThat(epoch.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(epoch.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(epoch.get(1000).at(Instant.now()).data).isEqualTo(bytes(50, 0));
        }
    }

    @Test
    public void should_update_a_epoch_using_non_sorted_stage() throws IOException {
        buildWithNonSortedStage();
        updateWithNonSortedStages();

        for (Epoch.Mode mode : Epoch.Mode.values()) {
            Epoch epoch = new Epoch(new File("epochs/single.epoch"));
            assertThat(epoch.get(1000).length()).isEqualTo(4);
            assertThat(epoch.get(1000).at(instant(2010, 1, 1)).data).isEqualTo(bytes());
            assertThat(epoch.get(1000).at(instant(2018, 2, 10)).data).isEqualTo(bytes(49, 0));
            assertThat(epoch.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(epoch.get(1000).at(instant(2018, 2, 25)).data).isEqualTo(bytes(50, 0));
            assertThat(epoch.get(1000).at(instant(2019, 1, 1)).data).isEqualTo(bytes(52, 0));
            assertThat(epoch.get(1000).at(instant(2019, 2, 15)).data).isEqualTo(bytes(53, 0));
            assertThat(epoch.get(1000).at(Instant.now()).data).isEqualTo(bytes(53, 0));
            assertThat(epoch.get(1000).last().instant.toString()).isEqualTo("2019-02-10T00:00:00Z");
            assertThat(epoch.get(1000).last().data).isEqualTo(bytes(53, 0));
        }
    }

    @Test
    public void should_create_a_epoch_with_many_items_with_stage() throws IOException {
        buildMultipleWithStage();
        updateMultipleWithStage();

        for (Epoch.Mode mode : Epoch.Mode.values()) {
            Epoch epoch = new Epoch(new File("epochs/multiple.epoch"));
            checkMultiple(epoch);
        }
    }

    @Test
    public void should_create_a_epoch_with_many_items() throws IOException {
        buildMultiple();
        updateMultiple();

        for (Epoch.Mode mode : Epoch.Mode.values()) {
            Epoch epoch = new Epoch(new File("epochs/multiple.epoch"));
            checkMultiple(epoch);
        }
    }

    private void checkMultiple(Epoch epoch) {
        Iterator<Timeline.Item> iterator;
        for (int k = 0; k < size; k++) {
            assertThat(epoch.get(k).length()).isEqualTo(updates);
            iterator = epoch.get(k).iterator();
            for (int i = 0; i < updates; i++)
                assertThat(iterator.next().data).isEqualTo(resize(String.valueOf(i*size+k).getBytes(), 32));
            iterator = epoch.get(k).iterator();
            iterator.next();
            Instant instant = instant(2018,2,1).plusMillis((k+size)*TimeOffset);
            for (int i = 1; i < updates; i++)
                assertThat(iterator.next().instant.toEpochMilli()-instant.toEpochMilli()).isEqualTo(((i-1)*size*TimeOffset));

        }
    }

    private Object resize(byte[] bytes, int dataSize) {
        return Arrays.copyOf(bytes, dataSize);
    }

    private void buildEmptyepoch() throws IOException {
        EpochBuilder.create(new File("epochs/empty.epoch"), 1)
                .close();
    }

    private void build() throws IOException {
        EpochBuilder.create(new File("epochs/single.epoch"), 2)
                .add(1000, instant(2018, 1, 2), bytes(49,0))
                .close();
    }

    private void buildWithNonSortedStage() throws IOException {
        EpochBuilder.create(new File("epochs/single.epoch"), 2)
                .stageOf(1000)
                    .add(instant(2018, 2, 24), bytes(50,0))
                    .add(instant(2018, 1, 1), bytes(49,0))
                    .add(instant(2018, 1, 1), bytes(51,0))
                    .add(instant(2018, 2, 20), bytes(50,0))
                .commit()
                .close();
    }

    private void update() throws IOException {
        EpochBuilder.update(new File("epochs/single.epoch"))
                .add(1000, instant(2018, 3, 20), bytes(50,0))
                .add(1000, instant(2018, 3, 24), bytes(50,0))
                .add(1000, instant(2018, 4, 13), bytes(50,0))
                .add(1000, instant(2018, 8, 18), bytes(52,0))
                .add(1000, instant(2019, 1, 6), bytes(52,0))
                .close();

        EpochBuilder.update(new File("epochs/single.epoch"))
                .add(1000, instant(2019,2,1), bytes(52,0))
                .add(1000, instant(2019,2,10), bytes(48,53))
                .close();
    }

    private void updateWithSameInstant() throws IOException {
        EpochBuilder.update(new File("epochs/single.epoch"))
                .add(1000, instant(2018, 2, 20), bytes(53,0))
                .close();
    }

    private void updateWithNonSortedStages() throws IOException {
        EpochBuilder builder = EpochBuilder.update(new File("epochs/single.epoch"))
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

        EpochBuilder.update(new File("epochs/single.epoch"))
                .add(1000, instant(2019,2,1), bytes(52,0))
                .add(1000, instant(2019,2,10), bytes(53,0))
                .close();
    }

    private void buildMultiple() throws IOException {
        EpochBuilder builder = EpochBuilder.create(new File("epochs/multiple.epoch"), 32);
        Instant instant = instant(2018,1,1);
        for (int i = 0; i < size; i++) {
            builder.add(i, instant, String.valueOf(i).getBytes());
            instant = instant.plusMillis(TimeOffset);
        }
        builder.close();
    }

    private void buildMultipleWithStage() throws IOException {
        EpochBuilder builder = EpochBuilder.create(new File("epochs/multiple.epoch"), 32);
        Instant instant = instant(2018,1,1);
        for (int i = 0; i < size; i++) {
            builder.stageOf(i).add(instant, String.valueOf(i).getBytes());
            instant = instant.plusMillis(TimeOffset);
        }
        builder.stages().forEach(Stage::commit);
        builder.close();
    }

    private void updateMultiple() throws IOException {
        EpochBuilder builder = EpochBuilder.update(new File("epochs/multiple.epoch"));
        Instant instant = instant(2018,2,1);
        for (int i = 0; i < size * updates; i++) {
            builder.add(i % size, instant, String.valueOf(i).getBytes());
            instant = instant.plusMillis(TimeOffset);
        }
        builder.close();
    }

    private void updateMultipleWithStage() throws IOException {
        EpochBuilder builder = EpochBuilder.update(new File("epochs/multiple.epoch"));
        Instant instant = instant(2018,2,1);
        for (int i = 0; i < size * updates; i++) {
            builder.stageOf(i % size).add(instant, String.valueOf(i).getBytes());
            instant = instant.plusMillis(TimeOffset);
        }
        builder.stages().forEach(Stage::commit);
        builder.close();
    }


}
