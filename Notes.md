# 🧩 Meal Planner — Notes

### 🎯 Scoring Design

The meal selection process combines both **rule-based filtering** and **numeric scoring** to recommend foods that best match a user's goals and preferences.

1. **Filtering Stage**
    - Remove foods containing allergens listed in `excludeAllergens`.
    - Resolve conflicts:
        - If both *vegan* and *non-vegetarian* are present, prefer *vegan* foods.
        - Otherwise, select foods that match the user's dominant dietary tag (`vegan`, `vegetarian`, or `non_vegetarian`).

2. **Scoring Stage (`computeScore`)**
   Each food is assigned a weighted score based on its nutritional properties:
    - `+` Up to **2.0 pts** for high protein (`≥15g`).
    - `+` Up to **2.0 pts** for high fiber (`≥5g`).
    - `+` Up to **1.0 pt** bonus for low carbs (`≤30g`).
    - `+` **1.0 pt** if milk is allowed and food contains milk.
    - (Future: small negative penalties for excess calories or macros that oppose user goals.)

3. **Sorting**
    - Foods are first ranked by **preference match count**, then by their computed score.
    - Calories are used as a final tie-breaker to slightly prefer lighter options.

---

### 🧰 Fallback Strategy

To maintain robustness, the planner includes fallback defaults:
- **UserProfile Fallback:**  
  If parsing fails, defaults are applied (`age=30`, `gender=other`, `calorieTarget=2000`, `goal=maintain`).
- **Scoring Fallback:**  
  Foods missing nutrient data are scored neutrally (0 impact on ranking).
- **Preference Conflicts:**  
  The system resolves contradictory dietary tags (e.g., “vegan + non-vegetarian”) by prioritizing *vegan*.

These ensure graceful degradation rather than runtime failure, even for incomplete or ambiguous user input.

---

### 🚀 What to Improve Next

1. **Calorie Balancing per Meal:**  
   Dynamically distribute daily calorie targets across breakfast, lunch, and dinner for balanced meal plans.

2. **Penalty-Based Scoring:**  
   Introduce negative weights for foods that overshoot calorie or macro limits, improving realism and explainability.

3. **NLP Enrichment:**  
   Enhance free-text parsing with synonyms (e.g., “no dairy” → milk exclusion) and contextual cues using lightweight NLP.

---

**Summary:**  
The current design ensures reliability, interpretability, and user alignment. Future iterations will focus on smarter scoring and more personalized nutrition insights.
