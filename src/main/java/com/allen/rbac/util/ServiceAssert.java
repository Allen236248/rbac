package com.allen.rbac.util;

public class ServiceAssert {

    public static void assertThat(boolean expression, String reason) {
        if(expression) {
            throw new ServiceException(reason);
        }
    }
}
