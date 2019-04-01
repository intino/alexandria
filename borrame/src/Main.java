import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RFileOutputStream;

import java.io.*;

public class Main {
	public static void main(String[] args) throws REngineException, REXPMismatchException, FileNotFoundException {
		RConnection connection = new RConnection();
		FileInputStream womenStream = new FileInputStream(new File("/Users/mcaballero/women.rds"));

		put(connection, womenStream, "women.rds");
		REXP women = connection.eval("women <- readRDS(\"women.rds\")");
		System.out.println(women.asList().names);
		connection.close();

		connection = new RConnection();
		connection.assign("women", women);
		REXP rexp = connection.parseAndEval("women");
		System.out.println(rexp.asList().names);
		connection.close();
	}

	public static void put(RConnection connection, InputStream content, String name) {
		try {
			RFileOutputStream serverStream = connection.createFile(name);
			copy(content, serverStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void copy(InputStream clientStream, OutputStream serverStream) throws IOException {
		byte [] buffer = new byte[8192];

		int c = clientStream.read(buffer);
		while(c >= 0) {
			serverStream.write(buffer,0, c);
			c = clientStream.read(buffer);
		}

		serverStream.close();
		clientStream.close();
	}

}
