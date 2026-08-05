"use client";

import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { Plus, Droplets, Utensils, TrendingUp, History, Activity, Clock, ShieldCheck, User, MessageSquareHeart } from "lucide-react";
import WidgetCard from "@/components/WidgetCard";
import Link from "next/link";
import { getApiBase } from "./api";
import { getWeeklyReport, formatTimeSpent, DayReport } from "./activityTracker";

export default function Dashboard() {
  const [userName, setUserName] = useState("User");
  const [symptomCount, setSymptomCount] = useState(11);
  const [backendOnline, setBackendOnline] = useState(false);
  const [viewMode, setViewMode] = useState<"hours" | "adherence">("hours");
  
  // Real-time activity state
  const [weeklyData, setWeeklyData] = useState<DayReport[]>([]);
  const [todaySeconds, setTodaySeconds] = useState(0);

  const [isAuthChecked, setIsAuthChecked] = useState(false);

  useEffect(() => {
    // ALWAYS open Login Page first on app launch
    const isFreshLogin = sessionStorage.getItem("symptomsync_session_active");
    if (!isFreshLogin) {
      window.location.href = "/login";
      return;
    }

    setIsAuthChecked(true);

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

    // Refresh graph every 1 second live as user spends time on the app
    const interval = setInterval(() => {
      const report = getWeeklyReport();
      setWeeklyData(report);

      const todayKey = new Date().toISOString().split("T")[0];
      const todayRecord = report.find(r => r.dateStr === todayKey);
      if (todayRecord) {
        setTodaySeconds(todayRecord.activeSeconds);
      }
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  // Summary calculations based on actual real data
  const totalWeeklySeconds = weeklyData.reduce((acc, curr) => acc + curr.activeSeconds, 0);
  const avgAdherence = weeklyData.length > 0 ? Math.round(weeklyData.reduce((acc, curr) => acc + curr.pct, 0) / weeklyData.length) : 0;
  
  // Max scale calculation
  const maxSecs = Math.max(...weeklyData.map(d => d.activeSeconds), 1800); // at least 30 mins scale

  if (!isAuthChecked) {
    return (
      <div className="min-h-screen bg-slate-950 flex flex-col items-center justify-center text-white">
        <div className="w-10 h-10 border-4 border-richOrange border-t-transparent rounded-full animate-spin mb-3" />
        <p className="font-bold text-xs text-wellness-white/80">Opening SymptomSync...</p>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto py-8 px-4">
      {/* Header */}
      <header className="mb-10 flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-white/5 pb-8">
        <div>
          <div className="flex flex-wrap items-center gap-3 mb-2">
            <h1 className="text-3xl md:text-4xl font-extrabold tracking-tight">
              Welcome Back, <span className="text-gradient-orange">{userName}!</span>
            </h1>
            {backendOnline && (
              <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
                Backend Live
              </span>
            )}
          </div>
          <p className="text-wellness-white/70 text-sm md:text-base flex items-center gap-2 font-medium">
            <Clock size={18} className="text-purple-400" />
            <span>Actual Time Spent Today: <strong className="text-white bg-purple-500/20 px-2.5 py-0.5 rounded-lg border border-purple-500/30">{formatTimeSpent(todaySeconds)}</strong></span>
          </p>
        </div>

        <Link href="/analyze" className="shrink-0">
          <button className="bg-gradient-to-r from-richOrange to-amber-600 hover:from-orange-600 hover:to-amber-700 text-white px-6 py-3.5 rounded-2xl font-bold flex items-center gap-2.5 active:scale-95 text-base shadow-xl shadow-orange-600/20 transition-all cursor-pointer whitespace-nowrap">
            <Plus size={20} />
            <span>Analyze Symptoms</span>
          </button>
        </Link>
      </header>

      {/* Widget Cards */}
      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4 md:gap-6">
        <WidgetCard title="Water Tracker" icon={Droplets} href="/water" color="text-blue-400" />
        <WidgetCard title="Diet Plan" icon={Utensils} href="/diet" color="text-emerald-400" />
        <WidgetCard title="Progress" icon={TrendingUp} href="/progress" color="text-purple-400" />
        <WidgetCard title="History" icon={History} href="/history" color="text-amber-400" />
        <WidgetCard title="Settings" icon={User} href="/settings" color="text-cyan-400" />
        <WidgetCard title="Feedback" icon={MessageSquareHeart} href="/feedback" color="text-rose-400" />
      </div>

      {/* Real-time Usage Chart Section */}
      <section className="mt-10 grid md:grid-cols-3 gap-8">
        <div className="md:col-span-2 glass-panel rounded-[32px] p-8 flex flex-col justify-between border border-white/10 shadow-2xl">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
            <div>
              <div className="flex items-center gap-2 mb-1">
                <h2 className="text-2xl font-bold text-gradient-purple">
                  {viewMode === "hours" ? "Real-Time App Usage" : "Weekly Adherence Score"}
                </h2>
                <span className="text-xs px-2.5 py-0.5 rounded-full font-bold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 flex items-center gap-1">
                  <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-ping" />
                  LIVE AUTOMATIC TRACKER
                </span>
              </div>
              <p className="text-wellness-white/50 text-sm">
                {viewMode === "hours" 
                  ? "Calculates exact hours and minutes spent inside SymptomSync per day" 
                  : "Consistency score calculated directly from actual screen time & water logs"}
              </p>
            </div>

            {/* Toggle View Mode */}
            <div className="flex items-center bg-wellness-charcoal p-1.5 rounded-2xl border border-white/10 shrink-0">
              <button
                onClick={() => setViewMode("hours")}
                className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                  viewMode === "hours" 
                    ? "bg-purple-600 text-white shadow-md shadow-purple-600/30" 
                    : "text-wellness-white/50 hover:text-white"
                }`}
              >
                Time Spent (Hrs/Mins)
              </button>
              <button
                onClick={() => setViewMode("adherence")}
                className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                  viewMode === "adherence" 
                    ? "bg-purple-600 text-white shadow-md shadow-purple-600/30" 
                    : "text-wellness-white/50 hover:text-white"
                }`}
              >
                Adherence Score (%)
              </button>
            </div>
          </div>

          {/* Bar Chart Container */}
          <div className="h-64 flex items-end justify-between gap-3 md:gap-6 px-2 pt-6 mb-2">
            {weeklyData.map((item, i) => {
              const heightPct = viewMode === "hours" 
                ? (item.activeSeconds > 0 ? `${Math.min(100, Math.max(12, (item.activeSeconds / maxSecs) * 100))}%` : "4%") 
                : (item.pct > 0 ? `${Math.max(10, item.pct)}%` : "4%");

              const displayLabel = viewMode === "hours" 
                ? item.formattedTime 
                : `${item.pct}%`;

              const hasActivity = item.activeSeconds > 0;

              return (
                <div key={i} className="flex-1 flex flex-col items-center gap-3 h-full justify-end group">
                  <span className={`text-xs font-bold transition-colors ${hasActivity ? "text-purple-300" : "text-wellness-white/30"}`}>
                    {displayLabel}
                  </span>

                  <div className="w-full bg-white/5 rounded-2xl h-full flex items-end p-1 overflow-hidden relative border border-white/5">
                    <motion.div
                      initial={{ height: "0%" }}
                      animate={{ height: heightPct }}
                      transition={{ duration: 0.4 }}
                      className={`w-full rounded-xl transition-all shadow-lg ${
                        hasActivity
                          ? "bg-gradient-to-t from-purple-600 via-indigo-500 to-purple-400 group-hover:from-orange-500 group-hover:to-amber-400 shadow-purple-500/30"
                          : "bg-white/5"
                      }`}
                    />
                  </div>

                  <span className={`text-xs font-bold uppercase ${hasActivity ? "text-wellness-white/80" : "text-wellness-white/40"}`}>
                    {item.day}
                  </span>
                </div>
              );
            })}
          </div>
        </div>

        {/* Real Stats Overview Sidebar */}
        <div className="space-y-6">
          <div className="glass-panel glass-card-hover p-6 rounded-3xl flex items-center gap-5 border border-white/10 shadow-lg">
            <div className="bg-purple-500/20 p-4 rounded-2xl text-purple-400 border border-purple-500/30">
              <Clock size={28} />
            </div>
            <div>
              <p className="text-wellness-white/40 text-xs font-bold uppercase tracking-wider">Total Time This Week</p>
              <p className="text-2xl font-black text-white">{formatTimeSpent(totalWeeklySeconds)}</p>
            </div>
          </div>

          <div className="glass-panel glass-card-hover p-6 rounded-3xl flex items-center gap-5 border border-white/10 shadow-lg">
            <div className="bg-amber-500/20 p-4 rounded-2xl text-amber-400 border border-amber-500/30">
              <Activity size={28} />
            </div>
            <div>
              <p className="text-wellness-white/40 text-xs font-bold uppercase tracking-wider">Symptoms Database</p>
              <p className="text-2xl font-black text-white">{symptomCount} Active Types</p>
            </div>
          </div>

          <div className="glass-panel glass-card-hover p-6 rounded-3xl flex items-center gap-5 border border-white/10 shadow-lg">
            <div className="bg-emerald-500/20 p-4 rounded-2xl text-emerald-400 border border-emerald-500/30">
              <ShieldCheck size={28} />
            </div>
            <div>
              <p className="text-wellness-white/40 text-xs font-bold uppercase tracking-wider">Real Adherence Avg</p>
              <p className="text-2xl font-black text-white">{avgAdherence}%</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
