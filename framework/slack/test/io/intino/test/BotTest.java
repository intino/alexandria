package io.intino.test;

import io.intino.alexandria.slack.Bot;

import javax.websocket.DeploymentException;
import java.io.IOException;

public class BotTest {
	public static void main(String[] args) throws IOException, DeploymentException {
		Bot bot = new Bot("xoxb-315657435732-cilpRyFZvJOxeyQqkg9gYgji") {
		};
		bot.connect();
	}

}
