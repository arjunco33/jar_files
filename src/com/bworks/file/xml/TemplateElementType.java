package com.bworks.file.xml;

public enum TemplateElementType {

	CHECKBOX("c"),
	RADIO_BUTTON("r"),
	TABLE_DYNAMIC_ROW("dyr"),
	IMAGE("i"),
	LIMITED_SIZE_PARAGRAPH("lp"),
	TRUNCATED_FIELD("trf");
	
	private TemplateElementType(String value) {
		this.value = value;
	}
	private String value;
	
	public String getValue() {
		
		return value;
	}
}
