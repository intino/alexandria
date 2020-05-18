package io.intino.alexandria.allotropy;

import io.intino.alexandria.allotropy.prototype.Packet;
import io.intino.alexandria.allotropy.prototype.Sprout;
import io.intino.alexandria.allotropy.prototype.packets.ErrorPacket;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AssetFactory {
    private final Prototype prototype;

    public AssetFactory(Prototype prototype) {
        this.prototype = prototype;
    }

    public File build(File seed) throws IOException {
        return build(seed, tempFolder());
    }

    public File build(File seed, File outFolder) throws IOException {
        try (Product product = new Product(outFolder)) {
            product.process(seed);
            return product.root;
        }
    }

    private class Product implements Closeable {
        private final File root;
        private final Map<BufferedWriter, Sprout.Serializer> sprouts;

        Product(File root) {
            this.root = root;
            this.sprouts = new HashMap<>();
        }

        void process(File seed) throws IOException {
            init();
            copy(seed);
            try (BufferedReader reader = readerOf(seed)) {
                process(Packet.BOF);
                process(reader);
                process(Packet.EOF);
            }
        }

        private void init() throws FileNotFoundException {
            for (Sprout sprout : prototype.sprouts())
                sprouts.put(writerOf(sprout), sprout.serializer());
        }

        private void process(BufferedReader reader) throws IOException {
            int index = 0;
            while (true) {
                index++;
                String line = reader.readLine();
                if (line == null) break;
                if (line.isEmpty()) continue;
                try {
                    process(wrap(index, prototype.seed().deserializer(line).deserialize(line)));
                } catch (FormatError formatError) {
                    process(new ErrorPacket(index, line, formatError));
                }
            }
        }

        private Packet wrap(int index, Packet packet) {
            return new Packet() {
                @Override
                public String name() {
                    return packet.name();
                }

                @Override
                public Item[] items() {
                    return packet.items();
                }

                @Override
                public int line() {
                    return index;
                }

                @Override
                public Iterator<Item> iterator() {
                    return packet.iterator();
                }

                @Override
                public Map<String, String> map() {
                    return packet.map();
                }
            };
        }

        private void process(Packet packet) throws IOException {
            for (BufferedWriter writer : sprouts.keySet()) {
                String line = sprouts.get(writer).serialize(packet);
                if (line != null) writer.write(line);
            }
        }

        private BufferedWriter writerOf(Sprout sprout) throws FileNotFoundException {
            return new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(new File(root, sprout.filename())))));
        }

        private void copy(File file) throws IOException {
            Files.copy(file.toPath(), new File(root, file.getName()).toPath());
        }

        private BufferedReader readerOf(File file) throws IOException {
            return new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
        }

        @Override
        public void close() throws IOException {
            for (BufferedWriter writer : sprouts.keySet()) writer.close();
        }
    }

    private File tempFolder() throws IOException {
        return Files.createTempDirectory(prototype.getClass().getName()).toFile();
    }
}
