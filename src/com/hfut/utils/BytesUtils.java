package com.hfut.utils;

import java.io.*;

public class BytesUtils {
    private BytesUtils() {
    }

    public static <T extends Serializable> T bytesToObject(byte[] bytes, Class<T> clazz) throws Exception {
        T obj = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            obj = (T) objectInputStream.readObject();
        } finally {
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
        }
        return obj;
    }

    public static  <T extends Serializable> byte[] objectToBytes(T obj) throws Exception {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();
        } finally {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        }
        return bytes;
    }

}
