package br.com.alteia.microservicechangeit.common.enums.enum_utils;

public class EnumUtils {

    private EnumUtils() {
    }

    public static String formatCode(Integer code) {
        StringBuilder codeStr = new StringBuilder(code.toString());
        while (codeStr.length() < 4) {
            codeStr.insert(0, "0");
        }
        return codeStr.toString();
    }

}
