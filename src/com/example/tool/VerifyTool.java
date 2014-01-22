package com.example.tool;

import android.view.SubMenu;
import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-24
 * Time: 上午10:17
 * 校验工具
 */
public class VerifyTool {
    //附件长度验证
    private static boolean checkAccFileLength(SubMenu subMenu) {
        int file_total_length = 0;
        for (int i = 0; i < subMenu.size(); i++) {
            //文件路径+文件名
            int file_length = readFile(subMenu.getItem(i).getTitleCondensed().toString());
            file_total_length += file_length;
        }
        if (300 * 1024 > file_total_length) {
            return true;
        } else {
            return false;
        }
    }


//读文件
    // File还有下面一些常用的操作：
//    String Name = File.getName();  //获得文件或文件夹的名称：
//    String parentPath = File.getParent();  获得文件或文件夹的父目录
//    String path = File.getAbsoultePath();//绝对路经
//    String path = File.getPath();//相对路经
//            File.createNewFile();//建立文件
//            File.mkDir(); //建立文件夹
//            File.isDirectory(); //判断是文件或文件夹
//    File[] files = File.listFiles();  //列出文件夹下的所有文件和文件夹名
//            File.renameTo(dest);  //修改文件夹和文件名
//            File.delete();  //删除文件夹或文件

    public static int readFile(String fileName) {
        String res = "";
        File file = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fis.close();
        } catch (FileNotFoundException e) {
            return 0;
        } catch (IOException e) {
            return 0;
        }
        return res.length();
    }


    //验证短信的内容长度 (控制在70个字符)
    public static boolean checkMessageLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {   /* 获取一个字符 */
            String temp = value.substring(i, i + 1);    /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {   /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                valueLength += 1;       /* 其他字符长度为1 */
            }
        }
        if (valueLength > 120) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkMessageWhite(String value) {
        if (value == null) {
            return true;
        }
        if (value.trim().length() < 1) {
            return true;
        }
        return false;
    }

    //要求3-32位，必须要包含数字，大小写字母
    public static boolean passWordCheck(String s) {
        String str = "^[0-9a-zA-z]{3,20}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    //判断字符串是否有中文
    public static boolean ChineseCheck(String s) {
        String str = "^[\u4e00-\u9fa5]*$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(s);
        return m.matches();
    }
}
