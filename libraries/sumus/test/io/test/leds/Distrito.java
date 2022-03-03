package io.test.leds;

import io.intino.alexandria.led.Schema;
import io.intino.alexandria.led.allocators.SchemaFactory;
import io.intino.alexandria.led.buffers.store.ByteBufferStore;
import io.intino.alexandria.led.buffers.store.ByteStore;
import io.intino.alexandria.led.util.SchemaSerialBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class Distrito extends Schema {

    public static final int ID_OFFSET = 0;
    public static final int ID_SIZE = Long.SIZE;
    public static final int LUGAR_OFFSET = ID_OFFSET + ID_SIZE;
    public static final int LUGAR_SIZE = Integer.SIZE;
    public static final int SUPERFICIE_OFFSET = LUGAR_OFFSET + LUGAR_SIZE;
    public static final int SUPERFICIE_SIZE = Float.SIZE;
    public static final int BALANCE_OFFSET = SUPERFICIE_OFFSET + SUPERFICIE_SIZE;
    public static final int BALANCE_SIZE = Integer.SIZE;

    public static final int SIZE = (int) Math.ceil((BALANCE_OFFSET + BALANCE_SIZE) / (float)Byte.SIZE);

    /**
     * The serial uuid of a schema should represent the layout of its attributes (offsets and sizes).
     * If the serial uuid changes, then the previous version are no longer readable with this class,
     * because the byte buffer should be interpreted differently.
     * */
    public static final UUID SERIAL_UUID = new SchemaSerialBuilder(Distrito.class.getName())
            .add("id", "long", ID_OFFSET, ID_SIZE)
            .add("lugar", "int", LUGAR_OFFSET, LUGAR_SIZE)
            .add("superficie", "float", SUPERFICIE_OFFSET, SUPERFICIE_OFFSET)
            .add("balance", "int", BALANCE_OFFSET, BALANCE_SIZE)
            .buildSerialId();

    /**
     * This allows to instantiate schemas dynamically without using reflection in each constructor call.
     * */
    public static final SchemaFactory<Distrito> FACTORY = new SchemaFactory<>(Distrito.class) {
        @Override
        public Distrito newInstance(ByteStore store) {
            return new Distrito(store);
        }
    };


    public Distrito() {
        this(new ByteBufferStore(SIZE));
    }

    public Distrito(ByteStore store) {
        super(store);
    }

    @Override
    public long id() {
        return bitBuffer.getAlignedLong(ID_OFFSET);
    }

    public Distrito id(int id) {
        bitBuffer.setAlignedLong(ID_OFFSET, id);
        return this;
    }

    public Lugar lugar() {
        return Lugar.byIndex(bitBuffer.getAlignedInteger(LUGAR_OFFSET));
    }

    public Distrito lugar(Lugar lugar) {
        bitBuffer.setAlignedInteger(LUGAR_OFFSET, lugar.index());
        return this;
    }

    public float superficie() {
        return bitBuffer.getAlignedReal32Bits(SUPERFICIE_OFFSET);
    }

    public Distrito superficie(float superficie) {
        bitBuffer.setAlignedReal32Bits(SUPERFICIE_OFFSET, superficie);
        return this;
    }

    public int balance() {
        return bitBuffer.getAlignedInteger(BALANCE_OFFSET);
    }

    public Distrito balance(int balance) {
        bitBuffer.setAlignedInteger(BALANCE_OFFSET, balance);
        return this;
    }

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public UUID serialUUID() {
        return SERIAL_UUID;
    }

    @Override
    public String toString() {
        return "Distrito{" +
                "id=" + id() +
                ", lugar=" + lugar() +
                ", supercifie=" + superficie() +
                ", balance=" + balance() +
                '}';
    }

    public static class Lugar {

        private static final List<Lugar> Lugares = getLugares();

        public static Lugar byIndex(int index) {
            if(index >= Lugares.size()) return Lugares.get(0); // NA
            return Lugares.get(index);
        }

        public static int size() {
            return Lugares.size();
        }


        private final String name;
        private int index;

        private Lugar(String name) {
            this.name = name;
        }

        public String name() {
            return name;
        }

        public int index() {
            return index;
        }

        @Override
        public String toString() {
            return name;
        }

        private static List<Lugar> getLugares() {
            List<Lugar> list = List.of(
                    new Lugar("NA"),
                    new Lugar("San Borondon"),
                    new Lugar("Gran Canaria.Santa María de Guía de Gran Canaria"),
                    new Lugar("Gran Canaria.Valsequillo de Gran Canaria"),
                    new Lugar("Gran Canaria.Las Palmas de Gran Canaria"),
                    new Lugar("Gran Canaria.San Bartolomé de Tirajana"),
                    new Lugar("Gran Canaria.La Aldea de San Nicolás"),
                    new Lugar("Gran Canaria.Santa Lucía de Tirajana"),
                    new Lugar("Tenerife.San Cristóbal de La Laguna"),
                    new Lugar("Gomera.San Sebastián de La Gomera"),
                    new Lugar("Fuerteventura.Puerto del Rosario"),
                    new Lugar("Tenerife.La Victoria de Acentejo"),
                    new Lugar("Tenerife.La Matanza de Acentejo"),
                    new Lugar("La Palma.Santa Cruz de La Palma"),
                    new Lugar("Tenerife.Santa Cruz de Tenerife"),
                    new Lugar("El Hierro.El Pinar de El Hierro"),
                    new Lugar("Gran Canaria.Vega de San Mateo"),
                    new Lugar("La Palma.Los Llanos de Aridane"),
                    new Lugar("Tenerife.San Juan de la Rambla"),
                    new Lugar("Tenerife.Buenavista del Norte"),
                    new Lugar("Tenerife.Granadilla de Abona"),
                    new Lugar("La Palma.San Andrés y Sauces"),
                    new Lugar("Tenerife.San Miguel de Abona"),
                    new Lugar("Tenerife.Santiago del Teide"),
                    new Lugar("Gran Canaria.Santa Brígida"),
                    new Lugar("Tenerife.Icod de los Vinos"),
                    new Lugar("Tenerife.Puerto de La Cruz"),
                    new Lugar("Fuerteventura.Betancuria"),
                    new Lugar("Lanzarote.San Bartolomé"),
                    new Lugar("Fuerteventura.La Oliva"),
                    new Lugar("Gran Canaria.Valleseco"),
                    new Lugar("Tenerife.Guía de Isora"),
                    new Lugar("La Palma.Villa de Mazo"),
                    new Lugar("Fuerteventura.Antigua"),
                    new Lugar("Gran Canaria.Artenara"),
                    new Lugar("Fuerteventura.Tuineje"),
                    new Lugar("La Palma.Fuencaliente"),
                    new Lugar("Tenerife.Los Realejos"),
                    new Lugar("Tenerife.Santa Úrsula"),
                    new Lugar("Gomera.Valle Gran Rey"),
                    new Lugar("Gran Canaria.Agüimes"),
                    new Lugar("Gran Canaria.Ingenio"),
                    new Lugar("Fuerteventura.Pájara"),
                    new Lugar("Gran Canaria.Agaete"),
                    new Lugar("Gran Canaria.Arucas"),
                    new Lugar("Gran Canaria.Firgas"),
                    new Lugar("Gran Canaria.Gáldar"),
                    new Lugar("Gran Canaria.Tejeda"),
                    new Lugar("La Palma.Barlovento"),
                    new Lugar("La Palma.Breña Alta"),
                    new Lugar("La Palma.Breña Baja"),
                    new Lugar("Tenerife.Candelaria"),
                    new Lugar("Tenerife.La Guancha"),
                    new Lugar("Tenerife.La Orotava"),
                    new Lugar("La Palma.Puntagorda"),
                    new Lugar("La Palma.Puntallana"),
                    new Lugar("Tenerife.El Rosario"),
                    new Lugar("Gomera.Vallehermoso"),
                    new Lugar("Lanzarote.Arrecife"),
                    new Lugar("Gran Canaria.Mogán"),
                    new Lugar("Gran Canaria.Telde"),
                    new Lugar("Gran Canaria.Teror"),
                    new Lugar("El Hierro.Frontera"),
                    new Lugar("Tenerife.Garachico"),
                    new Lugar("Tenerife.El Sauzal"),
                    new Lugar("Tenerife.Los Silos"),
                    new Lugar("Tenerife.Tacoronte"),
                    new Lugar("Tenerife.El Tanque"),
                    new Lugar("La Palma.Tazacorte"),
                    new Lugar("El Hierro.Valverde"),
                    new Lugar("Gran Canaria.Moya"),
                    new Lugar("Lanzarote.Teguise"),
                    new Lugar("Tenerife.Tegueste"),
                    new Lugar("La Palma.Tijarafe"),
                    new Lugar("Tenerife.Vilaflor"),
                    new Lugar("Lanzarote.Tinajo"),
                    new Lugar("La Palma.Garafía"),
                    new Lugar("La Palma.El Paso"),
                    new Lugar("Lanzarote.Haría"),
                    new Lugar("Lanzarote.Yaiza"),
                    new Lugar("Tenerife.Fasnia"),
                    new Lugar("Tenerife.Güímar"),
                    new Lugar("Gomera.Hermigua"),
                    new Lugar("Lanzarote.Tías"),
                    new Lugar("Tenerife.Adeje"),
                    new Lugar("Gomera.Alajeró"),
                    new Lugar("Tenerife.Arafo"),
                    new Lugar("Tenerife.Arico"),
                    new Lugar("Tenerife.Arona"),
                    new Lugar("Gomera.Agulo")
            );
            IntStream.range(0, list.size()).forEach(i -> list.get(i).index = i);
            return list;
        }
    }
}
