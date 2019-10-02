package com.luoheng.crawler.smart.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: lzh
 * @create: 2019-09-11 15:49
 **/
public class Command<T> {

    public Object execute(T t, String functionName, Object... args)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class clazz = t.getClass();
        Method targetMethod = null;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods){
            if (functionName.equals(method.getName())){
                int paramsCount = method.getParameterCount();
                if (args.length == paramsCount){
                    targetMethod = method;
                    Class[] paramsTypes = method.getParameterTypes();
                    for (int i = 0; i < paramsTypes.length; i++){
                        if (!paramsTypes[i].getName().equals(args[i].getClass().getName())){
                            String n = args[i].getClass().getName();
                            targetMethod = null;
                            break;
                        }
                    }
                }
                if (targetMethod != null)
                    break;
            }
        }
        if (targetMethod == null && args.length >= 1){
            targetMethod = clazz.getDeclaredMethod(functionName, Object[].class);
            return targetMethod.invoke(t, new Object[]{args});
        }
        return targetMethod.invoke(t, args);
    }

    public static void main(String[] args) throws Exception{
        System.out.println(System.currentTimeMillis());
    }
}
