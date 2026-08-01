"""
Comprehensive default Symptoms & Diet dataset for SymptomSync Backend.
Provides fallbacks when Firebase connection is offline or uninitialized.
"""

SYMPTOMS_DATA = [
    {
        "name": "Headache",
        "possibleCauses": [
            {"title": "Dehydration", "description": "Insufficient water intake leads to brain tissue shrinkage and skull pressure."},
            {"title": "Stress/Tension", "description": "Emotional stress causes muscle contractions in the neck and scalp."},
            {"title": "Magnesium Deficiency", "description": "Low magnesium levels affect nerve function and blood vessel constriction."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Oatmeal with Walnuts", "description": "Magnesium-rich oats and omega-3s to reduce nerve inflammation.", "calories": "350 kcal", "protein": "12g", "fiber": "8g", "time": "8:00 AM"},
            "Lunch": {"name": "Quinoa Spinach Salad", "description": "Riboflavin (B2) in spinach helps prevent migraine triggers.", "calories": "420 kcal", "protein": "14g", "fiber": "10g", "time": "1:00 PM"},
            "Snacks": {"name": "Watermelon Slices", "description": "High water content and essential minerals for rehydration.", "calories": "100 kcal", "protein": "1g", "fiber": "2g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Salmon & Asparagus", "description": "Anti-inflammatory fats to soothe neurological pathways.", "calories": "480 kcal", "protein": "35g", "fiber": "5g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Aged Cheese", "Processed Meats", "Artificial Sweeteners", "Excessive Caffeine"]
    },
    {
        "name": "Migraine",
        "possibleCauses": [
            {"title": "Tyramine Trigger", "description": "Aged foods and processed meats trigger vascular spasms in sensitive individuals."},
            {"title": "Hormonal Fluctuations", "description": "Changes in estrogen or serotonin levels impact nerve signaling."},
            {"title": "Bright Light & Fatigue", "description": "Overstimulation of the optic nerve combined with sleep deprivation."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Ginger & Oats Bowl", "description": "Ginger reduces neuro-inflammation and nausea associated with migraines.", "calories": "320 kcal", "protein": "10g", "fiber": "7g", "time": "8:00 AM"},
            "Lunch": {"name": "Grilled Chicken & Leafy Greens", "description": "Rich in B-complex vitamins to stabilize neural pathways.", "calories": "450 kcal", "protein": "38g", "fiber": "6g", "time": "1:00 PM"},
            "Snacks": {"name": "Pumpkin Seeds & Almonds", "description": "Packed with magnesium to regulate neurotransmitter release.", "calories": "180 kcal", "protein": "8g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Steamed Cod & Wild Rice", "description": "Low-histamine lean protein to soothe systemic inflammation.", "calories": "410 kcal", "protein": "32g", "fiber": "5g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Red Wine", "Aged Cheese", "Chocolate", "Monosodium Glutamate (MSG)"]
    },
    {
        "name": "Fever",
        "possibleCauses": [
            {"title": "Viral Infection", "description": "The body raises temperature to kill pathogens like cold or flu viruses."},
            {"title": "Dehydration", "description": "Lack of fluids hinders the body's ability to regulate core temperature."},
            {"title": "Inflammation", "description": "Systemic immune response to tissue injury or infection."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Warm Barley Water & Honey", "description": "Hydrating and gentle on the metabolic rate during active recovery.", "calories": "150 kcal", "protein": "2g", "fiber": "3g", "time": "8:00 AM"},
            "Lunch": {"name": "Clear Vegetable Broth", "description": "Replaces vital electrolytes without overburdening the digestive tract.", "calories": "200 kcal", "protein": "4g", "fiber": "4g", "time": "1:00 PM"},
            "Snacks": {"name": "Coconut Water & Steamed Apple", "description": "Replenishes potassium and soluble pectin for gut comfort.", "calories": "110 kcal", "protein": "1g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Mashed Mung Dal Soup", "description": "High-protein, soft-textured legume soup that is effortlessly digested.", "calories": "280 kcal", "protein": "14g", "fiber": "8g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Spicy Peppers", "Fried Foods", "Red Meat", "Heavy Dairy"]
    },
    {
        "name": "Bloating",
        "possibleCauses": [
            {"title": "Digestive Sensitivity", "description": "Difficulty breaking down fermentable oligosaccharides, disaccharides, monosaccharides, and polyols (FODMAPs)."},
            {"title": "High Sodium Intake", "description": "Excess sodium retention causes fluid buildup in the abdominal cavity."},
            {"title": "Imbalanced Gut Microbiome", "description": "Overgrowth of gas-producing bacterial strains in the small intestine."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Greek Yogurt & Blueberries", "description": "Probiotics to balance gut flora and minimize gas production.", "calories": "280 kcal", "protein": "18g", "fiber": "4g", "time": "8:00 AM"},
            "Lunch": {"name": "Grilled Chicken & Zucchini", "description": "Lean protein paired with low-FODMAP vegetables for smooth transit.", "calories": "400 kcal", "protein": "32g", "fiber": "5g", "time": "1:00 PM"},
            "Snacks": {"name": "Fresh Papaya Slices", "description": "Natural papain enzymes assist in protein digestion.", "calories": "120 kcal", "protein": "1g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Steamed White Fish & Carrots", "description": "Light, easily digestible meal preventing overnight gas buildup.", "calories": "350 kcal", "protein": "28g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Beans", "Carbonated Beverages", "Cruciferous Veggies", "Artificial Sweeteners"]
    },
    {
        "name": "Acidity",
        "possibleCauses": [
            {"title": "Acidic Foods", "description": "Citrus fruits, tomatoes, and coffee irritate the esophageal lining."},
            {"title": "Large Meal Volume", "description": "Overfilling the stomach exerts upward pressure on the lower esophageal sphincter."},
            {"title": "Post-Meal Recumbency", "description": "Lying down shortly after eating allows stomach acid to flow upward."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Oatmeal with Sliced Melon", "description": "Oats absorb stomach acid while cantaloupe has an alkaline pH.", "calories": "290 kcal", "protein": "9g", "fiber": "10g", "time": "8:00 AM"},
            "Lunch": {"name": "Boiled Chicken & Brown Rice", "description": "Low-fat, non-acidic complex carbohydrates gentle on gastric tissue.", "calories": "420 kcal", "protein": "30g", "fiber": "6g", "time": "1:00 PM"},
            "Snacks": {"name": "Raw Almonds", "description": "Alkaline nuts that help neutralize excess stomach acid naturally.", "calories": "160 kcal", "protein": "6g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Steamed Vegetables & Tofu", "description": "Alkaline-promoting plant proteins for smooth digestion.", "calories": "330 kcal", "protein": "20g", "fiber": "7g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Citrus Fruits", "Tomatoes", "Coffee & Espresso", "Deep-Fried Snacks"]
    },
    {
        "name": "Joint Pain",
        "possibleCauses": [
            {"title": "Systemic Inflammation", "description": "Elevated inflammatory cytokines affect cartilage and synovial fluid."},
            {"title": "Uric Acid Accumulation", "description": "Purine-rich diet leads to crystal deposits in joints."},
            {"title": "Overuse & Muscle Fatigue", "description": "Strain on connective tissues surrounding major joints."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Berry Chia Seed Smoothie", "description": "Rich in polyphenols and antioxidants to reduce joint tissue swelling.", "calories": "260 kcal", "protein": "8g", "fiber": "9g", "time": "8:00 AM"},
            "Lunch": {"name": "Salmon & Avocado Bowl", "description": "High concentration of Omega-3 EPA/DHA to soothe inflamed joints.", "calories": "510 kcal", "protein": "34g", "fiber": "8g", "time": "1:00 PM"},
            "Snacks": {"name": "Tart Cherry Juice", "description": "Natural anthocyanins lower serum inflammatory markers.", "calories": "140 kcal", "protein": "1g", "fiber": "1g", "time": "4:00 PM"},
            "Dinner": {"name": "Turmeric Roasted Cauliflower & Chicken", "description": "Curcumin in turmeric acts as a potent natural COX-2 inhibitor.", "calories": "440 kcal", "protein": "36g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Refined Sugars", "Trans Fats", "Excessive Red Meat", "Processed Snacks"]
    },
    {
        "name": "Fatigue",
        "possibleCauses": [
            {"title": "Iron Deficiency", "description": "Low hemoglobin limits oxygen transport to vital organs and tissues."},
            {"title": "Circadian Disruption", "description": "Irregular sleep patterns interrupt deep restorative REM cycles."},
            {"title": "Blood Sugar Spikes", "description": "High glycemic meals cause rapid insulin surge followed by energy crash."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Eggs & Whole Grain Toast", "description": "Complete protein and B-vitamins for sustained ATP production.", "calories": "360 kcal", "protein": "18g", "fiber": "5g", "time": "8:00 AM"},
            "Lunch": {"name": "Lentil Soup & Kale Salad", "description": "Bioavailable iron coupled with Vitamin C for maximum absorption.", "calories": "430 kcal", "protein": "22g", "fiber": "12g", "time": "1:00 PM"},
            "Snacks": {"name": "Dark Chocolate & Pumpkin Seeds", "description": "Flavonoids and iron boost alertness and vascular flow.", "calories": "190 kcal", "protein": "6g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Lean Turkey & Quinoa", "description": "Contains tryptophan for natural serotonin and melatonin synthesis.", "calories": "460 kcal", "protein": "38g", "fiber": "7g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Sugary Energy Drinks", "Refined White Bread", "Excessive Alcohol", "Heavy Fried Meals"]
    },
    {
        "name": "Insomnia",
        "possibleCauses": [
            {"title": "Late Caffeine Consumption", "description": "Blocks adenosine receptors in the brain, inhibiting sleepiness."},
            {"title": "High Cortisol Levels", "description": "Chronic stress prevents parasympathetic nervous system activation."},
            {"title": "Magnesium Deficit", "description": "Inadequate magnesium hinders GABA neurotransmitter production."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Oatmeal with Banana & Chia", "description": "Provides precursor amino acids to support evening melatonin.", "calories": "310 kcal", "protein": "9g", "fiber": "8g", "time": "8:00 AM"},
            "Lunch": {"name": "Turkey & Spinach Wrap", "description": "Tryptophan combined with complex carbs aids brain absorption.", "calories": "410 kcal", "protein": "28g", "fiber": "6g", "time": "1:00 PM"},
            "Snacks": {"name": "Chamomile Tea & Pumpkin Seeds", "description": "Calming flavonoids and magnesium to relax smooth muscles.", "calories": "130 kcal", "protein": "5g", "fiber": "2g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Salmon & Sweet Potato", "description": "Vitamin B6 and potassium aid nocturnal restorative sleep.", "calories": "470 kcal", "protein": "34g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Late Night Coffee", "Spicy Foods", "Heavy Meals before Bed", "Alcohol"]
    },
    {
        "name": "Anxiety",
        "possibleCauses": [
            {"title": "Caffeine Sensitivity", "description": "Stimulates adrenaline release and heightens sympathetic nervous response."},
            {"title": "Gut Microbiome Imbalance", "description": "Altered gut flora disrupts vagus nerve neurotransmitter exchange."},
            {"title": "Blood Glucose Instability", "description": "Rapid drops in blood sugar trigger cortisol and epinephrine discharge."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Avocado & Egg Toast", "description": "Healthy fats and B-vitamins steady neurotransmitter levels.", "calories": "380 kcal", "protein": "16g", "fiber": "7g", "time": "8:00 AM"},
            "Lunch": {"name": "Kefir & Salmon Salad", "description": "Probiotics and Omega-3 fatty acids attenuate stress response.", "calories": "440 kcal", "protein": "32g", "fiber": "5g", "time": "1:00 PM"},
            "Snacks": {"name": "Matcha Tea & Walnuts", "description": "L-theanine promotes alpha brainwaves for calm concentration.", "calories": "170 kcal", "protein": "5g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Turkey Stew & Roasted Asparagus", "description": "Folate and tryptophan foster calm neural activity.", "calories": "420 kcal", "protein": "35g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Caffeine", "Refined Sugar", "Alcohol", "Processed Trans Fats"]
    },
    {
        "name": "Nausea",
        "possibleCauses": [
            {"title": "Gastric Irritation", "description": "Inflammation of the stomach lining slows down gastric emptying."},
            {"title": "Motion or Vestibular Shift", "description": "Mismatch between visual and inner ear motion signals."},
            {"title": "Hypoglycemia", "description": "Low blood sugar causes lightheadedness and nausea feelings."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Ginger Tea & Dry Toast", "description": "Gingerol calms stomach spasm while dry carbs absorb acid.", "calories": "180 kcal", "protein": "4g", "fiber": "3g", "time": "8:00 AM"},
            "Lunch": {"name": "Steamed Rice & Boiled Chicken", "description": "Bland BRAT-style diet to minimize digestive stimulation.", "calories": "350 kcal", "protein": "26g", "fiber": "2g", "time": "1:00 PM"},
            "Snacks": {"name": "Applesauce & Saltines", "description": "Easy to assimilate glucose without taxing liver enzymes.", "calories": "140 kcal", "protein": "2g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Clear Vegetable Soup & Crackers", "description": "Replenishes fluids and minerals comfortably.", "calories": "220 kcal", "protein": "5g", "fiber": "3g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Greasy Foods", "Strong Spices", "Dairy Products", "Acidic Juices"]
    }
]
