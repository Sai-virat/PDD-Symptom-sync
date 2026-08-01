"use client";

import { motion } from "framer-motion";
import { TrendingUp, Award, CheckCircle2 } from "lucide-react";

export default function ProgressPage() {
  return (
    <div className="max-w-4xl mx-auto py-8">
      <motion.header initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="mb-10">
        <h1 className="text-4xl font-bold mb-4 flex items-center gap-3">
          <TrendingUp className="text-smoothPurple" size={36} />
          Your Health Progress
        </h1>
        <p className="text-wellness-white/60">Track your adherence, symptom improvements, and wellness trends</p>
      </motion.header>

      <div className="grid md:grid-cols-3 gap-6 mb-10">
        <div className="bg-wellness-card p-6 rounded-3xl border border-white/5">
          <Award className="text-yellow-500 mb-4" size={32} />
          <h3 className="text-wellness-white/60 text-sm font-bold uppercase">Streak</h3>
          <p className="text-3xl font-bold mt-1">7 Days Active</p>
        </div>

        <div className="bg-wellness-card p-6 rounded-3xl border border-white/5">
          <CheckCircle2 className="text-healthGreen mb-4" size={32} />
          <h3 className="text-wellness-white/60 text-sm font-bold uppercase">Diet Adherence</h3>
          <p className="text-3xl font-bold mt-1">92% Average</p>
        </div>

        <div className="bg-wellness-card p-6 rounded-3xl border border-white/5">
          <TrendingUp className="text-blue-400 mb-4" size={32} />
          <h3 className="text-wellness-white/60 text-sm font-bold uppercase">Symptom Reduction</h3>
          <p className="text-3xl font-bold mt-1">-35% Complaints</p>
        </div>
      </div>
    </div>
  );
}
