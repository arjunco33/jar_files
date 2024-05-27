package com.bworks.formdata;

import java.util.Map;

public interface IFormData {

	public Map<String, Object> getCustomProperties();
	public void setCustomProperties(Map<String, Object> customProperties);
}
