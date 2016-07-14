package com.lle.mydemo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StreamUtil {
    public static String stream2String(InputStream is) throws UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024*8];
        int len = 0;
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
