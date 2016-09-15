package teseo.jms;

import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.Message;

public class MessageCreator {

	public Message textMessage() {
		return new ActiveMQTextMessage();
	}

	public Message objectMessage() {
		return new ActiveMQObjectMessage();
	}

	public Message byteMessage() {
		return new ActiveMQBytesMessage();
	}
}
