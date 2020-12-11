package com.allen.rbac.util;

import org.apache.shiro.util.ByteSource;

import java.io.Serializable;

public class ByteSourceUtils {

    public static ByteSource bytes(byte[] bytes) {
        return new SimpleByteSource(bytes);
    }

    public static ByteSource bytes(String string) {
        return new SimpleByteSource(string.getBytes());
    }

    public static class SimpleByteSource extends org.apache.shiro.util.SimpleByteSource
            implements Serializable {

        private static final long serialVersionUID = 5528101080905698238L;

        public SimpleByteSource(byte[] bytes) {
            super(bytes);
        }
    }
}
