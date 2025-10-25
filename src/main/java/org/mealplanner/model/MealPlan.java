package org.mealplanner.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@Builder
public class MealPlan {
    private final List<Food> meals;
    private final Map<String, String> rationales;
    private Map<String, String> warnings;
}
