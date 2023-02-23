package io.intino.alexandria.ztp.tests;

import io.intino.alexandria.ztp.*;
import org.junit.Test;

import java.io.*;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class ZtpBuilder_ {

	public static final String REF_FILE = "/ref.tuples";

	@Test
	public void should_build_file_from_scratch() throws IOException {
		File file = new File("../temp/ztp_from_scratch.ztp");
		file.delete();
		file.getParentFile().mkdirs();
		try(ZtpBuilder builder = new ZtpBuilder(file)) {
			try(TupleReader reader = new TupleReader(getClass().getResourceAsStream(REF_FILE))) {
				while(reader.hasNext()) builder.put(reader.next());
			}
		}
		compareWithRefFile(file);
	}

	@Test
	public void should_append_to_existing_file() throws IOException {
		File file = new File("../temp/ztp_append.ztp");
		file.delete();
		file.getParentFile().mkdirs();
		createFileWithSomeTuplesAlreadyWritten(file);
		try(ZtpBuilder builder = new ZtpBuilder(file)) {
			try(TupleReader reader = new TupleReader(tuplesToAppend())) {
				while(reader.hasNext()) builder.put(reader.next());
			}
		}
		compareWithRefFile(file);
	}

	private void createFileWithSomeTuplesAlreadyWritten(File file) throws IOException {
		try(ZtpWriter writer = new ZtpWriter(Ztp.compressing(new FileOutputStream(file)))) {
			for(String line : tuplesAlreadyPresent()) {
				writer.write(line);
			}
		}
	}

	private void compareWithRefFile(File file) throws IOException {
		int count = 0;
		try(TupleReader ref = new TupleReader(getClass().getResourceAsStream(REF_FILE))) {
			Iterator<Tuple> iterator = ZtpStream.of(file).iterator();
			while(ref.hasNext()) {
				assertEquals(ref.next(), iterator.next());
				++count;
			}
		}
		System.out.println("Tuples are equal! (" + count + ")");
	}

	private static String[] tuplesAlreadyPresent() {
		return new String[]{"VID_PLA_CRI:videometry\tname\tVideometría Playa Los Cristianos\tdashboard\t2023-02-23T09:48:03.447325100Z",
				"VID_PLA_CRI:videometry\tplace\t28.0507,-16.71945\tdashboard\t2023-02-23T09:48:03.447325100Z",
				"VID_PLA_CRI:videometry\tzones\tZON_LC_O2,ZON_LC_O1,ZON_LC_P1\tdashboard\t2023-02-23T09:48:03.447325100Z",
				"VID_PLA_CRI:videometry\tenabled\ttrue\tdashboard\t2023-02-23T09:48:03.447325100Z",
				"",
				"VID_PLA_VIS:videometry\tname\tVideometría Playa Las Vistas\tdashboard\t2023-02-23T09:48:03.447325100Z",
				"VID_PLA_VIS:videometry\tplace\t28.05056,-16.72095\tdashboard\t2023-02-23T09:48:03.447325100Z",
				"VID_PLA_VIS:videometry\tzones\tZON_LV_O4,ZON_LV_O1,ZON_LV_P2,ZON_LV_P1,ZON_LV_O3,ZON_LV_O2\tdashboard\t2023-02-23T09:48:03.447325100Z",
				"VID_PLA_VIS:videometry\tenabled\ttrue\tdashboard\t2023-02-23T09:48:03.447325100Z"};
	}

	private static String tuplesToAppend() {
		return "MET_PLA_VIS:weatherStation\tname\tPlaya de las Vistas\tdashboard\t2023-02-23T09:48:03.447325100Z\n" +
				"MET_PLA_VIS:weatherStation\tplace\t28.05209,-16.72291\tdashboard\t2023-02-23T09:48:03.447325100Z\n" +
				"MET_PLA_VIS:weatherStation\turl\thttps://app.weathercloud.net/d6775102649#current\tdashboard\t2023-02-23T09:48:03.447325100Z\n" +
				"MET_PLA_VIS:weatherStation\tenabled\ttrue\tdashboard\t2023-02-23T09:48:03.447325100Z\n" +
				"\n" +
				"MET_PLA_HON:weatherStation\tname\tPlaya Honda\tdashboard\t2023-02-23T09:48:03.447325100Z\n" +
				"MET_PLA_HON:weatherStation\tplace\t28.06111,-16.73444\tdashboard\t2023-02-23T09:48:03.447325100Z\n" +
				"MET_PLA_HON:weatherStation\turl\thttps://app.weathercloud.net/d7470255965#current\tdashboard\t2023-02-23T09:48:03.447325100Z\n" +
				"MET_PLA_HON:weatherStation\tenabled\ttrue\tdashboard\t2023-02-23T09:48:03.447325100Z";
	}
}
