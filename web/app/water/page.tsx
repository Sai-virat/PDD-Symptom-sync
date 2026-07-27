"use client";

import { useState } from "react";
import { Plus, Bell, ChevronLeft } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import Link from "next/link";
import { clsx } from "clsx";

export default function WaterTrackerPage() {
  const [count, setCount] = useState(5);
  const goal = 8;
  const progress = Math.min(count / goal, 1);
  const isGoalAchieved = count >= goal;

  return (
    <div className="max-w-4xl mx-auto py-8">
      <header className="mb-12 flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Link href="/">
            <button className="p-3 rounded-2xl bg-white/5 hover:bg-white/10 transition-colors">
              <ChevronLeft size={24} />
            </button>
          </Link>
          <h1 className="text-4xl font-bold">Water Tracker</h1>
        </div>
        <button className="p-3 rounded-2xl bg-white/5 hover:bg-white/10 text-richOrange">
          <Bell size={24} />
        </button>
      </header>

      <div className="flex flex-col items-center">
        <div className="relative w-72 h-72 mb-12">
          {/* Droplet SVG with Fill Effect */}
          <svg viewBox="0 0 200 200" className="w-full h-full drop-shadow-2xl">
            <defs>
              <mask id="droplet-mask">
                <path
                  d="M100,0 C100,0 0,120 0,160 C0,200 200,200 200,160 C200,120 100,0 100,0 Z"
                  fill="white"
                />
              </mask>
            </defs>

            {/* Background (Empty Droplet) */}
            <path
              d="M100,0 C100,0 0,120 0,160 C0,200 200,200 200,160 C200,120 100,0 100,0 Z"
              className="fill-white/5"
            />

            {/* Fill Level */}
            <motion.rect
              initial={{ height: 0 }}
              animate={{ height: `${progress * 100}%` }}
              x="0"
              y="0"
              width="200"
              mask="url(#droplet-mask)"
              className="fill-blue-500/80"
              style={{ transformOrigin: "bottom" }}
              transform={`translate(0, ${200 - (progress * 200)})`}
            />

            <text
              x="50%"
              y="60%"
              textAnchor="middle"
              className="text-6xl font-black fill-white"
            >
              {count}
            </text>
          </svg>
        </div>

        <div className="text-center mb-16">
          <h2 className="text-3xl font-bold mb-2">{count} / {goal} Glasses</h2>
          <p className="text-wellness-white/40 font-medium text-lg">{(progress * 100).toFixed(0)}% of daily goal</p>
        </div>

        <AnimatePresence>
          {isGoalAchieved && (
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              className="mb-12 bg-healthGreen/10 border border-healthGreen px-8 py-3 rounded-full text-healthGreen font-bold"
            >
              Goal Achieved! 🎉
            </motion.div>
          )}
        </AnimatePresence>

        <button
          onClick={() => setCount(prev => prev + 1)}
          className="w-24 h-24 rounded-full bg-smoothPurple flex items-center justify-center shadow-2xl shadow-smoothPurple/40 hover:scale-110 active:scale-95 transition-all"
        >
          <Plus size={40} className="text-white" />
        </button>
      </div>

      <div className="mt-24 bg-wellness-card p-8 rounded-3xl border border-white/5 max-w-xl mx-auto">
        <div className="flex items-center justify-between mb-8">
          <div className="flex items-center gap-4">
            <div className="p-3 bg-richOrange/10 rounded-2xl text-richOrange">
              <Bell size={24} />
            </div>
            <h3 className="text-xl font-bold">Smart Reminders</h3>
          </div>
          <div className="w-14 h-8 bg-richOrange rounded-full relative p-1 cursor-pointer">
            <div className="w-6 h-6 bg-white rounded-full absolute right-1 shadow-md" />
          </div>
        </div>

        <div className="flex gap-4">
          {["1 Hour", "2 Hours", "3 Hours"].map(time => (
            <button
              key={time}
              className={clsx(
                "flex-1 py-3 rounded-2xl font-bold border transition-all",
                time === "2 Hours"
                  ? "bg-smoothPurple border-smoothPurple text-white"
                  : "bg-wellness-charcoal border-white/5 text-wellness-white/40 hover:border-white/20"
              )}
            >
              {time}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
