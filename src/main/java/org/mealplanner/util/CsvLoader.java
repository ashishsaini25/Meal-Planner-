package org.mealplanner.util;

import com.opencsv.CSVReader;
import org.mealplanner.model.Food;
import org.mealplanner.model.MealType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CsvLoader {

    private static final int BATCH_SIZE = 1000;

    public static List<Food> loadFoods(String resourceName) throws Exception {
        InputStream inputStream = CsvLoader.class.getClassLoader().getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new RuntimeException("CSV resource not found: " + resourceName);
        }

        // Read the entire CSV using OpenCSV
        List<String[]> rows;
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            rows = reader.readAll();
        }

        if (rows.isEmpty()) {
            return Collections.emptyList();
        }

        // Remove header row
        rows.remove(0);

        // Split into batches for multithreading
        int numThreads = Runtime.getRuntime().availableProcessors();
        List<Future<List<Food>>> futures = new ArrayList<>();
        List<Food> allFoods = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < rows.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, rows.size());
            List<String[]> batch = rows.subList(i, end);

            futures.add(executor.submit(() -> parseBatch(batch)));
        }

        // Collect results
        for (Future<List<Food>> future : futures) {
            allFoods.addAll(future.get());
        }

        executor.shutdown();

        return allFoods;
    }

    private static List<Food> parseBatch(List<String[]> batch) {
        return batch.stream().map(parts -> {
            try {
                if (parts.length < 7) return null;

                // Allergens
                List<String> allergens = parts[2].trim().equalsIgnoreCase("none") ?
                        Collections.emptyList() :
                        Arrays.stream(parts[2].split("[,;]"))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toList());

                // Tags
                List<String> tags = Arrays.stream(parts[5].split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());

                // Safe numeric parsing
                int calories = parseIntSafe(parts[1].trim());
                double protein = parseDoubleSafe(parts[3].trim());
                double fiber = parseDoubleSafe(parts[4].trim());

                // MealType validation
                String mealTypeStr = parts[6].trim().toLowerCase();
                MealType mealType;
                switch (mealTypeStr) {
                    case "breakfast": mealType = MealType.BREAKFAST; break;
                    case "lunch": mealType = MealType.LUNCH; break;
                    case "dinner": mealType = MealType.DINNER; break;
                    case "snack": mealType = MealType.SNACK; break;
                    default:
                        System.err.println("Unknown meal type: " + mealTypeStr + " in row: " + Arrays.toString(parts));
                        return null; // skip this row
                }

                return Food.builder()
                        .name(parts[0].trim())
                        .calories(calories)
                        .allergens(allergens)
                        .protein(protein)
                        .fiber(fiber)
                        .tags(tags)
                        .mealType(mealType)
                        .build();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
