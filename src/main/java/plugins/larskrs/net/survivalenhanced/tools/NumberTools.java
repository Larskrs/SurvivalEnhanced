package plugins.larskrs.net.survivalenhanced.tools;

import java.util.regex.Pattern;

public class NumberTools {

    private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
