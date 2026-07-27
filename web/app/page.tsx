"use client";

import { motion } from "framer-motion";
import { Plus, Droplets, Utensils, TrendingUp, History } from "lucide-react";
import WidgetCard from "@/components/WidgetCard";
import Link from "next/link";

export default function Dashboard() {
  return (
    <div className="max-w-6xl mx-auto py-8">
      <header className="mb-12 flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div>
          <h1 className="text-4xl md:text-5xl font-extrabold mb-2 tracking-tight">
            Good Morning, <span className="text-gradient-orange">John!</span>
          </h1>
          <p className="text-wellness-white/60 text-lg">Your wellness overview for today</p>
        </div>

        <Link href="/analyze">
          <button className="btn-glow-orange text-white px-8 py-4 rounded-2xl font-bold flex items-center gap-3 active:scale-95 text-lg">
            <Plus size={24} />
            <span>+ Add Symptom</span>
          </button>
        </Link>
      </header>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
        <WidgetCard title="Water Tracker" icon={Droplets} href="/water" color="text-blue-400" />
        <WidgetCard title="Diet Plan" icon={Utensils} href="/diet" color="text-emerald-400" />
        <WidgetCard title="Progress" icon={TrendingUp} href="/progress" color="text-purple-400" />
        <WidgetCard title="History" icon={History} href="/history" color="text-amber-400" />
      </div>

      <section className="mt-16 grid md:grid-cols-3 gap-8">
        <div className="md:col-span-2 glass-panel rounded-[32px] p-8">
          <h2 className="text-2xl font-bold mb-6 text-gradient-purple">Weekly Adherence</h2>
          <div className="h-64 flex items-end justify-between gap-4 px-4">
            {[0.8, 0.6, 0.9, 0.7, 0.85, 0.5, 0.4].map((val, i) => (
              <div key={i} className="flex-1 flex flex-col items-center gap-4 group">
                <motion.div
                  initial={{ height: 0 }}
                  animate={{ height: `${val * 100}%` }}
                  className="w-full bg-gradient-to-t from-purple-600 to-indigo-400 rounded-t-xl group-hover:from-orange-500 group-hover:to-amber-400 transition-colors shadow-lg shadow-purple-500/20"
                />
                <span className="text-xs text-wellness-white/50 uppercase font-bold">
                  {['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'][i]}
                </span>
              </div>
            ))}
          </div>
        </div>

        <div className="space-y-6">
          <div className="glass-panel glass-card-hover p-6 rounded-3xl flex items-center gap-5">
            <div className="bg-amber-500/20 p-4 rounded-2xl text-amber-400">
              <Plus size={28} />
            </div>
            <div>
              <p className="text-wellness-white/40 text-xs font-bold uppercase tracking-wider">Symptoms</p>
              <p className="text-3xl font-black text-white">24 Total</p>
            </div>
          </div>

          <div className="glass-panel glass-card-hover p-6 rounded-3xl flex items-center gap-5">
            <div className="bg-blue-500/20 p-4 rounded-2xl text-blue-400">
              <Droplets size={28} />
            </div>
            <div>
              <p className="text-wellness-white/40 text-xs font-bold uppercase tracking-wider">Water Goal</p>
              <p className="text-3xl font-black text-white">85% Met</p>
            </div>
          </div>

          <div className="glass-panel glass-card-hover p-6 rounded-3xl flex items-center gap-5">
            <div className="bg-emerald-500/20 p-4 rounded-2xl text-emerald-400">
              <Utensils size={28} />
            </div>
            <div>
              <p className="text-wellness-white/40 text-xs font-bold uppercase tracking-wider">Diet Accuracy</p>
              <p className="text-3xl font-black text-white">92%</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
