package io.intino.test;

import io.intino.alexandria.logger4j.Logger;
import io.intino.alexandria.slack.Bot;
import org.apache.log4j.Level;

import javax.websocket.DeploymentException;
import java.io.IOException;

public class BotTest {
	public static void main(String[] args) throws IOException, DeploymentException {
		Logger.init(Level.TRACE);
		Bot bot = new Bot("xoxb-315657435732-cilpRyFZvJOxeyQqkg9gYgji") {
		};
		bot.connect();
	}

}
