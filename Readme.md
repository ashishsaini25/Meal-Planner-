# 🍽️ Meal Planner — README

## 🚀 How to Run

### 1. Prerequisites

Before starting, ensure you have the following installed:

* **Java 17+** (verify with `java -version`)
* **Gradle** (or use the included Gradle wrapper)
* **Optional:** **curl** or **Python 3** for running example profiles

---

### 2. Build and Run the Spring Boot App

From the repository's root directory, execute:

```bash
./gradlew bootRun
```

The server will start and be accessible at:
👉 [http://localhost:8080](http://localhost:8080)

---

### 3. Generate a Meal Plan via REST

To generate a meal plan, send a **POST** request with free-text user input to the `/api/meals/generate` endpoint.

**Example Request:**

```bash
curl -X POST http://localhost:8080/api/meals/generate \
-H "Content-Type: text/plain" \
-d "38f, vegetarian, avoid peanuts; weight loss ~1600 kcal; prefers higher protein & fiber."
```

**Example JSON Response:**

```json
{
  "mealPlan": {
    "meals": [
      {
        "name": "Edamame & Spinach Salad",
        "calories": 200,
        "protein": 18,
        "carbs": 0,
        "fiber": 8,
        "allergens": ["soy"],
        "tags": ["vegan", "high_protein", "high_fiber"],
        "mealType": "BREAKFAST"
      },
      {
        "name": "Vegetable Soup",
        "calories": 150,
        "protein": 5,
        "carbs": 0,
        "fiber": 4,
        "allergens": [],
        "tags": ["vegan", "high_fiber"],
        "mealType": "LUNCH"
      },
      {
        "name": "Veggie Stir Fry",
        "calories": 300,
        "protein": 12,
        "carbs": 0,
        "fiber": 6,
        "allergens": ["soy"],
        "tags": ["vegan", "high_fiber"],
        "mealType": "DINNER"
      },
      {
        "name": "Orange",
        "calories": 80,
        "protein": 1,
        "carbs": 0,
        "fiber": 3,
        "allergens": [],
        "tags": ["vegan", "high_fiber"],
        "mealType": "SNACK"
      },
      {
        "name": "Coconut Flour Protein Muffins",
        "calories": 200,
        "protein": 15,
        "carbs": 0,
        "fiber": 4,
        "allergens": ["tree_nuts"],
        "tags": ["vegan", "high_protein", "high_fiber"],
        "mealType": "BREAKFAST"
      },
      {
        "name": "Avocado Salad",
        "calories": 250,
        "protein": 5,
        "carbs": 0,
        "fiber": 8,
        "allergens": [],
        "tags": ["vegan", "high_fiber"],
        "mealType": "LUNCH"
      }
    ],
    "rationales": {
      "Edamame & Spinach Salad": "Matches: high protein, high fiber, low carb",
      "Vegetable Soup": "Matches: low carb; Trade-offs: moderate protein: 5g, moderate fiber: 4g",
      "Veggie Stir Fry": "Matches: high fiber, low carb; Trade-offs: moderate protein: 12g",
      "Orange": "Matches: low carb; Trade-offs: moderate protein: 1g, moderate fiber: 3g",
      "Coconut Flour Protein Muffins": "Matches: high protein, low carb; Trade-offs: moderate fiber: 4g",
      "Avocado Salad": "Matches: high fiber, low carb; Trade-offs: moderate protein: 5g"
    },
    "warnings": {},
    "totalCalories": 1180
  },
  "userProfile": {
    "age": 38,
    "gender": "f",
    "calorieTarget": 1200,
    "excludeAllergens": ["milk"],
    "dietaryPreferences": ["vegetarian", "vegan"],
    "goal": "maintain"
  }
}
```

---

### 4. Run Example Profiles

The included script `run_profiles.py` demonstrates the service with three pre-defined user profiles:

```bash
python3 run_profiles.py
```

It will send requests and print the resulting meal plans.

---

## 📊 CSV Model and Schema

The service loads available food items from **`foods.csv`** into structured `Food` objects.

| Column        | Description                          | Example                  |
| ------------- | ------------------------------------ | ------------------------ |
| **name**      | Food item name                       | Lentil Soup              |
| **calories**  | Per serving (kcal)                   | 250                      |
| **protein**   | grams per serving                    | 18                       |
| **fiber**     | grams per serving                    | 6                        |
| **carbs**     | grams per serving                    | 28                       |
| **tags**      | Labels like vegetarian, high_protein | vegetarian, high_protein |
| **allergens** | Known allergens                      | peanuts                  |

**Performance Optimization:**
The CSV loader uses **multi-threaded parsing** to efficiently process large datasets, mapping each line into `Food` objects in parallel.

*The **RationaleGenerator** uses this data to explain meal choices and highlight trade-offs, e.g., “high protein, high fiber” or “slightly high in carbs.”*

---

## 🎯 Problem Framing

The goal is to **automatically generate personalized meal plans** from **free-text user input**, e.g., `"38f, vegetarian, avoid peanuts; weight loss ~1600 kcal"`.

**Challenges:**

* Parsing **vague natural language** into structured `UserProfile`
* Matching foods from `foods.csv` that satisfy constraints
* Generating rationales and highlighting trade-offs (e.g., calorie gaps, unmet preferences)

**Approach:**

1. Parse user text → `UserProfile`
2. Match foods that align with dietary restrictions, allergens, and goals
3. Generate rationales explaining choices and trade-offs

---

This design ensures **scalability**, **readability**, and **future flexibility** for enhancements such as macro tracking, custom scoring, or advanced NLP parsing.

---
