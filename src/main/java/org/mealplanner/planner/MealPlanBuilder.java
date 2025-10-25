package org.mealplanner.planner;

import org.mealplanner.model.Food;
import org.mealplanner.model.MealType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MealPlanBuilder {

    public static void addMeals(List<Food> selectedMeals,
                                Map<String, String> rationales,
                                Set<String> addedFoodNames,
                                Map<MealType, List<Food>> foodsByMeal,
                                Map<MealType, Integer> mealCounters,
                                Map<MealType, Integer> mealMax,
                                int calorieTarget,
                                int minCalories,
                                int maxCalories,
                                Map<String, String> warnings,
                                RationaleGenerator rationaleGenerator) {

        List<MealType> mealOrder = List.of(MealType.BREAKFAST, MealType.LUNCH, MealType.DINNER, MealType.SNACK);
        int mealIndex = 0;
        int totalCalories = 0;

        while (totalCalories < minCalories) {
            MealType currentMeal = mealOrder.get(mealIndex % mealOrder.size());
            List<Food> options = foodsByMeal.getOrDefault(currentMeal, List.of());
            int currentCount = mealCounters.get(currentMeal);

            if (currentCount < options.size()) {
                Food nextFood = options.get(currentCount);
                if (!addedFoodNames.contains(nextFood.getName()) &&
                        totalCalories + nextFood.getCalories() <= maxCalories) {

                    selectedMeals.add(nextFood);
                    addedFoodNames.add(nextFood.getName());
                    totalCalories += nextFood.getCalories();
                    mealCounters.put(currentMeal, currentCount + 1);
                    rationales.put(nextFood.getName(), rationaleGenerator.generateRationale(nextFood));
                    mealMax.put(currentMeal, mealMax.get(currentMeal) + 1);
                }
            } else {
                warnings.put("calorie_gap",
                        String.format("Calorie target of %d kcal could not be fully met. Only %d kcal achieved.",
                                calorieTarget, totalCalories));
                break;
            }

            mealIndex++;
            if (mealIndex > 1000) break;
        }
    }
}
