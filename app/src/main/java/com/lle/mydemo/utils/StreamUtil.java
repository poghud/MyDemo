package com.lle.mydemo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@SuppressWarnings("unused")
public class StreamUtil {
    /**
     * 将输入流转换成字符串
     * @param is 要转换的输入流
     * @return 转换后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String stream2String(InputStream is) throws UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024*8];
        int len;
        try {
            while((len = is.read(bytes)) != -1){
                baos.write(bytes, 0, len);
                baos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(baos.toByteArray(), "utf-8");
    }
}
