package dev.caceresenzo.gitbook.client.impl.dto;

import java.util.List;

import dev.caceresenzo.gitbook.model.Page;
import lombok.Data;

@Data
public class SpacePagesResponse {

	private List<Page> pages;

}