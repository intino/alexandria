package io.intino.benchmarks;

import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;
import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.xerial.snappy.SnappyInputStream;
import org.xerial.snappy.SnappyOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, warmups = 0)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
public class CompressionBenchmark {

	private static final File Root = new File("C:\\Users\\naits\\Desktop\\MonentiaDev\\alexandria\\temp\\compressbench");
	private static final byte[] INL = loadInl().getBytes();

	@Benchmark
	public void _1compressSnappy() throws IOException {
		try(OutputStream out = new SnappyOutputStream(new BufferedOutputStream(new FileOutputStream(new File(Root, "test.snappy"))))) {
			ByteArrayInputStream in = new ByteArrayInputStream(INL);
			byte[] buffer = new byte[1024];
			int read;
			while((read = in.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}
		}
	}

	@Benchmark
	public void _2compressLZ4() throws IOException {
		try(OutputStream out = new LZ4BlockOutputStream(new BufferedOutputStream(new FileOutputStream(new File(Root, "test.lz4"))))) {
			ByteArrayInputStream in = new ByteArrayInputStream(INL);
			byte[] buffer = new byte[1024];
			int read;
			while((read = in.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}
		}
	}

	@Benchmark
	public void _3compressZstd() throws IOException {
		try(OutputStream out = new ZstdOutputStream(new BufferedOutputStream(new FileOutputStream(new File(Root, "test.zstd"))))) {
			ByteArrayInputStream in = new ByteArrayInputStream(INL);
			byte[] buffer = new byte[1024];
			int read;
			while((read = in.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}
		}
	}

	@Benchmark
	public void _4uncompressSnappy(Blackhole blackhole) throws IOException {
		try(InputStream in = new SnappyInputStream(new BufferedInputStream(new FileInputStream(new File(Root, "test.snappy"))))) {
			byte[] buffer = new byte[1024];
			while(in.read(buffer) > 0) {
				blackhole.consume(buffer);
			}
		}
	}

	@Benchmark
	public void _5uncompressLZ4(Blackhole blackhole) throws IOException {
		try(InputStream in = new LZ4BlockInputStream(new BufferedInputStream(new FileInputStream(new File(Root, "test.lz4"))))) {
			byte[] buffer = new byte[1024];
			while(in.read(buffer) > 0) {
				blackhole.consume(buffer);
			}
		}
	}

	@Benchmark
	public void _6uncompressZstd(Blackhole blackhole) throws IOException {
		try(InputStream in = new ZstdInputStream(new BufferedInputStream(new FileInputStream(new File(Root, "test.zstd"))))) {
			byte[] buffer = new byte[1024];
			while(in.read(buffer) > 0) {
				blackhole.consume(buffer);
			}
		}
	}

	private static String loadInl() {
		try {
			return Files.readString(new File(Root, "test.tsv").toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
