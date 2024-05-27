package com.bworks.file.xml;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;

import com.bworks.log.LoggerProvider;
import com.bworks.util.AppConstants;
import com.bworks.util.ConfigurationManager;

/**
 * This class contains methods for binding the data to HTML templates of different reports. It also contains methods for 
 * generating some dynamic data, formatting some data, etc.
 */
public class TemplateLexicalParser {
	
	//Check box images
	private static final String IMG_CHECKED_CHECKBOX   = "./images/ui-check-box-icon.png";
	private static final String IMG_UNCHECKED_CHECKBOX = "./images/ui-check-box-uncheck-icon.png";
	
	//Radio button images
	private static final String IMG_SELECTED_RADIO_BUTTON   = "./images/ui-radio-button-icon.png";
	private static final String IMG_UNSELECTED_RADIO_BUTTON = "./images/ui-radio-button-uncheck-icon.png";
	
	//Audiogram place holders
	private static final String IMG_AUDIOGRAM_GRID_ONLY    = "./images/chartcontrol.png";
	private static final String IMG_AUDIOGRAM_CNT          = "./images/chartcontrol_cnt.png";
	private static final String IMG_AUDIOGRAM_DNT          = "./images/chartcontrol_dnt.png";
	private static final String IMG_AUDIOGRAM_SKIP_TO_PAGE = "./images/chartcontrol_skippage.png";
	private static final String IMG_AUDIOGRAM_TYMP         = "./images/chartcontrol_tymp.png";

	//Tymp/ABR place holder rectangular box
	private static final String IMG_TYMP_PLACEHOLDER = "./images/rect-placeholder.png";
	
	private static final String HTML_ATTR_DISPLAY_NONE    = "none";
	private static final String HTML_ATTR_DISPLAY_DEFAULT = "";
	
	private static final String TOKEN_START_SYMBOL   = "{{";
	private static final String TOKEN_END_SYMBOL     = "}}";
	private static final String TYPE_START_SYMBOL    = "<";
	private static final String TYPE_END_SYMBOL      = ">";
	private static final String TYPE_SEPARATOR       = ";";
	private static final String TYPE_PARAM_SEPARATOR = "-";
	private static final String ARRAY_START          = "[";
	private static final String ARRAY_END            = "]";
	private static final String OBJECT_START         = ".";
	
	//Change this value to true to show the new lines in the paragraphs
	private static final boolean PARAGRAPH_ALLOW_NEW_LINES     = false;
	
	private static final String PARAGRAPH_CONTNUED_SUFFIX      = "_continued";
	private static final String PARAGRAPH_CONTNUED_PAGE_SUFFIX = "_continued_page";
	
	private static final String HTML_NEW_LINE_CHAR      = "<br>";
	private static final String WHITE_SPACE_CHAR        = " ";
	
	private static final String REGEX_NEW_LINE_CHAR_R_N = "\r\n";
	private static final String REGEX_NEW_LINE_CHAR_N   = "\n";
	
	private static final Pattern PATTERN_NEW_LINE_R_N = Pattern.compile(REGEX_NEW_LINE_CHAR_R_N);
	private static final Pattern PATTERN_NEW_LINE_N   = Pattern.compile(REGEX_NEW_LINE_CHAR_N);
	
	private static final List<Character> PARAGRAPH_SENTENCE_DELIMITERS = List.of(',',';');
	
	//Modify the paragraph font family and font size value whenever there is a change in paragraph font family or font size in the templates.
	private static final String PARAGRAPH_FONT_FAMILY = "Arial";
	private static final int    PARAGRAPH_FONT_SIZE   = 12;
	
	private static final String TRUNCATED_FIELD_SUFFIX = "...";

	private String template   = null;
	private String templateId = null;
	private int    pageCount  = 0;
	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * Gets the PDF template name from the template id.
	 * @param templateId The template id
	 * @return
	 */
	public static String getTemplateFileName(String templateId) {
		
		String templateFileName = null;
		
		try {
			
			if (templateId.equals(AppConstants.TEMPLATE_ID_ABR)) {
				templateFileName = ConfigurationManager.createInstance().tryGetProperty(AppConstants.PDF_TEMPLATE_ABR, false);
				
			} else if (templateId.equals(AppConstants.TEMPLATE_ID_HEARING_TEST)) {
				
				templateFileName = ConfigurationManager.createInstance().tryGetProperty(AppConstants.PDF_TEMPLATE_HEARING_TEST, false);
				
			} else if(templateId.equals(AppConstants.TEMPLATE_ID_TYMP)){
				templateFileName = ConfigurationManager.createInstance().tryGetProperty(AppConstants.PDF_TEMPLATE_TYMP, false);
				
			} else {
				LoggerProvider.getLogger().debug("getTemplateFileName - Invald template id provided " + templateId);
			}
		
		} catch(Exception ex) {
			
			LoggerProvider.getLogger().error("getTemplateFileName - Failed to get the PDF template name from the template id " + templateId + " :", ex);
		}
		
		return templateFileName;
	}
	
	public static boolean isValidTemplateId(String templateId) {
		
		boolean isValid = false;
		
		if (null != templateId && (templateId.equals(AppConstants.TEMPLATE_ID_ABR)
				|| templateId.equals(AppConstants.TEMPLATE_ID_HEARING_TEST)
				|| templateId.equals(AppConstants.TEMPLATE_ID_TYMP))) {
			isValid = true;
		}
		
		return isValid;
	}
	
	/**
	 * Binds the data from <i>bindData</i> to the PDF template. The caller should set the PDF template using the <i><b>setTemplate()<b><i>
	 *  before calling this method.
	 * @param bindData The Map object containing the bindData
	 */
	public void bindDataToTemplate(Map<String, Object> bindData) {
		
		StringBuilder builder = new StringBuilder();
		int start = -1;
		int end = -1;
		String key = null;
		int typeEndIndex = -1;
		boolean forceStop = false;
		
		if(null != bindData && null != template) {
			
			pageCount = getTemplateDefaultPageCount(templateId);
			pageCount = addOptionalPageCount(pageCount, templateId, bindData);
			builder   = new StringBuilder(template);
			
			while(!forceStop && -1 < (start = builder.indexOf(TOKEN_START_SYMBOL))) {
				
				try {
					
					end = builder.indexOf(TOKEN_END_SYMBOL);
					key = builder.substring(start + TOKEN_START_SYMBOL.length(), end);
					
					//Get the types(Check box, Radio button, etc.) associated with the token.
					String[] typeArr = getValueTypes(key);
					
					//If there is type(s) associated with the token, 
					if(-1 < (typeEndIndex = key.indexOf(TYPE_END_SYMBOL))) {
						
						key = key.substring(typeEndIndex + 1);
					}
					
					List<Object> keyList = constructKeyList(key);
					Object valObj = getMappedValue(bindData, key, keyList);
					
					if(null != typeArr && 0 < typeArr.length) {
						
						bindDataWithSpecialMeaning(builder, bindData, key, start, end, typeArr, valObj);
						
					} else {
						
						//For the additional pages content (which is added only for the paragraph values that are larger to fit in the default space provided), 
						//the formatting is already done as part of splitting the paragraph. so such values should not be formatted again.
						if(key.endsWith(PARAGRAPH_CONTNUED_SUFFIX)) {
							
							replaceTokenWithValue(builder, start, end, valObj, true);
							
						} else {
							
							replaceTokenWithValue(builder, start, end, valObj);
						}
					}
					
				} catch (Exception ex) {
					
					forceStop = true;
					LoggerProvider.getLogger().error("bindDataToTemplate - Data binding failed for the key " + key + " :", ex);
				}
			}
			
			template = builder.toString();
		}
	}
	
	/**
	 * Replaces a token and surrounding token symbols by the value of the token
	 * @param builder The builder containing the template
	 * @param tokenStartIndex Start index of the token start symbol
	 * @param tokenEndIndex Start index of the token end symbol
	 * @param valObj Value of the token.
	 */
	private void replaceTokenWithValue(StringBuilder builder, int tokenStartIndex, int tokenEndIndex, Object valObj) {
		
		replaceTokenWithValue(builder, tokenStartIndex, tokenEndIndex, valObj, false);
	}
	
	/**
	 * Replaces a token and surrounding token symbols by the value of the token
	 * @param builder The builder containing the template
	 * @param tokenStartIndex Start index of the token start symbol
	 * @param tokenEndIndex Start index of the token end symbol
	 * @param valObj Value of the token.
	 * @param doNotFormat If false, the value will be converted to UTF-8 and HTML character escaping is done before binding.
	 */
	private void replaceTokenWithValue(StringBuilder builder, int tokenStartIndex, int tokenEndIndex, Object valObj, boolean doNotFormat) {
		
		String valStr = null != valObj ? valObj.toString() : "";
		
		if(!doNotFormat) {
			
			//Convert to UTF-8. This is to display some special characters like less than or equal to from the iPad application correctly in the report .
			valStr = getHTMLFormattedString(valStr);
		}
		
		builder.replace(tokenStartIndex, tokenEndIndex + TOKEN_END_SYMBOL.length(), valStr);
	}
	
	/**
	 * Gets the array of value types associated with the token.
	 * @param key The token key
	 * @return Array of value types associated with the token.
	 */
	private String[] getValueTypes(String key) {
		
		String typeStr = null;
		String[] typeArr = null;
		
		/*Gets the string between angle brackets(< and >) and splits it using semicolon which is the delimiter for a value type.*/
		if(null != key && !key.isEmpty() && key.startsWith(TYPE_START_SYMBOL) && key.contains(TYPE_END_SYMBOL)) {
			
			typeStr = key.substring(1, key.indexOf(TYPE_END_SYMBOL));
			typeArr = typeStr.split(TYPE_SEPARATOR);
		}
		
		return typeArr;
	}
	
	/**
	 * Constructs a list that contain keys which can be used to get values from array, map and objects.
	 * <br><br>
	 * For example if the keys is key1.key2[0][1].key3, the returned list will be [key1, key2, 0, 1, key3]
	 * 
	 * @param key
	 * @return List that contain keys which can be used to get values from array, map and objects.
	 */
	private List<Object> constructKeyList(String key) {
		
		List<Object> keyList = new LinkedList<>();
		
		try {
			
			if(null != key && !key.isEmpty()) {
				
				int arrayStartIndex = key.indexOf(ARRAY_START);
				int objectIndex = key.indexOf(OBJECT_START);
				
				if(-1 < arrayStartIndex && (-1 == objectIndex || arrayStartIndex < objectIndex)) {
					/* Scenario where the JSON array notation comes before the object notation.
					 * eg: key1[0], key1[0].key2, key1[0][0], etc. */
					
					String strKey = key.substring(0, arrayStartIndex);
					
					if(null != strKey && !strKey.isEmpty()) {
						keyList.add(strKey);
					}
					
					int arrayEndIndex = key.indexOf(ARRAY_END);
					int arrIndexVal = Integer.parseInt(key.substring(arrayStartIndex + 1, arrayEndIndex));
					keyList.add(arrIndexVal);
					keyList.addAll(constructKeyList(key.substring(arrayEndIndex + 1)));
					
				} else if (-1 < objectIndex && (-1 == arrayStartIndex || objectIndex < arrayStartIndex)){
					/* Scenario where the JSON object notation comes before the array notation.
					 * eg: key1.key2[0], key1.key2, etc. */
					
					String strKey = key.substring(0, objectIndex);
					
					if(null != strKey && !strKey.isEmpty()) {
						keyList.add(strKey);
					}
					
					keyList.addAll(constructKeyList(key.substring(objectIndex + 1)));
					
				} else {
					
					/* Scenario where the key contains no array or object notation.
					 * eg: key1 */
					
					keyList.add(key);
				}
			}
			
		} catch(Exception ex) {
			
			LoggerProvider.getLogger().error("constructKeyList failed for the key " + key + ": ", ex);
		}
		
		return keyList;
	}
	
	/**
	 * Gets the value for the key denoted by the list of parts of the key <i>keyList</i>
	 * @param bindData Map containing the data to bind
	 * @param key Key of the which the mapped value to be fetched from <i>bindData/i>
	 * @param keyList List of parts of the key which constructs the actual key.
	 * @return The mapped value object.
	 */
	private Object getMappedValue(Map<String, Object> bindData, String key, List<Object> keyList) {
		
		List<?> valueList = null;
		Map<?, ?> valueMap = null;
		int index = 0;
		Object valObj = null;
		
		if(null != bindData && null != keyList && !keyList.isEmpty()) {
			
			try {
				
				valObj = bindData.get(keyList.get(0));
				
				index = 1;
				
				while(index < keyList.size()) {
					
					if(valObj instanceof List<?>) {
						/*valObj is a List*/
						
						valueList = (List<?>) valObj;
						valObj = valueList.get((int) keyList.get(index));
					
					} else if(valObj instanceof Map) {
						/*valObj is a Map*/
						
						valueMap = (Map<?, ?>) valObj;
						valObj = valueMap.get(keyList.get(index));
						
					} else if(!isAtomicType(valObj)) {
						/*valObj is a Object*/
						
						Field field = valObj.getClass().getDeclaredField(keyList.get(index).toString());
						field.setAccessible(true);
						valObj = field.get(valObj);
					}
					
					index++;
				}
				
			} catch (Exception ex) {
				
				valObj = null;
				LoggerProvider.getLogger().debug("getMappedValue - failed to get value for the key: " + key);
			}
		}
		
		return valObj;
	}
	
	/**
	 * Checks whether the object has type which allows further digging into ot.
	 * @param object The object
	 * @return True if the object has atomic type, else false.
	 */
	private boolean isAtomicType(Object object) {
		
		boolean isOk = false;
		
		if(isOk) {
			
			isOk = object instanceof String || object instanceof Byte || object instanceof Short || object instanceof Integer 
					|| object instanceof Long || object instanceof Float || object instanceof Double || object instanceof Enum<?>;			
		}
		
		return isOk;
	}
	
	/**
	 * Binds data for the tokens with special meaning.
	 * @param builder String builder
	 * @param bindData The bind data map.
	 * @param key The key for image.
	 * @param start Token start index
	 * @param end Token end index
	 * @param typeArr Array with associated type keys
	 * @param valObj Value object
	 */
	private void bindDataWithSpecialMeaning(StringBuilder builder, Map<String, Object> bindData, String key, 
			int start, int end, String[] typeArr, Object valObj) {
		
		boolean isOk = null != builder && null != typeArr && 0 < typeArr.length;
		
		if(isOk) {
			
			//As of now, there is only one type associated with one token/key. So taking the first element.
			String type = typeArr[0];
			
			if(TemplateElementType.CHECKBOX.getValue().equals(type) && null != valObj) {
				
				bindCheckBox(builder, start, end, valObj);
			
			} else if(TemplateElementType.RADIO_BUTTON.getValue().equals(type) && null != valObj) {
				
				bindRadioButton(builder, start, end, valObj);
				
			} else if(TemplateElementType.TABLE_DYNAMIC_ROW.getValue().equals(type)) {
				
				bindDynamicDisplayElements(builder, start, end, valObj);
				
			} else if(TemplateElementType.IMAGE.getValue().equals(type)) {
				
				bindImages(builder, bindData, key, start, end, valObj);
				
			} else if(null != type && type.startsWith(TemplateElementType.LIMITED_SIZE_PARAGRAPH.getValue())) {
				
				processAndBindLimitedSizeParagraph(builder, bindData, key, type, start, end, valObj);
				
			} else if(null != type && type.startsWith(TemplateElementType.TRUNCATED_FIELD.getValue())) {
				
				bindTruncatedField(builder, type, start, end, valObj);
				
			} else {
				
				// Replace with empty string if value not found
				replaceTokenWithValue(builder, start, end, "");
			}
		}
	}
	
	/**
	 * Binds check box value to template
	 * @param builder String builder
	 * @param key The key for image.
	 * @param start Token start index
	 * @param end Token end index
	 * @param valObj Value object
	 */
	private void bindCheckBox(StringBuilder builder, int start, int end, Object valObj) {
		
		if(valObj.toString().equalsIgnoreCase("true")) {
			
			//Path to checked checkbox image
			replaceTokenWithValue(builder, start, end, IMG_CHECKED_CHECKBOX);
			
		} else if(valObj.toString().equalsIgnoreCase("false")) {
			
			//Path to unchecked checkbox image
			replaceTokenWithValue(builder, start, end, IMG_UNCHECKED_CHECKBOX);
			
		} else {
			// Replace with empty string if value not found
			replaceTokenWithValue(builder, start, end, "");
		}
	}

	/**
	 * Binds radio button value to template
	 * @param builder String builder
	 * @param key The key for image.
	 * @param start Token start index
	 * @param end Token end index
	 * @param valObj Value object
	 */
	private void bindRadioButton(StringBuilder builder, int start, int end, Object valObj) {
		
		if(valObj.toString().equalsIgnoreCase("true")) {
			
			//Path to checked checkbox image
			replaceTokenWithValue(builder, start, end, IMG_SELECTED_RADIO_BUTTON);
			
		} else if(valObj.toString().equalsIgnoreCase("false")) {
			
			//Path to unchecked checkbox image
			replaceTokenWithValue(builder, start, end, IMG_UNSELECTED_RADIO_BUTTON);
			
		} else {
			
			// Replace with empty string if value not found
			replaceTokenWithValue(builder, start, end, "");
		}
	}

	/**
	 * Binds the display property of the element. Page/element hiding of the report happened here.
	 * @param builder StringBuilder object containing the template 
	 * @param start Start index of the key in the template.
	 * @param end End index of the key in the template.
	 * @param valObj Optional object containing image.
	 */
	private void bindDynamicDisplayElements(StringBuilder builder, int start, int end, Object valObj) {
		
		boolean isOk = null != builder;
		
		if(isOk) {
			
			//This token is expected to present against display attribute of an HTML element.
			//eg: <tr style="display: {{<dyr>SpeechThreshold.speechAudiometry[3]}};">
			if(null != valObj) {
				
				//Path to checked checkbox image
				replaceTokenWithValue(builder, start, end, HTML_ATTR_DISPLAY_DEFAULT);
				
			} else {
				// Replace with empty string if value not found
				replaceTokenWithValue(builder, start, end, HTML_ATTR_DISPLAY_NONE);
			}
		}
	}

	/**
	 * Binds the image to template.
	 * @param builder The StringBuilder object containing the template.
	 * @param bindData The bind data map.
	 * @param key The key for image.
	 * @param start Start index of the key in the template.
	 * @param end End index of the key in the template.
	 * @param valObj Optional object containing image.
	 */
	private void bindImages(StringBuilder builder, Map<String, Object> bindData, String key, int start, int end, Object valObj) {
		
		boolean isOk = false;
		String image = null;
		
		isOk = null != builder && null != key;
		
		if(isOk) {
			
			if(AppConstants.KEY_FULL_CHART_PREVIEW.equals(key)) {
				
				image = getAudiogramImage(bindData, valObj);
				
			} else if(AppConstants.TYMP_FD_LEFT_IMAGE_KEY.equals(key) || AppConstants.TYMP_FD_RIGHT_IMAGE_KEY.equals(key) || 
					AppConstants.ABR_FD_LEFT_IMAGE_KEY.equals(key) || AppConstants.ABR_FD_RIGHT_IMAGE_KEY.equals(key)) {
				
				image = getTympanograOrAbrImage(valObj);
				
			} else {
				
				//Expecting Base64 image.
				image = null != valObj ? valObj.toString() : "";
			}
			
			replaceTokenWithValue(builder, start, end, image);
		}
	}
	
	/**
	 * Gets the audiogram image according to the value of the audiogramOption
	 * @param bindData Bind data map
	 * @param valObj Object containing the value for fullChartPreview
	 * @return The Base64 audiogram image or path to audiogram image.
	 */
	private String getAudiogramImage(Map<String, Object> bindData, Object valObj) {
		
		String audiogramImage  = "";
		String audiogramOption = null;
		
		if(null != bindData) {
			
			/*Get the audiogramOption. Use empty string as default audiogramOption which is equivalent to NONE.*/
			if(bindData.containsKey(AppConstants.KEY_AUDIOGRAM_OPTION)) {
				
				audiogramOption = bindData.get(AppConstants.KEY_AUDIOGRAM_OPTION).toString();
				
			} else {
				
				audiogramOption = "";
			}

			switch (audiogramOption) {
			
			case AppConstants.AUDIOGRAM_OPTION_CNT:
				audiogramImage = IMG_AUDIOGRAM_CNT;
				break;
	
			case AppConstants.AUDIOGRAM_OPTION_DNT:
				audiogramImage = IMG_AUDIOGRAM_DNT;
				break;
				
			case AppConstants.AUDIOGRAM_OPTION_SKIP_TO_PAGE:
				audiogramImage = IMG_AUDIOGRAM_SKIP_TO_PAGE;
				break;
				
			case AppConstants.AUDIOGRAM_OPTION_TYMP:
				audiogramImage = IMG_AUDIOGRAM_TYMP;
				break;
				
			case AppConstants.AUDIOGRAM_OPTION_NONE:
				
				/*If audiogramOption is NONE and fullChartPreview image is present, show that image as audiogram image. 
				 *If audiogramOption is NONE and no fullChartPreview image is not present, show a grid only image. */				
				if(null != valObj) {
				
					audiogramImage = !valObj.toString().isEmpty() ? valObj.toString() : IMG_AUDIOGRAM_GRID_ONLY;
					
				} else {
					
					audiogramImage = IMG_AUDIOGRAM_GRID_ONLY;
				}
				
				break;
				
			default:
				break;
			}
		}
		
		return audiogramImage;
	}
	
	/**
	 * Returns the Tympanometry image to be used in the PDF report.
	 * @param valObj Value object form the bind data map.
	 * @return The Base64 tympanogram/ABR image or path to placeholder image. 
	 */
	private String getTympanograOrAbrImage(Object valObj) {
		
		String tympImage = null;
		
		if(null != valObj) {
			
			tympImage = !valObj.toString().isEmpty() ? valObj.toString() : IMG_TYMP_PLACEHOLDER;
			
		} else {
			
			tympImage = IMG_TYMP_PLACEHOLDER;
		}
		
		return tympImage;
	}
	
	/**
	 * Processes and binds the limited size paragraph.
	 * @param builder StringBuilder containing the template
	 * @param bindData Bind data
	 * @param key Key
	 * @param type Type, should be LIMITED_SIZE_PARAGRAPH
	 * @param start Index of start of TOKEN_START
	 * @param end Index of start of TOKEN_END
	 * @param valObj Value
	 */
	private void processAndBindLimitedSizeParagraph(StringBuilder builder, Map<String, Object> bindData, String key,
			String type, int start, int end, Object valObj) {

		String[] splitParas      = null;
		String   value           = null;
		String   splitValue      = null;
		String[] paragraphParams = null;
		boolean  isOk            = false;
		
		try {
			
			isOk = null != builder && null != bindData && null != key && null != type;
			
			if(isOk) {
				
				paragraphParams = getTypeParameters(type);
				isOk            = null != paragraphParams && 3 <= paragraphParams.length;
			}
			
			if(isOk) {
				
				value      = null != valObj ? valObj.toString() : "";
				value      = getCleanedParagraph(value);
				splitParas = getSplitParagraph(value, Integer.parseInt(paragraphParams[1]), paragraphParams[2]);
				splitValue = null != splitParas && 0 < splitParas.length ? splitParas[0] : "";
				splitValue = formatAndReplaceEscapeSequence(splitValue);
				
				//Pass the value for doNotFormat as true, as the formatting is already done in method formatAndReplaceEscapeSequence
				replaceTokenWithValue(builder, start, end, splitValue, true);
				handleRemainigParagraphTextIfAny(bindData, key, splitParas);
			}
			
		} catch(Exception ex) {
			
			isOk = false;
			LoggerProvider.getLogger().error("TemplateLexicalParser - processAndBindLimitedSizeParagraph - failed due to: ", ex);
		}
		
		//Replace the token with the whole paragraph as is, in case of error.
		if(!isOk) {
			
			replaceTokenWithValue(builder, start, end, valObj);
		}
	}

	/**
	 * Does additional tasks to include the remaining portion of the paragraph
	 * @param bindData Bind data
	 * @param key Key
	 * @param splitParas Split paragraph array
	 */
	private void handleRemainigParagraphTextIfAny(Map<String, Object> bindData, String key, String[] splitParas) {
		
		if(null != bindData && null != key && null != splitParas && 1 < splitParas.length && 0 < splitParas[1].length()) {
			
			splitParas[1] = formatAndReplaceEscapeSequence(splitParas[1]);
			bindData.put(key + PARAGRAPH_CONTNUED_SUFFIX, splitParas[1]);
			
			//Increment the page count, as the second portion of the paragraph is going to be on a new page.
			//This logic will work as we are replacing the token in a sequential order from the start of the template, and 
			//the new pages for the remaining portion of the paragraphs are created in the same order as they appear in the default page.
			pageCount++;
			bindData.put(key + PARAGRAPH_CONTNUED_PAGE_SUFFIX, pageCount);
		}
	}
	
	/**
	 * Replaces escape sequences with corresponding HTML characters or tags.
	 * @param paraContent The paragraph content
	 * @return Escape sequence replaced paragraph content.
	 */
	private String formatAndReplaceEscapeSequence(String paraContent) {

        Matcher matcher = null;

        if(null != paraContent) {

        	try {
        		
        		//Format the paragraph for HTML
        		paraContent = getHTMLFormattedString(paraContent);
        		
                matcher = PATTERN_NEW_LINE_R_N.matcher(paraContent);
                paraContent = matcher.replaceAll(PARAGRAPH_ALLOW_NEW_LINES ? HTML_NEW_LINE_CHAR : WHITE_SPACE_CHAR);
                
                matcher = PATTERN_NEW_LINE_N.matcher(paraContent);
                paraContent = matcher.replaceAll(PARAGRAPH_ALLOW_NEW_LINES ? HTML_NEW_LINE_CHAR : WHITE_SPACE_CHAR);
                
        	} catch(Exception ex) {
        		
        		LoggerProvider.getLogger().error("TemplateLexicalParser - replaceEscapeSequence - Failed to replace escape sequences: ", ex);
        	}
        }

        return paraContent;
    }
	
	/**
	 * Gets the array of hyphen(-) separated parameters associated with the paragraph.
	 * @param type The type associated with the token. This should be a LIMITED_SIZE_PARAGRAPH
	 * @return Array of parameters. Returns null if any validation fails.
	 */
	private String[] getTypeParameters(String type) {
		
		String[] params = null;
		
		if(null != type && type.contains(TYPE_PARAM_SEPARATOR)) {
			
			params = type.split(TYPE_PARAM_SEPARATOR);
		}
		
		return params;
	}
	
	/**
	 * Gets the width required by the text with given font family and font size
	 * @param text Text
	 * @return The required width
	 */
	private int getWidth(String text) {
		
		Font font = new Font(PARAGRAPH_FONT_FAMILY, Font.PLAIN, PARAGRAPH_FONT_SIZE);
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);

		int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
		
		return textwidth;
	}
	
	/**
	 * Splits paragraph into two if the whole paragraph is not going to fit in the given space.
	 * @param paragraph Paragraph
	 * @param maxLine Maximum number of lines allowed in the default section.
	 * @param widthCategory Expecting either F or H.
	 * @return If a split is required, an array containing two elements, where the first one is first portion of the paragraph and 
	 * second one is the remaining portion of the paragraph. 
	 * 
	 * If no split is required, there will be only one element in the array, which will be the full paragraph.
	 */
	private String[] getSplitParagraph(String paragraph, int maxLine, String widthCategory) {
		
		//This width has been calculated by trial and error for the full width
		//paragraph:  DO NOT TOUCH or your head will explode
		final int maxWidthFullPage = 760;
		int lineWidth = 0;
		
		int maxWidth = getMaxWidth(maxWidthFullPage, widthCategory);
		
		String[] splitParas = {""};
		
		String lineStr = "";
		String finalLineStr = "";
		String remainingString = "";
		int lineNumber = 1;
		boolean spaceFull = false;
		
		String[] paras = null;
		
		//New line characters are going to shown as new line in the report only if value of PARAGRAPH_ALLOW_NEW_LINES is true.
		//So split the paragraph at '\n' only if this value is true. Otherwise use the whole paragraph as is.
		if(PARAGRAPH_ALLOW_NEW_LINES) {
			
			paras = paragraph.split("\n");
			
		} else {
			
			paras = new String[1];
			paras[0] = paragraph;
		}
		
		ArrayList<String> lineList = new ArrayList<>();

		//splitting into paragraphs
		for(int index1 = 0; index1 < paras.length; index1++) {
			
			String para = paras[index1];
			if(!spaceFull) {
				String[] words = para.split(" ");
				finalLineStr = "";
				lineStr = "";
				
				//splitting into words
				for(String word: words) {
					
					if(null != word && !word.equals("\r")) {
						
						String currString = word + " ";
						if( !spaceFull ) {
							
							//calculating the width of the string 
							lineStr += currString;
							lineWidth = getWidth(lineStr);
							
							if(lineWidth < maxWidth) {
								finalLineStr += currString;
							} else {
								lineList.add(finalLineStr);
								lineNumber++;
								if(lineNumber > maxLine) {
									spaceFull = true;
								}
								
								if(!spaceFull) {
									lineStr = currString;
									finalLineStr = currString;
								} else {
									remainingString += currString;
								}
							}
						} else {
							remainingString += currString;
						}
					}
				}
				
				if(spaceFull) {
					
					remainingString += "\n";
					
				} else {
					
					lineList.add(finalLineStr);
					lineNumber++;
					if(lineNumber > maxLine) {
						spaceFull = true;
					}
				}

			} else {
				remainingString += para + "\n";
			}
			
		}
		
		String finalMainString = String.join("\n", lineList);
		
		
		if(finalMainString.length() < paragraph.length()) {
			
			splitParas    = new String[2];
			splitParas[0] = finalMainString;
			splitParas[1] = remainingString;
			
		} else {
			
			splitParas = new String[1];
			splitParas[0] = finalMainString;
		}
		
		return splitParas;
	}
	
	/**
	 * Bind a truncated field
	 * @param builder StringBuilder containing the template
	 * @param type Type, should be TRUNCATED_FIELD
	 * @param start Index of start of TOKEN_START
	 * @param end Index of start of TOKEN_END
	 * @param valObj Value
	 */
	private void bindTruncatedField(StringBuilder builder, String type, int start, int end, Object valObj) {
		
		String[] truncatedFieldParams = null;
		String value = null;
		int allowedFieldLength = 0;
		boolean isOk = false;
		
		try {
			
			isOk = null != builder && null != type;
			
			if(isOk) {
				
				truncatedFieldParams = getTypeParameters(type);
				isOk = null != truncatedFieldParams && 2 <= truncatedFieldParams.length;
			}
			
			if(isOk) {
				
				allowedFieldLength = Integer.parseInt(truncatedFieldParams[1]);
				value = null != valObj ? valObj.toString() : "";
				
				if(allowedFieldLength < value.length()) {
					
					value = value.substring(0, allowedFieldLength) + TRUNCATED_FIELD_SUFFIX;
				}
				
				replaceTokenWithValue(builder, start, end, value);
			}
			
		} catch(Exception ex) {
			
			LoggerProvider.getLogger().error("TemplateLexicalParser - bindTruncatedField - failed due to: ", ex);
		}
		
		//In case of validation errors replace the token with whole value
		if(!isOk) {
			
			replaceTokenWithValue(builder, start, end, valObj);
		}
	}
	
	/**
	 * Gets the default page count of the PDF report.
	 * 
	 * @param templateId The template id of the report
	 * @return Default page count of the PDF report.
	 */
	private int getTemplateDefaultPageCount(String templateId) {
		
		int    pageCount    = 0;
		String pageCountStr = null;
		
		if(null != templateId) {
			
			try {
				
				if(AppConstants.TEMPLATE_ID_ABR.equals(templateId)) {
					
					pageCountStr = ConfigurationManager.createInstance().tryGetProperty(AppConstants.PDF_TEMPLATE_ABR_DEF_PAGE_COUNT);
					
				} else if(AppConstants.TEMPLATE_ID_HEARING_TEST.equals(templateId)) {
					
					pageCountStr = ConfigurationManager.createInstance().tryGetProperty(AppConstants.PDF_TEMPLATE_HEARING_TEST_DEF_PAGE_COUNT);
					
				} else if(AppConstants.TEMPLATE_ID_TYMP.equals(templateId)) {
					
					pageCountStr = ConfigurationManager.createInstance().tryGetProperty(AppConstants.PDF_TEMPLATE_TYMP_DEF_PAGE_COUNT);
				}
				
				pageCount = Integer.parseInt(pageCountStr);
				
			} catch(Exception ex) {
				
				LoggerProvider.getLogger().error("TemplateLexicalParser - getTemplateDefaultPageCount - failed due to: ", ex);
			}
		}
		
		return pageCount;
	}
	
	/**
	 * Adds the number of optional pages required to be there in the report. This does not include the additional pages added for large paragraphs.
	 * @param defaultPageCount Default page count from configuration file
	 * @param templateId Template Id
	 * @param bindData Map object containing the data to bind.
	 * @return Page count after considering the optional pages.
	 */
	private int addOptionalPageCount(int defaultPageCount, String templateId, Map<String, Object> bindData) {
		
		int totalDefaultPageCount = defaultPageCount;
		
		if(null != templateId && null != bindData) {
			
			try {
				
				/* TODO: Confirmation reqaired from Ashok
				 * if(AppConstants.TEMPLATE_ID_ABR.equals(templateId) && null !=
				 * bindData.get(AppConstants.ABR_FD_SECOND_PAGE_REQUIRED_KEY) && (boolean)
				 * bindData.get(AppConstants.ABR_FD_SECOND_PAGE_REQUIRED_KEY)) {
				 * 
				 * totalDefaultPageCount++;
				 * 
				 * } else
				 */
					
				if(AppConstants.TEMPLATE_ID_HEARING_TEST.equals(templateId) && 
						null != bindData.get(AppConstants.HEARING_TEST_TYMP_IMAGE_PAGE_REQUIRED_KEY) && 
						(boolean) bindData.get(AppConstants.HEARING_TEST_TYMP_IMAGE_PAGE_REQUIRED_KEY)) {
					
					totalDefaultPageCount++;
				}
				
			} catch(Exception ex) {
				
				LoggerProvider.getLogger().error("addOptionalPageCountIfRequired - failed due to: ", ex);
			}
		}
		
		return totalDefaultPageCount;
	}
	
	/**
	 * Gets the maximum width allowed for a paragraph from the width category.
	 * @param fullWidth Full width
	 * @param widthCatehory Width category of the paragraph
	 * @return Maximum width allowed for a paragraph.
	 */
	private int getMaxWidth(final int fullWidth, String widthCatehory) {
		
		int maxWidth = fullWidth;
		
		if(null != widthCatehory) {
			
			if(AppConstants.PDF_TEMPLATE_PARA_WIDTH_F.equals(widthCatehory)) {
				
				maxWidth = fullWidth;
			
			} else if(AppConstants.PDF_TEMPLATE_PARA_WIDTH_H.equals(widthCatehory)) {
				
				maxWidth = fullWidth / 2;
			}
		}
		
		return maxWidth;
	}
	
	/**
	 * Cleans a paragraph content.
	 * @param paragraph Paragraph content to be cleaned
	 * @return Cleaned string
	 */
	private String getCleanedParagraph(String paragraph) {
		
		Character period = '.';
		
		if(null != paragraph) {
			
			//Remove the leading and trailing white spaces
			paragraph = paragraph.trim();
			
			//Remove the starting semicolon, comma, new lines, etc. if any
			while(!paragraph.isEmpty() && PARAGRAPH_SENTENCE_DELIMITERS.contains(paragraph.charAt(0))) {
				
				paragraph = paragraph.substring(1).trim();
			}
			
			//Remove the ending semicolon, comma, new lines, etc. if any
			while(!paragraph.isEmpty() && PARAGRAPH_SENTENCE_DELIMITERS.contains(paragraph.charAt(paragraph.length() - 1))) {
				
				paragraph = paragraph.substring(0, paragraph.length() - 1).trim();
			}
			
			//Replace all semicolons with period.
			paragraph = paragraph.replaceAll(";", ".");
			
			if(!paragraph.isEmpty() && !period.equals(paragraph.charAt(paragraph.length() - 1))) {
				
				paragraph += ".";
			}
		}
		
		return paragraph;
	}
	
	/**
	 * Converts string from ISO_8859_1 to UTF-8 and escapes the characters for HTML
	 * @param str The string to be formated.
	 * @return The formated string.
	 */
	private String getHTMLFormattedString(String str) {
		
		if(null != str) {
			
			//Decode some special characters
			str = convertFromISO_8859_1ToUTF_8(str);
    		
    		//Make the string HTML compatible
			str = StringEscapeUtils.escapeHtml4(str);
		}
		
		return str;
	}
	
	/**
	 * Converts string from ISO_8859_1 to UTF-8
	 * @param str The string to be converted.
	 * @return The UTF-8 string.
	 */
	private String convertFromISO_8859_1ToUTF_8(String str) {
		
		if(null != str) {
			
			//Decode some special characters
			byte[] bytes = str.getBytes(StandardCharsets.ISO_8859_1);
			str = new String(bytes, StandardCharsets.UTF_8);
		}
		
		return str;
	}
}
