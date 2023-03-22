package io.intino.alexandria.event.resource;

import io.intino.alexandria.Json;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

class ResourceHelper {

	static final String METADATA_TYPE = "$type";
	static final String METADATA_SS = "$ss";
	static final String METADATA_TS = "$ts";
	static final String METADATA_REI = "$rei";
	static final String METADATA_FILE = "$file";

	static byte[] serializeMetadata(ResourceEvent event, File file) {
		Map<String, String> metadata = new HashMap<>(event.resource().metadata().properties());
		metadata.put(METADATA_TYPE, event.type());
		metadata.put(METADATA_SS, event.ss());
		metadata.put(METADATA_TS, event.ts().toString());
		metadata.put(METADATA_REI, event.getREI().toString());
		if(file != null) metadata.put(METADATA_FILE, file.getAbsolutePath());
		return Json.toJson(metadata).getBytes(UTF_8);
	}

	@SuppressWarnings("unchecked")
	static Map<String, String> deserializeMetadata(byte[] bytes) {
		return Json.fromJson(new String(bytes, UTF_8), Map.class);
	}
}
