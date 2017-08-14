package cht.com.cht.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2016/11/18.
 */
public class FromStringUtil {
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 验证手机号
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public  static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证身份证
     * @param idCard
     * @return
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     *   不包含%，&,$的任何字符串
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isIllegal(String str) throws PatternSyntaxException {
        String regex="[^//%$&]{1,}";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(str);
        return m.matches();
    }
}

