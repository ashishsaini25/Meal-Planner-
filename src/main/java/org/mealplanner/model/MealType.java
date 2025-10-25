package org.mealplanner.model;

public enum MealType {
    BREAKFAST,
    LUNCH,
    SNACK,
    DINNER;

    public static MealType fromString(String value) {
        if (value == null) return null;
        switch (value.trim().toLowerCase()) {
            case "breakfast": return BREAKFAST;
            case "lunch": return LUNCH;
            case "dinner": return DINNER;
            case "snack": return SNACK;
            default: throw new IllegalArgumentException("Unknown meal type: " + value);
        }
    }
}
