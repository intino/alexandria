package io.intino.test;

import io.intino.alexandria.movv.MovvBuilder;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Benchmarks_ {
    public static void main(String[] args) throws IOException {
        System.out.println("size;action;time");
        new File("movvs").mkdirs();
        for (int i = 1; i <= 10; i++) {
            Instant now = Instant.now();
            int size = i * 1000000;
            buildMany(size);
            System.out.println(size +";"+ "build" +";"+(Instant.now().toEpochMilli()-now.toEpochMilli())/1000.0);
            double time = 0;
            for (int j = 1; j < 5; j++) {
                now = Instant.now();
                update(size, 5000);
                time += (Instant.now().toEpochMilli()-now.toEpochMilli())/1000.0;
                System.out.println(size +";"+ "update " + (j*5000) +";"+ time);
            }

        }
    }

    private static void buildMany(int size) throws IOException {
        MovvBuilder builder = MovvBuilder.create(new File("movvs/many-" + size +".movv"), 32);
        Instant instant = instant(2018,1,1);

        for (int id = 0; id < size; id++)
            builder.add(id, instant, "init");
        builder.close();
    }

    private static void update(int size, int updates) throws IOException {
        MovvBuilder builder = MovvBuilder.update(new File("movvs/many-" + size +".movv"));
        Instant instant = instant(2018,1,1);

        int id = 0;
        int timeOffset = 5000;
        for (int i = 0; i < updates; i++) {
            builder.add(id, instant, String.valueOf(i));
            instant = instant.plusMillis(timeOffset);
            id = (int) ((id + updates) % (size * 1.1));
        }
        builder.close();
    }

    private static Instant instant(int year, int month, int day) {
        return LocalDateTime.of(year,month,day,0,0,0).toInstant(ZoneOffset.UTC);
    }



}
