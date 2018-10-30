package io.intino.alexandria.ness.setstore.file;

import io.intino.alexandria.ness.Scale;
import io.intino.alexandria.ness.setstore.session.SessionSealer;
import io.intino.alexandria.nessaccesor.NessAccessor;
import io.intino.sezzet.operators.SetStream;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

public class FSSetStore implements NessAccessor.SetStore {

	public static final String SessionExt = ".session";
	public static final String InfoExt = ".info";
	public static final String SetExt = ".set";
	public static final String TempExt = ".temp";
	public static final String PartExt = ".part";

	private final File store;
	private Scale scale;

	public FSSetStore(File store, Scale scale) {
		store.mkdirs();
		this.store = store;
		this.scale = scale;
	}

	public static void write(SetStream stream, File file) throws IOException {
		file.getParentFile().mkdirs();
		DataOutputStream dataStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		while (stream.hasNext()) dataStream.writeLong(stream.next());
		dataStream.close();
	}

	@Override
	public Scale scale() {
		return scale;
	}


	@Override
	public List<Tank> tanks() {
		File[] tanks = store.listFiles((dir, name) -> !name.equals("stage"));
		if (tanks == null) return emptyList();
		return Arrays.stream(tanks).map(directory -> new FSSetTank(directory, scale)).collect(toList());
	}

	@Override
	public Tank tank(String name) {
		File[] tanks = store.listFiles((dir, n) -> !n.equals("stage"));
		if (tanks == null) return null;
		File file = Arrays.stream(tanks).filter(f -> f.getName().equals(name)).findFirst().orElse(null);
		return file == null ? null : new FSSetTank(file, scale);
	}

	@Override
	public void storeSession(InputStream inputStream) {
		try {
			FileUtils.copyInputStreamToFile(inputStream, new File(stageFolder(), randomUUID().toString() + SessionExt));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void seal() {
		SessionSealer.seal(stageFolder());
	}

	private File stageFolder() {
		return new File(store, "stage/");
	}

}
