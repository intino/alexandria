package io.intino.alexandria;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Json_ {

	public static void main(String[] args) {
		Entity entity = new Entity();
		System.out.println(Json.toJsonPretty(entity));
		System.out.println(Json.toJson(entity));
		System.out.println(Json.fromJson(Json.toJsonPretty(entity), Entity.class));
		System.out.println(Json.fromJson(Json.toJson(entity), Entity.class));
	}

	private static class Entity {
		Instant ts = Instant.now();
		double n = Math.random();
		List<String> list = List.of("A", "B", "C");
		InputStream inputStream = new ByteArrayInputStream("abcdf12345".getBytes());
		Date date = new Date();
		Map<String, Integer> map = Map.of("A", 1, "B", 0, "C", 45);
		Object obj = null;
	}
}
