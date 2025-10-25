package org.mealplanner.config;

import org.mealplanner.model.Food;
import org.mealplanner.planner.MealPlanner;
import org.mealplanner.util.CsvLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public List<Food> allFoods() throws Exception {
        return CsvLoader.loadFoods("foods.csv");   // load CSV once
    }

    @Bean
    public MealPlanner mealPlanner(List<Food> allFoods) {
        return new MealPlanner(allFoods);          // inject into MealPlanner
    }
}

