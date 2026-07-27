"use client";

import { motion } from "framer-motion";
import { History, Calendar, AlertCircle } from "lucide-react";

const MOCK_HISTORY = [
  { id: 1, date: "Today, 9:30 AM", symptom: "Migraine", severity: "High", cause: "Tyramine Foods" },
  { id: 2, date: "Yesterday, 2:15 PM", symptom: "Bloating", severity: "Medium", cause: "Digestive Sensitivity" },
  { id: 3, date: "Jul 22, 2026", symptom: "Acidity", severity: "Low", cause: "Late Dinner" },
];

export default function HistoryPage() {
  return (
    <div className="max-w-4xl mx-auto py-8">
      <motion.header initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="mb-10">
        <h1 className="text-4xl font-bold mb-4 flex items-center gap-3">
          <History className="text-richOrange" size={36} />
          Symptom History
        </h1>
        <p className="text-wellness-white/60">Review your previously logged symptoms and historical logs</p>
      </motion.header>

      <div className="space-y-4">
        {MOCK_HISTORY.map((item, idx) => (
          <motion.div
            key={item.id}
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: idx * 0.1 }}
            className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex flex-col md:flex-row md:items-center justify-between gap-4"
          >
            <div>
              <div className="flex items-center gap-2 text-wellness-white/40 text-sm mb-1">
                <Calendar size={16} />
                <span>{item.date}</span>
              </div>
              <h3 className="text-2xl font-bold">{item.symptom}</h3>
              <p className="text-wellness-white/60 text-sm mt-1">Likely Cause: {item.cause}</p>
            </div>

            <div className="flex items-center gap-3">
              <span className={`px-4 py-2 rounded-xl text-sm font-bold ${
                item.severity === "High" ? "bg-red-500/20 text-red-400 border border-red-500/30" :
                item.severity === "Medium" ? "bg-yellow-500/20 text-yellow-400 border border-yellow-500/30" :
                "bg-green-500/20 text-green-400 border border-green-500/30"
              }`}>
                {item.severity} Intensity
              </span>
            </div>
          </motion.div>
        ))}
      </div>
    </div>
  );
}
