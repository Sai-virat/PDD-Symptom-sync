"use client";

import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { TrendingUp, Award, CheckCircle2, Clock, Zap, BarChart2, ShieldCheck } from "lucide-react";
import { getWeeklyReport, formatTimeSpent, DayReport } from "../activityTracker";

export default function ProgressPage() {
  const [weeklyData, setWeeklyData] = useState<DayReport[]>([]);
  const [activeTab, setActiveTab] = useState<"hours" | "adherence">("hours");
  const [todaySeconds, setTodaySeconds] = useState(0);

  useEffect(() => {
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

  const totalWeeklySeconds = weeklyData.reduce((acc, curr) => acc + curr.activeSeconds, 0);
  const activeDays = weeklyData.filter(d => d.activeSeconds > 0).length;
  const avgAdherence = weeklyData.length > 0 ? Math.round(weeklyData.reduce((acc, curr) => acc + curr.pct, 0) / weeklyData.length) : 0;
  const maxSecs = Math.max(...weeklyData.map(d => d.activeSeconds), 1800);

  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      <motion.header initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="mb-10">
        <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-purple-500/10 border border-purple-500/30 text-purple-300 text-sm font-bold mb-3">
          <Zap size={16} />
          <span>Automatic Real-Time Screen Time Tracker</span>
        </div>
        <h1 className="text-4xl font-extrabold mb-3 flex items-center gap-3">
          <TrendingUp className="text-smoothPurple" size={36} />
          Your Actual Health & Usage Progress
        </h1>
        <p className="text-wellness-white/60 text-lg">Real time statistics calculated automatically based on how much time you spend using SymptomSync.</p>
      </motion.header>

      {/* Top Key Metrics */}
      <div className="grid md:grid-cols-3 gap-6 mb-10">
        <div className="bg-wellness-card p-6 rounded-3xl border border-white/10 shadow-lg">
          <Award className="text-yellow-500 mb-3" size={32} />
          <h3 className="text-wellness-white/50 text-xs font-bold uppercase tracking-wider">Active Days Streak</h3>
          <p className="text-3xl font-black mt-1 text-white">{activeDays} Days Active</p>
        </div>

        <div className="bg-wellness-card p-6 rounded-3xl border border-white/10 shadow-lg">
          <Clock className="text-purple-400 mb-3" size={32} />
          <h3 className="text-wellness-white/50 text-xs font-bold uppercase tracking-wider">Time Spent Today</h3>
          <p className="text-3xl font-black mt-1 text-white">{formatTimeSpent(todaySeconds)}</p>
        </div>

        <div className="bg-wellness-card p-6 rounded-3xl border border-white/10 shadow-lg">
          <ShieldCheck className="text-healthGreen mb-3" size={32} />
          <h3 className="text-wellness-white/50 text-xs font-bold uppercase tracking-wider">Weekly Adherence Score</h3>
          <p className="text-3xl font-black mt-1 text-white">{avgAdherence}% Avg</p>
        </div>
      </div>

      {/* Detailed Real-Time Graph */}
      <div className="bg-wellness-card p-8 rounded-[36px] border border-white/10 mb-10 shadow-2xl">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
          <div>
            <h2 className="text-2xl font-bold text-white flex items-center gap-2">
              <BarChart2 className="text-smoothPurple" size={24} />
              <span>{activeTab === "hours" ? "Weekly Usage Breakdown (Hrs / Mins)" : "Weekly Adherence Score (%)"}</span>
            </h2>
            <p className="text-wellness-white/50 text-sm mt-1">Calculates actual screen time spent inside the app per day.</p>
          </div>

          <div className="flex bg-wellness-charcoal p-1.5 rounded-2xl border border-white/10 shrink-0">
            <button
              onClick={() => setActiveTab("hours")}
              className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                activeTab === "hours" ? "bg-purple-600 text-white" : "text-wellness-white/50 hover:text-white"
              }`}
            >
              Time Spent
            </button>
            <button
              onClick={() => setActiveTab("adherence")}
              className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                activeTab === "adherence" ? "bg-purple-600 text-white" : "text-wellness-white/50 hover:text-white"
              }`}
            >
              Adherence %
            </button>
          </div>
        </div>

        {/* Graph */}
        <div className="h-64 flex items-end justify-between gap-4 px-2 pt-6 mb-4">
          {weeklyData.map((item, idx) => {
            const heightPct = activeTab === "hours"
              ? (item.activeSeconds > 0 ? `${Math.min(100, Math.max(12, (item.activeSeconds / maxSecs) * 100))}%` : "4%")
              : (item.pct > 0 ? `${Math.max(10, item.pct)}%` : "4%");

            const valStr = activeTab === "hours" ? item.formattedTime : `${item.pct}%`;
            const hasActivity = item.activeSeconds > 0;

            return (
              <div key={idx} className="flex-1 flex flex-col items-center gap-3 h-full justify-end group">
                <span className={`text-xs font-bold ${hasActivity ? "text-purple-300" : "text-wellness-white/30"}`}>
                  {valStr}
                </span>

                <div className="w-full bg-white/5 rounded-2xl h-full flex items-end p-1 overflow-hidden relative border border-white/5">
                  <motion.div
                    initial={{ height: "0%" }}
                    animate={{ height: heightPct }}
                    transition={{ duration: 0.4 }}
                    className={`w-full rounded-xl transition-all ${
                      hasActivity
                        ? "bg-gradient-to-t from-purple-600 via-indigo-500 to-purple-400 shadow-lg shadow-purple-500/30"
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
    </div>
  );
}
