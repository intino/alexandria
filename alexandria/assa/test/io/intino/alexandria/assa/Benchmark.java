package io.intino.alexandria.assa;

import io.intino.alexandria.assa.loaders.FileLazyAssa;
import io.intino.alexandria.assa.loaders.InputStreamAssa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Runtime.getRuntime;

public class Benchmark {

	public static void main(String[] args) throws IOException {
		System.out.println("Store");
		//store();

		System.out.println("Type\tSearch Count\tDuration\tMemory");

		for (int i = 1000; i < 20000000; i *= 2) {
			long[] keys = randomKeys(i);
			System.gc();
			benchmark("Memory\t" + i, System.nanoTime(), memoryLoad(), keys);
			System.gc();
			benchmark("File\t" + i, System.nanoTime(), fileLoad(), keys);
		}
	}

	private static long[] randomKeys(int size) {
		long[] k = new long[size];
		for (int i = 0; i < size; i++)
			k[i] = (long) (randomInt(3000) * 50000 + randomInt(20000));
		Arrays.sort(k);
		return k;
	}

	private static int randomInt(int max) {
		return ThreadLocalRandom.current().nextInt(0, max) + 1;
	}

	private static void benchmark(String tag, long start, Assa<String> reader, long[] keys) {
		long used = getRuntime().totalMemory() - getRuntime().freeMemory();

		for (long key : keys) {
			String s = reader.get(key);
			if (s == null) System.out.println(key);
		}

		System.gc();
		long end = System.nanoTime();
		System.out.print(tag + "\t");
		System.out.print((end - start) / 1.0e9 + "\t");
		System.out.print(used + "\t");
		System.out.println();
	}

	public static void store() throws IOException {
		AssaBuilder<String> builder = new AssaBuilder<>("feature");
		for (int i = 1; i <= 20000; i++) {
			String value = UUID.randomUUID().toString();
			for (int j = 1; j <= 3000; j++) {
				long key = i + j * 50000;
				builder.put(key, value);
			}
		}
		builder.save(file());
	}

	private static InputStreamAssa<String> memoryLoad() throws IOException {
		return InputStreamAssa.of(new FileInputStream(file()), String.class);
	}

	private static FileLazyAssa<String> fileLoad() throws IOException {
		return FileLazyAssa.of(file(), String.class);
	}

	private static File file() {
		return new File("main.assa");
	}
}
