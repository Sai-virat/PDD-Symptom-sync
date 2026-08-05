import os
import json
import logging
from typing import List, Dict, Any, Optional
from dotenv import load_dotenv

load_dotenv()

logger = logging.getLogger("symptomsync_ai")

# Check Gemini API Key
GEMINI_API_KEY = os.getenv("GEMINI_API_KEY") or os.getenv("GOOGLE_API_KEY")

gemini_model = None
if GEMINI_API_KEY:
    try:
        import google.generativeai as genai
        genai.configure(api_key=GEMINI_API_KEY)
        gemini_model = genai.GenerativeModel("gemini-1.5-flash")
        logger.info("Google Gemini AI model successfully initialized.")
    except Exception as e:
        logger.warning(f"Failed to initialize Gemini AI: {e}")

def generate_ai_diet_plan(symptoms: List[str], fallback_symptoms_data: List[Dict[str, Any]]) -> Dict[str, Any]:
    """
    Generates a comprehensive AI-Powered Symptom-Based Full-Day Diet Plan.
    Tries Google Gemini 1.5 Flash first; if unavailable, uses intelligent multi-symptom synthesis.
    """
    cleaned_symptoms = [s.strip() for s in symptoms if s and s.strip()]
    if not cleaned_symptoms:
        cleaned_symptoms = ["General Wellness"]

    symptoms_str = ", ".join(cleaned_symptoms)

    # 1. Attempt Gemini Generative AI
    if gemini_model:
        try:
            prompt = f"""
You are a top-tier clinical nutritionist and functional medicine expert.
A patient is experiencing the following symptoms: {symptoms_str}.

Generate a personalized, symptom-targeted FULL-DAY DIET PLAN in strict JSON format.
Ensure the JSON matches this structure exactly without any markdown wrappers or text outside JSON:

{{
  "isAiGenerated": true,
  "aiBadgeText": "✨ AI-Powered Gemini Analysis",
  "analysis": [
    {{
      "name": "Symptom Name",
      "severity": "High/Medium/Low",
      "impact": "Biological explanation of why this happens."
    }}
  ],
  "possibleCauses": [
    {{
      "targetSymptom": "Migraine",
      "title": "Tyramine Trigger & Vascular Spasms",
      "description": "Aged foods and processed meats trigger vascular spasms in sensitive individuals.",
      "recommendedDiet": "Low-Tyramine & Magnesium-Rich Neuro-Balancing Diet",
      "foodsToAvoid": ["Aged Cheese", "Processed Meats", "Red Wine", "MSG", "Chocolate"]
    }}
  ],
  "dietPlan": [
    {{
      "mealType": "Breakfast",
      "time": "8:00 AM",
      "targetSymptom": "Migraine",
      "targetCause": "Tyramine Trigger",
      "name": "Ginger & Oats Anti-Inflammatory Bowl",
      "description": "Ginger reduces neuro-inflammation and settles nausea.",
      "calories": "320 kcal",
      "protein": "10g",
      "fiber": "7g",
      "keyNutrient": "Magnesium & Gingerol",
      "preparation": "Simmer oats in almond milk with fresh ginger."
    }},
    {{
      "mealType": "Lunch",
      "time": "1:00 PM",
      "targetSymptom": "Migraine",
      "targetCause": "Tyramine Trigger",
      "name": "B-Complex Spinach & Grilled Chicken Salad",
      "description": "Riboflavin (B2) stabilizes neural pathways.",
      "calories": "450 kcal",
      "protein": "38g",
      "fiber": "6g",
      "keyNutrient": "B-Complex & Plant Iron",
      "preparation": "Toss steamed spinach and grilled chicken with olive oil."
    }},
    {{
      "mealType": "Evening Snack",
      "time": "4:30 PM",
      "targetSymptom": "Migraine",
      "targetCause": "Tyramine Trigger",
      "name": "Magnesium Pumpkin Seeds & Almonds",
      "description": "Magnesium regulates neurotransmitter release.",
      "calories": "180 kcal",
      "protein": "8g",
      "fiber": "3g",
      "keyNutrient": "Magnesium & Healthy Fats",
      "preparation": "Handful of raw unsalted seeds and nuts."
    }},
    {{
      "mealType": "Dinner",
      "time": "7:30 PM",
      "targetSymptom": "Migraine",
      "targetCause": "Tyramine Trigger",
      "name": "Steamed Cod & Wild Rice",
      "description": "Low-histamine lean protein for soothing recovery.",
      "calories": "410 kcal",
      "protein": "32g",
      "fiber": "5g",
      "keyNutrient": "Omega-3 & Selenium",
      "preparation": "Steam cod with lemon over wild rice."
    }}
  ],
  "hydrationGoal": "3.0 Liters / day (Warm Water with Electrolytes & Herbal Tea)",
  "foodsToAvoid": [
    "Aged Cheese",
    "Processed Meats",
    "Red Wine"
  ],
  "lifestyleRecommendations": [
    "Tip 1 regarding sleep/stress/posture",
    "Tip 2 regarding meal timing"
  ]
}}

CRITICAL INSTRUCTIONS FOR MULTIPLE SYMPTOMS:
If the patient reports multiple symptoms (e.g., Symptoms: Migraine and Bloating):
1. Under "dietPlan", you must generate a separate, complete 4-meal plan (Breakfast, Lunch, Evening Snack, Dinner) for EACH symptom. For example, if there are 2 symptoms, the "dietPlan" array must contain exactly 8 meals (4 tailored to Migraine and 4 tailored to Bloating). Ensure they have distinct meals and specify "targetSymptom" and "targetCause" for each.
2. Under "possibleCauses", list causes for each symptom. Each cause object must include a customized "recommendedDiet" and a distinct "foodsToAvoid" array for that specific symptom. Ensure the "foodsToAvoid" lists are different and relevant to that particular symptom.
3. Under "lifestyleRecommendations", provide only a single list of 3 general tips.
"""
            response = gemini_model.generate_content(prompt)
            raw_text = response.text.strip()
            
            # Clean markdown JSON block if present
            if raw_text.startswith("```json"):
                raw_text = raw_text[7:]
            if raw_text.startswith("```"):
                raw_text = raw_text[3:]
            if raw_text.endswith("```"):
                raw_text = raw_text[:-3]
            raw_text = raw_text.strip()

            parsed = json.loads(raw_text)
            return parsed
        except Exception as e:
            logger.error(f"Gemini generation error: {e}. Falling back to Smart Multi-Symptom Synthesis.")

    # 2. Intelligent Dynamic Multi-Symptom Synthesis Engine (Fallback)
    return build_smart_synthesized_plan(cleaned_symptoms, fallback_symptoms_data)


def build_smart_synthesized_plan(symptoms: List[str], dataset: List[Dict[str, Any]]) -> Dict[str, Any]:
    """
    Intelligent synthesis engine that combines knowledge base items with dynamic meal balancing
    to provide a complete 4-meal Full-Day Diet Plan for each combination of symptoms.
    """
    data_map = {item["name"].lower(): item for item in dataset}
    matched_items = []
    
    for sym in symptoms:
        s_lower = sym.lower()
        if s_lower in data_map:
            matched_items.append(data_map[s_lower])
        else:
            # Partial match
            for name, item in data_map.items():
                if name in s_lower or s_lower in name:
                    matched_items.append(item)
                    break

    analysis = []
    possible_causes = []
    foods_to_avoid = set()

    for sym in symptoms:
        analysis.append({
            "name": sym,
            "severity": "Medium",
            "impact": f"Targeted dietary intervention designed to soothe systemic pathways for {sym}."
        })

    # Enrich causes with explicit symptom, diet, & foodsToAvoid mapping
    for item in matched_items:
        sym_name = item.get("name", "General")
        avoid_list = item.get("foodsToAvoid", ["Ultra-Processed Foods", "Refined Sugars"])
        for cause in item.get("possibleCauses", []):
            cause_copy = dict(cause)
            cause_copy["targetSymptom"] = sym_name
            cause_copy["associatedCondition"] = f"Condition: {sym_name} Pathological Mechanism"
            cause_copy["recommendedDiet"] = f"Recommended: {sym_name} Recovery & Anti-Inflammatory Diet"
            cause_copy["foodsToAvoid"] = avoid_list
            if not any(c.get("title") == cause_copy.get("title") for c in possible_causes):
                possible_causes.append(cause_copy)
        for food in avoid_list:
            foods_to_avoid.add(food)

    if not possible_causes:
        sym_label = ", ".join(symptoms)
        possible_causes = [
            {
                "targetSymptom": sym_label,
                "associatedCondition": f"Disease Target: {sym_label} Inflammatory Response",
                "title": "Metabolic & Inflammatory Strain",
                "description": f"Combined systemic reaction triggered by {sym_label}.",
                "recommendedDiet": f"Targeted {sym_label} Recovery Diet Plan",
                "foodsToAvoid": ["Refined Sugars", "Trans Fats", "Deep Fried Foods"]
            },
            {
                "targetSymptom": sym_label,
                "associatedCondition": f"Disease Target: {sym_label} Electrolyte & Micronutrient Shift",
                "title": "Nutritional Imbalance & Hydration Gap",
                "description": "Insufficient micronutrients and fluids heightening symptom severity.",
                "recommendedDiet": f"Hydrating & Micronutrient-Rich {sym_label} Diet",
                "foodsToAvoid": ["Excessive Caffeine", "Sodium-Dense Snacks", "Carbonated Drinks"]
            }
        ]

    if not foods_to_avoid:
        foods_to_avoid = {"Ultra-Processed Foods", "Refined Sugars", "Trans Fats", "Excessive Caffeine"}

    # Assemble Full-Day Meal Plan (4 meals per symptom)
    full_day_diet = []
    
    # Process matched items first
    for item in matched_items:
        sym_name = item.get("name", "General")
        dp = item.get("dietPlan", {})
        
        # Extract or create fallback meals for this symptom
        b_meal = dp.get("Breakfast") or {
            "name": f"{sym_name} Anti-Inflammatory Oats Bowl",
            "description": f"Nutritious oatmeal with berries to reduce cellular strain from {sym_name}.",
            "calories": "330 kcal", "protein": "11g", "fiber": "7g",
            "time": "8:00 AM", "keyNutrient": "Fiber & Antioxidants",
            "preparation": "Simmer oats in warm water or almond milk. Top with blueberries."
        }
        l_meal = dp.get("Lunch") or {
            "name": f"{sym_name} Vitality Grain Bowl",
            "description": f"Digestive-soothing quinoa and mineral-rich greens to support {sym_name} recovery.",
            "calories": "440 kcal", "protein": "18g", "fiber": "8g",
            "time": "1:00 PM", "keyNutrient": "Plant Iron & Minerals",
            "preparation": "Mix cooked quinoa with steamed spinach and lean protein."
        }
        s_meal = dp.get("Snacks") or {
            "name": f"{sym_name} Soothing Snack",
            "description": f"Hydrating, low-glycemic snack assisting {sym_name} relief.",
            "calories": "150 kcal", "protein": "7g", "fiber": "3g",
            "time": "4:30 PM", "keyNutrient": "Vitamins & Hydration",
            "preparation": "Slice fresh cucumbers or papaya. Serve with raw pumpkin seeds."
        }
        d_meal = dp.get("Dinner") or {
            "name": f"{sym_name} Restorative Supper",
            "description": f"Light, anti-inflammatory dinner to alleviate overnight {sym_name} symptoms.",
            "calories": "390 kcal", "protein": "28g", "fiber": "6g",
            "time": "7:30 PM", "keyNutrient": "Lean Omega-3",
            "preparation": "Steam white fish or tofu. Serve with roasted carrots."
        }
        
        # Append all 4 meal types for this symptom
        full_day_diet.append({
            "mealType": "Breakfast",
            "time": b_meal.get("time", "8:00 AM"),
            "targetSymptom": sym_name,
            "targetCause": item.get("possibleCauses", [{}])[0].get("title", f"{sym_name} Trigger"),
            "name": b_meal.get("name"),
            "description": b_meal.get("description"),
            "calories": b_meal.get("calories", "340 kcal"),
            "protein": b_meal.get("protein", "12g"),
            "fiber": b_meal.get("fiber", "8g"),
            "keyNutrient": b_meal.get("keyNutrient", "Fiber"),
            "preparation": b_meal.get("preparation", "Prepare simple ingredients.")
        })
        full_day_diet.append({
            "mealType": "Lunch",
            "time": l_meal.get("time", "1:00 PM"),
            "targetSymptom": sym_name,
            "targetCause": item.get("possibleCauses", [{}])[0].get("title", f"{sym_name} Trigger"),
            "name": l_meal.get("name"),
            "description": l_meal.get("description"),
            "calories": l_meal.get("calories", "450 kcal"),
            "protein": l_meal.get("protein", "18g"),
            "fiber": l_meal.get("fiber", "10g"),
            "keyNutrient": l_meal.get("keyNutrient", "Minerals"),
            "preparation": l_meal.get("preparation", "Combine and steam.")
        })
        full_day_diet.append({
            "mealType": "Evening Snack",
            "time": s_meal.get("time", "4:30 PM"),
            "targetSymptom": sym_name,
            "targetCause": item.get("possibleCauses", [{}])[0].get("title", f"{sym_name} Trigger"),
            "name": s_meal.get("name"),
            "description": s_meal.get("description"),
            "calories": s_meal.get("calories", "160 kcal"),
            "protein": s_meal.get("protein", "7g"),
            "fiber": s_meal.get("fiber", "3g"),
            "keyNutrient": s_meal.get("keyNutrient", "Vitamins"),
            "preparation": s_meal.get("preparation", "Eat raw and fresh.")
        })
        full_day_diet.append({
            "mealType": "Dinner",
            "time": d_meal.get("time", "7:30 PM"),
            "targetSymptom": sym_name,
            "targetCause": item.get("possibleCauses", [{}])[0].get("title", f"{sym_name} Trigger"),
            "name": d_meal.get("name"),
            "description": d_meal.get("description"),
            "calories": d_meal.get("calories", "390 kcal"),
            "protein": d_meal.get("protein", "30g"),
            "fiber": d_meal.get("fiber", "6g"),
            "keyNutrient": d_meal.get("keyNutrient", "Healthy Fats"),
            "preparation": d_meal.get("preparation", "Light cooking before bed.")
        })

    # Process symptoms that were NOT in the dataset
    matched_names = {item.get("name", "").lower() for item in matched_items}
    for sym in symptoms:
        if sym.lower() not in matched_names:
            full_day_diet.append({
                "mealType": "Breakfast",
                "time": "8:00 AM",
                "targetSymptom": sym,
                "targetCause": f"{sym} Inflammatory Trigger",
                "name": f"{sym} Anti-Inflammatory Oats Bowl",
                "description": f"Oatmeal with berries and nuts to soothe {sym}.",
                "calories": "330 kcal",
                "protein": "11g",
                "fiber": "7g",
                "keyNutrient": "Antioxidants",
                "preparation": "Simmer oats in warm water. Add berries and raw walnuts."
            })
            full_day_diet.append({
                "mealType": "Lunch",
                "time": "1:00 PM",
                "targetSymptom": sym,
                "targetCause": f"{sym} Inflammatory Trigger",
                "name": f"{sym} Vitality Grain Bowl",
                "description": "Quinoa & green salad providing essential micronutrients.",
                "calories": "440 kcal",
                "protein": "18g",
                "fiber": "8g",
                "keyNutrient": "Plant Minerals",
                "preparation": "Mix quinoa with steamed spinach, cucumber, and clean protein."
            })
            full_day_diet.append({
                "mealType": "Evening Snack",
                "time": "4:30 PM",
                "targetSymptom": sym,
                "targetCause": f"{sym} Inflammatory Trigger",
                "name": f"Hydrating {sym} Snack",
                "description": "Zinc and antioxidant rich seeds for cellular repair.",
                "calories": "150 kcal",
                "protein": "7g",
                "fiber": "3g",
                "keyNutrient": "Zinc & Hydration",
                "preparation": "Enjoy sliced fresh papaya or cucumber with raw seeds."
            })
            full_day_diet.append({
                "mealType": "Dinner",
                "time": "7:30 PM",
                "targetSymptom": sym,
                "targetCause": f"{sym} Inflammatory Trigger",
                "name": f"Restorative {sym} Supper",
                "description": "Easily digestible lean protein and vegetables.",
                "calories": "390 kcal",
                "protein": "28g",
                "fiber": "6g",
                "keyNutrient": "Omega-3",
                "preparation": "Steam white fish or organic tofu with carrots and ginger."
            })

    # Return structure matching what UI expects
    return {
        "isAiGenerated": False,
        "aiBadgeText": "💡 Smart Clinical Knowledge Base",
        "analysis": analysis,
        "possibleCauses": possible_causes,
        "dietPlan": full_day_diet,
        "hydrationGoal": "2.5 - 3.0 Liters / day (Infused warm water & chamomile herbal tea)",
        "foodsToAvoid": list(foods_to_avoid),
        "lifestyleRecommendations": [
            "Maintain consistent meal times to regulate digestive circadian rhythms.",
            "Avoid lying down within 2 hours after dinner to prevent acid buildup.",
            "Incorporate 10-15 minutes of light walking after your lunch meal."
        ]
    }
