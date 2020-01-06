package validator;

import commonMessages.ExceptionMessages;

public class Validator {

    public static void requireNonBlank(String text, String errorMessage) {
        if (text == null || text.trim().length() == 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void requireNonNegative(int number, String errorMessage) {
        if (number <= 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void requireNonNegative(double number, String errorMessage) {
        if (number <= 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void requireNonNull(Object object, String errorMessage) {
        if (object == null) {
            throw new NullPointerException(errorMessage);
        }
    }

    public static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(ExceptionMessages.QUANTITY_ZERO_OR_NEGATIVE);
        }
    }

    public static void validatePrice(double price, double minimumPrice) {
        if (minimumPrice < 0) {
            throw new IllegalArgumentException(ExceptionMessages.MINIMUM_PRICE_MUST_BE_POSITIVE);
        }
        if (price <= 0) {
            throw new IllegalArgumentException(ExceptionMessages.PRICE_ZERO_NEGATIVE);
        }
        if (price < minimumPrice) {
            throw new IllegalArgumentException(ExceptionMessages.PRICE_BELOW_MINIMUM_PRICE);
        }
    }

    public static void validateDiscountPercent(double discountPercent, double price, double minimumPrice) {
        if (discountPercent < 0 || price <= 0 || minimumPrice <= 0) {
            throw new IllegalArgumentException();
        }

        double promotionalPrice = price * (1 - discountPercent / 100);
        if (promotionalPrice < 0) {
            throw new IllegalArgumentException(ExceptionMessages.DISCOUNTED_PRICE_ZERO_NEGATIVE);
        }
        if (promotionalPrice < minimumPrice) {
            throw new IllegalArgumentException(ExceptionMessages.DISCOUNTED_PRICE_BELOW_MINIMUM_PRICE);
        }
    }


    public static void validatePromotionalPricePercent(double promotionalPricePercent, double price, double minimumPrice) {
        double promotionalPrice = price * (1 - promotionalPricePercent / 100);
        if (promotionalPrice <= 0) {
            throw new IllegalArgumentException(ExceptionMessages.DISCOUNTED_PRICE_ZERO_NEGATIVE);
        }
        if (promotionalPrice < minimumPrice) {
            throw new IllegalArgumentException(ExceptionMessages.DISCOUNTED_PRICE_BELOW_MINIMUM_PRICE);
        }
    }

    public static void validateMonth(int month) {
        if (month <= 0 || month > 12) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.MONTH_MUST_BE_BETWEEN_0_1, month));
        }
    }

}
