package dev.caceresenzo.gitbook.client.impl.expander;

import feign.Param.Expander;

public class ToLowerStringExpander implements Expander {

	@Override
	public String expand(Object value) {
		if (value == null) {
			return null;
		}

		return String.valueOf(value).toLowerCase();
	}

}