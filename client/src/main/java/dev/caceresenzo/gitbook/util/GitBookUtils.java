package dev.caceresenzo.gitbook.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GitBookUtils {

	public static ObjectMapper createMapper() {
		return JsonMapper.builder()
			.serializationInclusion(JsonInclude.Include.NON_NULL)
			.configure(SerializationFeature.INDENT_OUTPUT, true)
			.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.addModule(new JavaTimeModule())
			.build();
	}

}