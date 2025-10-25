import requests
import json

# Base URL of your Spring Boot service
BASE_URL = "http://localhost:8080/api/meals/generate"

# Define the three user profiles (free-text strings)
profiles = [
    "38f, vegetarian, avoid peanuts; weight loss ~1600 kcal; prefers higher protein & fiber",
    "38f, vegan, avoid soy, gluten, nuts; muscle gain ~2000 kcal; prefers high protein, fiber, low-carb",
    "38f, vegan, non-vegetarian, avoid milk, eggs, fish, soy, gluten, nuts; calorie restricted ~1200 kcal; picky eater"
]

def generate_meal_plan(profile_text):
    headers = {"Content-Type": "text/plain"}
    response = requests.post(BASE_URL, headers=headers, data=profile_text)
    if response.status_code == 200:
        return response.json()
    else:
        print(f"Error {response.status_code}: {response.text}")
        return None

# Run the three profiles
for i, profile in enumerate(profiles, start=1):
    print(f"\n=== Profile {i} ===")
    result = generate_meal_plan(profile)
    if result:
        print(json.dumps(result, indent=2))
