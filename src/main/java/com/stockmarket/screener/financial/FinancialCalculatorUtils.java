package com.stockmarket.screener.financial;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinancialCalculatorUtils {

    public static BigDecimal safeDivide(BigDecimal numerator, BigDecimal denominator, int scale) {
        if (numerator == null || denominator == null || denominator.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return numerator.divide(denominator, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal safePercentage(BigDecimal numerator, BigDecimal denominator) {
        BigDecimal ratio = safeDivide(numerator, denominator, 6);
        if (ratio == null) return null;
        return ratio.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal safeGrowth(BigDecimal current, BigDecimal previous) {
        if (current == null || previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        BigDecimal diff = current.subtract(previous);
        return diff.divide(previous.abs(), 6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }
}
