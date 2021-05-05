package io.intino.test;

import io.intino.alexandria.message.Message;
import io.intino.alexandria.message.MessageReader;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MessageReader_ {


	@Test
	@Ignore
	public void checkMessage() {
		String message = "[SEVERE]\n" +
				"\tts: 2021-04-23T08:20:15.056773Z\n" +
				"\tsource: io.intino.magritte.framework.LayerFactory:create\n" +
				"\tmessage: Concept AbstractAcquisition$Device hasn't layer registered. Node Assets#Assets_3962_0_0696810257 won't have it\n";
		Message next = new MessageReader(message).next();
		Assert.assertNotNull(next);
	}

	@Test
	public void should_read_empty_content() {
		MessageReader messages = new MessageReader("");
		assertThat(messages.hasNext()).isFalse();
		assertThat(messages.next()).isNull();
	}

	@Test
	public void should_read_content_with_complex_message() {
		MessageReader messages = new MessageReader(this.getClass().getResourceAsStream("/test1.inl"));
		Message next = messages.next();
		assertThat(next).isNotNull();
		assertThat(messages.hasNext()).isFalse();
	}

	@Test
	public void should_read_content_with_embed_message() {
		MessageReader messages = new MessageReader(this.getClass().getResourceAsStream("/test2.inl"));
		Message next = messages.next();
		assertThat(next).isNotNull();
		assertThat(messages.hasNext()).isFalse();
		String value = next.get("value").asString();
		messages = new MessageReader(value);
		messages.iterator().forEachRemaining(System.out::println);
	}

	@Test
	public void should_read_messages_in_a_class_with_parent() {
		String inl = "[Teacher]\n" +
				"name: Jose\n" +
				"money: 50.0\n" +
				"birthDate: 1984-11-01T22:34:25Z\n" +
				"university: ULPGC\n" +
				"\n" +
				"[Teacher.Country]\n" +
				"name: Spain\n" +
				"\n" +
				"[Teacher]\n" +
				"name: Juan\n" +
				"money: 40.0\n" +
				"birthDate: 1978-04-02T00:00:00Z\n" +
				"university: ULL\n" +
				"\n" +
				"[Teacher.Country]\n" +
				"name: France\n" +
				"\n" +
				"[Teacher.Country]\n" +
				"name: Germany\n";
		MessageReader messages = new MessageReader(inl);

		assertThat(messages.hasNext()).isTrue();
		Message m1 = messages.next();
		assertThat(m1.get("name").as(String.class)).isEqualTo("Jose");
		assertThat(m1.get("money").as(Double.class)).isEqualTo(50.0);
		assertThat(m1.get("birthDate").as(Instant.class)).isEqualTo(instant(1984, 11, 1, 22, 34, 25));
		assertThat(m1.components().size()).isEqualTo(1);
		assertThat(m1.components("country").size()).isEqualTo(1);
		assertThat(m1.components("country").get(0).get("name").as(String.class)).isEqualTo("Spain");

		assertThat(messages.hasNext()).isTrue();
		Message m2 = messages.next();
		assertThat(m2.get("name").as(String.class)).isEqualTo("Juan");
		assertThat(m2.get("money").as(Double.class)).isEqualTo(40.0);
		assertThat(m2.get("birthDate").as(Instant.class)).isEqualTo(instant(1978, 4, 2, 0, 0, 0));
		assertThat(m2.components().size()).isEqualTo(2);
		assertThat(m2.components("country").size()).isEqualTo(2);
		assertThat(m2.components("country").get(0).get("name").as(String.class)).isEqualTo("France");
		assertThat(m2.components("country").get(1).get("name").as(String.class)).isEqualTo("Germany");

		assertThat(messages.hasNext()).isFalse();
		assertThat(messages.next()).isNull();

		assertThat(m1.toString() + "\n" + m2.toString()).isEqualTo(inl);

	}

	@Test
	@Ignore
	public void should_read_messages_with_old_format() {
		String inl = "[Dialog]\n" +
				"instant = 2019-01-14T13:34:09.742Z\n" +
				"opinion = Satisfied\n" +
				"cancelled = false\n" +
				"contactSet = false\n" +
				"contactData = \n" +
				"wantsToBeContacted = false\n" +
				"area = MockChainHotelReception\n" +
				"eventId = \n" +
				"eventLabel = \n" +
				"issueId = \n" +
				"touchCounter = 1\n" +
				"sensorId = 3C15C2CBFF020000\n" +
				"apkVersion = 3.0.21\n" +
				"fingerSizes = 0\n" +
				"hearts = 1\n";
		MessageReader messages = new MessageReader(inl);
		Message message = messages.next();
		assertThat(message.contains("issueId")).isFalse();
		assertThat(message.get("wantsToBeContacted").as(Boolean.class)).isFalse();
		assertThat(message.get("hearts").as(Integer.class)).isEqualTo(1);
		assertThat(messages.hasNext()).isFalse();
	}


	@Test
	public void should_read_message_with_multi_lines_and_many_components() {
		String inl = "[Teacher]\n" +
				"name: Jose\u0001Hernandez\n" +
				"money: 50.0\n" +
				"birthDate: 2016-10-04T20:10:11Z\n" +
				"university: ULPGC\n" +
				"\n" +
				"[Teacher.Country]\n" +
				"name: Spain\n" +
				"\n" +
				"[Teacher.Phone]\n" +
				"value: +150512101402\n" +
				"\n" +
				"[Teacher.Phone.Country]\n" +
				"name: USA\n" +
				"\n" +
				"[Teacher.Phone]\n" +
				"value: +521005101402\n" +
				"\n" +
				"[Teacher.Phone.Country]\n" +
				"name: Mexico\n";

		MessageReader messages = new MessageReader(inl);
		assertThat(messages.hasNext()).isTrue();
		Message message = messages.next();
		assertThat(message.toString()).isEqualTo(inl);
		assertThat(message.get("name").asString().toCharArray()).contains((char) 1);
		assertThat(message.get("name").as(String[].class)).hasSize(2);
		assertThat(message.components().size()).isEqualTo(3);
		assertThat(message.components("Phone").get(0).components().size()).isEqualTo(1);
		assertThat(messages.hasNext()).isFalse();
		assertThat(messages.next()).isNull();
	}

	@Test
	public void should_read_messages_with_array_attributes() {
		String inl = "[Menu]\n" +
				"meals: " + "Soup\u0001" + "Lobster\u0001" + "Mussels\u0001" + "Cake\n" +
				"prices: " + "5.0\u0001" + "24.5\u0001" + "8.0\u0001" + "7.0\n" +
				"availability: " + "true\u0001" + "false\n";

		MessageReader messages = new MessageReader(inl);
		assertThat(messages.hasNext()).isTrue();
		Message message = messages.next();
		assertThat(message.toString()).isEqualTo(inl);
		assertThat(message.get("meals").as(String[].class)).isEqualTo(new String[]{"Soup", "Lobster", "Mussels", "Cake"});
		assertThat(message.get("prices").as(Double[].class)).isEqualTo(new Double[]{5., 24.5, 8., 7.});
		assertThat(message.get("availability").as(Boolean[].class)).isEqualTo(new Boolean[]{true, false});
		assertThat(messages.hasNext()).isFalse();
		assertThat(messages.next()).isNull();
	}


	@Test
	public void should_read_message_with_multiline_attribute1() {
		String inl = "[Contactos.Contacto]\n" +
				"nombre: LIC RAUL ALFONSO CABALLERO CONTRERAS\n" +
				"telefonos:\n" +
				"\t01\n" +
				"\t0177\n" +
				"\t01771\n" +
				"\t01771202\n" +
				"\t017712026837\n" +
				"cargo: CONTRALOR INTERNO\n" +
				"tipo: Comercial\n" +
				"email: raul.caballero@hidalgo.gob\n";
		Message message = new MessageReader(inl).next();
		String telefonos = message.get("telefonos").asString();
		System.out.println(telefonos);
	}

	@Test
	public void should_read_message_with_multiline_attribut2() {
		String inl = "[ERROR]\n" +
				"ts: 2020-05-05T19:24:32.533342Z\n" +
				"source: io.intino.alexandria.jms.TopicConsumer:close:36\n" +
				"message:\n" +
				"\tjavax.jms.IllegalStateException: The Session is closed\n" +
				"\t\tat org.apache.activemq.ActiveMQSession.checkClosed(ActiveMQSession.java:771)\n" +
				"\t\tat java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)\n" +
				"\t\tat java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305)\n" +
				"\t\tat java.base/java.lang.Thread.run(Thread.java:834)\n";

		Message message = new MessageReader(inl).next();
		assertThat(message.get("ts").as(Instant.class)).isEqualTo(Instant.parse("2020-05-05T19:24:32.533342Z"));
		assertThat(message.get("source").as(String.class)).isEqualTo("io.intino.alexandria.jms.TopicConsumer:close:36");
		assertThat(message.get("message").as(String.class)).isEqualTo("javax.jms.IllegalStateException: The Session is closed\n" +
				"\tat org.apache.activemq.ActiveMQSession.checkClosed(ActiveMQSession.java:771)\n" +
				"\tat java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)\n" +
				"\tat java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:305)\n" +
				"\tat java.base/java.lang.Thread.run(Thread.java:834)");
	}

	@Test
	public void should_read_embeded_message() {
		String inl = "[ProcessLog]\n" +
				"ts: 2020-06-18T13:30:58.349496Z\n" +
				"serverId: deploy-alpgc-smartbeach-dev\n" +
				"id: com.monentia.smartbeach:control\n" +
				"value:\n" +
				"\t[INFO]\n" +
				"\tts: 2020-06-18T13:30:58.329749Z\n" +
				"\tsource: io.intino.alexandria.terminal.JmsConnector$1:transportResumed:306\n" +
				"\tmessage: Connection with Data Hub established!\n";
		Message message = new MessageReader(inl).next();
		assertThat(message).isNotNull();
		assertThat(message.get("value").asString()).isNotNull();
	}

	@Test
	public void should_read_log_message() {
		String inl = "[ERROR]\n" +
				"ts: 2020-06-19T11:00:52.105720Z\n" +
				"source: spark.http.matching.GeneralError:modify\n" +
				"message:\n" +
				"\tCaused by:\n" +
				"\tjava.lang.NullPointerException\n" +
				"\t\tat com.monentia.smartbeach.control.box.actions.GetSensorAction.execute(GetSensorAction.java:19)\n" +
				"\t\tat com.monentia.smartbeach.control.box.rest.resources.GetSensorResource.execute(GetSensorResource.java:25)\n" +
				"\t\tat com.monentia.smartbeach.control.box.ControlServiceService.lambda$setup$4(ControlServiceService.java:14)\n" +
				"\t\tat io.intino.alexandria.http.spark.SparkRouter.execute(SparkRouter.java:97)\n" +
				"\t\tat io.intino.alexandria.http.spark.SparkRouter.lambda$get$1(SparkRouter.java:33)\n" +
				"\t\tat spark.RouteImpl$1.handle(RouteImpl.java:72)\n" +
				"\t\tat spark.http.matching.Routes.execute(Routes.java:61)\n" +
				"\t\tat spark.http.matching.MatcherFilter.doFilter(MatcherFilter.java:130)\n" +
				"\t\tat spark.embeddedserver.jetty.JettyHandler.doHandle(JettyHandler.java:50)\n" +
				"\t\tat org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:1568)\n" +
				"\t\tat org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:141)\n" +
				"\t\tat org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:132)\n" +
				"\t\tat org.eclipse.jetty.server.Server.handle(Server.java:530)\n" +
				"\t\tat org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:347)\n" +
				"\t\tat org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:256)\n" +
				"\t\tat org.eclipse.jetty.io.AbstractConnection$ReadCallback.succeeded(AbstractConnection.java:279)\n" +
				"\t\tat org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:102)\n" +
				"\t\tat org.eclipse.jetty.io.ChannelEndPoint$2.run(ChannelEndPoint.java:124)\n" +
				"\t\tat org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.doProduce(EatWhatYouKill.java:247)\n" +
				"\t\tat org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.produce(EatWhatYouKill.java:140)\n" +
				"\t\tat org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.run(EatWhatYouKill.java:131)\n" +
				"\t\tat org.eclipse.jetty.util.thread.ReservedThreadExecutor$ReservedThread.run(ReservedThreadExecutor.java:382)\n" +
				"\t\tat org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:708)\n" +
				"\t\tat org.eclipse.jetty.util.thread.QueuedThreadPool$2.run(QueuedThreadPool.java:626)\n" +
				"\t\tat java.base/java.lang.Thread.run(Thread.java:834)";
		Message message = new MessageReader(inl).next();
		assertThat(message).isNotNull();
		assertThat(message.get("message").asString()).isNotNull();
	}

	private Instant instant(int y, int m, int d, int h, int mn, int s) {
		return LocalDateTime.of(y, m, d, h, mn, s).atZone(ZoneId.of("UTC")).toInstant();
	}

}
