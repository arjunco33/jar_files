package com.bworks.file.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.geronimo.mail.util.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bworks.data.file.FileManager;
import com.bworks.log.LoggerProvider;
import com.bworks.util.AppConstants;
import com.bworks.util.ConfigurationManager;
import com.bworks.util.Utility;


/**
 * Modify DOM
 * @author Nikhil N K
 */
public class DomModifier {

    private final int IMAGES_PER_PAGE = 4;

	private org.jsoup.nodes.Document document = null;
	private boolean isPreventCacheResources = false;


	private class KeyValPair {

	    // begin region private Member variables

	    private String keyName;
	    private String value;

        // end region private Member variables
	    
	    public KeyValPair(String keyName, String value) {
	            
	            this.keyName = keyName;
	            this.value   = value;
	        }
        // end region constructors
	    
	    // begin region Public Functions.

	    public String getKeyName() {

            return keyName;
        }

        public String getValue() {

            return value;
        }

        public void setValue(String value) {

            this.value = value;
        }

        // end region Public Functions.
        
	}

	/**
     * 
     * @Description: This is a wrapper class to cache the image data for binding to html DOM.
     *
     */	
	private class ImageInfo{

	    // begin region Private Member Variables.
	    private String     type;
	    private KeyValPair imageData;
	    private KeyValPair note;

        // end region Private Member Variables.

	    // begin region Constructor

	    public ImageInfo(String imageKeyName, String noteKeyName, String type) {

            this.imageData = new KeyValPair(imageKeyName, "");
            this.note      = new KeyValPair(noteKeyName, "");
            this.type      = type;
        }
	    // end region Constructor

	    // begin region Public functions.
	    
        public KeyValPair getImageData() {

            return this.imageData;
        }
        public KeyValPair getNote() {

            return note;
        }

        public String getType() {
            return type;
        }
        
	    // end region Public functions.
	}
	
	/**
	 * Load document from dom string
	 * @param xml
	 * @param isFilePath
	 * @throws Exception 
	 */
	public void loadDocument(String xml, boolean isFilePath) throws Exception {

		File source = null;

		try {
			if(isFilePath) {
				source = new File(xml);
				if(source.exists()) {
					document = Jsoup.parse(source, "UTF-8");
				}
			} else {
				document = Jsoup.parse(xml);
			}
		} catch(Exception e) {
			throw e;
		}
	}

	/**
	 * Load xml document from file
	 * @param xml
	 * @throws Exception 
	 */
	public void loadDocument(File xmlPath) throws Exception {

		loadDocument(xmlPath.getAbsolutePath(), true);
	}

	/**
	 * Bind data to dom elements
	 * @param keyValuePair
	 */
	@SuppressWarnings("unchecked")
	public void bindData(Map<String, Object> keyValuePair) {

		if(keyValuePair != null) {

		    ArrayList<ImageInfo>  imageInfoList  = new ArrayList<ImageInfo>();
		    Entry<String, Object> entry          = null;
		    Elements              elements       = null;
		    List<String> tableWithSpecialSymbols = Arrays.asList(new String[]{"abrSleepAirTable"});

		    Iterator<Entry<String, Object>> iterator = keyValuePair.entrySet().iterator();

		    // Prepare the image info list. basically its a local cache to store the image information.
		    // The image data shall be append to the page at the end.
		    prepareImageInfoList(imageInfoList);

		    while (iterator.hasNext()) {

				entry    = iterator.next();
				elements = document.getElementsByAttributeValue("name", entry.getKey());

				// If entry contain required image, then save the image to temporary path 
				// and update the url to imageInfoList. If entry contain required note, 
				// then  update the note to imageInfoList.
				if(!updateImageInfoList(imageInfoList, entry)) {
     
    				// If the element is inserted to the image list, 
    				// then we don't need to run the following lines for this iteration. 
    
    				if(elements.size()  > 1) {
    
    					if(entry.getValue() != null                    && 
    					   entry.getValue().getClass() == String.class && 
    					   !entry.getValue().toString().isEmpty()) {
    
    					    if(elements.first().attr("type").equals("checkbox") ||
    						   elements.first().attr("type").equals("radio")) {
    
    					        Element element = elements.select("[value=" + entry.getValue() + "]").first();
    
    							if(element != null) {
    
    								element.attr("src", element.attr("src").replace("uncheck-", ""));
    							}
    						} else {
    
    						    elements.html(entry.getValue().toString().replace("\n", " ").
    									replace("\r", ""));//.replace(";", "."));
    						}
    					}
    				} else {
    
    				    Element element = null;
    				    String  key     = null;
    				    
    				    key = entry.getKey();
    				    element = document.getElementsByAttributeValue("name", entry.getKey()).first();
    					element = (element == null) ? document.getElementById(entry.getKey()) : element;
    
    					if(key.equalsIgnoreCase("CPTCodes"))
    					{
    					    element = null; // Ignore unwanted data process.
    					}
    					
    					if(element != null) {
    
    					    if(entry.getValue() instanceof String) {
    
    					        if(element.tagName().equalsIgnoreCase("div") || element.tagName().equalsIgnoreCase("label")) {
    
    					            String type = element.attr("type");
    					            String isAlgResultContainer = element.attr("algResult");
    								List<String> types = Arrays.asList(new String[]{"text", "textarea", "select"});
    								String value = entry.getValue().toString().replace("\n", " ")
    										.replace("\r", "");//.replace(";", ".");
    
    								if(isAlgorithmContainerElm(isAlgResultContainer)) {

    								    value = convertDefault(value);
    								}

    								if(types.contains(type)) {
    
    								    element.html(value);
    
    								} else {
    
    								    element.html(value);
    								}
    
    								if(element.parent().hasClass("adjustWidth")) {
    
    								    setStyle(element, "width", "auto");
    								}
    							} else if(element.tagName().equalsIgnoreCase("img")) {
    
    							    if(element.attr("type").equalsIgnoreCase("radio") || 
    							       element.attr("type").equalsIgnoreCase("checkbox")) {
    
    							        if(entry.getValue().equals("true") || 
    									   Boolean.valueOf(entry.getValue().toString())) {
    
    							            element.attr("src", element.attr("src").replace("uncheck-", ""));
    									}
    
    								} else if(element.attr("type") == null || element.attr("type").isEmpty()) {
    
    								    try {
    
    									    if(!isPreventCacheResources) {
    
    									        element.attr("src", saveResource(entry.getValue().toString().
    													replaceAll("\\s", "+")));
    										}
    
    									} catch (IOException e) {
    									}
    								}
    							}
    						} else if(entry.getValue() instanceof List) {

    							if(tableWithSpecialSymbols.contains(entry.getKey())) {
    							    bindTableWithSpecialSymbol(element, (List<List<String>>) entry.getValue());
    							} else {
    							    bindTable(element, (List<List<String>>) entry.getValue());
    							}
    						}
    					}
    				}
				}
			}

		    // Prepare the appendix pages with images and attach to the html page. 
		    prepareAppendixPages(imageInfoList, keyValuePair);
        }
    }

	private String convertDefault(String value) {

	    String convertedValue = "";

	    convertedValue = value;
	    if(null == value || value.trim().isEmpty()) {
	        convertedValue = "- - -";
	    }
	    return convertedValue;
    }

    private boolean isAlgorithmContainerElm(String isAlgResultContainer) {

        // Locals.
        boolean bIsAlgElm = false;

        if(null != isAlgResultContainer) {

            bIsAlgElm = isAlgResultContainer.trim().equals("1");
        }

        return bIsAlgElm;
    }

    private void prepareAppendixPages(ArrayList<ImageInfo> imageInfoList, Map<String, Object> keyValuePair) {

	    if(null != imageInfoList && 0 < imageInfoList.size()) {

	        // Local variables.
	        String    pageTemplate     = "<div style=\"height: 1716.45px; margin-top: 7px;\" class=\"content_ex\"><div style=\"margin-top:15px;\" class=\"mainTitle\"><label id=\"lblimgHead\">%IMG_DOC_HEAD%</label></div><div class=\"rowStyle\"><div class=\"columnLeft\"><div class=\"columnImg\">%IMG1%</div><div class=\"columnImgNote\"><label>%NOTES_LABEL1%</label><div>%NOTE1%</div></div></div><div class=\"columnRight\"><div class=\"columnImg\">%IMG2%</div><div class=\"columnImgNote\"><label>%NOTES_LABEL2%</label><div>%NOTE2%</div></div></div></div><div class=\"rowStyle\"><div class=\"columnLeft\"><div class=\"columnImg\">%IMG3%</div><div class=\"columnImgNote\"><label>%NOTES_LABEL3%</label><div>%NOTE3%</div></div></div><div class=\"columnRight\"><div class=\"columnImg\">%IMG4%</div><div class=\"columnImgNote\"><label>%NOTES_LABEL4%</label><div>%NOTE4%</div></div></div></div></div><div style=\"width:100%;margin-top:-22px;\"><div style=\"width:52%; float:left;\"><div style=\"width:40%; float: left; text-align: left; padding-left: 13px;\"><span>Date of Exam:</span><span name=\"\">%DOE%</span></div><div id=\"\" style=\"width: 50%;text-align:right;float:right\" class=\"footer\">Page<span id=\"pagenumber\"></span> of <span id=\"pagecount\"></span></div></div><div style=\"width:45%; float:right\"><div style=\"float: right;text-align: right; margin-right: 13px;\" >%PAGE_INFO%</div></div></div><div id=\"break0\" class=\"break\" style=\"height: 0px;width: 100%;\"></div>";
	        //String    pageTemplate     = "<div style=\"height: 1716.45px; margin-top: 7px;\" class=\"content_ex\"><div style=\"margin-top:15px;\" class=\"mainTitle\"><label id=\"lblimgHead\">%IMG_DOC_HEAD%</label></div><div class=\"rowStyle\"><div class=\"columnLeft\"><div class=\"columnImg\">%IMG1%</div><div class=\"columnImgNote\"><label>%NOTES_LABEL1%</label><div>%NOTE1%</div></div></div><div class=\"columnRight\"><div class=\"columnImg\">%IMG2%</div><div class=\"columnImgNote\"><label>%NOTES_LABEL2%</label><div>%NOTE2%</div></div></div></div><div class=\"rowStyle\"><div class=\"columnLeft\"><div class=\"columnImg\">%IMG3%</div><div class=\"columnImgNote\"><label>%NOTES_LABEL3%</label><div>%NOTE3%</div></div></div><div class=\"columnRight\"><div class=\"columnImg\">%IMG4%</div><div class=\"columnImgNote\"><label>%NOTES_LABEL4%</label><div>%NOTE4%</div></div></div></div></div><div id=\"\" style=\"width: 100%;text-align:center; margin-top:-25px;\" class=\"footer\">Page<span id=\"pagenumber\"></span> of <span id=\"pagecount\"></span></div><div style=\"float: left; margin-top:-22px; text-align: left;\"><span>Date of Exam:</span><span name=\"\">%DOE%</span></div><div style=\"float: right; margin-top:-22px; text-align: right; margin-right: 13px;\" >%PAGE_INFO%</div><div id=\"break0\" class=\"break\" style=\"height: 0px;width: 100%;\"></div>";
	        String    pages            = "";
	        String    pageContent      = null;
	        int       itrIndex         = 0;
	        ImageInfo imgInfo          = null;
	        int       pagesToAppend    = 0;
	        int       validImgListSize = 0;
	        int       imgItemIndex     = 0;
	        int       index            = 0;
	        String    imgContent       = "";
	        String    imgNote          = "";
	        String    imgNoteLbl       = "";
	        double    curDocHeight     = 0.0;
	        Element   containerElm     = null;
	        String    lastName         = "";
	        String    mrn              = "";
	        String    encounterNo      = "";
	        String    pageInfoText     = "";
	        String    dateOfExam       = "";
	        String    pageHeading      = "";

	        // This list contain reference of valid elements from the master list.
	        ArrayList<ImageInfo> validImageList = new ArrayList<ImageInfo>();

	        // Finding the valid item count in image list.
	        for (ImageInfo imageInfo : imageInfoList) {

	            if(null != imageInfo.getImageData().getValue() && 
	                    !imageInfo.getImageData().getValue().isEmpty()) {

	                validImageList.add(imageInfo);
	            }
	        }

	        validImgListSize = validImageList.size();
	        pagesToAppend    = calculatePageesToAppend(validImgListSize);

	        if(0 < pagesToAppend) {

    	        // Prepare pageContent
	            lastName     = (String) keyValuePair.get("lastName");
	            mrn          = (String) keyValuePair.get("mrn");
	            encounterNo  = (String) keyValuePair.get("encounterNumber");
                pageInfoText = lastName + "," + mrn  + ":" + encounterNo;
                dateOfExam   = (String) keyValuePair.get("dateOfExam");

	            for(itrIndex = 0; itrIndex < pagesToAppend; itrIndex ++) {
    
    	            pageContent = new String(pageTemplate);

    	            // Following loop will prepare a single page with available images and its notes.
    	            for(imgItemIndex = 0; imgItemIndex < IMAGES_PER_PAGE; imgItemIndex++, index++) {

    	                imgInfo = null;

    	                if(index < validImgListSize) {

    	                    imgInfo = validImageList.get(index);
    	                }

    	                imgContent = "";
    	                imgNote    = "";
    	                imgNoteLbl = "";

    	                if(null != imgInfo) {

    	                    imgContent = "<img style=\"height:100%; width:100%;\" "+
    	                                 "class=\"captureImage\" alt=\"\" src="    +
    	                                 imgInfo.getImageData().getValue() + "/>";
    	                    imgNote    = imgInfo.getNote().getValue();
    	                    imgNoteLbl = imgInfo.getType() + " Notes:";
    	                }

    	                pageContent = pageContent.replace("%IMG"  + (imgItemIndex + 1) + "%", imgContent);
    	                pageContent = pageContent.replace("%NOTE" + (imgItemIndex + 1) + "%", imgNote);
    	                pageContent = pageContent.replace("%NOTES_LABEL" + (imgItemIndex + 1) + "%", imgNoteLbl );

    	            } // End of image iteration.

                    // Add heading to first page.
    	            pageHeading = "";
                    if(0 == itrIndex) {
                        pageHeading = "APPENDIX";
                    }
                    pageContent = pageContent.replace("%IMG_DOC_HEAD%", pageHeading );

                    // Set page information.
                    if(null != pageInfoText) {
                        pageContent = pageContent.replace("%PAGE_INFO%", pageInfoText );
                    }

                    // Set the date of exam field.
                    if(null != dateOfExam) {
                        pageContent = pageContent.replace("%DOE%", dateOfExam );
                    }

                    // Combine the pages.
    	            pages      += pageContent;

                } // End of page iteration.

               // get the current height of master div.
    	        curDocHeight = Double.parseDouble(getStyle(document.body().child(0), 
    	                                          "height").replace("px", "").trim());

    	        // Set the pages to html document.
    	        containerElm = document.getElementById("contentX");

    	        if(null != containerElm) {

    	            containerElm.html(pages);
    	        }

    	        // update the master div height.
                setStyle(document.body().child(0),"height", 
                        String.valueOf(curDocHeight + (((double)pagesToAppend * 1716.45)) + "px"));
	        }
	    } // End of Master if.
    }

	// Calculate the page count to be append.
    private int calculatePageesToAppend(int imageListSize) {
        
        int pageesToAppend = 0;

        if( 0 < imageListSize) {

            if(imageListSize < IMAGES_PER_PAGE) {
    
                pageesToAppend = 1;

            }else{

                pageesToAppend = (int)(imageListSize / IMAGES_PER_PAGE);
                
                if(0 < imageListSize % IMAGES_PER_PAGE) {
    
                    pageesToAppend += 1;
                }
            }
        }

        return pageesToAppend;
    }

    private boolean updateImageInfoList(ArrayList<ImageInfo> imageInfoList, 
	                                    Entry<String, Object> entry) {

        boolean itemFound = false;

        if(null != imageInfoList  && 0 < imageInfoList.size() && null != entry) {

            // We need this variables only if the condition become true.
            // Local variables.
            String    resourcePath   = "";
            String    encodedImgData = null;
            Integer   itrIndex       = 0;
            ImageInfo imageInfo      = null;
            Integer   listSize       = 0;

	        try {

	            listSize = imageInfoList.size();

	            // According to the preparation of imageInfoList, 
	            // expecting that imageInfoList won't contain any null items.
	            for(itrIndex = 0; itrIndex < listSize; itrIndex++) {

	                imageInfo  = imageInfoList.get(itrIndex);
                
    	            if(imageInfo.getImageData().getKeyName().equals(entry.getKey())) {

    	                encodedImgData = null;
                        itemFound      = true;

    	                if(null != entry.getValue()) {

    	                    encodedImgData = entry.getValue().toString();
    	                }

    	                if(null != encodedImgData && !encodedImgData.isEmpty()) {

    	                    resourcePath   = saveResource(encodedImgData.replaceAll("\\s", "+"));
    	                    imageInfo.getImageData().setValue(resourcePath);
    	                }

    	            }else if(imageInfo.getNote().getKeyName().equals(entry.getKey())) {

                        itemFound = true;
                        if(null != entry.getValue()) {

                            imageInfo.getNote().setValue(entry.getValue().toString());
                        }
    	            }
    	            else {

    	                itemFound = false;
    	            }

    	            if(itemFound) {

    	             // Item found, no need further iteration, exit the loop safely.
    	                itrIndex = listSize + 1;
    	            }
    	        }

	        } catch(Exception ex) {

	            // TODO: log this exception.
	        }
	    } // End of master if

        return itemFound;

    } // End of method

    private void prepareImageInfoList(ArrayList<ImageInfo> imageInfoList) {

        // Local Variables.
        ImageInfo imgInfo = null;

	    if(null != imageInfoList ) {

	        // Images would be attached to html page on the elements inserted to the imageInfoList.
	        // Tympanometry form.
	        imgInfo = new ImageInfo("rightImage","rightComment", "Right Tympanometry");
	        imageInfoList.add(imgInfo);

	        imgInfo = new ImageInfo("leftImage","leftComment", "Left Tympanometry");
            imageInfoList.add(imgInfo);

	        // ABR form.
	        imgInfo = new ImageInfo("abrAttachmentIMG","abrAttachmentTEXTAREA", "ABR");
	        imageInfoList.add(imgInfo);

            imgInfo = new ImageInfo("abrAttachment178321abrAttachmentIMG",
                                    "abrAttachment178321abrAttachmentTEXTAREA", "ABR");
            imageInfoList.add(imgInfo);

            // Hearing form.
            imgInfo = new ImageInfo("hearingAttachment142096hearingAttachmentIMG",
                                    "hearingAttachment142096hearingAttachmentTEXTAREA", 
                                    "Hearing Tech");
            imageInfoList.add(imgInfo);

            imgInfo = new ImageInfo("hearingAttachmentIMG", "hearingAttachmentTEXTAREA", 
                                    "Hearing Tech");
            imageInfoList.add(imgInfo);

            imgInfo = new ImageInfo("hearingAttachment990043hearingAttachmentIMG",
                                    "hearingAttachment990043hearingAttachmentTEXTAREA", 
                                    "Hearing Tech");
            imageInfoList.add(imgInfo);

            imgInfo = new ImageInfo("hearingAttachment55178hearingAttachmentIMG",
                                    "hearingAttachment55178hearingAttachmentTEXTAREA", 
                                    "Hearing Tech");
            imageInfoList.add(imgInfo);
            
            // Result tab
            imgInfo = new ImageInfo("resultAttachment643683resultAttachmentIMG",
                                    "resultAttachment643683resultAttachmentTEXTAREA", 
                                    "Result");
            imageInfoList.add(imgInfo);

            imgInfo = new ImageInfo("resultAttachmentIMG", "resultAttachmentTEXTAREA", "Result");
            imageInfoList.add(imgInfo);
	    }
    }

    /**
	 * Bind data to table
	 * @param element
	 * @param value
	 */
	private void bindTable(Element element, List<List<String>> value) {

		Elements elements = element.select("[type~=text|number|select-one|select-multiple|radio|checkbox]");
		List<String> types = Arrays.asList(new String[]{"text", "number", "select-one", "select-multiple"});
		int index          = 0;
		
		for(List<String> row : value) {
			for(String item : row) {
				if(index < elements.size()) {
					Element innerElement = elements.get(index);
					if(types.contains(innerElement.attr("type"))) {
						innerElement.html(item);
					} else {
						if(item.equals("true") || Boolean.valueOf(item)) {
							innerElement.attr("src", innerElement.attr("src").replace("uncheck-", ""));
						}
					}
				} else {
					break;
				}
				index++;
			}
		}
	}

	   /**
     * Bind data to table
     * @param element
     * @param value
     */
    private void bindTableWithSpecialSymbol(Element element, List<List<String>> value) {

        Elements elements = element.select("[type~=text|number|select-one|select-multiple|radio|checkbox]");
        List<String> types = Arrays.asList(new String[]{"text", "number", "select-one", "select-multiple"});

        int index = 0;
        String  convertedTxt = "";
        
        for(List<String> row : value) {
            for(String item : row) {
                if(index < elements.size()) {
                    Element innerElement = elements.get(index);
                    if(types.contains(innerElement.attr("type"))) {
                        convertedTxt = Utility.convertASCIISymbolsToUnicode(item);
                        innerElement.html(convertedTxt);
                    } else {
                        if(item.equals("true") || Boolean.valueOf(item)) {
                            innerElement.attr("src", innerElement.attr("src").replace("uncheck-", ""));
                        }
                    }
                } else {
                    break;
                }
                index++;
            }
        }
    }

	/**
	 * Get DOM String
	 * @return
	 */
	public String getDomString() {

		return (document != null) ? document.html() : null;
	}

	/**
	 * Write dom string to a file 
	 * @param destinationFile
	 * @return
	 * @throws Exception 
	 */
	public boolean save(String destinationFile) throws Exception {

		FileManager fileManager = null;
		boolean status = false;

		try {
			fileManager = new FileManager();
			status = fileManager.write(destinationFile, getDomString());
		} catch (Exception e) {
			throw e;
		}

		return status;
	}

	private String saveResource(String base64String) throws IOException {

		byte[] data = null;
		String outPath = null;
		OutputStream stream = null;
		String resourcePath = "temp";
		String extension = "";

		try {
			outPath = new File(ConfigurationManager.getRootPath() +
					AppConstants.Resources.ROOT).getAbsolutePath() + AppConstants.FILE_SEPARATOR;
			cleanUpResources(outPath + resourcePath);
			if(base64String != null & !base64String.isEmpty()) {
				if(base64String.startsWith("data:")) {
					extension = "." + base64String.substring(0,  50).split(";")[0].split("/")[1];
					base64String = base64String.split(",")[1];
				}
				resourcePath += AppConstants.FILE_SEPARATOR + new Date().getTime() + "_" +
							Long.toString((long)(Math.random() * Math.pow(10, 18))) + extension;
				outPath += resourcePath;
				new File(outPath).getParentFile().mkdirs();
				stream = new FileOutputStream(outPath);
				data = Base64.decode(base64String);
			    stream.write(data);
			    new File(outPath).deleteOnExit();
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if(stream != null) {
				stream.close();
			}
		}
		
		return resourcePath;
	}

	/**
	 * Delete old resource files
	 * @param outPath 
	 */
	private void cleanUpResources(String outPath) {

		File resourceDirectory =  new File(outPath);
		if(resourceDirectory.exists()) {
			File[] resources = resourceDirectory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if(name.matches("^\\d{12,13}(_\\d{18})?[.][\\w]+$")) {
						long difference = new Date().getTime() - Long.parseLong(
								name.substring(0, name.lastIndexOf(".")).split("_")[0]);
						difference = difference / (60 * 1000);
						return difference > AppConstants.DEFAULT_CLEANUP_TIME;
					}
					return false;
				}
			});
			for(File resource : resources) {
				try {
					resource.delete();
				} catch (Exception e) {
					LoggerProvider.getLogger().error(e);
				}
			}
		}
	}

	/**
	 * Hide empty pages and adjust following borders
	 * @param emptyForms
	 */
	public void hidePages(List<String> emptyForms) {

	    List<String> pagesTobeHidden = new ArrayList<String>();

	    // We don't need HearingFormData1 and AbrFormData1 pages in the html document. 
	    // Manual edit need more rework on the template(based on the current design of html template). 
	    pagesTobeHidden.addAll(emptyForms);
	    pagesTobeHidden.add("HearingFormData1");
	    pagesTobeHidden.add("AbrFormData1");
	    
		int pageCount = document.getElementsByClass("content").size(); 
		Elements boxControls = document.getElementsByClass("draggableBox");
		float pageHeight = Float.parseFloat(getStyle(
				document.getElementsByClass("content").get(0), "height").replace("px", ""));
		
		for(String formName : pagesTobeHidden) {
			Elements elements = document.getElementsByClass(formName);
			for (int index1 = 0; index1 < elements.size(); index1++) {
				Element content = document.getElementsByClass(formName).first();
				int currentPage = Integer.parseInt(content.id().replace("content", ""));
				Element currentContent = content;
				for(int index = currentPage; index < pageCount - 1; index++) {
					Element nextContent = document.getElementById("content" + (index + 1));
					currentContent.html(nextContent.html());
					//currentContent.classNames(nextContent.classNames());
					String[] classNames = currentContent.classNames().toArray(new String[0]);
					for(String className : classNames) {
						currentContent.removeClass(className);
					}
					classNames = nextContent.classNames().toArray(new String[0]);
					for(String className : classNames) {
						currentContent.addClass(className);
					}
					currentContent = nextContent;
				}

				for (Element boxControl : boxControls) {
					float top = Float.parseFloat(getStyle(boxControl, "top").replace("px", ""));
					if(top < (currentPage + 1) * (pageHeight + 8.5) && 
							top >  (currentPage + 1)  * (pageHeight + 8.5) - pageHeight - 1) {
						setStyle(boxControl, "display", "none");
					} else if(top > (currentPage + 1) * (pageHeight + 8.5)) {
						setStyle(boxControl, "top", (top - (pageHeight + 10)) + "px");
					}
				}

				currentContent.remove();
				pageCount--;
				document.getElementById("header" + pageCount).remove();
				document.getElementById("footer" + pageCount).remove();
				document.getElementById("break" + pageCount).remove();
				@SuppressWarnings("unused")
                double currentDocumentHeight = Double.parseDouble(getStyle(document.body().child(0),
						"height").replace("px", "").trim());
				setStyle(document.body().child(0), "height", 
						String.valueOf(pageCount * pageHeight) + "px");
			}
		}
	}

	/**
	 * Set style to a element
	 * @param element
	 * @param styleName
	 * @param value
	 */
	private void setStyle(Element element, String styleName, String value) {

		String cssText = element.attr("style");
		String[] styleArray = cssText.split("\\s*;\\s*");
		Map<String, String> styles = new HashMap<String, String>();
		
		for(String style : styleArray) {
			styles.put(style.split(":")[0].trim(), style.split(":")[1].trim());
		}
		styles.put(styleName, value);
		Iterator<Entry<String, String>> it = styles.entrySet().iterator();
		cssText = "";
	    while (it.hasNext()) {
	        Map.Entry<String, String> keyValuePair = (Map.Entry<String, String>)it.next();
	        cssText += keyValuePair.getKey() + ": " + keyValuePair.getValue() + "; ";
	        it.remove();
	    }
	    element.attr("style", cssText);
	}

	/**
	 * Set style to a element
	 * @param element
	 * @param styleName
	 * @param value
	 */
	private String getStyle(Element element, String styleName) {

		String cssText = element.attr("style");
		String[] styleArray = cssText.split("\\s*;\\s*");
		Map<String, String> styles = new HashMap<String, String>();
		String value = null;
		
		for(String style : styleArray) {
			styles.put(style.split(":")[0].trim(), style.split(":")[1].trim());
		}
		
		if(styles.containsKey(styleName)) {
			value = styles.get(styleName);
		}

		return value;
	}

	public void setPreventCacheResources(boolean isPreventCacheResources) {
		this.isPreventCacheResources = isPreventCacheResources;
	}
}