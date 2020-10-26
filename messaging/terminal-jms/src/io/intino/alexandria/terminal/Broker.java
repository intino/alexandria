package io.intino.alexandria.terminal;

import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Broker {
	public static boolean isRunning(String brokerUrl) {
		String[] values = brokerUrl.substring(brokerUrl.indexOf("//") + 2).replace(")", "").split(":");
		return values.length == 2 && isRunning(values[0], Integer.parseInt(values[1]));
	}

	private static boolean isRunning(String address, int port) {
		try {
			try (Socket socket = new Socket()) {
				socket.connect(new InetSocketAddress(address, port), 5000);
			}
			return true;
		} catch (IOException e) {
			Logger.error(e);
			return false;
		}
	}
}
