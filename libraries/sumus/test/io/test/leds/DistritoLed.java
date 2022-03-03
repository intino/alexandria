package io.test.leds;

import io.intino.alexandria.led.LedStream;
import io.intino.alexandria.led.leds.ListLed;
import io.intino.alexandria.led.legacy.LegacyLedReader;
import io.intino.alexandria.led.legacy.LegacyLedWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DistritoLed {

    public static final File LedFile = new File("temp/leds/", Distrito.class.getSimpleName() + ".led");

    public static void main(String[] args) {
        write();
        read();
    }

    private static void read() {
        try(LedStream<Distrito> distritos = new LegacyLedReader(LedFile).read(Distrito.class)) {
            while(distritos.hasNext()) {
                Distrito distrito = distritos.next();
                System.out.println(distrito);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void write() {
        List<Distrito> distritos = new ArrayList<>();

        for(int i = 0;i < 1000;i++) {
            distritos.add(new Distrito()
                    .id(i)
                    .lugar(Distrito.Lugar.byIndex(i % Distrito.Lugar.size()))
                    .superficie(i * 1000)
                    .balance(i * 100));
        }

        new LegacyLedWriter(LedFile).write(new ListLed<>(Distrito.class, distritos));
    }
}
