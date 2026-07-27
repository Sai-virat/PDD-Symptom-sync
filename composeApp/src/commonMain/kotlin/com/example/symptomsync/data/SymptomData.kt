package com.example.symptomsync.data

import com.example.symptomsync.ui.viewmodels.MealDetailSpec
import com.example.symptomsync.ui.viewmodels.PossibleCause

internal fun getSymptomData(): List<SymptomInfo> = listOf(
    SymptomInfo(
        name = "Fever",
        possibleCauses = listOf(
            PossibleCause("Viral Infection", "Body increasing temperature to fight off pathogens."),
            PossibleCause("Heat Exhaustion", "Result of prolonged exposure to high temperatures."),
            PossibleCause("Inflammation", "Immune response to injury or systemic stressors.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Warm Barley Water", "Hydrating and provides gentle electrolytes to stabilize core temp.", "150 kcal", "2g", "1g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Clear Vegetable Broth", "Easy to digest, nutrient-dense without taxing the digestive system.", "220 kcal", "4g", "3g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Coconut Water", "Natural source of potassium and sodium to prevent dehydration.", "60 kcal", "0g", "0g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Mashed Moong Dal", "High-protein, soft-textured legume soup that's very light on the stomach.", "280 kcal", "12g", "5g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Spicy Peppers", "Fried Foods", "Heavy Cream", "Red Meat")
    ),
    SymptomInfo(
        name = "Migraine",
        possibleCauses = listOf(
            PossibleCause("Tyramine Sensitivity", "Natural compounds in aged foods can trigger vascular changes."),
            PossibleCause("Magnesium Deficiency", "Low mineral levels affect nerve transmission and blood flow."),
            PossibleCause("Vascular Dilation", "Rapid changes in blood vessel size within the brain.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Flaxseed & Berry Oats", "Rich in Omega-3 and magnesium to reduce neuro-inflammation.", "320 kcal", "10g", "8g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Grilled Salmon Salad", "High in CoQ10 and fatty acids which have neuroprotective effects.", "450 kcal", "30g", "6g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Pumpkin Seeds", "The highest natural source of magnesium for nerve support.", "180 kcal", "8g", "4g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Turmeric Spinach Soup", "Anti-inflammatory curcumin helps soothe head pressure.", "350 kcal", "12g", "10g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Caffeine", "Aged Cheese", "Alcohol", "Artificial Sweeteners")
    ),
    SymptomInfo(
        name = "Bloating",
        possibleCauses = listOf(
            PossibleCause("FODMAP Intolerance", "Certain short-chain carbs ferment in the gut causing gas."),
            PossibleCause("Low Enzyme Activity", "Insufficient digestive enzymes to break down complex fibers."),
            PossibleCause("Swallowed Air", "Eating too quickly leads to air trapping in the GI tract.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Probiotic Yogurt Bowl", "Greek yogurt with active cultures to restore gut balance.", "250 kcal", "15g", "3g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Zucchini & Fish Steam", "Low-FODMAP vegetables that are extremely easy to process.", "400 kcal", "25g", "5g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Papaya Slices", "Contains papain, a natural enzyme that breaks down proteins.", "120 kcal", "1g", "4g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Fennel Seed Tea & Tofu", "Fennel is a carminative herb that relaxes GI muscles.", "380 kcal", "20g", "8g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Beans", "Carbonated Drinks", "Cruciferous Veggies", "Wheat")
    ),
    SymptomInfo(
        name = "Joint Pain",
        possibleCauses = listOf(
            PossibleCause("Systemic Inflammation", "Pro-inflammatory cytokines targeting synovial fluid."),
            PossibleCause("Uric Acid Buildup", "Crystal formation in joints due to high purine intake."),
            PossibleCause("Collagen Depletion", "Wear and tear of cartilage without adequate repair nutrients.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Walnut & Chia Pudding", "ALA Omega-3s to inhibit the COX-2 inflammatory pathway.", "350 kcal", "8g", "12g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Mediterranean Quinoa", "Olive oil and plant-based protein for joint lubrication.", "450 kcal", "18g", "10g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Pineapple Cubes", "Contains bromelain, an enzyme that reduces joint swelling.", "110 kcal", "1g", "3g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Ginger Garlic Chicken", "Powerful natural anti-inflammatories to reduce stiffness.", "480 kcal", "32g", "5g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Refined Sugar", "Trans Fats", "Red Meat", "High-Salt Snacks")
    ),
    SymptomInfo(
        name = "Anxiety",
        possibleCauses = listOf(
            PossibleCause("Blood Sugar Spikes", "Rapid insulin changes leading to cortisol release."),
            PossibleCause("Gut-Brain Axis", "Serotonin production in the gut affecting mood."),
            PossibleCause("Caffeine Overload", "Stimulants over-activating the nervous system.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Complex Carb Porridge", "Whole grains provide a steady glucose stream to stabilize mood.", "300 kcal", "9g", "10g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Turkey & Spinach Wrap", "Contains tryptophan, the essential precursor to serotonin.", "420 kcal", "24g", "6g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Dark Chocolate (85%)", "Flavonoids that lower stress hormones in the bloodstream.", "150 kcal", "2g", "3g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Baked Salmon & Asparagus", "High folate levels to support neurotransmitter health.", "450 kcal", "28g", "5g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Energy Drinks", "High-Sugar Candy", "Excessive Salt", "Alcohol")
    ),
    SymptomInfo(
        name = "Insomnia",
        possibleCauses = listOf(
            PossibleCause("Melatonin Suppression", "Lack of precursors or light exposure timing."),
            PossibleCause("Evening Indigestion", "Heavy meals causing acid reflux that disrupts REM."),
            PossibleCause("Cortisol Imbalance", "Inability to wind down due to late-night stress.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Banana & Almond Butter", "Potassium and magnesium to prime muscle relaxation early.", "280 kcal", "6g", "8g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Chickpea Salad", "Rich in B6, which is crucial for natural melatonin synthesis.", "400 kcal", "16g", "14g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Tart Cherry Juice", "Natural source of external melatonin to signal sleepiness.", "120 kcal", "1g", "1g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Herbal Rice & Cod", "Light protein and easy carbs to aid tryptophan absorption.", "380 kcal", "25g", "2g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Late Caffeine", "Dark Chocolate", "Spicy Foods", "Heavy Fats")
    ),
    SymptomInfo(
        name = "Fatigue",
        possibleCauses = listOf(
            PossibleCause("Iron Deficiency", "Lack of hemoglobin reducing oxygen delivery to cells."),
            PossibleCause("Mitochondrial Lag", "Inefficient energy production at the cellular level."),
            PossibleCause("Dehydration", "Reduced blood volume making the heart work harder.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Spinach & Egg Scramble", "Iron and B12 combination for immediate red blood cell support.", "320 kcal", "18g", "3g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Lentil & Sweet Potato", "Complex starch and plant-iron for sustained energy release.", "420 kcal", "20g", "15g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Mixed Raw Nuts", "Healthy fats and minerals to avoid the afternoon slump.", "170 kcal", "5g", "4g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Lean Steak & Broccoli", "High density protein and iron to replenish storage overnight.", "500 kcal", "35g", "7g", "7:00 PM")
        ),
        foodsToAvoid = listOf("White Bread", "Sugary Cereals", "Energy Drinks", "Excessive Coffee")
    ),
    SymptomInfo(
        name = "Acidity",
        possibleCauses = listOf(
            PossibleCause("LES Weakness", "Lower Esophageal Sphincter failing to stay closed."),
            PossibleCause("Acidic Food Intake", "Low pH foods irritating the stomach lining."),
            PossibleCause("Pressure Overload", "Large portions forcing acid up into the esophagus.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Oatmeal & Melon", "High pH fruits that help neutralize stomach acid.", "220 kcal", "8g", "6g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Grilled Chicken & Aloe", "Soothing, lean protein with non-acidic complex carbs.", "400 kcal", "25g", "4g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Fennel Tea & Almonds", "Fennel relaxes the GI tract and prevents acid flow.", "100 kcal", "3g", "2g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Boiled Veggie & Tofu", "Extremely light and non-spicy to ensure reflux-free sleep.", "350 kcal", "22g", "10g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Citrus Fruits", "Tomatoes", "Onions", "Spicy Spices")
    ),
    SymptomInfo(
        name = "Cough",
        possibleCauses = listOf(
            PossibleCause("Mucus Hyper-secretion", "Body producing excess phlegm to trap pathogens."),
            PossibleCause("Throat Inflammation", "Dryness or infection irritating the upper airway."),
            PossibleCause("Silent Reflux", "Micro-amounts of acid reaching the respiratory tract.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Warm Honey & Ginger Tea", "Honey is a natural antitussive that coats the throat.", "150 kcal", "0g", "0g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Garlic Chicken Soup", "Garlic has antimicrobial properties to support immunity.", "380 kcal", "26g", "4g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Lemon & Manuka Honey", "Vitamin C and high-grade honey for throat repair.", "80 kcal", "0g", "0g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Steamed Cod & Veggies", "Light and easy to swallow, non-mucus forming protein.", "400 kcal", "30g", "6g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Dairy Products", "Cold Drinks", "Caffeine", "Fried Snacks")
    ),
    SymptomInfo(
        name = "Nausea",
        possibleCauses = listOf(
            PossibleCause("Gastric Stasis", "Slow stomach emptying causing discomfort."),
            PossibleCause("Hypoglycemia", "Low blood sugar triggering the nausea center in the brain."),
            PossibleCause("Inner Ear Disturbance", "Balance issues manifesting as digestive distress.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Plain Dry Toast", "Absorbs excess acid and provides easy-to-digest starch.", "180 kcal", "4g", "2g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Boiled Rice & Carrots", "The most gentle combination for a sensitive stomach.", "320 kcal", "6g", "4g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Ginger Snaps", "The gold standard for reducing nausea symptoms.", "120 kcal", "2g", "1g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Clear Miso Soup", "Hydrating broth with electrolytes to settle the gut.", "250 kcal", "8g", "5g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Strong Smelling Foods", "Oily Meals", "Coffee", "Dairy")
    ),
    SymptomInfo(
        name = "Back Pain",
        possibleCauses = listOf(
            PossibleCause("Muscle Inflammation", "Cytokine response in the spinal support muscles."),
            PossibleCause("Nerve Compression", "Swelling or disc pressure affecting signal flow."),
            PossibleCause("Structural Stress", "Poor posture or strain leading to micro-tears.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Berry Spinach Smoothie", "High in Vitamin C and antioxidants for tissue repair.", "280 kcal", "10g", "8g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Anti-Inflammatory Salad", "Spinach, walnuts, and fish to reduce pain enzymes.", "450 kcal", "28g", "10g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Cherries & Nuts", "Anthocyanins in cherries are natural pain relievers.", "160 kcal", "5g", "5g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Sweet Potato & Lean Beef", "B12 and potassium for nerve and muscle recovery.", "500 kcal", "32g", "9g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Refined Sugar", "Vegetable Oils", "Alcohol", "Processed Carbs")
    ),
    SymptomInfo(
        name = "Muscle Cramps",
        possibleCauses = listOf(
            PossibleCause("Electrolyte Imbalance", "Lack of Magnesium, Potassium, or Calcium in the diet."),
            PossibleCause("Muscle Overuse", "Physical strain without adequate recovery nutrients."),
            PossibleCause("Dehydration", "Insufficient fluid intake affecting muscle contraction.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Banana & Spinach Oats", "High in potassium and magnesium for muscle health.", "320 kcal", "12g", "8g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Quinoa & Black Bean Bowl", "Rich in minerals that prevent involuntary contractions.", "420 kcal", "18g", "12g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Salted Nuts & Seeds", "Provides essential sodium for nerve-to-muscle signals.", "160 kcal", "5g", "3g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Grilled Salmon & Broccoli", "Omega-3s and Calcium to support muscle repair.", "450 kcal", "30g", "6g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Excessive Caffeine", "Alcohol", "High Sodium Snacks", "Refined Sugars")
    ),
    SymptomInfo(
        name = "Dizziness",
        possibleCauses = listOf(
            PossibleCause("Orthostatic Stress", "Blood pressure changes affecting brain perfusion."),
            PossibleCause("Iron Deficiency", "Reduced oxygen carrying capacity causing lightheadedness."),
            PossibleCause("Inner Ear Imbalance", "Vestibular system disruption.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Oatmeal with Almonds", "Slow-release carbs to maintain steady blood sugar.", "320 kcal", "10g", "8g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Steamed Chicken & Greens", "Iron-rich meal to boost alertness and red cell count.", "400 kcal", "28g", "5g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Ginger Tea & Fruit", "Ginger helps settle the associated nausea.", "80 kcal", "1g", "4g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Lentil Soup & Toast", "Light but nutrient-dense to prevent nighttime drops.", "350 kcal", "15g", "10g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Energy Drinks", "High-Sugar Sweets", "Excessive Salt", "Fried Foods")
    ),
    SymptomInfo(
        name = "Constipation",
        possibleCauses = listOf(
            PossibleCause("Low Fiber Intake", "Lack of roughage to facilitate digestive transit."),
            PossibleCause("Inadequate Hydration", "Dehydration making stool harder to pass."),
            PossibleCause("Gut Dysbiosis", "Imbalance of bacteria slowing down movement.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("High-Fiber Bran & Flax", "Bulk-forming fiber to stimulate bowel movement.", "250 kcal", "8g", "15g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Mixed Bean Salad", "Legumes provide the necessary fiber and hydration.", "380 kcal", "14g", "12g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Papaya & Prunes", "Natural laxative enzymes and high fiber content.", "140 kcal", "2g", "6g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Brown Rice & Roasted Veg", "Complex carbs for long-term digestive support.", "400 kcal", "10g", "9g", "7:00 PM")
        ),
        foodsToAvoid = listOf("White Bread", "Processed Meats", "Unripe Bananas", "Cheese")
    ),
    SymptomInfo(
        name = "Heartburn",
        possibleCauses = listOf(
            PossibleCause("LES Malfunction", "Valve failing to prevent acid backflow."),
            PossibleCause("Gastric Pressure", "Overeating or obesity putting pressure on the gut."),
            PossibleCause("Vagus Nerve Stress", "Stress affecting the digestive process timing.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Alkaline Melons", "Neutral pH fruits that don't trigger acid production.", "200 kcal", "2g", "4g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Boiled Chicken & White Rice", "Extremely plain protein that's easy to digest.", "420 kcal", "25g", "2g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Chamomile Tea", "Soothes the esophageal lining and reduces burn.", "50 kcal", "0g", "0g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Steamed Tofu & Squash", "Non-oily and non-spicy to ensure a calm night.", "350 kcal", "20g", "10g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Spicy Food", "Chocolate", "Peppermint", "Garlic", "Fried Foods")
    ),
    SymptomInfo(
        name = "Skin Rash",
        possibleCauses = listOf(
            PossibleCause("Histamine Response", "Body overreacting to allergens or toxins."),
            PossibleCause("Systemic Inflammation", "Immune system issues manifesting on the skin."),
            PossibleCause("Gut Permeability", "Leaky gut allowing toxins into the bloodstream.")
        ),
        dietPlan = mapOf(
            "Breakfast" to MealDetailSpec("Berry & Chia Pudding", "Omega-3s to reduce skin-level inflammation.", "300 kcal", "8g", "12g", "8:00 AM"),
            "Lunch" to MealDetailSpec("Grilled Salmon & Greens", "Vitamin A and fatty acids for skin repair.", "450 kcal", "32g", "6g", "1:00 PM"),
            "Snacks" to MealDetailSpec("Cucumber & Hummus", "Hydrating vegetables with anti-inflammatory chickpeas.", "180 kcal", "6g", "5g", "4:00 PM"),
            "Dinner" to MealDetailSpec("Turmeric Veggie Stew", "Curcumin to suppress systemic rash triggers.", "380 kcal", "12g", "10g", "7:00 PM")
        ),
        foodsToAvoid = listOf("Dairy", "Processed Sugar", "Artificial Colors", "Shellfish")
    )
)
