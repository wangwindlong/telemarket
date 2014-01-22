/*
 * StringUtil.java        1.0     2009-10-27  9:27:42
 *
 * Copyright (c) 2004, 2005 Works Systems, Inc.
 * Copyright (c) 2004, 2005 Works Systems (Tianjin) Co., Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Works Systems, Inc and Works Systems (Tianjin) Co., Ltd. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Works Systems.
 */
package com.example.tool;

import android.util.Log;
import com.example.telemarket.bean.user.User;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Chris.F
 * Date: 2009-10-27
 * Time: 9:27:42
 */
public final class StringUtil {
    private static String encoding = "UTF-8";
    static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212,
            3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z'};

    public static void setEncoding(String enc) throws UnsupportedEncodingException {
        byte[] b = "".getBytes(enc); // validate encoding, do not remove this line
        assert b != null;
        encoding = enc;
    }

    public static String getEncoding() {
        return encoding;
    }

    public static byte[] getBytes(String str) {
        return getBytes(str, encoding);
    }

    public static String toUtf8(String str) {
        return changeCharset(str, encoding);
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param str        待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws java.io.UnsupportedEncodingException
     *
     */
    public static String changeCharset(String str, String newCharset) {
        try {
            if (str != null) {
                //用默认字符编码解码字符串。
                byte[] bs = str.getBytes();
                //用新的字符编码生成字符串
                return new String(bs, newCharset);
            }

        } catch (UnsupportedEncodingException ex) {

        }
        return null;
    }

    public static byte[] getBytes(String str, String encoding) {
        if (isEmpty(str)) return new byte[0];
        try {
            return isEmpty(encoding) ? str.getBytes() : str.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("invalid encoding: " + encoding, e);
        }
    }

    public static String formString(byte[] bytes) {
        return formString(bytes, encoding);
    }

    public static String formString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) return "";
        try {
            return isEmpty(encoding) ? new String(bytes) : new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("invalid encoding: " + encoding, e);
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isBlank(String s) {
        return isEmpty(s) || isWhitespace(s);
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public static String deletePoint(String s) {
        if (!isBlank(s) && s.contains(".")) {
            return s.substring(0, s.indexOf("."));
        } else {
            return s;
        }
    }

    public static String[] split(String str, String seperator) {
        if (isEmpty(str))
            return new String[0];
        return str.split(seperator);
    }

    public static boolean isWhitespace(String text) {
        if (text == null)
            return false;
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isWhitespace(text.charAt(i)))
                return false;
        }
        return true;
    }

    public static String trim(String str) {
        if (str == null)
            return null;
        return str.trim();
    }

    public static String trimToEmpty(String str) {
        if (str == null)
            return "";
        return str.trim();
    }

    public static String trimToNull(String str) {
        if (isEmpty(str))
            return null;
        return str.trim();
    }

    public static int indexOf(String str, String sub) {
        if (str == null)
            return -1;
        return str.indexOf(sub);
    }

    public static String replaceAllSubstring(String source, String substring, String replacement) {
        return Pattern.compile(substring, Pattern.LITERAL).matcher(source).replaceAll(replacement);
    }


    /**
     * support Numeric format:<br>
     * "33" "+33" "033.30" "-.33" ".33" " 33." " 000.000 "
     *
     * @param str String
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        str = str.trim();
        String regExp = "-?[0-9]+(\\.[0-9]+)?";
        return str.matches(regExp);
    }

    /**
     * support Integer format:<br>
     * "33" "003300" "+33" " -0000 "
     *
     * @param str String
     * @return boolean
     */
    public static boolean isInteger(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        str = str.trim();

        return str.matches("-?[0-9]+");
    }

    public static boolean isContainJson(String data) {
        //获取<U><U>内部的字符串 json 格式  {"TITLE":"CDD","KEY":"jine",VALUES;"fdd"}
        String json_data = "<U(.*?)>(.*?)</U>";
//        String json_data = "<U\\s+name='([^']+)'\\s+value='([^']+)'>(.*?)</U>";
        boolean result = Pattern.compile(json_data).matcher(data).find();
        if (result) {
            return true;
        }
        return false;
    }

    public static List<String> gainContainJson(String data) {
        //获取<U><U>内部的字符串 json 格式  {"TITLE":"CDD","KEY":"jine",VALUES;"fdd"}
        List<String> arrayList = new ArrayList<String>();
        String json_data = "<U(.*?)</U>";
        Matcher result = Pattern.compile(json_data).matcher(data);
        while (result.find()) {
            arrayList.add(result.group());
        }
        return arrayList;
    }

    //判断json是否合法
    public static boolean validateJson(String json) {
        try {
            String str = json.replaceAll("@sy", "\"");
            new JSONObject(str).getString("title");
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 序列化对象
     *
     * @param person
     * @return
     */
    public static String serialize(User person) throws IOException {
//        startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(person);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.e("serial", "serialize str =" + serStr);
//        endTime = System.currentTimeMillis();
//        Log.d("serial", "序列化耗时为:" + (endTime - startTime));
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws ClassNotFoundException
     */
    public static User deSerialization(String str) throws IOException,
            ClassNotFoundException {
        User person = new User();
        try {
//        startTime = System.currentTimeMillis();
            String redStr = java.net.URLDecoder.decode("" + str, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            person = (User) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
//        endTime = System.currentTimeMillis();
            Log.e("deserial", "反序列化str" + person.toString());
//        Log.d("serial", "反序列化耗时为:" + (endTime - startTime));
        } catch (ClassNotFoundException e) {

        } catch (IOException e) {
        }
        return person;
    }


    public static String getSpells(String characters) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < characters.length(); i++) {
            char ch = characters.charAt(i);
            if ((ch >> 7) == 0) {
                //判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
            } else {
                char spell = getFirstLetter(ch);
                buffer.append(String.valueOf(spell));
            }
        }
        return buffer.toString();
    }

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {
        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i] && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }
}