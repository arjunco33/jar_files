package com.bworks.util;

/**
 * Description: This is a general purpose utility class to handle common methods which can be re used.
 *              It does not hold any data to its member variable.
 *
 */
public class Utility {

    /**
     * 
     * @param text to be converted
     * @return Unicode char converted text.
     */
    public static String convertASCIISymbolsToUnicode(String text) {
        
        String  convertedText = "";
        boolean isOk          = false;
        
        isOk = null != text && !text.isEmpty();
        
        convertedText = text;

        if(isOk && (text.contains("<=") || text.contains("=<"))) {

            convertedText = text.replace("<=", "≤");
            convertedText = convertedText.replace("=<", "≤");
        } else if(isOk && (text.contains(">=") || text.contains("=>"))) {

            convertedText = text.replace(">=", "≥");
            convertedText = convertedText.replace("=>", "≥");
        } else {

            convertedText =  text;
        }
        
        return convertedText;
    }

    /**
     * 
     * @param text to be converted
     * @return ASCII char converted text.
     */
    public static String applyAsciiSymbolCorrection(String text) {
        
        String  convertedText = "";
        boolean isOk          = false;
        
        isOk = null != text && !text.isEmpty();
        
        convertedText = text;

        if(isOk && text.contains("=<")) {

            convertedText = text.replace("=<", "<=");

        } else if(isOk && text.contains("=>")) {

            convertedText = text.replace("=>", ">=");

        } else {

            convertedText =  text;
        }
        
        return convertedText;
    }

    /**
     * 
     * @param text to be converted
     * @return ASCII char converted text.
     */
    public static String convertUniCodeSymbolsToAscii(String text) {
        
        String  convertedText = "";
        boolean isOk          = false;
        
        isOk = null != text && !text.isEmpty();
        
        convertedText = text;

        if(isOk && (text.contains("≤") || text.contains("=<"))) {

            convertedText = text.replace("≤", "<=");
            convertedText = convertedText.replace("=<", "<=");

        } else if(isOk && (text.contains("≥") || text.contains("=>"))) {

            convertedText = text.replace("≥", ">=");
            convertedText = convertedText.replace("=>", ">=");

        } else {

            convertedText =  text;
        }
        
        return convertedText;
    }
    
    public static String asciiToUniCode(String asciiSymbol) {

        String  unicodeSymbol = "";
        boolean isOk          = false;
        
        isOk = null != asciiSymbol && !asciiSymbol.isEmpty();

        // usually "=<" entry should be validated at iPad application but 
        // Client asked to support this entry to as the users may insert <= symbol like this way also. 
        if(isOk && (asciiSymbol.equals("<=") || 
                    asciiSymbol.equals("=<"))) {

            unicodeSymbol = "≤";

        } else if(isOk && (asciiSymbol.equals(">=") || 
                           asciiSymbol.equals("=>"))) {

            unicodeSymbol = "≥";
        }
        else {

            unicodeSymbol = "";
        }

        return unicodeSymbol;
    }

    public static String uniCodeToAscii(String unicodeSymbol) {
        
        String  asciiSymbol = "";
        boolean isOk        = false;
        
        isOk = null != unicodeSymbol && !unicodeSymbol.isEmpty();

        if(isOk && (unicodeSymbol.equals("≤") || 
                    unicodeSymbol.equals("=<"))) {

            asciiSymbol = "<=";

        } else if(isOk && (unicodeSymbol.equals("≥") || 
                unicodeSymbol.equals("=>"))) {

            asciiSymbol = ">=";
        }
        else {

            asciiSymbol = "";
        }

        return asciiSymbol;
    }

    public static String convertUniCodeToHTMLCode(String text) {
        
        String  convertedText = "";
        boolean isOk          = false;
        
        isOk = null != text && !text.isEmpty();
        
        convertedText = text;

        if(isOk && (text.contains("≤") || text.contains("=<"))) {

            convertedText = text.replace("≤", "&le;");
            convertedText = convertedText.replace("=<", "&le;");

        } else if(isOk && (text.contains("≥") || text.contains("=>"))) {

            convertedText = text.replace("≥", "&ge;");
            convertedText = convertedText.replace("=>", "&ge;");

        } else {

            convertedText =  text;
        }
        
        return convertedText;
    }
}
