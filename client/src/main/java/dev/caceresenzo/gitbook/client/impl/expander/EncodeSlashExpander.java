package dev.caceresenzo.gitbook.client.impl.expander;

import feign.Param.Expander;

public class EncodeSlashExpander implements Expander {

	@Override
	public String expand(Object value) {
		return String.valueOf(value)
			.replace("/", "%2F")
			.replace("+", "%2B");
	}

}