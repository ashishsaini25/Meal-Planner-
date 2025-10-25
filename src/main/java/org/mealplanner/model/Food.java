package org.mealplanner.model;

import java.util.*;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Food {
   private String name;
   private int calories;
   private double protein;
   private double carbs;
   private double fiber;
   private List<String> allergens;
   private List<String> tags;
   private MealType  mealType;

    public double computeScore(UserProfile profile) {
        double score = 0.0;

        if(profile.dietaryPreferences().contains("high_protein")) {
            score += Math.min(2.0, this.protein / 10.0);
        }

        if(profile.dietaryPreferences().contains("high_fiber")) {
            score += Math.min(2.0, this.fiber / 5.0);
        }

        if(profile.dietaryPreferences().contains("low_carb")) {
            score += Math.max(0, 1.0 - (this.carbs / 50.0));
        }

        if(profile.dietaryPreferences().contains("milk_allowed") && this.tags.contains("milk")) {
            score += 1.0;
        }

        return score;
    }


}
