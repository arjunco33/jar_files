package com.bworks.service;

import java.util.List;
import java.util.Map;

import com.bworks.formdata.IFormData;

public interface IServiceDataProvider {

	Map<String, Object> getServiceData();

	void generateServiceData(List<IFormData> formData);

	List<String> getEmptyForms();
}
