package dev.caceresenzo.gitbook.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import dev.caceresenzo.gitbook.client.impl.serial.DataDelegatingDeserializer;
import dev.caceresenzo.gitbook.client.impl.serial.NodeDelegatingDeserializer;
import dev.caceresenzo.gitbook.model.document.Mark;
import dev.caceresenzo.gitbook.model.document.Node;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GitBookUtils {

	@SneakyThrows
	public static ObjectMapper createMapper() {
		return JsonMapper.builder()
			.serializationInclusion(JsonInclude.Include.NON_NULL)
			.configure(SerializationFeature.INDENT_OUTPUT, true)
			.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION, true)
			.addModule(new JavaTimeModule())
			.addModule(new NodeModule())
			.addModule(new ParameterNamesModule())
			.build();
	}

	@SuppressWarnings("serial")
	public class NodeModule extends SimpleModule {

		@Override
		public void setupModule(SetupContext context) {
			super.setupModule(context);

			context.addBeanDeserializerModifier(new BeanDeserializerModifier() {

				@Override
				public JsonDeserializer<?> modifyDeserializer(
					DeserializationConfig config,
					BeanDescription beanDesc,
					JsonDeserializer<?> deserializer
				) {
					//					System.out.println("GitBookUtils.NodeModule.setupModule(...).new BeanDeserializerModifier() {...}.modifyDeserializer()  " + beanDesc.getBeanClass());

					if (Node.class.equals(beanDesc.getBeanClass()) || Mark.class.equals(beanDesc.getBeanClass())) {
						deserializer = new NodeDelegatingDeserializer(deserializer);
					} else if (Node.class.isAssignableFrom(beanDesc.getBeanClass()) || Mark.class.isAssignableFrom(beanDesc.getBeanClass())) {
						deserializer = new DataDelegatingDeserializer(deserializer);
					}

					return deserializer;
				}

			});
		}

	}

}