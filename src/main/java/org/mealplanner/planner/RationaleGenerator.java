package org.mealplanner.planner;

import org.mealplanner.model.Food;

import java.util.ArrayList;
import java.util.List;

public class RationaleGenerator {

    public String generateRationale(Food food) {
        List<String> positives = new ArrayList<>();
        List<String> tradeOffs = new ArrayList<>();

        // Protein
        if (food.getProtein() >= 15) positives.add("high protein");
        else if (food.getProtein() > 0) tradeOffs.add("moderate protein: " + (int) food.getProtein() + "g");

        // Fiber
        if (food.getFiber() >= 5) positives.add("high fiber");
        else if (food.getFiber() > 0) tradeOffs.add("moderate fiber: " + (int) food.getFiber() + "g");

        // Carbs
        if (food.getCarbs() <= 30) positives.add("low carb");
        else tradeOffs.add("higher carbs: " + (int) food.getCarbs() + "g");

        // Calories
        if (food.getCalories() > 500) tradeOffs.add("high calorie: " + food.getCalories() + " kcal");

        // Build rationale string
        StringBuilder rationale = new StringBuilder();
        if (!positives.isEmpty()) {
            rationale.append("Matches: ").append(String.join(", ", positives));
        }
        if (!tradeOffs.isEmpty()) {
            if (!rationale.isEmpty()) rationale.append("; ");
            rationale.append("Trade-offs: ").append(String.join(", ", tradeOffs));
        }

        return rationale.isEmpty() ? "No notable features" : rationale.toString();
    }
}
