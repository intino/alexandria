package io.intino.test;

import io.intino.alexandria.inl.Message;
import io.intino.alexandria.inl.MessageReader;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MessageReader_ {

	@Test
	public void should_read_empty_content() {
		MessageReader messages = new MessageReader("");
		assertThat(messages.hasNext()).isFalse();
		assertThat(messages.next()).isNull();
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
				"name:\n\tJose\n\tHernandez\n" +
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
		assertThat(message.components().size()).isEqualTo(3);
		assertThat(message.components("Phone").get(0).components().size()).isEqualTo(1);
		assertThat(messages.hasNext()).isFalse();
		assertThat(messages.next()).isNull();
	}

	@Test
	public void should_read_messages_with_array_attributes() {
		String inl = "[Menu]\n" +
				"meals:\n" +
				"\tSoup\n" +
				"\tLobster\n" +
				"\tMussels\n" +
				"\tCake\n" +
				"prices:\n" +
				"\t5.0\n" +
				"\t24.5\n" +
				"\t8.0\n" +
				"\t7.0\n" +
				"availability:\n" +
				"\ttrue\n" +
				"\tfalse\n";

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
	public void should_read_message_in_old_format() {
		String inl = "[Teacher]\n" +
				"name = Jose\n" +
				"money=50.0\n" +
				"birthDate= 2016-10-04T20:10:12Z\n" +
				"university = ULPGC\n" +
				"\n" +
				"[Teacher.Country]\n" +
				"name=Spain\n" +
				"continent=\n";
		Message message = new MessageReader(inl).next();
		assertThat(message.get("name").as(String.class)).isEqualTo("Jose");
		assertThat(message.get("money").as(Double.class)).isEqualTo(50.0);
		assertThat(message.get("birthDate").as(Instant.class)).isEqualTo(instant(2016, 10, 4, 20, 10, 12));
		assertThat(message.get("university").as(String.class)).isEqualTo("ULPGC");
		assertThat(message.components("country").get(0).get("name").as(String.class)).isEqualTo("Spain");
		assertThat(message.components("country").get(0).get("continent").as(String.class)).isNull();
	}

	@Test
	public void should_read_messages_with_attachments() {
		String inl = "[Document]\n" +
				"name: my attachment\n" +
				"file1: photo.png@002eb31f-f3b3-4ba2-adfa-d758c4a55abe\n" +
				"file2:\n" +
				"\tcv.pdf@885d35f3-2811-42c2-a202-b6a7e4b03465\n" +
				"\txx.png@b112828f-d7d0-4694-9880-5657f570ee04\n" +
				"\tyy.png@2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8\n" +
				"\n" +
				"[&]\n" +
				"002eb31f-f3b3-4ba2-adfa-d758c4a55abe:744\nTG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4gVXQgc2VtcGVyIG1ldHVzIG1hbGVzdWFkYSBlbGl0IHZlc3RpYnVsdW0gZGFwaWJ1cy4gVXQgcHJldGl1bSBtYWxlc3VhZGEgc2VtcGVyLiBTZWQgdmVsIG9kaW8gdmVzdGlidWx1bSwgcnV0cnVtIHF1YW0gZXQsIHBvcnRhIGFudGUuIE51bGxhbSBmcmluZ2lsbGEgbmliaCBhdCBudW5jIGNvbnNlcXVhdCBpbXBlcmRpZXQuIFBoYXNlbGx1cyBzZWQgcGVsbGVudGVzcXVlIG51bGxhLiBQcmFlc2VudCBmYWNpbGlzaXMgbGVjdHVzIGVmZmljaXR1ciwgYmliZW5kdW0gZXggZWdldCwgdmFyaXVzIG5pYmguIEFsaXF1YW0gdHJpc3RpcXVlIGVuaW0gZXQgZmV1Z2lhdCBlZ2VzdGFzLiBOdWxsYW0gbm9uIHZlbGl0IHBvcnRhLCB2aXZlcnJhIG1hZ25hIHNlZCwgdWx0cmljZXMgbWkuIERvbmVjIGV1IGxlY3R1cyBhdWN0b3IsIGltcGVyZGlldCBtZXR1cyBxdWlzLCBibGFuZGl0IGR1aS4gQWVuZWFuIHZpdmVycmEganVzdG8gZmVsaXMsIGlkIGxhb3JlZXQgcHVydXMgdGluY2lkdW50IGEu\n\n" +
				"885d35f3-2811-42c2-a202-b6a7e4b03465:928\nTWF1cmlzIG9ybmFyZSBuaWJoIGFjIGxpYmVybyBwbGFjZXJhdCwgZWdldCBlbGVpZmVuZCByaXN1cyB1bGxhbWNvcnBlci4gTWFlY2VuYXMgcG9zdWVyZSBjb21tb2RvIGR1aSwgdml0YWUgZWxlaWZlbmQgZXN0LiBEdWlzIG5vbiBhdWd1ZSBuZWMgaXBzdW0gY3Vyc3VzIHNhZ2l0dGlzIHF1aXMgdml0YWUgbGVvLiBOdW5jIGV1IHBoYXJldHJhIGVuaW0uIFN1c3BlbmRpc3NlIHBvdGVudGkuIFBoYXNlbGx1cyBudW5jIGVuaW0sIGJsYW5kaXQgdmVsIGxlbyBhYywgYWxpcXVhbSBsdWN0dXMgZXJvcy4gU2VkIGV0IHF1YW0gZXVpc21vZCwgZnJpbmdpbGxhIGR1aSBldSwgcnV0cnVtIGxhY3VzLiBDdXJhYml0dXIgbGFjaW5pYSB2dWxwdXRhdGUgdG9ydG9yIHZpdGFlIHNlbXBlci4gVmVzdGlidWx1bSBhbnRlIGlwc3VtIHByaW1pcyBpbiBmYXVjaWJ1cyBvcmNpIGx1Y3R1cyBldCB1bHRyaWNlcyBwb3N1ZXJlIGN1YmlsaWEgQ3VyYWU7IFV0IG9ybmFyZSBldSB0ZWxsdXMgaW4gZmV1Z2lhdC4gUHJhZXNlbnQgcXVpcyB0cmlzdGlxdWUgdGVsbHVzLCBub24gY29uZ3VlIG5pc2wuIE5hbSBuZWMgZW5pbSBldCBwdXJ1cyB2dWxwdXRhdGUgdWx0cmljZXMgcXVpcyBldSBqdXN0by4gVXQgaWQgdmVzdGlidWx1bSBwdXJ1cy4gUGhhc2VsbHVzIHNlZCBmZWxpcyBvcm5hcmUsIHBoYXJldHJhIGR1aSBlZ2V0LCBkYXBpYnVzIGVuaW0u\n\n" +
				"b112828f-d7d0-4694-9880-5657f570ee04:940\nU3VzcGVuZGlzc2UgdmFyaXVzIGF1Y3RvciBleCwgc2l0IGFtZXQgZnJpbmdpbGxhIGVyYXQgb3JuYXJlIGFjLiBGdXNjZSBvcmNpIGV4LCBmcmluZ2lsbGEgYWMgaWFjdWxpcyBxdWlzLCBkaWN0dW0gbHVjdHVzIGVyYXQuIFZpdmFtdXMgZXggbWFzc2EsIHZlbmVuYXRpcyBuZWMgZXN0IHZlbCwgdmVoaWN1bGEgaW1wZXJkaWV0IGF1Z3VlLiBBZW5lYW4gc2VtIG9yY2ksIHBsYWNlcmF0IHZpdGFlIGN1cnN1cyBldSwgc2FnaXR0aXMgcXVpcyB0dXJwaXMuIFNlZCB1dCBlcm9zIHZlbCBhcmN1IGRpY3R1bSB2YXJpdXMgYXQgdml0YWUgdG9ydG9yLiBQaGFzZWxsdXMgY29uc2VxdWF0IHVsdHJpY2llcyBsYW9yZWV0LiBJbnRlZ2VyIHRyaXN0aXF1ZSwgbGVjdHVzIGluIHVsdHJpY2llcyBlZ2VzdGFzLCBhcmN1IHB1cnVzIG1vbGVzdGllIG1hc3NhLCBpbiB2aXZlcnJhIGVzdCBudW5jIGV1IG1hZ25hLiBTZWQgYWNjdW1zYW4gZXUgdHVycGlzIGluIHBvcnRhLiBNb3JiaSB0aW5jaWR1bnQgc2FnaXR0aXMgdm9sdXRwYXQuIEluIGNvbnZhbGxpcyB0dXJwaXMgbWkuIFNlZCBhIG5pc2wgdXQgbGlndWxhIGNvbmRpbWVudHVtIGFsaXF1ZXQgc2l0IGFtZXQgaWQgcmlzdXMuIERvbmVjIGEgaWFjdWxpcyBtYXVyaXMuIFByYWVzZW50IHVsbGFtY29ycGVyIGxlbyBlZ2V0IG51bmMgYmliZW5kdW0sIGFjIGVmZmljaXR1ciBsZWN0dXMgdGVtcHVzLg==\n\n" +
				"2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8:428\nRXRpYW0gYmliZW5kdW0gc2VtcGVyIGltcGVyZGlldC4gU3VzcGVuZGlzc2UgYXQgb2RpbyBsaWJlcm8uIERvbmVjIHZlbCBwbGFjZXJhdCBtYXVyaXMuIFByYWVzZW50IGRvbG9yIGVyb3MsIGNvbnNlcXVhdCBzZWQgdmVoaWN1bGEgdml0YWUsIGFsaXF1ZXQgYWMgbWFnbmEuIE1hdXJpcyBpYWN1bGlzIG51bmMgYWMgZnJpbmdpbGxhIGFsaXF1YW0uIFZpdmFtdXMgc2l0IGFtZXQgdHJpc3RpcXVlIG5lcXVlLCB2aXRhZSBjb25zZXF1YXQgbmlzaS4gUHJhZXNlbnQgZnJpbmdpbGxhIGltcGVyZGlldCBsZWN0dXMsIHNpdCBhbWV0IHBlbGxlbnRlc3F1ZSBsYWN1cy4=\n";
		MessageReader messages = new MessageReader(inl + "\n" + inl);
		Message m1 = messages.next();
		Message m2 = messages.next();
		assertThat(messages.hasNext()).isFalse();
		assertThat(m1.attachments().size()).isEqualTo(4);
		assertThat(m1.attachment("002eb31f-f3b3-4ba2-adfa-d758c4a55abe").length).isEqualTo(558);
		assertThat(m1.attachment("885d35f3-2811-42c2-a202-b6a7e4b03465").length).isEqualTo(696);
		assertThat(m1.attachment("b112828f-d7d0-4694-9880-5657f570ee04").length).isEqualTo(703);
		assertThat(m1.attachment("2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8").length).isEqualTo(320);
		assertThat(m1.attachment("002eb31f-f3b3-4ba2-adfa-d758c4a55abe")).isEqualTo("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut semper metus malesuada elit vestibulum dapibus. Ut pretium malesuada semper. Sed vel odio vestibulum, rutrum quam et, porta ante. Nullam fringilla nibh at nunc consequat imperdiet. Phasellus sed pellentesque nulla. Praesent facilisis lectus efficitur, bibendum ex eget, varius nibh. Aliquam tristique enim et feugiat egestas. Nullam non velit porta, viverra magna sed, ultrices mi. Donec eu lectus auctor, imperdiet metus quis, blandit dui. Aenean viverra justo felis, id laoreet purus tincidunt a.".getBytes());
		assertThat(m1.attachment("885d35f3-2811-42c2-a202-b6a7e4b03465")).isEqualTo("Mauris ornare nibh ac libero placerat, eget eleifend risus ullamcorper. Maecenas posuere commodo dui, vitae eleifend est. Duis non augue nec ipsum cursus sagittis quis vitae leo. Nunc eu pharetra enim. Suspendisse potenti. Phasellus nunc enim, blandit vel leo ac, aliquam luctus eros. Sed et quam euismod, fringilla dui eu, rutrum lacus. Curabitur lacinia vulputate tortor vitae semper. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Ut ornare eu tellus in feugiat. Praesent quis tristique tellus, non congue nisl. Nam nec enim et purus vulputate ultrices quis eu justo. Ut id vestibulum purus. Phasellus sed felis ornare, pharetra dui eget, dapibus enim.".getBytes());
		assertThat(m1.attachment("b112828f-d7d0-4694-9880-5657f570ee04")).isEqualTo("Suspendisse varius auctor ex, sit amet fringilla erat ornare ac. Fusce orci ex, fringilla ac iaculis quis, dictum luctus erat. Vivamus ex massa, venenatis nec est vel, vehicula imperdiet augue. Aenean sem orci, placerat vitae cursus eu, sagittis quis turpis. Sed ut eros vel arcu dictum varius at vitae tortor. Phasellus consequat ultricies laoreet. Integer tristique, lectus in ultricies egestas, arcu purus molestie massa, in viverra est nunc eu magna. Sed accumsan eu turpis in porta. Morbi tincidunt sagittis volutpat. In convallis turpis mi. Sed a nisl ut ligula condimentum aliquet sit amet id risus. Donec a iaculis mauris. Praesent ullamcorper leo eget nunc bibendum, ac efficitur lectus tempus.".getBytes());
		assertThat(m1.attachment("2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8")).isEqualTo("Etiam bibendum semper imperdiet. Suspendisse at odio libero. Donec vel placerat mauris. Praesent dolor eros, consequat sed vehicula vitae, aliquet ac magna. Mauris iaculis nunc ac fringilla aliquam. Vivamus sit amet tristique neque, vitae consequat nisi. Praesent fringilla imperdiet lectus, sit amet pellentesque lacus.".getBytes());
		assertThat(m1.toString()).isEqualTo("[Document]\n" +
				"name: my attachment\n" +
				"file1: photo.png@002eb31f-f3b3-4ba2-adfa-d758c4a55abe\n" +
				"file2:\n" +
				"\tcv.pdf@885d35f3-2811-42c2-a202-b6a7e4b03465\n" +
				"\txx.png@b112828f-d7d0-4694-9880-5657f570ee04\n" +
				"\tyy.png@2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8\n" +
				"\n" +
				"[&]\n");
		assertThat(m2).isEqualTo(m1);
	}

	private Instant instant(int y, int m, int d, int h, int mn, int s) {
		return LocalDateTime.of(y, m, d, h, mn, s).atZone(ZoneId.of("UTC")).toInstant();
	}

}
