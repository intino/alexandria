package io.intino.pandora.slack;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ullink.slack.simpleslackapi.impl.SlackSessionFactory.createWebSocketSlackSession;

public abstract class Bot {

    private final String token;
    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, String> helps = new HashMap<>();
    private SlackSession session;


    public Bot(String token) {
        this.token = token;
        this.add("help", "Show this help", args -> showHelp());
    }

    private String showHelp() {
        String help = "";
        for (String key : helps.keySet())
            help += key + ": " + helps.get(key)  + "\n";
        return help;
    }

    public void execute() throws IOException {
        session = createWebSocketSlackSession(token);
        session.addMessagePostedListener(this::talk);
        session.connect();
    }

    private void talk(SlackMessagePosted message, SlackSession session) {
        if (message.getSender().isBot()) return;
        String[] content = message.getMessageContent().split(" ");
        Command command = commands.containsKey(content[0]) ? commands.get(content[0]) : args -> "Command not found";
        session.sendMessage(message.getChannel(), command.execute(content));
    }

    protected void add(String name, String help, Command command) {
        commands.put(name, command);
        helps.put(name, help);
    }

    protected void send(String notificationChannel, String message) {
        SlackChannel channel = session.findChannelByName(notificationChannel);
        if (channel == null) return;
        session.sendMessage(channel, message);
    }

    interface Command {
        String execute(String... args);
    }


}
