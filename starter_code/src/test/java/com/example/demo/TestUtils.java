package com.example.demo;


import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;

        try {
            Field targetField = target.getClass().getDeclaredField(fieldName);
            if(!targetField.canAccess(target)){
                targetField.setAccessible(true);
                wasPrivate = true;
            }

            targetField.set(target, toInject);
            if(wasPrivate){
                targetField.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

}

