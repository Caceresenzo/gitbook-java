package dev.caceresenzo.gitbook.spring.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.caceresenzo.gitbook.client.GitBookClient;
import dev.caceresenzo.gitbook.spring.boot.autoconfigure.GitBookAutoConfiguration;

@SpringBootTest(
	classes = {
		GitBookAutoConfiguration.class,
	}
)
class ClientAutoConfigurationTest {

	@Test
	void contextLoads(@Autowired GitBookClient client) {}

}