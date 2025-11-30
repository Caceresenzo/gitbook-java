package dev.caceresenzo.gitbook.model.document;

public sealed interface Block extends Node permits Heading1, Heading2, Heading3, Paragraph, ListItem, OrderedList, UnorderedList, Table, Code, CodeLine, Images, Image {}