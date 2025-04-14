package io.github.dracosomething.trawakened.helper;

import java.util.Arrays;

public class classHelper {
    public static boolean hasInterface(Class<?> targetClass, Class<?> targetTnterface) {
        System.out.println(targetClass);
        System.out.println(targetTnterface);
        System.out.println(Arrays.stream(targetClass.getInterfaces()).toList().contains(targetTnterface));
        return Arrays.stream(targetClass.getInterfaces()).toList().contains(targetTnterface);
    }
}
