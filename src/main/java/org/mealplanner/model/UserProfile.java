package org.mealplanner.model;

import lombok.Builder;

import java.util.List;

/**
 * @param excludeAllergens renamed for clarity
 */
@Builder
public record UserProfile(int age, String gender, int calorieTarget, List<String> excludeAllergens,
                          List<String> dietaryPreferences, String goal) {
    public static UserProfile fallback() {
        return UserProfile.builder()
                .age(30)
                .gender("other")
                .calorieTarget(2000)
                .excludeAllergens(List.of())
                .dietaryPreferences(List.of())
                .goal("maintain")
                .build();
    }
}
