package io.intino.alexandria.ness.setstore.file;

import io.intino.alexandria.TripleStore;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.SetFilter;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.Tank;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.Timetag;
import io.intino.alexandria.nessaccesor.NessAccessor.SetStore.Variable;
import io.intino.sezzet.operators.FileReader;
import io.intino.sezzet.operators.LongStream;
import io.intino.sezzet.operators.SetStream;
import io.intino.sezzet.operators.Union;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.intino.alexandria.ness.setstore.file.FSSetStore.*;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class FSSetTub implements Tank.Tub {
	public static final Logger LOGGER = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	private static final int MAX_ENTRIES = 1000;
	static Map<String, TripleStore> tripleStoreMap = new LinkedHashMap<String, TripleStore>(MAX_ENTRIES + 1, .75F, true) {
		public boolean removeEldestEntry(Map.Entry eldest) {
			return size() > MAX_ENTRIES;
		}
	};
	private final File tub;
	private final Timetag timetag;
	private final FSSetTank tank;

	public FSSetTub(File tub, Timetag timetag, FSSetTank tank) {
		this.tub = tub;
		this.timetag = timetag;
		this.tank = tank;
	}

	public String name() {
		return timetag.toString();
	}

	public Tank tank() {
		return this.tank;
	}

	@Override
	public Timetag timetag() {
		return timetag;
	}

	@Override
	public List<Set> sets() {
		File[] files = tub.listFiles((f, n) -> n.endsWith(SetExt));
		return files != null ? stream(files).sorted().map(this::newFileSet).collect(toList()) : emptyList();
	}

	@Override
	public List<Set> sets(SetFilter filter) {
		return sets().stream().filter(filter).collect(toList());
	}

	@Override
	public Set set(String set) {
		File file = new File(tub, set + SetExt);
		return file.exists() ? newFileSet(file) : null;
	}

	private FileSet newFileSet(File file) {
		return new FileSet(file, this);
	}

	public static class FileSet implements Set {
		private final File file;
		private final FSSetTub tub;
		private TripleStore variables;

		public FileSet(File file, FSSetTub tub) {
			this.file = file;
			this.tub = tub;
		}

		public String name() {
			return file.getName().replace(SetExt, "");
		}

		@Override
		public int size() {
			return (int) (file.length() / 8);
		}

		@Override
		public Tank.Tub tub() {
			return this.tub;
		}

		@Override
		public SetStream content() {
			return new io.intino.sezzet.operators.FileReader(file);
		}

		private TripleStore triplestore() {
			return this.variables != null ? variables : (variables = createTriplestore());
		}

		public InputStream inputStream() {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				LOGGER.error(e.getMessage(), e);
				return null;
			}
		}

		public List<Variable> variables() {
			if (variables == null) return null;
			return variables.matches(name()).map(a -> new Variable(a[1], a[2])).collect(toList());
		}

		public Variable variable(String name) {
			if (variables == null) return null;
			List<String[]> list = variables.matches(name(), name).collect(Collectors.toList());
			return !list.isEmpty() ? new Variable(list.get(0)[1], list.get(0)[2]) : null;
		}

		@Override
		public void define(Variable variable) {
			try {
				triplestore().put(name(), variable.name, variable.value);
				triplestore().save();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		@Override
		public void put(long... ids) {
			writeStream(file.exists() ? new Union(asList(new io.intino.sezzet.operators.FileReader(file), new LongStream(ids))) : new LongStream(ids));
		}

		@Override
		public void put(List<Long> ids) {
			writeStream(file.exists() ? new Union(asList(new FileReader(file), new LongStream(ids))) : new LongStream(ids));
		}

		private void writeStream(SetStream toWrite) {
			try {
				File tempFile = new File(file + TempExt);
				write(toWrite, tempFile);
				Files.move(tempFile.toPath(), file.toPath());
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		public void put(InputStream stream) {
			File tank = file.getParentFile();
			tank.mkdirs();
			try {
				Files.write(file.toPath(), IOUtils.readFully(stream, -1, true), APPEND, CREATE);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			tripleStoreMap.remove(tank + tub.name());
		}

		private TripleStore createTriplestore() {
			FSSetTank tank = tub.tank;
			String id = tank.name() + tub.timetag.toString();
			if (!tripleStoreMap.containsKey(id)) {
				File file = new File(this.file.getParentFile(), tank.name() + InfoExt);
				if (!file.exists()) newFile(file);
				tripleStoreMap.put(id, new TripleStore(file));
			}
			return tripleStoreMap.get(id);
		}

		private void newFile(File file) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

}
