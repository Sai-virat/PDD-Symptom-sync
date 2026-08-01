"use client";

import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { Plus, Droplets, Utensils, TrendingUp, History, Activity } from "lucide-react";
import WidgetCard from "@/components/WidgetCard";
import Link from "next/link";
import { getApiBase } from "./api";

export default function Dashboard() {
  const [userName, setUserName] = useState("John");
  const [symptomCount, setSymptomCount] = useState(24);
  const [backendOnline, setBackendOnline] = useState(false);

  useEffect(() => {
    try {
      const storedUser = localStorage.getItem("symptomsync_user");
      if (storedUser) {
        const u = JSON.parse(storedUser);
        if (u.name) setUserName(u.name);
      }
    } catch (e) {}

    const apiBase = getApiBase();
    fetch(`${apiBase}/symptoms`)
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) {
          setSymptomCount(data.length);
          setBackendOnline(true);
        }
      })
      .catch(() => setBackendOnline(false));
  }, []);

  const weeklyData = [
    { day: "Mon", val: 80, pct: "80%" },
    { day: "Tue", val: 65, pct: "65%" },
    { day: "Wed", val: 95, pct: "95%" },
    { day: "Thu", val: 75, pct: "75%" },
    { day: "Fri", val: 88, pct: "88%" },
    { day: "Sat", val: 60, pct: "60%" },
    { day: "Sun", val: 50, pct: "50%" },
  ];

  return (
    <div className="max-w-6xl mx-auto py-8">
      <header className="mb-12 flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div>
          <div className="flex items-center gap-3 mb-2">
            <h1 className="text-4xl md:text-5xl font-extrabold tracking-tight">
              Good Morning, <span className="text-gradient-orange">{userName}!</span>
            </h1>
            {backendOnline && (
              <span className="flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
                Backend Integrated
              </span>
            )}
          </div>
          <p className="text-wellness-white/60 text-lg">Your wellness overview for today</p>
        </div>

        <Link href="/analyze">
          <button className="btn-glow-orange text-white px-8 py-4 rounded-2xl font-bold flex items-center gap-3 active:scale-95 text-lg shadow-xl shadow-orange-600/30">
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

      <section className="mt-12 grid md:grid-cols-3 gap-8">
        <div className="md:col-span-2 glass-panel rounded-[32px] p-8 flex flex-col justify-between">
          <div className="flex items-center justify-between mb-8">
            <div>
              <h2 className="text-2xl font-bold text-gradient-purple">Weekly Adherence</h2>
              <p className="text-wellness-white/40 text-sm">Consistency score based on your logged diets & water goals</p>
            </div>
            <span className="px-4 py-2 bg-purple-500/20 text-purple-300 rounded-xl text-xs font-bold border border-purple-500/30">
              Avg 73%
            </span>
          </div>

          <div className="h-64 flex items-end justify-between gap-3 md:gap-6 px-2 pt-6">
            {weeklyData.map((item, i) => (
              <div key={i} className="flex-1 flex flex-col items-center gap-3 h-full justify-end group">
                <span className="text-xs text-purple-300 font-bold opacity-0 group-hover:opacity-100 transition-opacity">
                  {item.pct}
                </span>
                <div className="w-full bg-white/5 rounded-2xl h-full flex items-end p-1 overflow-hidden relative border border-white/5">
                  <motion.div
                    initial={{ height: "0%" }}
                    animate={{ height: item.pct }}
                    transition={{ duration: 0.8, delay: i * 0.1 }}
                    className="w-full bg-gradient-to-t from-purple-600 via-indigo-500 to-purple-400 rounded-xl group-hover:from-orange-500 group-hover:to-amber-400 transition-all shadow-lg shadow-purple-500/30"
                  />
                </div>
                <span className="text-xs text-wellness-white/60 font-bold uppercase">
                  {item.day}
                </span>
              </div>
            ))}
          </div>
        </div>

        <div className="space-y-6">
          <div className="glass-panel glass-card-hover p-6 rounded-3xl flex items-center gap-5 border border-white/10">
            <div className="bg-amber-500/20 p-4 rounded-2xl text-amber-400 border border-amber-500/30">
              <Activity size={28} />
            </div>
            <div>
              <p className="text-wellness-white/40 text-xs font-bold uppercase tracking-wider">Symptoms Logged</p>
              <p className="text-3xl font-black text-white">{symptomCount} Types</p>
            </div>
          </div>

          <div className="glass-panel glass-card-hover p-6 rounded-3xl flex items-center gap-5 border border-white/10">
            <div className="bg-blue-500/20 p-4 rounded-2xl text-blue-400 border border-blue-500/30">
              <Droplets size={28} />
            </div>
            <div>
              <p className="text-wellness-white/40 text-xs font-bold uppercase tracking-wider">Water Goal</p>
              <p className="text-3xl font-black text-white">85% Met</p>
            </div>
          </div>

          <div className="glass-panel glass-card-hover p-6 rounded-3xl flex items-center gap-5 border border-white/10">
            <div className="bg-emerald-500/20 p-4 rounded-2xl text-emerald-400 border border-emerald-500/30">
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
