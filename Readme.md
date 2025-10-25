## 🍽️ Meal Planner — README

### 🚀 How to Run

#### 1. Prerequisites

Before starting, ensure you have the following installed:

* **Java 17+** (verify with `java -version`)
* **Gradle** (or use the included Gradle wrapper)
* **(Optional)** **curl** or **Python 3** for running example profiles.

#### 2. Build and Run the Spring Boot App

Execute the following command in the repository's root directory:

```bash
./gradlew bootRun
````

The server will start and be accessible at:
👉 **[http://localhost:8080](http://localhost:8080)**

---

### 3. Generate a Meal Plan via REST

To generate a meal plan, send a **POST** request with a free-text user description to the `/api/meals/generate` endpoint.

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
      // ... meal details ...
    ],
    "rationales": {
      // ... explanations for meal choices ...
    },
    "warnings": {
      // ... notes on unmet goals ...
    },
    "totalCalories": 1580
  },
  "userProfile": {
    "age": 38,
    "gender": "f",
    "goal": "weight_loss",
    "dietaryPreferences": [
      "vegetarian",
      "high_protein",
      "high_fiber"
    ],
    "excludeAllergens": [
      "peanuts"
    ]
  }
}
```

#### 4. Run Example Profiles

The included script `run_profiles.py` demonstrates the service with three pre-defined user profiles (normal, flexible, and conflicting).

```bash
python3 run_profiles.py
```

This script will send the requests and print the resulting generated meal plans.

---

### 📊 CSV Model and Schema

The available food items are modeled in the **`foods.csv`** file, which the service loads into structured `Food` objects.

| Column        | Description                                         | Example                  |
| :------------ | :-------------------------------------------------- | :----------------------- |
| **name**      | Food item name                                      | Lentil Soup              |
| **calories**  | Per serving (kcal)                                  | 250                      |
| **protein**   | grams per serving                                   | 18                       |
| **fiber**     | grams per serving                                   | 6                        |
| **carbs**     | grams per serving                                   | 28                       |
| **tags**      | Qualitative labels (e.g., vegetarian, high_protein) | vegetarian, high_protein |
| **allergens** | Known allergens                                     | peanuts                  |

**💡 Performance Optimization:**
The **CSV loader** uses **multi-threaded parsing** to efficiently process large datasets. Each line of the CSV is parsed in parallel and mapped into `Food` objects, significantly reducing startup time when scaling to hundreds or thousands of food entries.

*The **RationaleGenerator** uses this data to explain meal choices and highlight trade-offs (e.g., “high protein, high fiber” or “slightly high in carbs”).*

---

### 🎯 Problem Framing

The core goal is to **automatically generate personalized meal plans** from descriptive, **free-text user input** (e.g., “38f, vegetarian, avoid peanuts; weight loss ~1600 kcal”).

**Challenge:**
Interpret **vague, human-style text** into structured nutrition data, including age, gender, preferences, allergens, and calorie goals.

**Approach:**

1. **Parse natural language** → structured `UserProfile`.
2. **Match foods** from `foods.csv` aligning with all required constraints.
3. **Generate rationales** and highlight trade-offs or unmet goals (e.g., calorie gap or missing preferences).

---

📂 *This design ensures scalability, readability, and flexibility for future enhancements like macro tracking, custom scoring, or advanced NLP parsing.*

