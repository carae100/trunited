package io.github.dracosomething.trawakened.helper;

public class MathHelper {
    public static boolean isBetween(Number numbToCheck, Number firstValue, Number secondValue) {
        return numbToCheck.doubleValue() >= firstValue.doubleValue() && numbToCheck.doubleValue() < secondValue.doubleValue();
    }

    public static boolean isAboveOrEqualTo(Number numbToCheck, Number value) {
        return numbToCheck.doubleValue() >= value.doubleValue();
    }
}
