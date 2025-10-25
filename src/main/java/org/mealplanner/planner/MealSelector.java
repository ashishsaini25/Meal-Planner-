package org.mealplanner.planner;

import org.mealplanner.model.Food;
import org.mealplanner.model.MealType;
import org.mealplanner.model.UserProfile;

import java.util.*;
import java.util.stream.Collectors;

public record MealSelector(List<Food> allFoods) {

    public Map<MealType, List<Food>> selectFoods(UserProfile userProfile) {
        boolean conflictingPrefs = userProfile.dietaryPreferences().contains("vegan") &&
                userProfile.dietaryPreferences().contains("non_vegetarian");

        return allFoods.stream()
                .filter(f -> Collections.disjoint(f.getAllergens(), userProfile.excludeAllergens()))
                .filter(f -> {
                    if (conflictingPrefs) return f.getTags().contains("vegan");
                    if (userProfile.dietaryPreferences().contains("vegan")) return f.getTags().contains("vegan");
                    if (userProfile.dietaryPreferences().contains("vegetarian"))
                        return f.getTags().contains("vegetarian");
                    if (userProfile.dietaryPreferences().contains("non_vegetarian"))
                        return f.getTags().contains("non_vegetarian");
                    return true;
                })
                .collect(Collectors.groupingBy(Food::getMealType));
    }

    public void sortFoodsByPreference(Map<MealType, List<Food>> foodsByMeal, UserProfile userProfile) {
        foodsByMeal.values().forEach(list -> list.sort((f1, f2) -> {
            int cmp = Integer.compare(computePreferenceMatch(f2, userProfile),
                    computePreferenceMatch(f1, userProfile));
            if (cmp != 0) return cmp;
            cmp = Double.compare(f2.computeScore(userProfile), f1.computeScore(userProfile));
            if (cmp != 0) return cmp;
            return Integer.compare(f1.getCalories(), f2.getCalories());
        }));
    }

    private int computePreferenceMatch(Food food, UserProfile userProfile) {
        int score = 0;
        if (userProfile.dietaryPreferences().contains("high_protein") && food.getProtein() >= 15) score++;
        if (userProfile.dietaryPreferences().contains("high_fiber") && food.getFiber() >= 5) score++;
        if (userProfile.dietaryPreferences().contains("low_carb") && food.getCarbs() <= 30) score++;
        return score;
    }
}
