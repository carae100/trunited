package io.github.dracosomething.trawakened.helper;

import java.util.Random;

public class MathHelper {
    private static Random random = new Random();

    public static boolean isBetween(Number numbToCheck, Number firstValue, Number secondValue) {
        return numbToCheck.doubleValue() >= firstValue.doubleValue() && numbToCheck.doubleValue() < secondValue.doubleValue();
    }

    public static boolean isAboveOrEqualTo(Number numbToCheck, Number value) {
        return numbToCheck.doubleValue() >= value.doubleValue();
    }

    public static boolean RandomChance(Number chance) {
        return random.nextInt(1, 100) <= 25;
    }
}
