from firebase_setup import db

# Dataset with 50+ common symptoms, causes, and diet plans
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
        "name": "Fever",
        "possibleCauses": [
            {"title": "Viral Infection", "description": "The body raises temperature to kill pathogens like cold or flu viruses."},
            {"title": "Dehydration", "description": "Lack of fluids hinders the body's ability to regulate core temperature."},
            {"title": "Inflammation", "description": "Systemic response to injury or autoimmune triggers."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Warm Barley Water", "description": "Extremely hydrating and easy on the metabolism during recovery.", "calories": "150 kcal", "protein": "2g", "fiber": "3g", "time": "8:00 AM"},
            "Lunch": {"name": "Clear Vegetable Broth", "description": "Nutrient-dense liquid to replace lost electrolytes without taxing the gut.", "calories": "200 kcal", "protein": "4g", "fiber": "4g", "time": "1:00 PM"},
            "Snacks": {"name": "Coconut Water", "description": "Natural source of potassium and sodium for fluid balance.", "calories": "60 kcal", "protein": "0g", "fiber": "0g", "time": "4:00 PM"},
            "Dinner": {"name": "Mashed Mung Dal Soup", "description": "High-protein, soft-textured legume soup that is very easy to digest.", "calories": "280 kcal", "protein": "14g", "fiber": "8g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Spicy Peppers", "Fried Foods", "Red Meat", "Heavy Dairy"]
    },
    {
        "name": "Bloating",
        "possibleCauses": [
            {"title": "Digestive Sensitivity", "description": "Difficulty breaking down fibers or sugars like lactose and fructose."},
            {"title": "High Sodium Intake", "description": "Excess salt causes the body to retain water in the abdominal cavity."},
            {"title": "Imbalanced Gut Microbiome", "description": "Overgrowth of gas-producing bacteria in the small intestine."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Greek Yogurt & Blueberries", "description": "Probiotics to balance gut flora and reduce gas formation.", "calories": "280 kcal", "protein": "18g", "fiber": "4g", "time": "8:00 AM"},
            "Lunch": {"name": "Grilled Chicken & Zucchini", "description": "Lean protein with low-FODMAP vegetables for easy transit.", "calories": "400 kcal", "protein": "32g", "fiber": "5g", "time": "1:00 PM"},
            "Snacks": {"name": "Papaya Slices", "description": "Contains papain enzymes that aid in protein digestion.", "calories": "120 kcal", "protein": "1g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Steamed Cod & Carrots", "description": "Light, easily digestible meal to prevent nighttime fermentation.", "calories": "350 kcal", "protein": "28g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Beans", "Carbonated Drinks", "Cruciferous Veggies", "Chewing Gum"]
    },
    {
        "name": "Acidity",
        "possibleCauses": [
            {"title": "Acidic Foods", "description": "Consumption of citrus, tomatoes, or coffee irritates the stomach lining."},
            {"title": "Large Meals", "description": "Overfilling the stomach pushes acid into the esophagus via the LES."},
            {"title": "Lying Down Post-Meal", "description": "Gravity-aided digestion is prevented, leading to reflux."},
        ],
        "dietPlan": {
            "Breakfast": {"name": "Oatmeal with Sliced Melon", "description": "Oats absorb excess acid while melons provide a high alkaline pH.", "calories": "290 kcal", "protein": "9g", "fiber": "10g", "time": "8:00 AM"},
            "Lunch": {"name": "Boiled Chicken & Brown Rice", "description": "Low-fat, non-acidic complex carbs that are gentle on the gut.", "calories": "420 kcal", "protein": "30g", "fiber": "6g", "time": "1:00 PM"},
            "Snacks": {"name": "Raw Almonds", "description": "Alkaline nuts that help neutralize stomach acid naturally.", "calories": "160 kcal", "protein": "6g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Steamed Zucchini & Tofu", "description": "Light and non-spicy to ensure a calm digestive system overnight.", "calories": "340 kcal", "protein": "20g", "fiber": "8g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Citrus Fruits", "Chocolate", "Peppermint", "Fried Foods"]
    },
    {
        "name": "Back Pain",
        "possibleCauses": [
            {"title": "Inflammation", "description": "Systemic inflammatory markers affecting spinal joints and nerves."},
            {"title": "Mineral Deficiency", "description": "Lack of calcium and vitamin D affecting bone density and muscle support."},
            {"title": "Muscle Tension", "description": "Tightness from poor posture or physical strain without recovery nutrition."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Chia Seed & Berry Pudding", "description": "Omega-3s from chia to reduce spinal joint inflammation.", "calories": "310 kcal", "protein": "8g", "fiber": "12g", "time": "8:00 AM"},
            "Lunch": {"name": "Tuna Spinach Salad", "description": "Vitamin D and lean protein to support bone and muscle repair.", "calories": "440 kcal", "protein": "30g", "fiber": "9g", "time": "1:00 PM"},
            "Snacks": {"name": "Brazil Nuts", "description": "Selenium-rich nuts to support tissue healing and reduce soreness.", "calories": "190 kcal", "protein": "4g", "fiber": "2g", "time": "4:00 PM"},
            "Dinner": {"name": "Turmeric Sweet Potato Stew", "description": "Curcumin and potassium for nerve relief and muscle relaxation.", "calories": "400 kcal", "protein": "12g", "fiber": "10g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Refined Sugars", "Vegetable Oils", "Red Meat", "Alcohol"]
    },
    {
        "name": "Insomnia",
        "possibleCauses": [
            {"title": "Caffeine/Stimulants", "description": "Late-day consumption blocks adenosine receptors, preventing sleepiness."},
            {"title": "Magnesium Deficiency", "description": "Prevents the nervous system and muscles from entering a relaxed state."},
            {"title": "Late Heavy Meals", "description": "Metabolic activity during digestion disrupts the body's cooling cycle for sleep."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Banana Almond Smoothie", "description": "Potassium and magnesium to help regulate the nervous system.", "calories": "300 kcal", "protein": "10g", "fiber": "7g", "time": "8:00 AM"},
            "Lunch": {"name": "Turkey & Avocado Wrap", "description": "Turkey is high in tryptophan, a precursor to sleep-regulating serotonin.", "calories": "420 kcal", "protein": "25g", "fiber": "8g", "time": "1:00 PM"},
            "Snacks": {"name": "Tart Cherry Juice", "description": "One of the few natural dietary sources of melatonin.", "calories": "110 kcal", "protein": "1g", "fiber": "2g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Salmon & Brown Rice", "description": "B6 in salmon helps the body produce melatonin effectively.", "calories": "450 kcal", "protein": "32g", "fiber": "5g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Dark Chocolate", "Spicy Foods", "Alcohol", "Aged Cheese"]
    },
    {
        "name": "Brain Fog",
        "possibleCauses": [
            {"title": "B12 Deficiency", "description": "Crucial for maintaining the myelin sheath that protects brain nerves."},
            {"title": "Sugar Volatility", "description": "Rapid spikes and crashes in blood sugar affect cognitive clarity."},
            {"title": "Neuro-Inflammation", "description": "Inflammatory response affecting the speed of neural signaling."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Egg & Avocado Toast", "description": "Choline from eggs and healthy fats for cognitive focus.", "calories": "360 kcal", "protein": "18g", "fiber": "7g", "time": "8:00 AM"},
            "Lunch": {"name": "Mackerel Spinach Salad", "description": "High DHA omega-3s are essential for brain cell structure.", "calories": "450 kcal", "protein": "28g", "fiber": "9g", "time": "1:00 PM"},
            "Snacks": {"name": "Walnuts & Green Tea", "description": "L-theanine and omega-3s for calm alertness and focus.", "calories": "180 kcal", "protein": "4g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Lean Beef & Broccoli", "description": "B12 and Iron to support neurotransmitter health and oxygen flow.", "calories": "480 kcal", "protein": "35g", "fiber": "7g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Added Sugars", "Refined Flours", "Trans Fats", "Artificial Sweeteners"]
    },
    {
        "name": "Hair Fall",
        "possibleCauses": [
            {"title": "Iron Anemia", "description": "Low iron reduces oxygen flow to the scalp and hair bulbs."},
            {"title": "Protein Malnutrition", "description": "Hair is made of keratin; lack of protein halts growth and repair."},
            {"title": "Zinc Deficiency", "description": "Zinc is vital for hair tissue growth and the oil glands on the scalp."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Eggs & Pumpkin Seeds", "description": "Biotin from eggs and zinc from seeds for follicular strength.", "calories": "330 kcal", "protein": "20g", "fiber": "4g", "time": "8:00 AM"},
            "Lunch": {"name": "Lentil & Spinach Stew", "description": "High plant-based iron and protein to boost scalp circulation.", "calories": "410 kcal", "protein": "22g", "fiber": "15g", "time": "1:00 PM"},
            "Snacks": {"name": "Greek Yogurt & Berries", "description": "B5 (Pantothenic acid) to support hair health and blood flow.", "calories": "200 kcal", "protein": "15g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Grilled Salmon & Carrots", "description": "Omega-3s for hair shine and Vitamin A for sebum control.", "calories": "460 kcal", "protein": "30g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["High-Mercury Fish", "Sugary Drinks", "Alcohol", "Raw Egg Whites"]
    },
    {
        "name": "Muscle Cramps",
        "possibleCauses": [
            {"title": "Electrolyte Imbalance", "description": "Lack of potassium or sodium prevents normal muscle contraction."},
            {"title": "Dehydration", "description": "Insufficient water interferes with electrochemical signals to muscles."},
            {"title": "Magnesium Depletion", "description": "Prevents muscles from relaxing after a contraction signal."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Banana & Peanut Butter Oats", "description": "Potassium-rich bananas to prevent involuntary spasms.", "calories": "380 kcal", "protein": "14g", "fiber": "9g", "time": "8:00 AM"},
            "Lunch": {"name": "Black Bean & Quinoa Bowl", "description": "High in magnesium and protein for muscular health.", "calories": "420 kcal", "protein": "18g", "fiber": "15g", "time": "1:00 PM"},
            "Snacks": {"name": "Salted Pumpkin Seeds", "description": "Provides necessary sodium and zinc for nerve-muscle signaling.", "calories": "160 kcal", "protein": "8g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Cod & Steamed Spinach", "description": "Calcium and Omega-3s to aid in rapid muscle recovery.", "calories": "390 kcal", "protein": "32g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Excessive Alcohol", "Caffeine", "Processed Sugar", "Very Salty Junk Food"]
    },
    {
        "name": "Dizziness",
        "possibleCauses": [
            {"title": "Iron Deficiency", "description": "Poor oxygen transport to the brain leads to lightheadedness."},
            {"title": "Blood Sugar Dip", "description": "Hypoglycemia deprives the brain of its essential fuel source."},
            {"title": "Dehydration", "description": "Reduced blood volume causes sudden drops in blood pressure."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Whole Grain Toast & Avocado", "description": "Complex carbs for a steady, non-crashing glucose supply.", "calories": "310 kcal", "protein": "8g", "fiber": "9g", "time": "8:00 AM"},
            "Lunch": {"name": "Lean Beef & Green Salad", "description": "High heme-iron for immediate red blood cell support.", "calories": "450 kcal", "protein": "32g", "fiber": "7g", "time": "1:00 PM"},
            "Snacks": {"name": "Orange Slices & Almonds", "description": "Vitamin C to boost the absorption of dietary iron.", "calories": "150 kcal", "protein": "4g", "fiber": "5g", "time": "4:00 PM"},
            "Dinner": {"name": "Lentil Soup with Ginger", "description": "Hydrating and ginger-rich to settle lightheaded-related nausea.", "calories": "360 kcal", "protein": "18g", "fiber": "12g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Energy Drinks", "Salty Snacks", "Alcohol", "High-Sugar Candies"]
    },
    {
        "name": "Constipation",
        "possibleCauses": [
            {"title": "Low Dietary Fiber", "description": "Insufficient roughage to move waste through the colon."},
            {"title": "Inadequate Hydration", "description": "Water is absorbed from waste, making it hard and difficult to pass."},
            {"title": "Sedentary Metabolism", "description": "Lack of physical activity slows down intestinal contractions."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "High-Fiber Bran & Pear", "description": "Insoluble fiber to physically stimulate bowel movement.", "calories": "300 kcal", "protein": "9g", "fiber": "15g", "time": "8:00 AM"},
            "Lunch": {"name": "Mixed Bean & Veggie Bowl", "description": "Soluble fiber and magnesium to soften stools naturally.", "calories": "450 kcal", "protein": "20g", "fiber": "14g", "time": "1:00 PM"},
            "Snacks": {"name": "Dried Prunes & Warm Water", "description": "Contains sorbitol, a natural and safe mild laxative.", "calories": "130 kcal", "protein": "1g", "fiber": "6g", "time": "4:00 PM"},
            "Dinner": {"name": "Brown Rice & Roasted Squash", "description": "Complex carbohydrates for long-term digestive regularity.", "calories": "380 kcal", "protein": "12g", "fiber": "10g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["White Bread", "Cheese", "Processed Meats", "Unripe Bananas"]
    },
    {
        "name": "Skin Rash",
        "possibleCauses": [
            {"title": "Histamine Response", "description": "Immune system overreaction to allergens or internal toxins."},
            {"title": "Gut Permeability", "description": "Leaky gut allowing inflammatory particles into the bloodstream."},
            {"title": "Omega-3 Deficiency", "description": "Lack of healthy fats weakens the skin's protective barrier."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Berry Chia Pudding", "description": "Omega-3s and antioxidants to calm skin inflammation.", "calories": "310 kcal", "protein": "10g", "fiber": "12g", "time": "8:00 AM"},
            "Lunch": {"name": "Grilled Salmon & Kale", "description": "Vitamin A and D to support skin cell repair and health.", "calories": "440 kcal", "protein": "32g", "fiber": "7g", "time": "1:00 PM"},
            "Snacks": {"name": "Cucumber & Hummus", "description": "Hydrating vegetables to help flush out skin-irritating toxins.", "calories": "180 kcal", "protein": "7g", "fiber": "5g", "time": "4:00 PM"},
            "Dinner": {"name": "Turmeric Tofu & Peppers", "description": "Curcumin to suppress systemic inflammatory rash triggers.", "calories": "380 kcal", "protein": "22g", "fiber": "8g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Dairy", "Artificial Colors", "Shellfish", "Highly Processed Sugar"]
    },
    {
        "name": "Sore Throat",
        "possibleCauses": [
            {"title": "Viral/Bacterial Infection", "description": "Pathogens irritating and inflaming the pharyngeal tissue."},
            {"title": "Acid Reflux", "description": "Silent reflux during sleep burning the esophageal and throat lining."},
            {"title": "Dryness/Dehydration", "description": "Low humidity or mouth breathing drying out mucus membranes."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Warm Honey & Lemon Oats", "description": "Honey coats the throat and acts as a natural antiseptic.", "calories": "300 kcal", "protein": "8g", "fiber": "10g", "time": "8:00 AM"},
            "Lunch": {"name": "Mashed Potato & Broth", "description": "Soft texture to prevent mechanical irritation while swallowing.", "calories": "350 kcal", "protein": "12g", "fiber": "4g", "time": "1:00 PM"},
            "Snacks": {"name": "Herbal Tea & Mashed Banana", "description": "Soothing fluids and easy-to-swallow energy.", "calories": "110 kcal", "protein": "1g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Poached Egg & Soft Rice", "description": "High-quality protein that is very gentle on a painful throat.", "calories": "380 kcal", "protein": "18g", "fiber": "2g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Crispy Crackers", "Spicy Chili", "Citrus Juices", "Vinegar"]
    },
    {
        "name": "Eye Strain",
        "possibleCauses": [
            {"title": "Blue Light Exposure", "description": "Digital screens overstimulating and fatiguing retinal cells."},
            {"title": "Lutein Deficiency", "description": "Lack of eye-protective pigments found in leafy greens."},
            {"title": "Poor Circulation", "description": "Reduced nutrient flow to the tiny muscles controlling focus."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Carrot & Apple Juice", "description": "High beta-carotene for retinal health and repair.", "calories": "220 kcal", "protein": "4g", "fiber": "6g", "time": "8:00 AM"},
            "Lunch": {"name": "Spinach & Kale Salad", "description": "Rich in Lutein and Zeaxanthin to filter harmful light.", "calories": "380 kcal", "protein": "18g", "fiber": "7g", "time": "1:00 PM"},
            "Snacks": {"name": "Blueberries & Walnuts", "description": "Antocyanins to improve blood capillary flow to the eyes.", "calories": "160 kcal", "protein": "5g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Cod & Broccoli", "description": "DHA fatty acids for neural protection of the eyes.", "calories": "410 kcal", "protein": "30g", "fiber": "8g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["High-Salt Snacks", "Fried Foods", "Energy Drinks", "Excessive Caffeine"]
    },
    {
        "name": "Low Appetite",
        "possibleCauses": [
            {"title": "Zinc Deficiency", "description": "Zinc is required for the enzymes that regulate taste and smell."},
            {"title": "Digestive Lag", "description": "Slow metabolic emptying making you feel full for too long."},
            {"title": "Emotional Stress", "description": "Cortisol can temporarily shut down non-essential hunger signals."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Nut & Seed Energy Bowl", "description": "Calorie-dense zinc source to jumpstart hunger hormones.", "calories": "250 kcal", "protein": "10g", "fiber": "5g", "time": "8:00 AM"},
            "Lunch": {"name": "Spiced Tomato Bisque", "description": "Warming spices like cumin to stimulate gastric secretions.", "calories": "280 kcal", "protein": "6g", "fiber": "7g", "time": "1:00 PM"},
            "Snacks": {"name": "Ginger Tea & Sliced Apple", "description": "Ginger is a potent natural appetite and digestive stimulant.", "calories": "100 kcal", "protein": "1g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Small Portion Ginger Chicken", "description": "Light but protein-rich to ensure nutrition without bulk.", "calories": "380 kcal", "protein": "28g", "fiber": "2g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Heavy Creams", "Large Fiber Portions", "Sugary Sodas", "Deep Fried Food"]
    },
    {
        "name": "Night Sweats",
        "possibleCauses": [
            {"title": "Thermogenic Foods", "description": "Spicy foods or alcohol before bed raise core temperature."},
            {"title": "Hormonal Shift", "description": "Imbalances in estrogen or cortisol affecting the cooling system."},
            {"title": "Sugar Hypoglycemia", "description": "Glucose drops during sleep trigger a sweating emergency response."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Cooling Yogurt & Fruits", "description": "Hydrating and temperature-neutral to stabilize metabolic heat.", "calories": "270 kcal", "protein": "15g", "fiber": "5g", "time": "8:00 AM"},
            "Lunch": {"name": "Spinach & Mint Salad", "description": "Magnesium for hormone regulation and refreshing mint herbs.", "calories": "340 kcal", "protein": "12g", "fiber": "10g", "time": "1:00 PM"},
            "Snacks": {"name": "Sage Tea & Fresh Pear", "description": "Sage is traditionally used to reduce excessive perspiration.", "calories": "90 kcal", "protein": "1g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Plain Rice & Steamed Greens", "description": "Very low thermogenic effect for a cool night's sleep.", "calories": "360 kcal", "protein": "10g", "fiber": "9g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Hot Chili Peppers", "Alcohol", "Caffeine", "Garlic at Night"]
    },
    {
        "name": "Mouth Ulcers",
        "possibleCauses": [
            {"title": "B-Vitamin Deficiency", "description": "Lack of Folate or B12 affects the repair of oral tissues."},
            {"title": "Acidic Food Irritation", "description": "Citrus or high-acid intake eroding existing minor lesions."},
            {"title": "Iron Deficiency", "description": "Weakens the epithelial lining making it prone to ulceration."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Banana & Milk Smoothie", "description": "Non-acidic and smooth to soothe the mouth's sensitive lining.", "calories": "260 kcal", "protein": "12g", "fiber": "5g", "time": "8:00 AM"},
            "Lunch": {"name": "Creamy Lentil Soup", "description": "High folate content and easy texture for painful sores.", "calories": "390 kcal", "protein": "18g", "fiber": "12g", "time": "1:00 PM"},
            "Snacks": {"name": "Mashed Avocado", "description": "Rich in repair-aiding healthy fats without mechanical irritation.", "calories": "150 kcal", "protein": "2g", "fiber": "7g", "time": "4:00 PM"},
            "Dinner": {"name": "Boiled Chicken & Soft Spinach", "description": "Iron and B12 rich for accelerating the healing of ulcers.", "calories": "420 kcal", "protein": "30g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Citrus Fruits", "Spicy Curry", "Salted Nuts", "Hard Crusty Bread"]
    },
    {
        "name": "Anxiety",
        "possibleCauses": [
            {"title": "Caffeine Overload", "description": "Stimulants mimic and trigger the physical 'flight' response."},
            {"title": "Blood Sugar Spikes", "description": "Unstable glucose levels cause irritability and panic sensations."},
            {"title": "Gut Dysbiosis", "description": "Imbalanced flora affecting the production of calming serotonin."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Whole Grain Porridge", "description": "Complex carbs for a slow, steady, and calm energy release.", "calories": "310 kcal", "protein": "10g", "fiber": "11g", "time": "8:00 AM"},
            "Lunch": {"name": "Turkey & Pumpkin Seed Salad", "description": "Tryptophan and Magnesium to support neural relaxation.", "calories": "430 kcal", "protein": "28g", "fiber": "8g", "time": "1:00 PM"},
            "Snacks": {"name": "Dark Chocolate & Almonds", "description": "Flavonoids and Magnesium for quick mood stabilization.", "calories": "170 kcal", "protein": "5g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Salmon & Asparagus", "description": "Omega-3s and Folate for long-term brain health and calm.", "calories": "460 kcal", "protein": "32g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["High Caffeine", "Alcohol", "Sugary Snacks", "Processed Meats"]
    },
    {
        "name": "Nausea",
        "possibleCauses": [
            {"title": "Slow Digestion", "description": "Food sitting in the stomach triggers protective gag reflexes."},
            {"title": "Low Blood Sugar", "description": "Sudden glucose drops can induce waves of metabolic sickness."},
            {"title": "Inner Ear Stress", "description": "Equilibrium disruption causing gastric signals to malfunction."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Dry Toast & Banana", "description": "Gentle, low-acid foods that won't trigger further sickness.", "calories": "230 kcal", "protein": "4g", "fiber": "6g", "time": "8:00 AM"},
            "Lunch": {"name": "White Rice & Clear Broth", "description": "Simple carbs and salt to settle the gut and maintain fluids.", "calories": "320 kcal", "protein": "6g", "fiber": "2g", "time": "1:00 PM"},
            "Snacks": {"name": "Ginger Snaps or Tea", "description": "Ginger is the gold standard for reducing digestive nausea.", "calories": "100 kcal", "protein": "1g", "fiber": "1g", "time": "4:00 PM"},
            "Dinner": {"name": "Boiled Chicken & Soft Carrots", "description": "Plain, protein-rich nutrition that is easy on the stomach.", "calories": "350 kcal", "protein": "25g", "fiber": "4g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Fried Oily Foods", "Strong Spices", "Heavy Dairy", "Pungent Smells"]
    },
    {
        "name": "Fatigue",
        "possibleCauses": [
            {"title": "Iron Deficiency", "description": "Inadequate oxygen supply to muscles and brain causing lethargy."},
            {"title": "Sugar Overload", "description": "Heavy insulin response leads to a rapid post-meal energy crash."},
            {"title": "Dehydration", "description": "Blood thickening makes the heart work harder for every beat."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Spinach & Egg Omelet", "description": "Iron and B-vitamins to start the day with sustained energy.", "calories": "330 kcal", "protein": "20g", "fiber": "3g", "time": "8:00 AM"},
            "Lunch": {"name": "Lentil Stew & Quinoa", "description": "High-fiber complex carbs for non-stop afternoon fuel.", "calories": "440 kcal", "protein": "19g", "fiber": "14g", "time": "1:00 PM"},
            "Snacks": {"name": "Mixed Seeds & Nuts", "description": "Magnesium and healthy fats for mental and physical alertness.", "calories": "180 kcal", "protein": "7g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Steak & Sweet Potato", "description": "B12 and complex starch to replenish nightly energy stores.", "calories": "500 kcal", "protein": "35g", "fiber": "8g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Sugary Cereals", "Energy Drinks", "White Bread", "Fried Fast Food"]
    },
    {
        "name": "Knee Pain",
        "possibleCauses": [
            {"title": "Cartilage Inflammation", "description": "Systemic response affecting the knee joint lining."},
            {"title": "Low Collagen", "description": "Insufficient building blocks for joint repair."},
            {"title": "Fluid Retention", "description": "High salt causing swelling in the knee capsule."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Chia & Flaxseed Porridge", "description": "Rich in ALA Omega-3s to reduce joint swelling.", "calories": "300 kcal", "protein": "10g", "fiber": "12g", "time": "8:00 AM"},
            "Lunch": {"name": "Grilled Fish & Broccoli", "description": "Vitamin K and lean protein for joint support.", "calories": "420 kcal", "protein": "30g", "fiber": "7g", "time": "1:00 PM"},
            "Snacks": {"name": "Green Tea & Almonds", "description": "Polyphenols that inhibit cartilage breakdown.", "calories": "150 kcal", "protein": "5g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Turmeric Chicken & Peppers", "description": "Curcumin to naturally block inflammatory pain signals.", "calories": "480 kcal", "protein": "32g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Refined Carbs", "Trans Fats", "Sugary Drinks", "Red Meat"]
    },
    {
        "name": "Bad Breath",
        "possibleCauses": [
            {"title": "Bacterial Overgrowth", "description": "Excess sulfur-producing bacteria in the mouth/gut."},
            {"title": "Dry Mouth", "description": "Low saliva flow allowing odor particles to linger."},
            {"title": "Indigestion", "description": "Gases from the stomach escaping through the esophagus."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Fresh Apple & Water", "description": "Apples stimulate saliva and mechanically clean teeth.", "calories": "100 kcal", "protein": "1g", "fiber": "4g", "time": "8:00 AM"},
            "Lunch": {"name": "Parsley & Greens Salad", "description": "Parsley is a natural deodorizer rich in chlorophyll.", "calories": "280 kcal", "protein": "8g", "fiber": "10g", "time": "1:00 PM"},
            "Snacks": {"name": "Probiotic Yogurt", "description": "Restores gut flora balance to reduce odor from the source.", "calories": "150 kcal", "protein": "12g", "fiber": "0g", "time": "4:00 PM"},
            "Dinner": {"name": "Ginger Fish & Steamed Veg", "description": "Ginger aids digestion and prevents stomach gas reflux.", "calories": "380 kcal", "protein": "28g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Garlic", "Raw Onions", "Strong Coffee", "Excessive Sugar"]
    },
    {
        "name": "Heartburn",
        "possibleCauses": [
            {"title": "Acidic Food Irritation", "description": "Consuming tomatoes, citrus, or coffee can relax the esophageal sphincter."},
            {"title": "Large Nighttime Meals", "description": "Eating heavy portions before bed causes stomach acid to rise while lying down."},
            {"title": "High Fat Intake", "description": "Greasy foods slow down digestion, keeping acid in the stomach for longer periods."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Oatmeal with Sliced Banana", "description": "Low-acid oats and alkaline bananas to soothe the esophagus.", "calories": "320 kcal", "protein": "8g", "fiber": "10g", "time": "8:30 AM"},
            "Lunch": {"name": "Grilled Chicken & Brown Rice", "description": "Lean protein and complex carbs that are gentle on the digestive tract.", "calories": "450 kcal", "protein": "32g", "fiber": "6g", "time": "1:30 PM"},
            "Snacks": {"name": "Melon Slices", "description": "High-alkaline fruits like cantaloupe help neutralize excess stomach acid.", "calories": "110 kcal", "protein": "1g", "fiber": "2g", "time": "4:30 PM"},
            "Dinner": {"name": "Steamed Zucchini & Tofu", "description": "Light, non-spicy meal to prevent nighttime reflux and ensure calm sleep.", "calories": "340 kcal", "protein": "22g", "fiber": "7g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Spicy Peppers", "Peppermint", "Chocolate", "Carbonated Soda"]
    },
    {
        "name": "Chest Congestion",
        "possibleCauses": [
            {"title": "Mucus Overproduction", "description": "The body produces excess phlegm in response to respiratory irritants or infection."},
            {"title": "Dehydration", "description": "Lack of fluids makes lung secretions thick and difficult to clear."},
            {"title": "Inflammatory Response", "description": "Systemic inflammation narrowing the airways and trapping fluids."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Warm Lemon & Honey Water", "description": "Honey acts as a natural expectorant to thin out chest mucus.", "calories": "80 kcal", "protein": "0g", "fiber": "0g", "time": "8:00 AM"},
            "Lunch": {"name": "Hot Chicken Vegetable Soup", "description": "Steaming broth helps hydrate the lungs and clear nasal passages.", "calories": "320 kcal", "protein": "24g", "fiber": "5g", "time": "1:00 PM"},
            "Snacks": {"name": "Fresh Ginger Tea", "description": "Gingerol compounds help reduce bronchial inflammation naturally.", "calories": "40 kcal", "protein": "0g", "fiber": "0g", "time": "4:00 PM"},
            "Dinner": {"name": "Garlic Sautéed Spinach & Cod", "description": "Garlic has antimicrobial properties that support respiratory health.", "calories": "380 kcal", "protein": "30g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Cold Milk", "Ice Cream", "Fried Foods", "Refined Sugar"]
    },
    {
        "name": "Sneezing",
        "possibleCauses": [
            {"title": "Histamine Sensitivity", "description": "Overreaction to environmental triggers like pollen, dust, or pet dander."},
            {"title": "Nasal Irritation", "description": "Chemicals or strong odors triggering the protective sneeze reflex."},
            {"title": "Low Antioxidant Levels", "description": "Lack of Vitamin C and Quercetin to stabilize mast cell response."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Golden Turmeric Milk & Berries", "description": "Curcumin and berries provide natural antihistamine support.", "calories": "260 kcal", "protein": "12g", "fiber": "6g", "time": "8:30 AM"},
            "Lunch": {"name": "Quinoa & Roasted Pepper Salad", "description": "High Vitamin C levels to help the body break down histamines.", "calories": "410 kcal", "protein": "15g", "fiber": "9g", "time": "1:30 PM"},
            "Snacks": {"name": "Red Apple with Skin", "description": "Apples are rich in Quercetin, a natural compound that reduces sneezing.", "calories": "95 kcal", "protein": "1g", "fiber": "4g", "time": "4:30 PM"},
            "Dinner": {"name": "Grilled Salmon & Broccoli", "description": "Omega-3s to lower the overall inflammatory threshold of the body.", "calories": "460 kcal", "protein": "34g", "fiber": "7g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Aged Cheese", "Fermented Soy", "Alcohol", "Processed Meats"]
    },
    {
        "name": "Watery Eyes",
        "possibleCauses": [
            {"title": "Allergic Rhinitis", "description": "Airborne allergens causing the tear ducts to overproduce moisture."},
            {"title": "Meibomian Gland Clog", "description": "Inadequate oil in tears leads to evaporation and compensatory watering."},
            {"title": "Vitamin A Scarcity", "description": "Lack of retinol affecting the health of the eye's protective surface."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Carrot & Mango Smoothie", "description": "Beta-carotene rich fruits to support eye membrane health.", "calories": "280 kcal", "protein": "5g", "fiber": "7g", "time": "8:00 AM"},
            "Lunch": {"name": "Spinach & Tuna Salad", "description": "Lutein and Omega-3s to protect and lubricate the eye surface.", "calories": "390 kcal", "protein": "28g", "fiber": "8g", "time": "1:00 PM"},
            "Snacks": {"name": "Walnuts", "description": "Healthy fats to improve the quality of the tear film's oil layer.", "calories": "180 kcal", "protein": "4g", "fiber": "2g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Sweet Potato & Kale", "description": "High Vitamin A and C to reduce ocular inflammatory responses.", "calories": "350 kcal", "protein": "10g", "fiber": "11g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Excessive Sodium", "Highly Spiced Foods", "Sugary Drinks", "Alcohol"]
    },
    {
        "name": "Dry Mouth",
        "possibleCauses": [
            {"title": "Electrolyte Imbalance", "description": "Inadequate potassium and sodium levels affecting saliva production."},
            {"title": "Mouth Breathing", "description": "Constant airflow drying out the oral mucosa during sleep or exercise."},
            {"title": "Caffeine Overload", "description": "Stimulants acting as diuretics, reducing systemic and oral hydration."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Cottage Cheese & Peaches", "description": "Hydrating fruit and protein to stimulate early saliva flow.", "calories": "240 kcal", "protein": "25g", "fiber": "3g", "time": "8:30 AM"},
            "Lunch": {"name": "Cucumber & Chickpea Salad", "description": "Very high water content to hydrate the body from within.", "calories": "380 kcal", "protein": "18g", "fiber": "12g", "time": "1:30 PM"},
            "Snacks": {"name": "Coconut Water", "description": "Natural electrolytes to restore fluid balance in the mouth.", "calories": "60 kcal", "protein": "0g", "fiber": "0g", "time": "4:30 PM"},
            "Dinner": {"name": "Steamed Cod & Stewed Tomatoes", "description": "Soft, moist meal that is easy to chew and swallow.", "calories": "360 kcal", "protein": "30g", "fiber": "5g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Salty Crackers", "Dry Bread", "Alcoholic Mouthwash", "Strong Spices"]
    },
    {
        "name": "Bleeding Gums",
        "possibleCauses": [
            {"title": "Vitamin C Deficiency", "description": "Lack of ascorbic acid weakens the collagen in gum tissues."},
            {"title": "Bacterial Plaque", "description": "Microbes triggering an inflammatory immune response in the gums."},
            {"title": "Vitamin K Shortage", "description": "Low levels of Vitamin K affect the body's natural blood clotting ability."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Orange & Kiwi Fruit Bowl", "description": "Extreme Vitamin C boost to strengthen delicate gum capillaries.", "calories": "220 kcal", "protein": "4g", "fiber": "8g", "time": "8:00 AM"},
            "Lunch": {"name": "Red Bell Pepper & Kale Salad", "description": "Vitamin K and C duo to support tissue repair and clotting.", "calories": "340 kcal", "protein": "10g", "fiber": "9g", "time": "1:00 PM"},
            "Snacks": {"name": "Strawberries & Greek Yogurt", "description": "Antioxidants and probiotics for oral microbiome health.", "calories": "200 kcal", "protein": "15g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Steamed Broccoli & Salmon", "description": "High-quality protein and vitamins for rapid gum healing.", "calories": "450 kcal", "protein": "35g", "fiber": "7g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Acidic Soda", "Hard Crunchy Tacos", "Sugary Candies", "Sticky Toffee"]
    },
    {
        "name": "Brittle Nails",
        "possibleCauses": [
            {"title": "Biotin Deficiency", "description": "Lack of Vitamin B7 which is essential for keratin structure."},
            {"title": "Iron Anemia", "description": "Poor oxygen delivery to the nail bed makes nails thin and weak."},
            {"title": "Protein Malnutrition", "description": "Insufficient amino acids to build the hard nail plate."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Scrambled Eggs & Spinach", "description": "High Biotin and Iron for foundational nail strength.", "calories": "310 kcal", "protein": "22g", "fiber": "3g", "time": "8:30 AM"},
            "Lunch": {"name": "Lentil & Quinoa Bowl", "description": "Plant-based protein and minerals for keratin production.", "calories": "420 kcal", "protein": "20g", "fiber": "14g", "time": "1:30 PM"},
            "Snacks": {"name": "Handful of Almonds", "description": "Rich in Vitamin E and magnesium to support nail growth.", "calories": "160 kcal", "protein": "6g", "fiber": "4g", "time": "4:30 PM"},
            "Dinner": {"name": "Grilled Salmon & Asparagus", "description": "Omega-3s and zinc to prevent nail splitting and dryness.", "calories": "440 kcal", "protein": "32g", "fiber": "6g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Raw Egg Whites", "Excessive Alcohol", "Highly Processed Sugar", "High-Mercury Fish"]
    },
    {
        "name": "Cold Sores",
        "possibleCauses": [
            {"title": "Arginine/Lysine Imbalance", "description": "High levels of arginine relative to lysine can trigger viral flares."},
            {"title": "Immune Suppression", "description": "Nutritional gaps making the body unable to keep the virus dormant."},
            {"title": "Stress/Fatigue", "description": "Emotional strain depleting B-vitamins and weakening immunity."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Greek Yogurt with Blueberries", "description": "High-lysine dairy to counteract viral replication triggers.", "calories": "280 kcal", "protein": "18g", "fiber": "4g", "time": "8:00 AM"},
            "Lunch": {"name": "Turkey & Avocado Wrap", "description": "Turkey is a great lysine source for maintaining viral dormancy.", "calories": "430 kcal", "protein": "28g", "fiber": "8g", "time": "1:00 PM"},
            "Snacks": {"name": "Pumpkin Seeds", "description": "Zinc-rich seeds to support skin-level immune defenses.", "calories": "150 kcal", "protein": "8g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Fish & Steamed Broccoli", "description": "Lean protein and Vitamin C to speed up sore healing time.", "calories": "390 kcal", "protein": "32g", "fiber": "7g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Chocolate", "Peanuts", "Gelatin", "Highly Acidic Citrus"]
    },
    {
        "name": "Excessive Thirst",
        "possibleCauses": [
            {"title": "High Sodium Intake", "description": "Excess salt pulls water out of cells, triggering thirst signals."},
            {"title": "Blood Sugar Spikes", "description": "Hyperglycemia forces kidneys to use more water to flush sugar."},
            {"title": "Systemic Dehydration", "description": "Chronic low water intake leading to a constant state of thirst."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Chia Seed & Fruit Pudding", "description": "Hydrating seeds that hold 10x their weight in water.", "calories": "300 kcal", "protein": "9g", "fiber": "12g", "time": "8:30 AM"},
            "Lunch": {"name": "Cucumber & Quinoa Bowl", "description": "Potassium-rich vegetables to balance internal sodium levels.", "calories": "380 kcal", "protein": "15g", "fiber": "10g", "time": "1:30 PM"},
            "Snacks": {"name": "Coconut Water & Celery", "description": "Natural electrolytes to quench cellular thirst effectively.", "calories": "90 kcal", "protein": "1g", "fiber": "2g", "time": "4:30 PM"},
            "Dinner": {"name": "Steamed Leafy Greens & Tofu", "description": "Low-sodium, high-moisture meal for nighttime fluid balance.", "calories": "350 kcal", "protein": "22g", "fiber": "8g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Salty Processed Meats", "Soy Sauce", "Sugary Energy Drinks", "Alcohol"]
    },
    {
        "name": "Frequent Urination",
        "possibleCauses": [
            {"title": "Bladder Irritants", "description": "Caffeine and artificial sweeteners irritating the bladder lining."},
            {"title": "Unstable Blood Sugar", "description": "The body trying to eliminate excess glucose through urine."},
            {"title": "High Fluid Intake", "description": "Consuming large amounts of water in a very short window."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Warm Oatmeal & Blueberries", "description": "Low-irritant complex carbs for a stable start to the day.", "calories": "310 kcal", "protein": "10g", "fiber": "9g", "time": "8:00 AM"},
            "Lunch": {"name": "Boiled Chicken & Brown Rice", "description": "Gentle on the kidneys and bladder, providing steady energy.", "calories": "440 kcal", "protein": "32g", "fiber": "6g", "time": "1:00 PM"},
            "Snacks": {"name": "Pumpkin Seeds", "description": "Magnesium and zinc for muscle and nerve control in the bladder.", "calories": "160 kcal", "protein": "8g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Steamed Squash & Poached Egg", "description": "Hydrating but low-acid vegetables to calm the bladder overnight.", "calories": "330 kcal", "protein": "15g", "fiber": "7g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Caffeine", "Alcohol", "Artificial Sweeteners", "Acidic Fruit Juices"]
    },
    {
        "name": "Muscle Weakness",
        "possibleCauses": [
            {"title": "Potassium Deficiency", "description": "Lack of potassium prevents muscles from firing and contracting."},
            {"title": "Vitamin D Scarcity", "description": "Vital for muscle fiber repair and overall neuromuscular function."},
            {"title": "Insufficient Protein", "description": "Not enough amino acids to maintain and repair muscle tissue."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Banana & Peanut Butter Toast", "description": "Potassium and protein to support early muscle energy.", "calories": "360 kcal", "protein": "12g", "fiber": "7g", "time": "8:30 AM"},
            "Lunch": {"name": "Lean Beef Salad & Quinoa", "description": "Iron, B12, and protein for sustained muscular strength.", "calories": "480 kcal", "protein": "35g", "fiber": "8g", "time": "1:30 PM"},
            "Snacks": {"name": "Greek Yogurt & Walnuts", "description": "Calcium and healthy fats for nerve-to-muscle signaling.", "calories": "220 kcal", "protein": "16g", "fiber": "3g", "time": "4:30 PM"},
            "Dinner": {"name": "Baked Salmon & Mushrooms", "description": "High Vitamin D and Omega-3s to reduce muscle fatigue.", "calories": "450 kcal", "protein": "32g", "fiber": "5g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Added Sugars", "Alcohol", "Trans Fats", "Highly Salty Snacks"]
    },
    {
        "name": "Eye Twitching",
        "possibleCauses": [
            {"title": "Magnesium Deficiency", "description": "Prevents small eye muscles from relaxing after contraction."},
            {"title": "Caffeine Overload", "description": "Excess stimulants causing involuntary nerve firing around the eye."},
            {"title": "High Cortisol (Stress)", "description": "Stress hormones increasing neuromuscular excitability."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Spinach & Egg Omelet", "description": "High magnesium and protein to stabilize nerve activity.", "calories": "320 kcal", "protein": "22g", "fiber": "3g", "time": "8:00 AM"},
            "Lunch": {"name": "Black Bean & Quinoa Bowl", "description": "Fiber and magnesium rich for steady energy and calm nerves.", "calories": "410 kcal", "protein": "18g", "fiber": "14g", "time": "1:00 PM"},
            "Snacks": {"name": "Small Piece of Dark Chocolate", "description": "Rich in flavonoids and magnesium to relax eye muscles.", "calories": "140 kcal", "protein": "2g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Mackerel & Kale", "description": "Omega-3s to support the nervous system and reduce twitching.", "calories": "430 kcal", "protein": "30g", "fiber": "8g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Excessive Caffeine", "Energy Drinks", "High-Sugar Snacks", "Alcohol"]
    },
    {
        "name": "Cold Hands and Feet",
        "possibleCauses": [
            {"title": "Poor Peripheral Circulation", "description": "Blood struggling to reach the extremities due to narrowing vessels."},
            {"title": "Iron Deficiency", "description": "Lack of hemoglobin reducing the blood's heat-carrying capacity."},
            {"title": "Low Caloric Intake", "description": "Not enough fuel for the body to maintain its core temperature."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Warm Oats with Ginger & Nuts", "description": "Ginger acts as a vasodilator to improve blood circulation.", "calories": "340 kcal", "protein": "10g", "fiber": "9g", "time": "8:30 AM"},
            "Lunch": {"name": "Lentil Soup with Cumin", "description": "Iron-rich legumes and warming spices to boost heat.", "calories": "390 kcal", "protein": "20g", "fiber": "12g", "time": "1:30 PM"},
            "Snacks": {"name": "Brazil Nuts & Warm Tea", "description": "Selenium to support the thyroid's metabolism.", "calories": "170 kcal", "protein": "4g", "fiber": "2g", "time": "4:30 PM"},
            "Dinner": {"name": "Grilled Beef & Roasted Veggies", "description": "Heme iron and B12 for red blood cell and thermal health.", "calories": "500 kcal", "protein": "35g", "fiber": "7g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Iced Drinks", "Raw Cold Salads", "Excessive Caffeine", "Salty Snacks"]
    },
    {
        "name": "Swollen Ankles",
        "possibleCauses": [
            {"title": "High Sodium Retention", "description": "Excess salt causing the body to hold water in the lower limbs."},
            {"title": "Poor Venous Return", "description": "Weak circulation allowing fluid to pool around the ankles."},
            {"title": "Potassium/Sodium Gap", "description": "Insufficient potassium to help flush out excess water and salt."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Potassium-Rich Banana Smoothie", "description": "Potassium to help the kidneys excrete excess sodium.", "calories": "280 kcal", "protein": "8g", "fiber": "6g", "time": "8:00 AM"},
            "Lunch": {"name": "Dandelion Green & Pear Salad", "description": "Dandelion is a natural diuretic that reduces water retention.", "calories": "320 kcal", "protein": "7g", "fiber": "9g", "time": "1:00 PM"},
            "Snacks": {"name": "Celery Sticks & Hummus", "description": "Celery contains phthalides that act as natural diuretics.", "calories": "150 kcal", "protein": "6g", "fiber": "5g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Sweet Potato & Salmon", "description": "Low-salt, high-potassium meal to reduce nightly swelling.", "calories": "440 kcal", "protein": "32g", "fiber": "10g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Canned Soups", "Processed Meats", "Soy Sauce", "Salty Chips"]
    },
    {
        "name": "Pale Skin",
        "possibleCauses": [
            {"title": "Iron Deficiency Anemia", "description": "Lack of red blood cells to give skin its healthy pinkish glow."},
            {"title": "Vitamin B12 Scarcity", "description": "Needed for the production of large, healthy red blood cells."},
            {"title": "Low Sunlight Exposure", "description": "Lack of Vitamin D synthesis affecting overall skin vitality."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Spinach & Mushroom Omelet", "description": "High in Iron and B-vitamins for blood health.", "calories": "330 kcal", "protein": "22g", "fiber": "3g", "time": "8:30 AM"},
            "Lunch": {"name": "Roast Beef & Beetroot Salad", "description": "Heme iron and nitrates for optimal blood flow and color.", "calories": "460 kcal", "protein": "32g", "fiber": "7g", "time": "1:30 PM"},
            "Snacks": {"name": "Pomegranate Seeds", "description": "Rich in antioxidants that support micro-circulation in skin.", "calories": "120 kcal", "protein": "2g", "fiber": "4g", "time": "4:30 PM"},
            "Dinner": {"name": "Lentil Stew with Kale", "description": "Double dose of plant iron to boost hemoglobin levels.", "calories": "410 kcal", "protein": "24g", "fiber": "15g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Excessive Coffee", "Strong Black Tea", "Highly Processed Flour", "Alcohol"]
    },
    {
        "name": "Dark Circles",
        "possibleCauses": [
            {"title": "Systemic Dehydration", "description": "Skin around eyes thins when dehydrated, revealing blood vessels."},
            {"title": "Iron Deficiency", "description": "Low oxygen in blood makes vessels under the eyes look darker."},
            {"title": "Poor Kidney Filtration", "description": "Water retention and toxin buildup affecting the under-eye area."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Berry & Greek Yogurt Bowl", "description": "Antioxidants and hydration to plump the under-eye skin.", "calories": "280 kcal", "protein": "18g", "fiber": "5g", "time": "8:00 AM"},
            "Lunch": {"name": "Grilled Fish & Spinach Salad", "description": "Iron and healthy fats to support skin structure and blood.", "calories": "410 kcal", "protein": "30g", "fiber": "8g", "time": "1:00 PM"},
            "Snacks": {"name": "Cucumber Slices & Mint Water", "description": "Hydrating duo to flush out eye-puffing toxins.", "calories": "50 kcal", "protein": "1g", "fiber": "2g", "time": "4:00 PM"},
            "Dinner": {"name": "Iron-Rich Beef & Broccoli", "description": "High B12 and Iron for long-term under-eye brightness.", "calories": "480 kcal", "protein": "35g", "fiber": "7g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Salty Snacks", "Alcohol", "Caffeine at Night", "Sugary Treats"]
    },
    {
        "name": "Brittle Hair",
        "possibleCauses": [
            {"title": "Zinc Deficiency", "description": "Zinc is needed for the production of the protein that builds hair."},
            {"title": "Lack of Essential Fats", "description": "Omega-3s are required to keep the hair shaft lubricated and shiny."},
            {"title": "Silica Shortage", "description": "Trace mineral needed for the strength and elasticity of hair strands."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Eggs & Pumpkin Seeds", "description": "Biotin and Zinc to strengthen the hair from the follicle.", "calories": "340 kcal", "protein": "22g", "fiber": "4g", "time": "8:30 AM"},
            "Lunch": {"name": "Quinoa & Chickpea Bowl", "description": "Plant proteins and silica for hair shaft structural integrity.", "calories": "420 kcal", "protein": "18g", "fiber": "12g", "time": "1:30 PM"},
            "Snacks": {"name": "Walnuts & Berries", "description": "Omega-3s and antioxidants to prevent hair breakage.", "calories": "190 kcal", "protein": "5g", "fiber": "4g", "time": "4:30 PM"},
            "Dinner": {"name": "Baked Salmon & Carrots", "description": "Vitamin A and healthy oils for a naturally shiny hair shaft.", "calories": "460 kcal", "protein": "32g", "fiber": "6g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Sugary Sodas", "Alcohol", "Refined Seed Oils", "High-Mercury Fish"]
    },
    {
        "name": "Chronic Cough",
        "possibleCauses": [
            {"title": "Persistent Inflammation", "description": "Ongoing irritation of the throat and bronchial tubes."},
            {"title": "Acid Reflux (LPR)", "description": "Silent acid reaching the throat and triggering a cough reflex."},
            {"title": "Mucus Buildup", "description": "Thick secretions that the body is constantly trying to expel."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Warm Honey & Lemon Water", "description": "Soothes the throat and acts as a natural cough suppressant.", "calories": "80 kcal", "protein": "0g", "fiber": "0g", "time": "8:00 AM"},
            "Lunch": {"name": "Chicken Broth & Soft Veggies", "description": "Hydrating and non-irritating to a sensitive throat lining.", "calories": "310 kcal", "protein": "20g", "fiber": "5g", "time": "1:00 PM"},
            "Snacks": {"name": "Ginger Tea", "description": "Reduces inflammation in the airways and settles the stomach.", "calories": "20 kcal", "protein": "0g", "fiber": "0g", "time": "4:00 PM"},
            "Dinner": {"name": "Poached Fish & Mashed Squash", "description": "Soft texture and non-spicy to prevent reflux-driven coughing.", "calories": "360 kcal", "protein": "28g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Cold Dairy", "Fried Oily Foods", "Spicy Chili", "Vinegar"]
    },
    {
        "name": "Wheezing",
        "possibleCauses": [
            {"title": "Bronchial Spasms", "description": "Tightening of the muscles around the airways during breathing."},
            {"title": "Allergic Inflammation", "description": "Internal swelling of airways in response to food or air triggers."},
            {"title": "Excess Mucus Lining", "description": "Narrowing of the air passage due to thick internal secretions."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Turmeric & Berry Smoothie", "description": "Powerful anti-inflammatory duo to open up airways.", "calories": "270 kcal", "protein": "10g", "fiber": "7g", "time": "8:30 AM"},
            "Lunch": {"name": "Salmon & Spinach Salad", "description": "Magnesium and Omega-3s to help relax bronchial muscles.", "calories": "410 kcal", "protein": "30g", "fiber": "8g", "time": "1:30 PM"},
            "Snacks": {"name": "Apple & Walnuts", "description": "Flavonoids that are linked to better overall lung function.", "calories": "180 kcal", "protein": "4g", "fiber": "5g", "time": "4:30 PM"},
            "Dinner": {"name": "Garlic-Rich Vegetable Stew", "description": "Allicin in garlic helps reduce respiratory inflammation.", "calories": "340 kcal", "protein": "12g", "fiber": "10g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Cold Milk", "Processed Meats", "Salty Snacks", "Artificial Additives"]
    },
    {
        "name": "Memory Lapses",
        "possibleCauses": [
            {"title": "Vitamin B12 Deficiency", "description": "Lack of the vitamin needed to maintain brain nerve pathways."},
            {"title": "Omega-3 Scarcity", "description": "The brain's structure is 60% fat; lack of DHA causes lapses."},
            {"title": "High Sugar Volatility", "description": "Brain 'fog' and memory drops due to blood sugar crashes."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Egg & Avocado Toast", "description": "Choline and healthy fats to support neurotransmitters.", "calories": "360 kcal", "protein": "18g", "fiber": "7g", "time": "8:00 AM"},
            "Lunch": {"name": "Mackerel & Greens Salad", "description": "Extreme DHA Omega-3 boost for brain cell communication.", "calories": "450 kcal", "protein": "32g", "fiber": "9g", "time": "1:00 PM"},
            "Snacks": {"name": "Blueberries & Walnuts", "description": "Antocyanins that improve memory and cognitive focus.", "calories": "190 kcal", "protein": "5g", "fiber": "4g", "time": "4:00 PM"},
            "Dinner": {"name": "Lean Beef & Steamed Broccoli", "description": "Iron and B12 to keep brain energy and memory sharp.", "calories": "490 kcal", "protein": "35g", "fiber": "8g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Trans Fats", "Added Sugars", "Artificial Sweeteners", "Alcohol"]
    },
    {
        "name": "Irritability",
        "possibleCauses": [
            {"title": "Blood Sugar Swings", "description": "Rapid glucose drops trigger 'hangry' feelings and anger."},
            {"title": "Magnesium Depletion", "description": "Lack of the 'relaxation mineral' for the nervous system."},
            {"title": "Excessive Caffeine", "description": "Overstimulation of the adrenal glands leading to edginess."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Whole Grain Oats & Almonds", "description": "Steady-release energy to prevent morning mood crashes.", "calories": "320 kcal", "protein": "12g", "fiber": "10g", "time": "8:30 AM"},
            "Lunch": {"name": "Turkey & Spinach Wrap", "description": "Tryptophan to boost serotonin and calm the mood.", "calories": "410 kcal", "protein": "28g", "fiber": "8g", "time": "1:30 PM"},
            "Snacks": {"name": "Pumpkin Seeds", "description": "High magnesium to physically relax the nervous system.", "calories": "160 kcal", "protein": "8g", "fiber": "3g", "time": "4:30 PM"},
            "Dinner": {"name": "Baked Salmon & Sweet Potato", "description": "Omega-3s and B6 to regulate emotional stability.", "calories": "460 kcal", "protein": "32g", "fiber": "9g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Sugary Snacks", "Alcohol", "High Caffeine", "Processed Meats"]
    },
    {
        "name": "Sensitivity to Cold",
        "possibleCauses": [
            {"title": "Iron Deficiency", "description": "Inability to produce enough heat due to low hemoglobin levels."},
            {"title": "Low Metabolic Rate", "description": "Insufficient calorie intake to fuel the body's furnace."},
            {"title": "Thyroid Nutrient Gaps", "description": "Lack of iodine or selenium needed for temperature control."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Warm Quinoa Porridge", "description": "Complex carbs and heat to raise core temperature early.", "calories": "330 kcal", "protein": "12g", "fiber": "8g", "time": "8:30 AM"},
            "Lunch": {"name": "Lentil Soup with Ginger", "description": "Warming spices and iron to boost internal heat production.", "calories": "400 kcal", "protein": "22g", "fiber": "14g", "time": "1:30 PM"},
            "Snacks": {"name": "Brazil Nuts", "description": "Selenium to support the thyroid's temperature regulation.", "calories": "170 kcal", "protein": "4g", "fiber": "2g", "time": "4:30 PM"},
            "Dinner": {"name": "Beef Stew with Root Veggies", "description": "Dense calories and iron to maintain heat through the night.", "calories": "510 kcal", "protein": "35g", "fiber": "8g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Iced Water", "Cold Salads", "Excessive Raw Fruit", "Caffeine"]
    },
    {
        "name": "Sensitivity to Heat",
        "possibleCauses": [
            {"title": "Hyper-Metabolism", "description": "Body burning fuel too fast, leading to excess internal heat."},
            {"title": "Electrolyte Depletion", "description": "Lack of salt and minerals preventing efficient cooling."},
            {"title": "Caffeine/Stimulants", "description": "Raising heart rate and core temperature artificially."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Cooling Yogurt & Berries", "description": "Temperature-neutral start to avoid metabolic heat spikes.", "calories": "280 kcal", "protein": "18g", "fiber": "5g", "time": "8:00 AM"},
            "Lunch": {"name": "Cucumber & Mint Quinoa", "description": "Cooling herbs and water-rich veggies to lower body heat.", "calories": "370 kcal", "protein": "12g", "fiber": "9g", "time": "1:00 PM"},
            "Snacks": {"name": "Coconut Water", "description": "Essential electrolytes to support the body's cooling system.", "calories": "60 kcal", "protein": "0g", "fiber": "0g", "time": "4:00 PM"},
            "Dinner": {"name": "Steamed Zucchini & Soft Rice", "description": "Low-thermogenic meal that won't raise nightly body heat.", "calories": "330 kcal", "protein": "10g", "fiber": "7g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Hot Chili Peppers", "Alcohol", "Hot Coffee", "Heavy Red Meat"]
    },
    {
        "name": "Ringing in Ears",
        "possibleCauses": [
            {"title": "Zinc Deficiency", "description": "Lack of zinc in the inner ear affecting neural signaling."},
            {"title": "High Blood Pressure", "description": "Excessive salt intake causing pressure in the ear's blood vessels."},
            {"title": "B12 Scarcity", "description": "Degradation of the nerves that carry sound to the brain."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Pumpkin Seed Oatmeal", "description": "Extreme zinc boost to support inner ear nerve health.", "calories": "340 kcal", "protein": "14g", "fiber": "10g", "time": "8:30 AM"},
            "Lunch": {"name": "Grilled Fish & Spinach Salad", "description": "Magnesium and B12 to improve blood flow to the ears.", "calories": "410 kcal", "protein": "30g", "fiber": "8g", "time": "1:30 PM"},
            "Snacks": {"name": "Cashews & Apple", "description": "Zinc and flavonoids to protect auditory nerve pathways.", "calories": "190 kcal", "protein": "6g", "fiber": "4g", "time": "4:30 PM"},
            "Dinner": {"name": "Baked Beans & Steamed Veggies", "description": "Potassium-rich meal to help regulate ear-related blood pressure.", "calories": "380 kcal", "protein": "20g", "fiber": "11g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Excessive Salt", "Caffeine", "Alcohol", "High-Sugar Sweets"]
    },
    {
        "name": "Indigestion",
        "possibleCauses": [
            {"title": "Low Gastric Acid", "description": "Inability to break down proteins efficiently in the stomach."},
            {"title": "High Fat Consumption", "description": "Greasy meals slowing down the emptying of the stomach."},
            {"title": "Rapid Eating", "description": "Swallowing air and not chewing enough for proper enzyme mix."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Fresh Papaya & Oats", "description": "Papain enzymes to jumpstart protein digestion early.", "calories": "290 kcal", "protein": "8g", "fiber": "10g", "time": "8:30 AM"},
            "Lunch": {"name": "Boiled Chicken & White Rice", "description": "Simple, easily processed meal to avoid digestive lag.", "calories": "420 kcal", "protein": "32g", "fiber": "3g", "time": "1:30 PM"},
            "Snacks": {"name": "Ginger Tea", "description": "Potent natural digestive aid to settle stomach churning.", "calories": "20 kcal", "protein": "0g", "fiber": "0g", "time": "4:30 PM"},
            "Dinner": {"name": "Steamed Veggies & Silken Tofu", "description": "Light and easy to ensure calm nighttime digestion.", "calories": "310 kcal", "protein": "20g", "fiber": "8g", "time": "7:30 PM"}
        },
        "foodsToAvoid": ["Deep Fried Foods", "Carbonated Drinks", "Raw Heavy Onions", "Spicy Chili"]
    },
    {
        "name": "Joint Stiffness",
        "possibleCauses": [
            {"title": "Synovial Inflammation", "description": "Reduced lubrication in joints due to inflammatory response."},
            {"title": "Morning Fluid Retention", "description": "Accumulation of interstitial fluid in joint spaces overnight."},
            {"title": "Collagen Degradation", "description": "Lack of repair nutrients affecting joint flexibility."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Flaxseed Oatmeal", "description": "Omega-3s to improve joint mobility from the morning.", "calories": "320 kcal", "protein": "10g", "fiber": "9g", "time": "8:00 AM"},
            "Lunch": {"name": "Sardine & Spinach Salad", "description": "High in Vitamin D and Omega-3s for joint lubrication.", "calories": "400 kcal", "protein": "25g", "fiber": "7g", "time": "1:00 PM"},
            "Snacks": {"name": "Pineapple Cubes", "description": "Contains bromelain which helps reduce joint swelling.", "calories": "120 kcal", "protein": "1g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Bone Broth with Tofu", "description": "Natural collagen precursors and easy protein.", "calories": "350 kcal", "protein": "22g", "fiber": "4g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Dairy", "Nightshade Veggies (if sensitive)", "Trans Fats", "Sugary Beverages"]
    },
    {
        "name": "Low Energy",
        "possibleCauses": [
            {"title": "Glycemic Instability", "description": "Frequent blood sugar spikes followed by severe crashes."},
            {"title": "B-Vitamin Scarcity", "description": "Lack of catalysts for turning food into cellular energy (ATP)."},
            {"title": "Cortisol Burnout", "description": "Adrenal fatigue from prolonged mental or physical stress."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Complex Carb Porridge", "description": "Steady energy release to avoid morning crashes.", "calories": "300 kcal", "protein": "12g", "fiber": "11g", "time": "8:00 AM"},
            "Lunch": {"name": "Chicken & Quinoa Bowl", "description": "Balanced macros for sustained afternoon focus.", "calories": "450 kcal", "protein": "32g", "fiber": "10g", "time": "1:00 PM"},
            "Snacks": {"name": "Dark Chocolate & Walnuts", "description": "Quick pick-me-up with magnesium and healthy fats.", "calories": "180 kcal", "protein": "4g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Iron-Rich Beef Stew", "description": "B12 and iron to replenish energy stores while you sleep.", "calories": "480 kcal", "protein": "35g", "fiber": "6g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Energy Drinks", "White Flour Products", "Added Sugars", "Excessive Caffeine"]
    },
    {
        "name": "Poor Sleep Quality",
        "possibleCauses": [
            {"title": "High Evening Cortisol", "description": "Stress hormones remaining elevated during rest hours."},
            {"title": "Melatonin Inhibition", "description": "Late-night screen use or dietary stimulants blocking sleep signals."},
            {"title": "Micronutrient Gaps", "description": "Lack of zinc or magnesium needed for deep sleep cycles."}
        ],
        "dietPlan": {
            "Breakfast": {"name": "Greek Yogurt & Pumpkin Seeds", "description": "Zinc and probiotics to start the day with hormone balance.", "calories": "280 kcal", "protein": "18g", "fiber": "4g", "time": "8:00 AM"},
            "Lunch": {"name": "Turkey & Kale Salad", "description": "Tryptophan source to prime serotonin/melatonin pathways early.", "calories": "350 kcal", "protein": "25g", "fiber": "9g", "time": "1:00 PM"},
            "Snacks": {"name": "Tart Cherries", "description": "Natural melatonin source for evening sleep readiness.", "calories": "100 kcal", "protein": "1g", "fiber": "3g", "time": "4:00 PM"},
            "Dinner": {"name": "Baked Cod & Sweet Potato", "description": "B6 and complex carbs to aid in tryptophan brain entry.", "calories": "420 kcal", "protein": "28g", "fiber": "8g", "time": "7:00 PM"}
        },
        "foodsToAvoid": ["Caffeine after 2 PM", "Alcohol", "Spicy Dinners", "Heavy Night Snacks"]
    }
]

def migrate():
    print("Starting migration to Firestore...")
    collection_ref = db.collection("symptoms")

    for symptom in SYMPTOMS_DATA:
        # Use symptom name as document ID for easier lookup
        doc_id = symptom["name"].lower().replace(" ", "_")
        doc_ref = collection_ref.document(doc_id)

        print(f"Uploading: {symptom['name']}...")
        doc_ref.set(symptom)

    print(f"Migration completed successfully! Total symptoms: {len(SYMPTOMS_DATA)}")

if __name__ == "__main__":
    migrate()
