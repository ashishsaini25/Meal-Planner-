package org.mealplanner.util;

import org.mealplanner.model.UserProfile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfileParser {

    // Known allergens
    private static final List<String> KNOWN_ALLERGENS = List.of(
            "peanuts", "milk", "gluten", "soy", "egg", "fish"
    );

    // Natural-language → canonical tags
    private static final Map<String, String> PREFERENCE_MAP = new LinkedHashMap<>();
    static {
        // Order matters — “non vegetarian” before “vegetarian”
        PREFERENCE_MAP.put("non vegetarian", "non_vegetarian");
        PREFERENCE_MAP.put("vegetarian", "vegetarian");
        PREFERENCE_MAP.put("vegan", "vegan");
        PREFERENCE_MAP.put("high protein", "high_protein");
        PREFERENCE_MAP.put("higher protein", "high_protein");
        PREFERENCE_MAP.put("high fiber", "high_fiber");
        PREFERENCE_MAP.put("higher fiber", "high_fiber");
        PREFERENCE_MAP.put("low carb", "low_carb");
        PREFERENCE_MAP.put("milk allowed", "milk_allowed");
    }

    public static UserProfile parse(String freeText) {
        try {
            String text = freeText.toLowerCase();
            text = text.replace("-", " ");

            // Default values
            int age = 30;
            String gender = "other";

            // Extract age and gender
            Matcher ageGenderMatcher = Pattern.compile("(\\d+)(f|m)").matcher(text);
            if (ageGenderMatcher.find()) {
                age = Integer.parseInt(ageGenderMatcher.group(1));
                gender = ageGenderMatcher.group(2);
            }

            // Extract calorie target
            int calorieTarget = 2000;
            Matcher calorieMatcher = Pattern.compile("~?(\\d+)\\s*kcal").matcher(text);
            if (calorieMatcher.find()) {
                calorieTarget = Integer.parseInt(calorieMatcher.group(1));
            }

            // Extract goal
            String goal = "maintain";
            if (text.contains("weight loss")) goal = "weight_loss";
            else if (text.contains("muscle gain")) goal = "muscle_gain";

            // Extract allergens
            List<String> excludeAllergens = new ArrayList<>();
            Matcher allergenMatcher = Pattern.compile("(no|avoid)\\s+(\\w+)").matcher(text);
            while (allergenMatcher.find()) {
                String allergen = allergenMatcher.group(2).trim();
                if (KNOWN_ALLERGENS.contains(allergen)) {
                    excludeAllergens.add(allergen);
                }
            }

            // Extract dietary preferences
            List<String> dietaryPreferences = new ArrayList<>();
            for (Map.Entry<String, String> entry : PREFERENCE_MAP.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                Pattern pattern = Pattern.compile("\\b" + Pattern.quote(key) + "\\b");
                Matcher prefMatcher = pattern.matcher(text);

                if (prefMatcher.find()) {
                    dietaryPreferences.add(value);
                    text = prefMatcher.replaceAll(""); // remove matched part
                }
            }

            // Build user profile
            return UserProfile.builder()
                    .age(age)
                    .gender(gender)
                    .calorieTarget(calorieTarget)
                    .excludeAllergens(excludeAllergens)
                    .dietaryPreferences(dietaryPreferences)
                    .goal(goal)
                    .build();

        } catch (Exception e) {
            return UserProfile.fallback();
        }
    }
}
