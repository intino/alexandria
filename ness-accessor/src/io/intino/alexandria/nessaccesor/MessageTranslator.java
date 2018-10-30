package io.intino.alexandria.nessaccesor;


import io.intino.alexandria.jms.MessageFactory;
import io.intino.ness.inl.Message;
import io.intino.ness.inl.Message.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class MessageTranslator {
	private static Logger logger = LoggerFactory.getLogger(MessageTranslator.class);

	private static String ATTACHMENT_IDS = "__attachment-ids__";
	private static String ATTACHMENT_SIZES = "__attachment-sizes__";
	private static String MESSAGE = "__text-message__";

	public static Message toInlMessage(javax.jms.Message message) {
		try {
			if (message instanceof BytesMessage) {
				final Message result = Message.load(message.getStringProperty(MESSAGE));
				Map<String, Integer> attachments = loadAttachmentProperties(message);
				for (String id : attachments.keySet()) {
					byte[] array = new byte[attachments.get(id)];
					((BytesMessage) message).readBytes(array, attachments.get(id));
					result.attachment(id).data(array);
				}
				return result;
			} else return Message.load(((TextMessage) message).getText());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public static javax.jms.Message fromInlMessage(Message message) {
		List<Attachment> attachments = getAttachments(message);
		if (!attachments.isEmpty()) {
			javax.jms.Message result = MessageFactory.byteMessage();
			addAttachments((BytesMessage) result, attachments);
			addTextMessage(message, (BytesMessage) result);
			return result;
		} else return MessageFactory.createMessageFor(message.toString());
	}

	private static List<Attachment> getAttachments(Message message) {
		List<Attachment> attachments = new ArrayList<>(message.attachments());
		message.components().stream().map(MessageTranslator::getAttachments).forEach(attachments::addAll);
		return attachments;
	}

	private static Map<String, Integer> loadAttachmentProperties(javax.jms.Message message) {
		try {
			final String[] ids = message.getStringProperty(ATTACHMENT_IDS).split(",");
			final String[] sizes = message.getStringProperty(ATTACHMENT_SIZES).split(",");
			return IntStream.range(0, ids.length).boxed().collect(toMap(i -> ids[i], i -> Integer.parseInt(sizes[i])));
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			return Collections.emptyMap();
		}
	}

	private static void addTextMessage(Message message, BytesMessage result) {
		try {
			result.setStringProperty(MESSAGE, message.toString());
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static void addAttachments(BytesMessage bytesMessage, List<Attachment> attachments) {
		try {
			bytesMessage.setStringProperty(ATTACHMENT_IDS, String.join(",", attachments.stream().map(Attachment::id).toArray(String[]::new)));
			bytesMessage.setStringProperty(ATTACHMENT_SIZES, String.join(",", attachments.stream().map(a -> a.data().length + "").toArray(String[]::new)));
			for (Attachment attachment : attachments) bytesMessage.writeBytes(attachment.data());
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
