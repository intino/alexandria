package io.intino.alexandria;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Json_ {

	public static void main(String[] args) throws IOException {
		Entity entity = new Entity();
		System.out.println(Json.toJsonPretty(entity));
		System.out.println(Json.toJson(entity));
		System.out.println(Json.fromJson(Json.toJsonPretty(entity), Entity.class));
		System.out.println(Json.fromJson(Json.toJson(entity), Entity.class));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Json.toJsonPretty(entity, new OutputStreamWriter(outputStream));
		String s = outputStream.toString();
		System.out.println(s);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(s.getBytes());
		Entity entity1;
		try(Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			entity1 = Json.fromJson(reader, Entity.class);
		}
		System.out.println(Json.toJson(entity));
		System.out.println(Json.toJson(entity1));

		System.out.println(Instant.ofEpochMilli(1677592761613L));
		System.out.println(Instant.ofEpochMilli(1677592761613L));
	}

	private static class Entity {
		Instant ts = Instant.now();
		double n = Math.random();
		List<String> list = List.of("A", "B", "C");
		ByteArrayInputStream inputStream = new ByteArrayInputStream("abcdf12345".getBytes());
		Date date = new Date();
		Map<String, Integer> map = Map.of("A", 1, "B", 0, "C", 45);
		Object obj = null;
	}
}
