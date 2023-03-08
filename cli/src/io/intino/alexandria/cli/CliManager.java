package io.intino.alexandria.cli;

import io.intino.alexandria.Base64;
import io.intino.alexandria.cli.command.MessageProperties;
import io.intino.alexandria.cli.response.Attachment;
import io.intino.alexandria.cli.response.MultiLine;
import io.intino.alexandria.cli.response.Question;
import io.intino.alexandria.cli.response.Text;
import io.intino.alexandria.cli.schemas.BotResponse;
import io.intino.alexandria.cli.schemas.BotTalk;
import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.exceptions.BadRequest;
import io.intino.alexandria.logger.Logger;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.TimeZone;

public class CliManager {
	public Cli cli;
	public BotTalk talk;

	public CliManager(Cli cli, BotTalk talk) {
		this.cli = cli;
		this.talk = talk;
	}

	public BotResponse execute(String token) {
		Response response = query(token);
		if (response instanceof Text)
			return new BotResponse().type(BotResponse.Type.text).raw(((Text) response).content);
		if (response instanceof Attachment) {
			return new BotResponse()
					.title(((Attachment) response).getTitle())
					.fileName(((Attachment) response).getFileName())
					.type(BotResponse.Type.valueOf(((Attachment) response).getType().name().toLowerCase()))
					.raw(encode((Attachment) response));
		}
		if (response instanceof Question) {
			Question r = (Question) response;
			return new BotResponse()
					.type(BotResponse.Type.question)
					.title(r.question)
					.options(r.options);
		}
		if (response instanceof MultiLine) {
			MultiLine r = (MultiLine) response;
			return new BotResponse()
					.type(BotResponse.Type.multiline)
					.raw(r.toString());
		}
		return new BotResponse().type(BotResponse.Type.text).raw("Impossible to send file to this environment");
	}

	private Response query(String token) {
		try {
			return cli.talk(token, talk.conversation(), properties(token));
		} catch (Exception e) {
			Logger.error(e);
			return new Text("Error processing command");
		}
	}

	private String encode(Attachment response) {
		try {
			return Base64.encode(response.getInputStream().readAllBytes());
		} catch (IOException e) {
			return "";
		}
	}

	private MessageProperties properties(String token) {
		Context context = cli.loadContext(token);

		return new MessageProperties() {
			@Override
			public String channel() {
				return "rest";
			}

			@Override
			public String token() {
				return token;
			}

			@Override
			public String ts() {
				return Instant.now().toString();
			}

			@Override
			public String timeZone() {
				return talk.timeZone() == null || talk.timeZone().isEmpty() ? TimeZone.getDefault().getID() : talk.timeZone();
			}

			@Override
			public Context context() {
				return context;
			}

			@Override
			public String rawMessage() {
				return talk.conversation();
			}

			@Override
			public Attachment attachment() {
				return null;
			}
		};
	}

	public void onMalformedRequest(Throwable e) throws AlexandriaException {
		throw new BadRequest("Malformed request");
	}
}
