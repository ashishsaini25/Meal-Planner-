package org.mealplanner.planner;

import org.mealplanner.model.Food;
import org.mealplanner.model.MealPlan;
import org.mealplanner.model.MealType;
import org.mealplanner.model.UserProfile;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MealPlanner {

    private final MealSelector mealSelector;
    private final RationaleGenerator rationaleGenerator;

    public MealPlanner(List<Food> allFoods) {
        this.mealSelector = new MealSelector(allFoods);
        this.rationaleGenerator = new RationaleGenerator();
    }

    public MealPlan generateMealPlan(UserProfile userProfile) {
        Map<String, String> warnings = new LinkedHashMap<>();
        Map<String, String> rationales = new LinkedHashMap<>();
        List<Food> selectedMeals = new ArrayList<>();
        Set<String> addedFoodNames = new HashSet<>();

        Map<MealType, List<Food>> foodsByMeal = mealSelector.selectFoods(userProfile);
        mealSelector.sortFoodsByPreference(foodsByMeal, userProfile);

        boolean conflictingPrefs = userProfile.dietaryPreferences().contains("vegan") &&
                userProfile.dietaryPreferences().contains("non_vegetarian");
        if (conflictingPrefs) {
            warnings.put("conflict",
                    "Conflicting dietary preferences detected: 'vegan' vs 'non-vegetarian'. " +
                            "'Non-vegetarian' preferences ignored to prioritize vegan foods.");
        }

        int minCalories = (int) (userProfile.calorieTarget() * 0.9);
        int maxCalories = (int) (userProfile.calorieTarget() * 1.1);


        Map<MealType, Integer> mealCounters = new EnumMap<>(MealType.class);
        Map<MealType, Integer> mealMax = new EnumMap<>(MealType.class);
        for (MealType type : MealType.values()) mealCounters.put(type, 0);
        mealMax.put(MealType.BREAKFAST, 2);
        mealMax.put(MealType.LUNCH, 2);
        mealMax.put(MealType.DINNER, 1);
        mealMax.put(MealType.SNACK, 5);


        MealPlanBuilder.addMeals(selectedMeals, rationales, addedFoodNames, foodsByMeal,
                mealCounters, mealMax, userProfile.calorieTarget(), minCalories, maxCalories,
                warnings, rationaleGenerator);

        return MealPlan.builder()
                .meals(selectedMeals)
                .rationales(rationales)
                .warnings(warnings)
                .build();
    }
}
