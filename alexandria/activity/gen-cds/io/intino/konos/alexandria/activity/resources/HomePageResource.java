package io.intino.konos.alexandria.activity.resources;

import io.intino.konos.alexandria.activity.ActivityBox;
import io.intino.konos.alexandria.activity.AlexandriaActivityBox;
import io.intino.konos.alexandria.activity.actions.HomePageAction;
import io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.exceptions.AlexandriaException;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class HomePageResource extends io.intino.konos.alexandria.activity.spark.resources.Resource {
	private final ActivityBox box;

	public HomePageResource(ActivityBox box, io.intino.konos.alexandria.activity.spark.ActivitySparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
		super(manager, notifierProvider);
		this.box = box;
	}

	@Override
	public void execute() throws AlexandriaException {
		super.execute();
		render();
	}

	private void render() {
		String clientId = UUID.randomUUID().toString();
		HomePageAction action = new HomePageAction();
		action.session = manager.currentSession();
		action.session.whenLogin(new Function<String, String>() {
			@Override
			public String apply(String baseUrl) {
				return HomePageResource.this.authenticate(baseUrl);
			}
		});
		action.session.whenLogout(b -> logout());
		action.box = box;
		action.clientId = clientId;

		manager.pushService().onOpen(client -> {
			if (!client.id().equals(action.clientId))
				return false;

			if (client.soul() != null)
				return false;

			io.intino.konos.alexandria.activity.displays.Soul soul = action.prepareSoul(client);
			soul.addRegisterDisplayListener(display -> {
				display.inject(notifier(action.session, client, display));
				display.inject(action.session);
				display.inject(soul);
				display.inject(() -> soul);
			});
			client.soul(soul);
			box.registerSoul(clientId, soul);

			return true;
		});

		manager.pushService().onClose(clientId).execute(new Consumer<io.intino.konos.alexandria.activity.services.push.ActivityClient>() {
			@Override
			public void accept(io.intino.konos.alexandria.activity.services.push.ActivityClient client) {
				box.unRegisterSoul(client.id());
				manager.unRegister(client);
			}
		});

		manager.write(action.execute());
	}

}