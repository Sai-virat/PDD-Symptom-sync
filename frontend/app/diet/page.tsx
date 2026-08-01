"use client";

import { useState, useEffect } from "react";
import { ChevronRight, AlertTriangle, Coffee, Sun, Apple, Moon } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";

interface MealSpec {
  name: string;
  description: string;
  calories: string;
  protein: string;
  fiber: string;
  time: string;
}

interface AnalyzedMeal {
  title: string;
  time: string;
  details: MealSpec;
}

const DEFAULT_MEALS: Record<string, { icon: any; color: string; spec: MealSpec }> = {
  Breakfast: {
    icon: Coffee,
    color: "text-orange-400",
    spec: {
      name: "Ginger & Oats Bowl",
      description: "Ginger reduces inflammation and nausea often associated with migraines and acidity. Mixed with complex oats for steady energy release.",
      calories: "320 kcal",
      protein: "10g",
      fiber: "7g",
      time: "8:00 AM"
    }
  },
  Lunch: {
    icon: Sun,
    color: "text-green-400",
    spec: {
      name: "Quinoa & Spinach Power Salad",
      description: "Riboflavin (B2) and magnesium in spinach stabilize neural pathways and aid digestion.",
      calories: "420 kcal",
      protein: "14g",
      fiber: "10g",
      time: "1:00 PM"
    }
  },
  Snacks: {
    icon: Apple,
    color: "text-pink-400",
    spec: {
      name: "Papaya & Almond Bites",
      description: "Papain enzymes assist digestive breakdown while raw almonds help neutralize gastric acid naturally.",
      calories: "160 kcal",
      protein: "6g",
      fiber: "4g",
      time: "4:00 PM"
    }
  },
  Dinner: {
    icon: Moon,
    color: "text-indigo-400",
    spec: {
      name: "Steamed Salmon & Asparagus",
      description: "Anti-inflammatory omega-3 fatty acids soothing systemic inflammation and promoting REM recovery.",
      calories: "460 kcal",
      protein: "35g",
      fiber: "5g",
      time: "7:00 PM"
    }
  }
};

const DEFAULT_FOODS_TO_AVOID = ["Caffeine", "Alcohol", "Processed Meats", "Artificial Sweeteners"];

export default function DietPlanPage() {
  const [selectedMealType, setSelectedMealType] = useState<string | null>(null);
  const [showAvoid, setShowAvoid] = useState(false);
  const [customMeals, setCustomMeals] = useState<Record<string, MealSpec>>({});
  const [foodsToAvoid, setFoodsToAvoid] = useState<string[]>(DEFAULT_FOODS_TO_AVOID);

  useEffect(() => {
    try {
      const storedPlanStr = localStorage.getItem("symptomsync_dietPlan");
      if (storedPlanStr) {
        const parsedPlan: AnalyzedMeal[] = JSON.parse(storedPlanStr);
        const map: Record<string, MealSpec> = {};
        parsedPlan.forEach(item => {
          if (item.title && item.details) {
            map[item.title] = item.details;
          }
        });
        if (Object.keys(map).length > 0) {
          setCustomMeals(map);
        }
      }

      const storedAvoidStr = localStorage.getItem("symptomsync_foodsToAvoid");
      if (storedAvoidStr) {
        const parsedAvoid: string[] = JSON.parse(storedAvoidStr);
        if (parsedAvoid.length > 0) {
          setFoodsToAvoid(parsedAvoid);
        }
      }
    } catch (e) {
      console.warn("Failed to load local diet plan cache", e);
    }
  }, []);

  const mealTypes = ["Breakfast", "Lunch", "Snacks", "Dinner"];

  const getMealData = (type: string) => {
    const defaultData = DEFAULT_MEALS[type];
    const spec = customMeals[type] || defaultData.spec;
    return {
      icon: defaultData.icon,
      color: defaultData.color,
      spec
    };
  };

  const activeMealData = selectedMealType ? getMealData(selectedMealType) : null;

  return (
    <div className="max-w-4xl mx-auto py-8">
      <header className="mb-12">
        <h1 className="text-4xl font-bold mb-4">Your Diet Plan</h1>
        <p className="text-wellness-white/60">Tailored to alleviate your detected symptoms</p>
      </header>

      <div className="space-y-6 mb-16">
        {mealTypes.map((type) => {
          const data = getMealData(type);
          const Icon = data.icon;
          return (
            <motion.div
              key={type}
              whileHover={{ x: 10 }}
              className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex items-center justify-between cursor-pointer hover:border-white/20 transition-all"
              onClick={() => setSelectedMealType(type)}
            >
              <div className="flex items-center gap-6">
                <div className={`p-4 rounded-2xl bg-white/5 ${data.color}`}>
                  <Icon size={32} />
                </div>
                <div>
                  <h3 className="text-2xl font-bold text-white">{type}</h3>
                  <p className="text-wellness-white/40 font-medium uppercase text-sm tracking-wider">
                    {data.spec.time} • {data.spec.name}
                  </p>
                </div>
              </div>
              <ChevronRight className="text-wellness-white/20" size={32} />
            </motion.div>
          );
        })}
      </div>

      <button
        onClick={() => setShowAvoid(true)}
        className="w-full bg-healthGreen/10 border border-healthGreen text-healthGreen font-bold py-6 rounded-3xl flex items-center justify-center gap-3 hover:bg-healthGreen hover:text-white transition-all active:scale-[0.99]"
      >
        <AlertTriangle size={24} />
        <span>View Foods to Avoid ({foodsToAvoid.length})</span>
      </button>

      <AnimatePresence>
        {showAvoid && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              onClick={() => setShowAvoid(false)}
              className="absolute inset-0 bg-black/80 backdrop-blur-sm"
            />
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="relative max-w-lg w-full bg-wellness-card border border-white/10 text-white p-8 rounded-[40px] shadow-2xl"
            >
              <h2 className="text-3xl font-black text-richOrange mb-4">Foods to Avoid</h2>
              <p className="text-wellness-white/60 mb-8 font-medium">The following items may trigger flare-ups for your selected symptoms:</p>

              <ul className="space-y-4 mb-10">
                {foodsToAvoid.map(item => (
                  <li key={item} className="flex items-center gap-4">
                    <div className="w-2.5 h-2.5 rounded-full bg-richOrange" />
                    <span className="font-bold text-lg text-white">{item}</span>
                  </li>
                ))}
              </ul>

              <button
                onClick={() => setShowAvoid(false)}
                className="w-full bg-richOrange hover:bg-orange-600 text-white py-5 rounded-2xl font-bold text-xl transition-all"
              >
                Got it
              </button>
            </motion.div>
          </div>
        )}

        {selectedMealType && activeMealData && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              onClick={() => setSelectedMealType(null)}
              className="absolute inset-0 bg-black/80 backdrop-blur-sm"
            />
            <motion.div
              className="relative max-w-2xl w-full bg-wellness-card text-wellness-white border border-white/10 p-10 rounded-[40px] shadow-2xl overflow-hidden"
            >
              <div className="absolute top-0 right-0 w-32 h-32 bg-richOrange/10 rounded-bl-[80px]" />

              <header className="mb-8">
                <span className="text-richOrange font-black uppercase tracking-widest text-sm">Balanced {selectedMealType}</span>
                <h2 className="text-4xl font-bold mt-2 text-white">{activeMealData.spec.name}</h2>
                <p className="text-wellness-white/40 mt-1 font-medium italic">Recommended at {activeMealData.spec.time}</p>
              </header>

              <p className="text-lg text-wellness-white/80 leading-relaxed mb-10">
                {activeMealData.spec.description}
              </p>

              <div className="grid grid-cols-3 gap-4 mb-10">
                <div className="bg-wellness-charcoal p-5 rounded-3xl border border-white/5 text-center">
                  <div className="w-2 h-2 rounded-full bg-indicator-yellow mx-auto mb-3" />
                  <p className="text-indicator-yellow text-sm font-bold uppercase mb-1">Calories</p>
                  <p className="text-xl font-black tracking-tight text-white">{activeMealData.spec.calories}</p>
                </div>
                <div className="bg-wellness-charcoal p-5 rounded-3xl border border-white/5 text-center">
                  <div className="w-2 h-2 rounded-full bg-indicator-red mx-auto mb-3" />
                  <p className="text-indicator-red text-sm font-bold uppercase mb-1">Protein</p>
                  <p className="text-xl font-black tracking-tight text-white">{activeMealData.spec.protein}</p>
                </div>
                <div className="bg-wellness-charcoal p-5 rounded-3xl border border-white/5 text-center">
                  <div className="w-2 h-2 rounded-full bg-indicator-green mx-auto mb-3" />
                  <p className="text-indicator-green text-sm font-bold uppercase mb-1">Fiber</p>
                  <p className="text-xl font-black tracking-tight text-white">{activeMealData.spec.fiber}</p>
                </div>
              </div>

              <button
                onClick={() => setSelectedMealType(null)}
                className="w-full bg-wellness-white text-wellness-charcoal py-5 rounded-2xl font-bold text-xl hover:bg-white/90 transition-colors"
              >
                Close Details
              </button>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </div>
  );
}
