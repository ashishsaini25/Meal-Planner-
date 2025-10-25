package org.mealplanner.controller;

import org.mealplanner.model.MealPlan;
import org.mealplanner.model.MealPlanResponse;
import org.mealplanner.model.UserProfile;
import org.mealplanner.planner.MealPlanner;
import org.mealplanner.util.UserProfileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealPlannerController {

    @Autowired
    private MealPlanner mealPlanner;


    @PostMapping("/generate")
    public MealPlanResponse generateMealPlan(@RequestBody String request) {
        UserProfile profile = UserProfileParser.parse(request);
        MealPlan plan = mealPlanner.generateMealPlan(profile);
        return new MealPlanResponse(plan, profile);
    }

}
