package com.ysk.myview.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/16
 *     desc  : 字符串相关工具类
 * </pre>
 */
public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return (char) (s.charAt(0) - 32) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return (char) (s.charAt(0) + 32) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }


    /**
     * 判断str1中包含str2的个数
     *
     * @param str1
     * @param str2
     * @return counter
     */
    public static int containStr(String str1, String str2) {
        int counter = 0;
        if (!str1.contains(str2)) {
            return 0;
        } else if (str1.contains(str2)) {
            counter++;
            containStr(str1.substring(str1.indexOf(str2) +
                    str2.length()), str2);
            return counter;
        }
        return 0;
    }

    /**
     * 获取汉字字符串的首字母，英文字符不变
     * 例如：阿飞→af
     */
  /*  public static String getSellingsingle(String chines) {
        StringBuffer sb = new StringBuffer();
        char[] chars = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] > 128) {
                try {
                    String[] str = PinyinHelper.toHanyuPinyinStringArray(chars[i], defaultFormat);
                    if (str != null && str.length >= 1) {
                        sb.append(str[0].charAt(0));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }*/

    /**
     * 获取汉字字符串的汉语拼音，英文字符不变
     */
  /*  public static String getSelling(String chines) {
        StringBuffer sb = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    String[] str = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if (str != null) {
                        sb.append(str[0]);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sb.append(nameChar[i]);
            }
        }
        return sb.toString();
    }*/

    /**
     *中文转数字
     * @param inString
     * @return
     */
    public static String getNumber(String inString) {   //提取话术的数字

        char[] strChars = reInt(inString).toCharArray(); // 转化成一个char

        List<String> strKHZ = new ArrayList();

        for (int i = 0; i < strChars.length; i++) {
            try {
                int n = Integer.parseInt(strChars[i] + "");
                strKHZ.add(String.valueOf(n));
            } catch (NumberFormatException e) {
                String s = String.valueOf(inString.charAt(i));
                if (getLenght(s)!=null){
                    strKHZ.add(s);
                }
            }
        }
        if (strKHZ.size()==0){
            return null;
        }
        String numStr = "";
        int numInt = 0;
        for (int i = 0; i < strKHZ.size(); i++) {
            String s1 = strKHZ.get(i);
            numStr = numStr + s1;
            try {
                Integer.parseInt(numStr);
            }catch (Exception e){
                if (numStr.length()==1){
                    numInt = numInt*getLenghtInt(numStr);
                }else {
                    String numCurInt = numStr.substring(0, numStr.length()-1);
                    String numCurStr = numStr.substring(numStr.length()-1);
                    numInt = numInt+ (Integer.valueOf(numCurInt)*getLenghtInt(numCurStr));
                }
                numStr = "";
            }
        }
        if (!numStr.equals("")){
            numInt = numInt+ Integer.parseInt(numStr);
        }
        return numInt+"";
    }

    private static String getLenght(String words){
        switch (words){
            case "十":
            case "百":
            case "千":
            case "万":
            case "亿":
                return words;
            default:
                return null;
        }
    }
    private static int getLenghtInt(String words){
        switch (words){
            case "十":
                return 10;
            case "百":
                return 100;
            case "千":
                return 1000;
            case "万":
                return 10000;
            case "亿":
                return 100000000;
            default:
                return 0;
        }
    }

    public static String reInt(String word) {
        if (word.contains("两")) {
            word = word.replace("两", "2");
        }
        if (word.contains("点")) {
            word = word.replace("点", ".");
        }
        if (word.contains("零")) {
            word = word.replace("零", "0");
        }
        if (word.contains("幺")) {
            word = word.replace("幺", "1");
        }
        if (word.contains("一")) {
            word = word.replace("一", "1");
        }
        if (word.contains("二")) {
            word = word.replace("二", "2");
        }
        if (word.contains("三")) {
            word = word.replace("三", "3");
        }
        if (word.contains("四")) {
            word = word.replace("四", "4");
        }
        if (word.contains("五")) {
            word = word.replace("五", "5");
        }
        if (word.contains("六")) {
            word = word.replace("六", "6");
        }
        if (word.contains("七")) {
            word = word.replace("七", "7");
        }
        if (word.contains("八")) {
            word = word.replace("八", "8");
        }
        if (word.contains("九")) {
            word = word.replace("九", "9");
        }
        return word;
    }

    /**
     * 去除特殊字符
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 比较两个数组的大小
     *
     * @param aList
     * @param bList
     * @return
     */
    public static boolean compareList(List aList, List bList) {
        Collections.sort(aList);
        Collections.sort(bList);

        if (aList == bList) {
            return true;
        }

        if (aList.size() != bList.size()) {
            return false;
        }

        int n = aList.size();
        int i = 0;
        while (n-- != 0) {
            if (aList.get(i).equals(bList.get(i))) {
                return false;
            }

            i++;
        }
        return true;
    }

}