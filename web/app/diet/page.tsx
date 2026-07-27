"use client";

import { useState } from "react";
import { ChevronRight, Info, AlertTriangle, Coffee, Sun, Apple, Moon } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import { clsx } from "clsx";

const MEALS = [
  { type: "Breakfast", time: "8:00 AM", icon: Coffee, color: "text-orange-400" },
  { type: "Lunch", time: "1:00 PM", icon: Sun, color: "text-green-400" },
  { type: "Snacks", time: "4:00 PM", icon: Apple, color: "text-pink-400" },
  { type: "Dinner", time: "7:00 PM", icon: Moon, color: "text-indigo-400" },
];

export default function DietPlanPage() {
  const [selectedMeal, setSelectedMeal] = useState<string | null>(null);
  const [showAvoid, setShowAvoid] = useState(false);

  return (
    <div className="max-w-4xl mx-auto py-8">
      <header className="mb-12">
        <h1 className="text-4xl font-bold mb-4">Your Diet Plan</h1>
        <p className="text-wellness-white/60">Tailored to alleviate your detected symptoms</p>
      </header>

      <div className="space-y-6 mb-16">
        {MEALS.map((meal) => (
          <motion.div
            key={meal.type}
            whileHover={{ x: 10 }}
            className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex items-center justify-between cursor-pointer hover:border-white/20 transition-all"
            onClick={() => setSelectedMeal(meal.type)}
          >
            <div className="flex items-center gap-6">
              <div className={`p-4 rounded-2xl bg-white/5 ${meal.color}`}>
                <meal.icon size={32} />
              </div>
              <div>
                <h3 className="text-2xl font-bold">{meal.type}</h3>
                <p className="text-wellness-white/40 font-medium uppercase text-sm tracking-wider">{meal.time}</p>
              </div>
            </div>
            <ChevronRight className="text-wellness-white/20" size={32} />
          </motion.div>
        ))}
      </div>

      <button
        onClick={() => setShowAvoid(true)}
        className="w-full bg-healthGreen/10 border border-healthGreen text-healthGreen font-bold py-6 rounded-3xl flex items-center justify-center gap-3 hover:bg-healthGreen hover:text-white transition-all active:scale-[0.99]"
      >
        <AlertTriangle size={24} />
        <span>View Foods to Avoid</span>
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
              className="relative max-w-lg w-full bg-wellness-white text-wellness-navy p-8 rounded-[40px] shadow-2xl"
            >
              <h2 className="text-3xl font-black text-richOrange mb-4">Foods to Avoid</h2>
              <p className="text-wellness-navy/60 mb-8 font-medium">The following items may trigger flare-ups for your selected symptoms:</p>

              <ul className="space-y-4 mb-10">
                {["Caffeine", "Alcohol", "Processed Meats", "Artificial Sweeteners"].map(item => (
                  <li key={item} className="flex items-center gap-4">
                    <div className="w-2 h-2 rounded-full bg-richOrange" />
                    <span className="font-bold text-lg">{item}</span>
                  </li>
                ))}
              </ul>

              <button
                onClick={() => setShowAvoid(false)}
                className="w-full bg-wellness-navy text-white py-5 rounded-2xl font-bold text-xl hover:opacity-90 transition-opacity"
              >
                Got it
              </button>
            </motion.div>
          </div>
        )}

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
              layoutId={selectedMeal}
              className="relative max-w-2xl w-full bg-wellness-card text-wellness-white border border-white/10 p-10 rounded-[40px] shadow-2xl overflow-hidden"
            >
              <div className="absolute top-0 right-0 w-32 h-32 bg-richOrange/10 rounded-bl-[80px]" />

              <header className="mb-8">
                <span className="text-richOrange font-black uppercase tracking-widest text-sm">Balanced {selectedMeal}</span>
                <h2 className="text-4xl font-bold mt-2">Ginger & Oats Bowl</h2>
                <p className="text-wellness-white/40 mt-1 font-medium italic">Best consumed by 8:30 AM</p>
              </header>

              <p className="text-lg text-wellness-white/80 leading-relaxed mb-10">
                Ginger reduces inflammation and nausea often associated with migraines. Mixed with complex carbohydrates for steady energy release.
              </p>

              <div className="grid grid-cols-3 gap-4 mb-10">
                <div className="bg-wellness-charcoal p-5 rounded-3xl border border-white/5 text-center">
                  <div className="w-2 h-2 rounded-full bg-indicator-yellow mx-auto mb-3" />
                  <p className="text-indicator-yellow text-sm font-bold uppercase mb-1">Calories</p>
                  <p className="text-xl font-black tracking-tight">320 kcal</p>
                </div>
                <div className="bg-wellness-charcoal p-5 rounded-3xl border border-white/5 text-center">
                  <div className="w-2 h-2 rounded-full bg-indicator-red mx-auto mb-3" />
                  <p className="text-indicator-red text-sm font-bold uppercase mb-1">Protein</p>
                  <p className="text-xl font-black tracking-tight">10g</p>
                </div>
                <div className="bg-wellness-charcoal p-5 rounded-3xl border border-white/5 text-center">
                  <div className="w-2 h-2 rounded-full bg-indicator-green mx-auto mb-3" />
                  <p className="text-indicator-green text-sm font-bold uppercase mb-1">Fiber</p>
                  <p className="text-xl font-black tracking-tight">7g</p>
                </div>
              </div>

              <button
                onClick={() => setSelectedMeal(null)}
                className="w-full bg-wellness-white text-wellness-charcoal py-5 rounded-2xl font-bold text-xl hover:bg-wellness-pink transition-colors"
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
