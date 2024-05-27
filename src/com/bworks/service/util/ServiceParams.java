package com.bworks.service.util;

/**
 * {@link IPostSubmissionService} Parameters
 * @author Nikhil N K
 */
public class ServiceParams {

	private String fileName = null;
	private String inputPath = null;
	private String outputPath = null;
	private Object data = null;
	private String pdfTemplateId = null;

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getInputPath() {
		return inputPath;
	}
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}
	public String getOutputPath() {
		return outputPath;
	}
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getPdfTemplateId() {
		return pdfTemplateId;
	}
	public void setPdfTemplateId(String pdfTemplateId) {
		this.pdfTemplateId = pdfTemplateId;
	}
	
	
}
