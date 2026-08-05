"use client";

import { useState, useEffect } from "react";
import { ChevronRight, AlertTriangle, Coffee, Sun, Apple, Moon, Sparkles, Droplets, Utensils, CheckCircle, Clock } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";

interface MealItem {
  mealType: string;
  time: string;
  name: string;
  description: string;
  calories: string;
  protein: string;
  fiber: string;
  keyNutrient?: string;
  preparation?: string;
  targetSymptom?: string;
  targetCause?: string;
}

interface CauseDietMapping {
  symptom: string;
  cause: string;
  dietName: string;
  foodsToAvoid: string[];
}

const DEFAULT_FULL_DAY_DIET: MealItem[] = [
  {
    mealType: "Breakfast",
    time: "8:00 AM",
    targetSymptom: "Migraine",
    targetCause: "Tyramine Trigger",
    name: "Ginger & Oats Bowl",
    description: "Ginger reduces neuro-inflammation and nausea associated with migraines.",
    calories: "320 kcal",
    protein: "10g",
    fiber: "7g",
    keyNutrient: "Magnesium & Gingerol",
    preparation: "Simmer oats in almond milk with fresh ginger slices. Top with walnuts."
  },
  {
    mealType: "Lunch",
    time: "1:00 PM",
    targetSymptom: "Bloating",
    targetCause: "FODMAP Sensitivity",
    name: "Grilled Chicken & Leafy Greens",
    description: "Rich in B-complex vitamins to stabilize neural pathways and ease gut transit.",
    calories: "450 kcal",
    protein: "38g",
    fiber: "6g",
    keyNutrient: "Riboflavin & Plant Iron",
    preparation: "Combine grilled chicken breast with steamed spinach and olive oil."
  },
  {
    mealType: "Evening Snack",
    time: "4:00 PM",
    targetSymptom: "Acidity",
    targetCause: "Gastric Acid Reflux",
    name: "Pumpkin Seeds & Almonds",
    description: "Packed with magnesium and alkaline minerals to neutralize stomach acid.",
    calories: "180 kcal",
    protein: "8g",
    fiber: "3g",
    keyNutrient: "Magnesium & Healthy Fats",
    preparation: "Enjoy a handful of raw unsalted pumpkin seeds and raw almonds."
  },
  {
    mealType: "Dinner",
    time: "7:00 PM",
    targetSymptom: "Joint Pain",
    targetCause: "Systemic Cartilage Strain",
    name: "Steamed Cod & Wild Rice",
    description: "Low-histamine lean protein and omega-3s to soothe systemic inflammation.",
    calories: "410 kcal",
    protein: "32g",
    fiber: "5g",
    keyNutrient: "Omega-3 & Selenium",
    preparation: "Steam cod fillet with ginger and serve over warm wild rice."
  }
];

const MEAL_ICONS: Record<string, { icon: any; color: string; badgeBg: string }> = {
  Breakfast: { icon: Coffee, color: "text-amber-400", badgeBg: "bg-amber-500/10 border-amber-500/20" },
  Lunch: { icon: Sun, color: "text-emerald-400", badgeBg: "bg-emerald-500/10 border-emerald-500/20" },
  "Evening Snack": { icon: Apple, color: "text-pink-400", badgeBg: "bg-pink-500/10 border-pink-500/20" },
  Dinner: { icon: Moon, color: "text-indigo-400", badgeBg: "bg-indigo-500/10 border-indigo-500/20" }
};

export default function DietPlanPage() {
  const [dietPlan, setDietPlan] = useState<MealItem[]>(DEFAULT_FULL_DAY_DIET);
  const [causeMappings, setCauseMappings] = useState<CauseDietMapping[]>([]);
  const [selectedMeal, setSelectedMeal] = useState<MealItem | null>(null);
  const [showAvoidModal, setShowAvoidModal] = useState(false);
  const [foodsToAvoid, setFoodsToAvoid] = useState<string[]>([
    "Aged Cheese & Processed Meats (Tyramine triggers)",
    "Refined Sugars & Artificial Sweeteners",
    "Excessive Caffeine & Carbonated Drinks",
    "Deep Fried Foods (Delays stomach emptying)"
  ]);
  const [aiBadge, setAiBadge] = useState<string>("✨ AI-Powered Full-Day Diet Planner");
  const [hydrationGoal, setHydrationGoal] = useState<string>("3.0 Liters / day (Warm Water with Lemon & Chamomile)");
  const [lifestyleTips, setLifestyleTips] = useState<string[]>([
    "Eat meals at consistent times to regulate digestive circadian rhythm.",
    "Avoid lying down within 2 hours after dinner to prevent acid reflux.",
    "Take a gentle 10-minute walk post-lunch for gut motility."
  ]);

  useEffect(() => {
    try {
      const storedAnalysis = localStorage.getItem("symptomsync_analysisData");
      if (storedAnalysis) {
        const parsed = JSON.parse(storedAnalysis);
        if (parsed.aiBadgeText) setAiBadge(parsed.aiBadgeText);
        if (parsed.hydrationGoal) setHydrationGoal(parsed.hydrationGoal);
        if (parsed.lifestyleRecommendations) setLifestyleTips(parsed.lifestyleRecommendations);
        
        if (Array.isArray(parsed.dietPlan) && parsed.dietPlan.length > 0) {
          const normalized: MealItem[] = parsed.dietPlan.map((item: any, idx: number) => ({
            mealType: item.mealType || item.title || "Meal",
            time: item.time || "Scheduled",
            name: item.name || item.details?.name || item.title || "Targeted Meal",
            description: item.description || item.details?.description || "Nutrient-dense meal tailored for recovery.",
            calories: item.calories || item.details?.calories || "350 kcal",
            protein: item.protein || item.details?.protein || "15g",
            fiber: item.fiber || item.details?.fiber || "6g",
            keyNutrient: item.keyNutrient,
            preparation: item.preparation,
            targetSymptom: item.targetSymptom || (parsed.analysis && parsed.analysis[idx % Math.max(1, parsed.analysis.length)]?.name),
            targetCause: item.targetCause || (parsed.possibleCauses && parsed.possibleCauses[idx % Math.max(1, parsed.possibleCauses.length)]?.title)
          }));
          setDietPlan(normalized);
        }

        if (Array.isArray(parsed.possibleCauses) && parsed.possibleCauses.length > 0) {
          const mappings: CauseDietMapping[] = parsed.possibleCauses.map((c: any) => ({
            symptom: c.targetSymptom || "General Symptom",
            cause: c.title || "Metabolic Trigger",
            dietName: c.recommendedDiet || `Prescribed ${c.targetSymptom || 'Recovery'} Diet`,
            foodsToAvoid: Array.isArray(c.foodsToAvoid) ? c.foodsToAvoid : ["Refined Sugars", "Ultra-Processed Foods"]
          }));
          setCauseMappings(mappings);
        }
        
        if (Array.isArray(parsed.foodsToAvoid) && parsed.foodsToAvoid.length > 0) {
          setFoodsToAvoid(parsed.foodsToAvoid);
        }
      }
    } catch (e) {
      console.warn("Failed to load local diet plan cache", e);
    }
  }, []);

  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      {/* Header */}
      <header className="mb-10">
        <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-healthGreen/10 border border-healthGreen/30 text-healthGreen font-bold text-sm mb-4 shadow-sm">
          <Sparkles size={16} />
          <span>{aiBadge}</span>
        </div>
        <h1 className="text-4xl font-extrabold mb-3">Your Full-Day Diet Schedule</h1>
        <p className="text-wellness-white/60 text-lg">A 24-hour nutrition plan designed to relieve your symptoms and boost vitality.</p>
      </header>

      {/* Hydration Banner */}
      <div className="bg-gradient-to-r from-blue-900/30 to-indigo-900/30 border border-blue-500/30 p-6 rounded-3xl mb-10 flex items-center justify-between gap-6 shadow-lg">
        <div className="flex items-center gap-4">
          <div className="p-3.5 rounded-2xl bg-blue-500/20 text-blue-400">
            <Droplets size={28} />
          </div>
          <div>
            <h3 className="text-xl font-bold text-white mb-1">Daily Hydration Target</h3>
            <p className="text-wellness-white/70 text-sm font-medium">{hydrationGoal}</p>
          </div>
        </div>
        <span className="hidden sm:block text-xs font-bold px-3 py-1.5 rounded-xl bg-blue-500/20 text-blue-300 border border-blue-400/20">
          Target Active
        </span>
      </div>

      {/* Full-Day Meal Timeline */}
      <div className="space-y-6 mb-12">
        <h2 className="text-2xl font-bold text-white flex items-center gap-2 mb-4">
          <Utensils className="text-smoothPurple" size={24} />
          <span>Full-Day Meal Timeline</span>
        </h2>

        {dietPlan.map((meal, idx) => {
          const config = MEAL_ICONS[meal.mealType] || MEAL_ICONS["Breakfast"];
          const Icon = config.icon;

          return (
            <motion.div
              key={idx}
              whileHover={{ scale: 1.01 }}
              onClick={() => setSelectedMeal(meal)}
              className="bg-wellness-card p-6 md:p-8 rounded-[32px] border border-white/10 flex flex-col md:flex-row items-start md:items-center justify-between gap-6 cursor-pointer hover:border-white/20 transition-all shadow-xl"
            >
              <div className="flex items-center gap-5">
                <div className={`p-4 rounded-2xl bg-wellness-charcoal ${config.color} border border-white/5 shrink-0`}>
                  <Icon size={32} />
                </div>
                <div>
                  <div className="flex flex-wrap items-center gap-2 mb-1.5">
                    <span className={`text-xs font-bold px-3 py-1 rounded-full border ${config.badgeBg} ${config.color}`}>
                      {meal.mealType}
                    </span>
                    <span className="text-wellness-white/50 text-xs font-semibold flex items-center gap-1">
                      <Clock size={12} />
                      {meal.time}
                    </span>
                    {meal.targetSymptom && (
                      <span className="text-[11px] font-black px-3 py-1 rounded-full bg-purple-500/20 text-purple-300 border border-purple-500/30">
                        🎯 For Symptom: {meal.targetSymptom}
                      </span>
                    )}
                    {meal.targetCause && (
                      <span className="text-[11px] font-bold px-3 py-1 rounded-full bg-blue-500/10 text-blue-300 border border-blue-500/20">
                        ⚡ Cause: {meal.targetCause}
                      </span>
                    )}
                  </div>
                  <h3 className="text-2xl font-bold text-white mt-1">{meal.name}</h3>
                  <p className="text-wellness-white/60 text-sm mt-1 line-clamp-2">{meal.description}</p>
                </div>
              </div>

              <div className="flex items-center gap-6 w-full md:w-auto justify-between md:justify-end border-t md:border-t-0 border-white/5 pt-4 md:pt-0">
                <div className="flex gap-4 text-center">
                  <div className="bg-white/5 px-3.5 py-2 rounded-xl border border-white/5">
                    <span className="text-xs text-amber-400 font-bold block">CAL</span>
                    <span className="text-sm font-black text-white">{meal.calories}</span>
                  </div>
                  <div className="bg-white/5 px-3.5 py-2 rounded-xl border border-white/5">
                    <span className="text-xs text-emerald-400 font-bold block">PRO</span>
                    <span className="text-sm font-black text-white">{meal.protein}</span>
                  </div>
                </div>
                <ChevronRight className="text-wellness-white/30 shrink-0" size={28} />
              </div>
            </motion.div>
          );
        })}
      </div>

      {/* Symptom to Prescribed Diet Mappings Breakdown */}
      {causeMappings.length > 0 && (
        <div className="mb-12">
          <h2 className="text-2xl font-bold text-white flex items-center gap-2 mb-6">
            <Sparkles className="text-emerald-400" size={24} />
            <span>Prescribed Diet Breakdown per Symptom & Cause</span>
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {causeMappings.map((m, i) => (
              <div key={i} className="bg-wellness-card p-6 rounded-3xl border border-white/10 space-y-3 shadow-lg">
                <div className="flex items-center justify-between">
                  <span className="px-3 py-1 rounded-full bg-purple-500/20 text-purple-300 border border-purple-500/30 text-xs font-black uppercase">
                    🩺 {m.symptom}
                  </span>
                  <span className="text-xs text-wellness-white/50 font-medium">⚡ {m.cause}</span>
                </div>
                <h4 className="text-lg font-extrabold text-white">{m.dietName}</h4>
                {m.foodsToAvoid.length > 0 && (
                  <div>
                    <span className="text-[11px] font-bold text-red-400 uppercase tracking-wider block mb-1">🚫 Foods to Avoid:</span>
                    <div className="flex flex-wrap gap-1.5">
                      {m.foodsToAvoid.map((food, fIdx) => (
                        <span key={fIdx} className="text-[11px] font-semibold px-2.5 py-0.5 rounded-lg bg-red-500/10 text-red-300 border border-red-500/20">
                          {food}
                        </span>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Action Banner for Trigger Foods & Lifestyle Tips */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-16">
        <button
          onClick={() => setShowAvoidModal(true)}
          className="bg-red-500/10 border border-red-500/30 p-6 rounded-3xl flex items-center justify-between text-left hover:bg-red-500/20 transition-all active:scale-[0.98] group"
        >
          <div className="flex items-center gap-4">
            <div className="p-3.5 rounded-2xl bg-red-500/20 text-red-400">
              <AlertTriangle size={24} />
            </div>
            <div>
              <h3 className="text-lg font-bold text-white">Foods to Avoid</h3>
              <p className="text-wellness-white/60 text-xs">{foodsToAvoid.length} trigger items detected</p>
            </div>
          </div>
          <ChevronRight className="text-red-400 group-hover:translate-x-1 transition-transform" size={24} />
        </button>

        <div className="bg-wellness-card border border-white/10 p-6 rounded-3xl">
          <div className="flex items-center gap-3 mb-3">
            <CheckCircle className="text-healthGreen" size={22} />
            <h3 className="text-lg font-bold text-white">Lifestyle Optimization</h3>
          </div>
          <ul className="space-y-2">
            {lifestyleTips.slice(0, 2).map((tip, i) => (
              <li key={i} className="text-wellness-white/70 text-xs font-medium flex items-center gap-2">
                <span className="w-1.5 h-1.5 rounded-full bg-healthGreen shrink-0" />
                <span>{tip}</span>
              </li>
            ))}
          </ul>
        </div>
      </div>

      {/* Modals */}
      <AnimatePresence>
        {/* Foods to Avoid Modal */}
        {showAvoidModal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              onClick={() => setShowAvoidModal(false)}
              className="absolute inset-0 bg-black/80 backdrop-blur-sm"
            />
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="relative max-w-lg w-full bg-wellness-card border border-white/10 text-white p-8 rounded-[40px] shadow-2xl z-10"
            >
              <h2 className="text-3xl font-extrabold text-red-400 mb-2">Trigger Foods to Avoid</h2>
              <p className="text-wellness-white/60 mb-6 font-medium text-sm">
                Steer clear of these items during active recovery to prevent symptom flare-ups:
              </p>

              <ul className="space-y-3.5 mb-8 max-h-60 overflow-y-auto pr-2">
                {foodsToAvoid.map((item, idx) => (
                  <li key={idx} className="flex items-start gap-3 bg-wellness-charcoal p-4 rounded-2xl border border-white/5">
                    <AlertTriangle size={18} className="text-red-400 shrink-0 mt-0.5" />
                    <span className="font-semibold text-sm text-white/90">{item}</span>
                  </li>
                ))}
              </ul>

              <button
                onClick={() => setShowAvoidModal(false)}
                className="w-full bg-red-500 hover:bg-red-600 text-white py-4 rounded-2xl font-bold text-lg transition-all"
              >
                Understood
              </button>
            </motion.div>
          </div>
        )}

        {/* Meal Details Modal */}
        {selectedMeal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              onClick={() => setSelectedMeal(null)}
              className="absolute inset-0 bg-black/80 backdrop-blur-sm"
            />
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="relative max-w-2xl w-full bg-wellness-card text-wellness-white border border-white/10 p-8 md:p-10 rounded-[40px] shadow-2xl z-10 overflow-hidden"
            >
              <header className="mb-6">
                <div className="flex flex-wrap items-center gap-2 mb-2">
                  <span className="text-smoothPurple font-bold uppercase tracking-widest text-xs">
                    {selectedMeal.mealType} • {selectedMeal.time}
                  </span>
                  {selectedMeal.targetSymptom && (
                    <span className="text-xs font-black px-3 py-0.5 rounded-full bg-purple-500/20 text-purple-300 border border-purple-500/30">
                      🎯 Targeted for {selectedMeal.targetSymptom}
                    </span>
                  )}
                  {selectedMeal.targetCause && (
                    <span className="text-xs font-bold px-3 py-0.5 rounded-full bg-blue-500/10 text-blue-300 border border-blue-500/20">
                      ⚡ Cause: {selectedMeal.targetCause}
                    </span>
                  )}
                </div>
                <h2 className="text-3xl font-extrabold text-white">{selectedMeal.name}</h2>
              </header>

              <p className="text-base text-wellness-white/80 leading-relaxed mb-8">
                {selectedMeal.description}
              </p>

              {/* Macros Breakdown */}
              <div className="grid grid-cols-3 gap-4 mb-8">
                <div className="bg-wellness-charcoal p-4 rounded-2xl border border-white/5 text-center">
                  <p className="text-amber-400 text-xs font-bold uppercase mb-1">Calories</p>
                  <p className="text-xl font-black text-white">{selectedMeal.calories}</p>
                </div>
                <div className="bg-wellness-charcoal p-4 rounded-2xl border border-white/5 text-center">
                  <p className="text-emerald-400 text-xs font-bold uppercase mb-1">Protein</p>
                  <p className="text-xl font-black text-white">{selectedMeal.protein}</p>
                </div>
                <div className="bg-wellness-charcoal p-4 rounded-2xl border border-white/5 text-center">
                  <p className="text-pink-400 text-xs font-bold uppercase mb-1">Fiber</p>
                  <p className="text-xl font-black text-white">{selectedMeal.fiber}</p>
                </div>
              </div>

              {/* Preparation Steps if present */}
              {selectedMeal.preparation && (
                <div className="bg-wellness-charcoal/60 p-5 rounded-2xl border border-white/5 mb-8">
                  <h4 className="text-xs font-bold uppercase tracking-wider text-smoothPurple mb-2 flex items-center gap-2">
                    <Utensils size={14} />
                    <span>Quick Preparation Recipe</span>
                  </h4>
                  <p className="text-sm text-wellness-white/80 leading-relaxed">{selectedMeal.preparation}</p>
                </div>
              )}

              <button
                onClick={() => setSelectedMeal(null)}
                className="w-full bg-white text-black py-4 rounded-2xl font-bold text-lg hover:bg-white/90 transition-colors"
              >
                Close Meal Details
              </button>
            </motion.div>
          </div>
        )}
      </AnimatePresence>

      {/* Action Banner for Trigger Foods & Lifestyle Tips */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-16">
        <button
          onClick={() => setShowAvoidModal(true)}
          className="bg-red-500/10 border border-red-500/30 p-6 rounded-3xl flex items-center justify-between text-left hover:bg-red-500/20 transition-all active:scale-[0.98] group"
        >
          <div className="flex items-center gap-4">
            <div className="p-3.5 rounded-2xl bg-red-500/20 text-red-400">
              <AlertTriangle size={24} />
            </div>
            <div>
              <h3 className="text-lg font-bold text-white">Foods to Avoid</h3>
              <p className="text-wellness-white/60 text-xs">{foodsToAvoid.length} trigger items detected</p>
            </div>
          </div>
          <ChevronRight className="text-red-400 group-hover:translate-x-1 transition-transform" size={24} />
        </button>

        <div className="bg-wellness-card border border-white/10 p-6 rounded-3xl">
          <div className="flex items-center gap-3 mb-3">
            <CheckCircle className="text-healthGreen" size={22} />
            <h3 className="text-lg font-bold text-white">Lifestyle Optimization</h3>
          </div>
          <ul className="space-y-2">
            {lifestyleTips.slice(0, 2).map((tip, i) => (
              <li key={i} className="text-wellness-white/70 text-xs font-medium flex items-center gap-2">
                <span className="w-1.5 h-1.5 rounded-full bg-healthGreen shrink-0" />
                <span>{tip}</span>
              </li>
            ))}
          </ul>
        </div>
      </div>

      {/* Modals */}
      <AnimatePresence>
        {/* Foods to Avoid Modal */}
        {showAvoidModal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              onClick={() => setShowAvoidModal(false)}
              className="absolute inset-0 bg-black/80 backdrop-blur-sm"
            />
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="relative max-w-lg w-full bg-wellness-card border border-white/10 text-white p-8 rounded-[40px] shadow-2xl z-10"
            >
              <h2 className="text-3xl font-extrabold text-red-400 mb-2">Trigger Foods to Avoid</h2>
              <p className="text-wellness-white/60 mb-6 font-medium text-sm">
                Steer clear of these items during active recovery to prevent symptom flare-ups:
              </p>

              <ul className="space-y-3.5 mb-8 max-h-60 overflow-y-auto pr-2">
                {foodsToAvoid.map((item, idx) => (
                  <li key={idx} className="flex items-start gap-3 bg-wellness-charcoal p-4 rounded-2xl border border-white/5">
                    <AlertTriangle size={18} className="text-red-400 shrink-0 mt-0.5" />
                    <span className="font-semibold text-sm text-white/90">{item}</span>
                  </li>
                ))}
              </ul>

              <button
                onClick={() => setShowAvoidModal(false)}
                className="w-full bg-red-500 hover:bg-red-600 text-white py-4 rounded-2xl font-bold text-lg transition-all"
              >
                Understood
              </button>
            </motion.div>
          </div>
        )}

        {/* Meal Details Modal */}
        {selectedMeal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              onClick={() => setSelectedMeal(null)}
              className="absolute inset-0 bg-black/80 backdrop-blur-sm"
            />
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="relative max-w-2xl w-full bg-wellness-card text-wellness-white border border-white/10 p-8 md:p-10 rounded-[40px] shadow-2xl z-10 overflow-hidden"
            >
              <header className="mb-6">
                <div className="flex items-center gap-3 mb-2">
                  <span className="text-smoothPurple font-bold uppercase tracking-widest text-xs">
                    {selectedMeal.mealType} • {selectedMeal.time}
                  </span>
                </div>
                <h2 className="text-3xl font-extrabold text-white">{selectedMeal.name}</h2>
              </header>

              <p className="text-base text-wellness-white/80 leading-relaxed mb-8">
                {selectedMeal.description}
              </p>

              {/* Macros Breakdown */}
              <div className="grid grid-cols-3 gap-4 mb-8">
                <div className="bg-wellness-charcoal p-4 rounded-2xl border border-white/5 text-center">
                  <p className="text-amber-400 text-xs font-bold uppercase mb-1">Calories</p>
                  <p className="text-xl font-black text-white">{selectedMeal.calories}</p>
                </div>
                <div className="bg-wellness-charcoal p-4 rounded-2xl border border-white/5 text-center">
                  <p className="text-emerald-400 text-xs font-bold uppercase mb-1">Protein</p>
                  <p className="text-xl font-black text-white">{selectedMeal.protein}</p>
                </div>
                <div className="bg-wellness-charcoal p-4 rounded-2xl border border-white/5 text-center">
                  <p className="text-pink-400 text-xs font-bold uppercase mb-1">Fiber</p>
                  <p className="text-xl font-black text-white">{selectedMeal.fiber}</p>
                </div>
              </div>

              {/* Preparation Steps if present */}
              {selectedMeal.preparation && (
                <div className="bg-wellness-charcoal/60 p-5 rounded-2xl border border-white/5 mb-8">
                  <h4 className="text-xs font-bold uppercase tracking-wider text-smoothPurple mb-2 flex items-center gap-2">
                    <Utensils size={14} />
                    <span>Quick Preparation Recipe</span>
                  </h4>
                  <p className="text-sm text-wellness-white/80 leading-relaxed">{selectedMeal.preparation}</p>
                </div>
              )}

              <button
                onClick={() => setSelectedMeal(null)}
                className="w-full bg-white text-black py-4 rounded-2xl font-bold text-lg hover:bg-white/90 transition-colors"
              >
                Close Meal Details
              </button>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </div>
  );
}
