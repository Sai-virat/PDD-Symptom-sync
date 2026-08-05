"use client";

import { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { History, Calendar, Activity, Droplets, Utensils, Trash2, Sparkles, AlertCircle } from "lucide-react";
import { loadHistoryLogs, clearAllHistory, RealtimeHistoryEntry } from "../historyTracker";
import { getApiBase } from "../api";

const DEFAULT_FALLBACK_HISTORY: RealtimeHistoryEntry[] = [
  {
    id: "default_1",
    timestamp: "Today at 9:30 AM",
    rawTime: Date.now() - 1000 * 60 * 60 * 3,
    type: "analysis",
    title: "Migraine & Acidity Analysis",
    symptoms: ["Migraine", "Acidity"],
    severity: "High",
    cause: "Tyramine Foods & Gastric Acid Spikes",
    details: "Generated Full-Day Anti-Inflammatory Diet Plan (Ginger Oats, Quinoa Bowl, Salmon)"
  },
  {
    id: "default_2",
    timestamp: "Yesterday at 2:15 PM",
    rawTime: Date.now() - 1000 * 60 * 60 * 24,
    type: "water",
    title: "Daily Water Goal Achieved",
    symptoms: ["Hydration"],
    severity: "Low",
    cause: "8/8 Glasses Goal Completed",
    details: "2500ml total water intake logged. Sound & SMS alert dispatched."
  },
  {
    id: "default_3",
    timestamp: "Aug 2, 2026 at 7:00 PM",
    rawTime: Date.now() - 1000 * 60 * 60 * 48,
    type: "diet",
    title: "Full-Day Diet Schedule Created",
    symptoms: ["Bloating"],
    severity: "Medium",
    cause: "Digestive Sensitivity & FODMAP Irritation",
    details: "Assigned Probiotic Yogurt, Zucchini Chicken, Papaya Enzymes"
  }
];

export default function HistoryPage() {
  const [historyList, setHistoryList] = useState<RealtimeHistoryEntry[]>([]);
  const [activeTab, setActiveTab] = useState<"all" | "analysis" | "water_diet">("all");

  useEffect(() => {
    loadCombinedHistory();
  }, []);

  const loadCombinedHistory = async () => {
    const isCleared = localStorage.getItem("symptomsync_history_cleared") === "true";
    const localLogs = loadHistoryLogs();
    
    try {
      const apiBase = getApiBase();
      const res = await fetch(`${apiBase}/history`);
      if (res.ok) {
        const remoteData = await res.json();
        if (Array.isArray(remoteData) && remoteData.length > 0) {
          const formattedRemote: RealtimeHistoryEntry[] = remoteData.map((item: any) => ({
            id: `remote_${item.id || Math.random()}`,
            timestamp: item.date || "Recorded",
            rawTime: Date.now() - (item.id * 3600000),
            type: "analysis",
            title: `${item.symptom || "Symptom"} Log`,
            symptoms: item.symptom ? [item.symptom] : ["General"],
            severity: (item.severity as any) || "Medium",
            cause: item.cause || "Dietary factors"
          }));

          const combined = [...localLogs];
          formattedRemote.forEach(remote => {
            if (!combined.some(c => c.title === remote.title && c.timestamp === remote.timestamp)) {
              combined.push(remote);
            }
          });
          
          setHistoryList(combined);
          return;
        }
      }
    } catch (e) {
      console.warn("Backend history API unavailable", e);
    }

    if (isCleared && localLogs.length === 0) {
      setHistoryList([]);
    } else {
      setHistoryList(localLogs.length > 0 ? localLogs : DEFAULT_FALLBACK_HISTORY);
    }
  };

  const handleClearHistory = async () => {
    if (confirm("Are you sure you want to clear your full symptom & diet history?")) {
      clearAllHistory();
      localStorage.setItem("symptomsync_history_cleared", "true");
      setHistoryList([]);

      try {
        const apiBase = getApiBase();
        await fetch(`${apiBase}/history/clear`, { method: "POST" });
        await fetch(`${apiBase}/history/clear`, { method: "DELETE" });
      } catch (e) {}
    }
  };

  const filteredLogs = historyList.filter(item => {
    if (activeTab === "analysis") return item.type === "analysis";
    if (activeTab === "water_diet") return item.type === "water" || item.type === "diet";
    return true;
  });

  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      {/* Header */}
      <motion.header initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="mb-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div>
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-richOrange/10 border border-richOrange/30 text-richOrange font-bold text-xs mb-3">
            <Sparkles size={14} />
            <span>Real-time Activity Timeline</span>
          </div>
          <h1 className="text-4xl font-extrabold mb-2 flex items-center gap-3">
            <History className="text-richOrange" size={36} />
            Real-Time Symptom & Diet History
          </h1>
          <p className="text-wellness-white/60 text-base">Comprehensive audit trail of your symptom analyses, AI diet plans, and water tracking.</p>
        </div>

        {historyList.length > 0 && (
          <button
            onClick={handleClearHistory}
            className="p-3.5 rounded-2xl bg-red-500/10 border border-red-500/30 text-red-400 font-bold hover:bg-red-500 hover:text-white transition-all flex items-center gap-2 text-xs shrink-0 self-start md:self-auto"
          >
            <Trash2 size={16} />
            <span>Clear History</span>
          </button>
        )}
      </motion.header>

      {/* Filter Tabs */}
      <div className="flex bg-wellness-charcoal p-1.5 rounded-2xl border border-white/10 mb-8 max-w-md">
        <button
          onClick={() => setActiveTab("all")}
          className={`flex-1 py-2.5 rounded-xl text-xs font-bold transition-all ${
            activeTab === "all" ? "bg-richOrange text-white shadow-lg shadow-orange-600/30" : "text-wellness-white/50 hover:text-white"
          }`}
        >
          All Logs ({historyList.length})
        </button>
        <button
          onClick={() => setActiveTab("analysis")}
          className={`flex-1 py-2.5 rounded-xl text-xs font-bold transition-all ${
            activeTab === "analysis" ? "bg-richOrange text-white shadow-lg shadow-orange-600/30" : "text-wellness-white/50 hover:text-white"
          }`}
        >
          Analyses ({historyList.filter(i => i.type === "analysis").length})
        </button>
        <button
          onClick={() => setActiveTab("water_diet")}
          className={`flex-1 py-2.5 rounded-xl text-xs font-bold transition-all ${
            activeTab === "water_diet" ? "bg-richOrange text-white shadow-lg shadow-orange-600/30" : "text-wellness-white/50 hover:text-white"
          }`}
        >
          Water & Diet ({historyList.filter(i => i.type === "water" || i.type === "diet").length})
        </button>
      </div>

      {/* History List */}
      <div className="space-y-4 mb-16">
        <AnimatePresence>
          {filteredLogs.length === 0 ? (
            <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="bg-wellness-card p-12 rounded-[32px] border border-white/10 text-center">
              <History size={48} className="text-wellness-white/20 mx-auto mb-4" />
              <h3 className="text-2xl font-bold text-white mb-2">No History Logs Found</h3>
              <p className="text-wellness-white/50 text-sm">Perform a symptom analysis or log water intake to generate real-time history records.</p>
            </motion.div>
          ) : (
            filteredLogs.map((item, idx) => {
              const Icon = item.type === "water" ? Droplets : item.type === "diet" ? Utensils : Activity;
              const iconColor = item.type === "water" ? "text-blue-400 bg-blue-500/10 border-blue-500/20" : item.type === "diet" ? "text-emerald-400 bg-emerald-500/10 border-emerald-500/20" : "text-purple-400 bg-purple-500/10 border-purple-500/20";

              return (
                <motion.div
                  key={item.id}
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: idx * 0.05 }}
                  className="bg-wellness-card p-6 md:p-8 rounded-[32px] border border-white/10 flex flex-col md:flex-row md:items-center justify-between gap-6 shadow-xl hover:border-white/20 transition-all"
                >
                  <div className="flex items-start gap-5">
                    <div className={`p-4 rounded-2xl border ${iconColor} shrink-0 mt-1 md:mt-0`}>
                      <Icon size={26} />
                    </div>
                    <div>
                      <div className="flex items-center gap-2 text-wellness-white/50 text-xs font-semibold mb-1">
                        <Calendar size={14} />
                        <span>{item.timestamp}</span>
                      </div>
                      <h3 className="text-2xl font-bold text-white mb-1.5">{item.title}</h3>
                      <p className="text-wellness-white/70 text-sm font-medium leading-relaxed">
                        <strong>Likely Cause / Target:</strong> {item.cause}
                      </p>
                      {item.details && (
                        <p className="text-wellness-white/50 text-xs mt-1 italic">{item.details}</p>
                      )}

                      {/* Symptom Tags */}
                      {item.symptoms && item.symptoms.length > 0 && (
                        <div className="flex flex-wrap gap-2 mt-3">
                          {item.symptoms.map(s => (
                            <span key={s} className="px-3 py-1 rounded-xl bg-white/5 border border-white/10 text-xs font-bold text-wellness-white/80">
                              {s}
                            </span>
                          ))}
                        </div>
                      )}
                    </div>
                  </div>

                  <div className="flex items-center justify-between md:justify-end gap-4 border-t md:border-t-0 border-white/5 pt-4 md:pt-0">
                    <span className={`px-4 py-2 rounded-2xl text-xs font-extrabold tracking-wide ${
                      item.severity === "High" ? "bg-red-500/20 text-red-400 border border-red-500/30" :
                      item.severity === "Medium" ? "bg-yellow-500/20 text-yellow-400 border border-yellow-500/30" :
                      "bg-emerald-500/20 text-emerald-400 border border-emerald-500/30"
                    }`}>
                      {item.severity} Intensity
                    </span>
                  </div>
                </motion.div>
              );
            })
          )}
        </AnimatePresence>
      </div>
    </div>
  );
}
