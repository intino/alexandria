package io.intino.konos.jms.channel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Channel {

	private LocalDateTime cursor;
	private TimeScale cursorStep;
	private MessageReader[] messageReaders;
	private TimeSlot timeSlot;

	public List<Message> read() {
		for (MessageReader messageReader : messageReaders) {
			Message message;
			while ((message = messageReader.read()) != null)
				if (!timeSlot.add(message)) return timeSlot.finish();
		}
		return timeSlot.finish();
	}

	private class TimeSlot {
		private List<Message> current = new ArrayList<>();
		private List<Message> future = new ArrayList<>();

		boolean add(Message message) {
			if (message == null) return false;
			return isIn(message.date()) ?
					current.add(message) :
					future.add(message);
		}

		private boolean isIn(LocalDateTime date) {
			return date.isAfter(cursor);//&& cursor.plus(cursorStep);
		}

		List<Message> finish() {
			List<Message> currentMessages = new ArrayList<>(current);
			this.current = new ArrayList<>(future);
			this.future.clear();
			return currentMessages;
		}
	}


}
