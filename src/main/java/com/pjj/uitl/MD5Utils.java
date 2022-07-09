package com.pjj.uitl;

import org.apache.catalina.security.SecurityUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MD5Utils {

    public static void main(String[] args) {

//        使用MD5加密
        try {
            String str2 = MD5Utils.md5Encode("789456".getBytes("utf-8"));
            System.out.println(str2);//打印结果  20d7ac67de698a6f67b4effa72da3c53
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    /**
     * 异或实现加密 与 解密 思路:
     * 加密: 输入一个字符串 将字符串转为字符数组 然后 对 字符数组的每一个元素进行异或操作. 最后将每个元素都异或后的字符数组 转为 字符串 返回
     * 解密: 一个数 两次异或后, 就是原数本身 就是说 将第一次调用该方法返回的结果 当成该方法的参数第二次调用 就相当解密了.
     */
    public static String xor(String input) {
        char[] chs = input.toCharArray();//将String转为char[] 这样就可以为 每一个字符进行 异或(加密)
        for (int i = 0; i < chs.length; i++) {
            chs[i] = (char) (chs[i] ^ 3000);//对每一位字符进行异或(加密)
        }
        return new String(chs);//将循环遍历 异或后的 chs 变成 字符串返回
    }

    /**
     * 使用MD5加密(不可逆)
     * MD5 与 SHA256 区别:
     * MD5: 速度较快 (安全性相对低一点)
     * SHA256: 安全性较高 (速度相对慢一点)
     * 需要引入jar   commons-codec.jar
     */
    public static String md5Encode(byte[] input) {
        return DigestUtils.md5Hex(input);
        //DigestUtils.md5(input) 输入一个byte[] 返回一个加密后的的byte[]
        //DigestUtils.md5Hex(input); 输入一个byte[] 返回一个加密后的 String
        //就是说 md5()  和  md5Hex()  都是加密的方法 不过一个是返回byte[] 一个是返回 String
    }

    /**
     * 使用SHA256加密(不可逆)
     * MD5 与 SHA256 区别:
     * MD5: 速度较快 (安全性相对低一点)
     * SHA256: 安全性较高 (速度相对慢一点)
     * 需要引入jar   commons-codec.jar
     */
    public static String sha256Encode(byte[] input) {
        return DigestUtils.sha256Hex(input);
        //DigestUtils.sha256(input); 输入一个byte[] 返回一个加密后的的byte[]
        //DigestUtils.sha256Hex(input); 输入一个byte[] 返回一个加密后的 String
        //就是说 sha256()  和  sha256Hex()  都是加密的方法 不过一个是返回byte[] 一个是返回 String
    }

    /**
     * Base64加密
     */
    public static String base64Encode(byte[] input) {
        Class clz = null;
        String result = "";
        try {
            //这个Base64类 貌似没有构造方法 也不能直接用类名调用静态方法  所以只能通过反射调用
            clz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
            Method encode = clz.getMethod("encode", byte[].class);//利用反射找到encode方法
            result = (String) encode.invoke(null, input);//通过反射调用encode方法    反射调用static方法 不需要传入对象
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Base64解密
     */
    public static byte[] base64Decode(String input) {
        Class clz = null;
        byte[] result = null;
        try {
            //这个Base64类 貌似没有构造方法 也不能直接用类名调用静态方法  所以只能通过反射调用
            clz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
            Method decode = clz.getMethod("decode", String.class);//利用反射找到decode方法
            result = (byte[]) decode.invoke(null, input);//通过反射调用decode方法    反射调用static方法 不需要传入对象
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

}
