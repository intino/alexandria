package io.intino.performance;

import io.intino.alexandria.message.FastMessageReader;
import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import io.intino.performance.impl.FastMessageReaderOld;
import io.intino.performance.impl.MessageReaderOld;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, warmups = 0)
@Warmup(iterations = 2)
@Measurement(iterations = 8)
public class MessageReaderBenchmark {
//	Windows 10, i9-10885H 2.40 GHz
//  Benchmark                  Mode  Cnt     Score    Error  Units
//	MessageReaderBenchmark.v0  avgt   10  1616,321 ± 17,921  ms/op
//	MessageReaderBenchmark.v1  avgt   10  1392,100 ± 67,550  ms/op
//	MessageReaderBenchmark.v2  avgt   10   681,445 ± 66,251  ms/op
//	MessageReaderBenchmark.v3  avgt   10   388,133 ± 14,735  ms/op

	private static final byte[] INL = loadInl().getBytes();

	@Benchmark
	public Blackhole v0(Blackhole bk) {
		Iterator<Message> iterator = new MessageReaderOld(new ByteArrayInputStream(INL));
		while(iterator.hasNext()) {
			bk.consume(iterator.next());
		}
		return bk;
	}

	@Benchmark
	public Blackhole v1(Blackhole bk) {
		Iterator<Message> iterator = new MessageReader(new ByteArrayInputStream(INL));
		while(iterator.hasNext()) {
			bk.consume(iterator.next());
		}
		return bk;
	}

	@Benchmark
	public Blackhole v2(Blackhole bk) {
		Iterator<Message> iterator = new FastMessageReaderOld(new ByteArrayInputStream(INL));
		while(iterator.hasNext()) {
			bk.consume(iterator.next());
		}
		return bk;
	}

	@Benchmark
	public Blackhole v3(Blackhole bk) {
		Iterator<Message> iterator = new FastMessageReader(new ByteArrayInputStream(INL));
		while(iterator.hasNext()) {
			bk.consume(iterator.next());
		}
		return bk;
	}

	private static String loadInl() {
		try {
			return Files.readString(new File("C:\\Users\\naits\\Desktop\\MonentiaDev\\alexandria\\temp\\messages.inl").toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

//	public static void main(String[] args) {
//		Iterator<Message> iterator = new FastMessageReader(new ByteArrayInputStream(INL));
//		while(iterator.hasNext()) {
//			Message next = iterator.next();
//			System.out.println(next);
//		}
//	}
}
