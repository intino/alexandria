package io.intino.alexandria.datalake.file.eventsourcing;

import io.intino.alexandria.datalake.Datalake;
import io.intino.alexandria.event.Event;
import io.intino.alexandria.event.EventStream;

import java.util.Arrays;

public class FileEventPump implements EventPump {
	private final Datalake.EventStore store;

	public FileEventPump(Datalake.EventStore store) {
		this.store = store;
	}

	@Override
	public Reflow reflow(Reflow.Filter filter) {
		return new Reflow() {
			private EventStream is = new EventStream.Merge(tankInputStreams());

			EventStream tankInputStream(Datalake.EventStore.Tank tank) {
				return tank.content(ts -> filter.allow(tank, ts));
			}

			private EventStream[] tankInputStreams() {
				return store.tanks()
						.filter(filter::allow)
						.map(this::tankInputStream)
						.toArray(EventStream[]::new);
			}

			public boolean hasNext() {
				return is.hasNext();
			}

			@Override
			public void next(int blockSize, EventHandler... eventHandlers) {
				new ReflowBlock(is, eventHandlers).reflow(blockSize);
			}

		};
	}


	private static class ReflowBlock {
		private final EventStream is;
		private final EventHandler[] eventHandlers;

		ReflowBlock(EventStream is, EventHandler[] eventHandlers) {
			this.is = is;
			this.eventHandlers = eventHandlers;
		}

		void reflow(int blockSize) {
			terminate(process(blockSize));
		}

		private int process(int messages) {
			int pendingMessages = messages;
			while (is.hasNext() && pendingMessages-- >= 0) {
				Event event = is.next();
				Arrays.stream(eventHandlers).forEach(mh -> mh.handle(event));
			}
			return messages - pendingMessages;
		}

		private void terminate(int reflowedMessages) {
			Arrays.stream(eventHandlers)
					.filter(m -> m instanceof ReflowHandler)
					.map(m -> (ReflowHandler) m)
					.forEach(m -> terminate(m, reflowedMessages));
		}

		private void terminate(ReflowHandler reflowHandler, int reflowedMessages) {
			if (is.hasNext()) reflowHandler.onBlock(reflowedMessages);
			else reflowHandler.onFinish(reflowedMessages);
		}

	}
}
