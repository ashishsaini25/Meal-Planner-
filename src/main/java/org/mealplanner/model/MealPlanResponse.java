package org.mealplanner.model;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealPlanResponse {
    private  Object mealPlan;
    private  UserProfile userProfile;
    public MealPlanResponse(MealPlan mealPlan, UserProfile userProfile) {
        this.mealPlan = mealPlan;
        this.userProfile = userProfile;
    }
}
