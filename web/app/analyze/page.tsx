"use client";

import { useState } from "react";
import { Search, ChevronRight, AlertCircle, X } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import { clsx } from "clsx";
import Link from "next/link";

const ALL_SYMPTOMS = [
  "Migraine", "Bloating", "Joint Pain", "Anxiety", "Insomnia",
  "Fatigue", "Acidity", "Cough", "Nausea", "Back Pain"
];

export default function AnalyzePage() {
  const [step, setStep] = useState(1);
  const [query, setQuery] = useState("");
  const [selected, setSelected] = useState<string[]>([]);
  const [results, setResults] = useState<{name: string, severity: string}[]>([]);
  const [causes, setCauses] = useState<{title: string, description: string}[]>([]);

  const filtered = ALL_SYMPTOMS.filter(s => s.toLowerCase().includes(query.toLowerCase()));

  const toggleSymptom = (s: string) => {
    setSelected(prev => prev.includes(s) ? prev.filter(item => item !== s) : [...prev, s]);
  };

  const handleContinue = () => {
    setResults(selected.map(s => ({ name: s, severity: "Medium" })));
    setStep(2);
  };

  const showCauses = () => {
    // Mocking causes based on selection
    setCauses([
      { title: "Trigger Foods", description: "Tyramine-rich foods like aged cheese or processed meats." },
      { title: "Digestive Sensitivity", description: "Certain fibers or sugars can cause excessive gas." }
    ]);
    setStep(3);
  };


  const updateSeverity = (name: string, sev: string) => {
    setResults(prev => prev.map(r => r.name === name ? { ...r, severity: sev } : r));
  };

  return (
    <div className="max-w-4xl mx-auto py-8">
      {step === 1 ? (
        <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }}>
          <header className="mb-10">
            <h1 className="text-4xl font-bold mb-4">Add Symptoms</h1>
            <p className="text-wellness-white/60">Select or search for the symptoms you&apos;re experiencing</p>
          </header>

          <div className="relative mb-12">
            <Search className="absolute left-6 top-1/2 -translate-y-1/2 text-wellness-white/30" size={24} />
            <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="Search symptoms..."
              className="w-full bg-wellness-card border border-white/5 rounded-3xl py-6 pl-16 pr-6 text-xl focus:outline-none focus:border-smoothPurple transition-colors"
            />
          </div>

          <div className="flex flex-wrap gap-3 mb-24">
            {filtered.map(s => {
              const isActive = selected.includes(s);
              return (
                <button
                  key={s}
                  onClick={() => toggleSymptom(s)}
                  className={clsx(
                    "px-6 py-3 rounded-2xl font-medium transition-all active:scale-95 border",
                    isActive
                      ? "bg-smoothPurple border-smoothPurple text-white"
                      : "bg-wellness-card border-white/5 text-wellness-white/60 hover:border-white/20"
                  )}
                >
                  {s}
                </button>
              );
            })}
          </div>

          <div className="fixed bottom-12 left-0 right-0 md:left-64 px-8 pointer-events-none">
            <div className="max-w-4xl mx-auto flex justify-end">
              <button
                disabled={selected.length === 0}
                onClick={handleContinue}
                className="pointer-events-auto bg-smoothPurple hover:bg-indigo-600 disabled:opacity-50 disabled:cursor-not-allowed text-white px-10 py-5 rounded-2xl font-bold flex items-center gap-3 shadow-xl shadow-smoothPurple/20 transition-all active:scale-95"
              >
                <span>Continue</span>
                <ChevronRight size={24} />
              </button>
            </div>
          </div>
        </motion.div>
      ) : step === 2 ? (
        <motion.div initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }}>
          <header className="mb-10 flex items-center justify-between">
            <div>
              <h1 className="text-4xl font-bold mb-4">Analysis Results</h1>
              <p className="text-wellness-white/60">Adjust severity for each detected symptom</p>
            </div>
            <button
              onClick={() => setStep(1)}
              className="p-3 rounded-2xl bg-white/5 hover:bg-white/10 transition-colors"
            >
              <X size={24} />
            </button>
          </header>

          <div className="space-y-4 mb-24">
            {results.map(res => (
              <div key={res.name} className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div>
                  <h3 className="text-2xl font-bold mb-1">{res.name}</h3>
                  <div className="flex items-center gap-2 text-wellness-white/40">
                    <AlertCircle size={16} />
                    <span className="text-sm font-medium">Intensity: {res.severity}</span>
                  </div>
                </div>

                <div className="flex bg-wellness-charcoal p-1.5 rounded-2xl border border-white/5">
                  {["Low", "Medium", "High"].map(level => (
                    <button
                      key={level}
                      onClick={() => updateSeverity(res.name, level)}
                      className={clsx(
                        "px-6 py-2.5 rounded-xl text-sm font-bold transition-all",
                        res.severity === level
                          ? (level === "Low" ? "bg-healthGreen text-white" : level === "Medium" ? "bg-yellow-500 text-black" : "bg-red-500 text-white")
                          : "text-wellness-white/40 hover:text-white"
                      )}
                    >
                      {level}
                    </button>
                  ))}
                </div>
              </div>
            ))}
          </div>

          <div className="fixed bottom-12 left-0 right-0 md:left-64 px-8 pointer-events-none">
            <div className="max-w-4xl mx-auto flex flex-col md:flex-row justify-end gap-4">
              <button
                onClick={showCauses}
                className="pointer-events-auto bg-wellness-card border border-white/10 hover:border-white/30 text-white px-10 py-5 rounded-2xl font-bold transition-all active:scale-95"
              >
                View Possible Causes
              </button>
              <Link href="/diet" className="pointer-events-auto">
                <button className="w-full bg-healthGreen hover:bg-green-700 text-white px-10 py-5 rounded-2xl font-bold shadow-xl shadow-green-900/20 transition-all active:scale-95">
                  Generate Diet Plan
                </button>
              </Link>
            </div>
          </div>
        </motion.div>
      ) : step === 3 ? (
        <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }}>
          <header className="mb-10 flex items-center justify-between">
            <div>
              <h1 className="text-4xl font-bold mb-4">Possible Causes</h1>
              <p className="text-wellness-white/60">Understanding the roots of your symptoms</p>
            </div>
            <button
              onClick={() => setStep(2)}
              className="p-3 rounded-2xl bg-white/5 hover:bg-white/10 transition-colors"
            >
              <ChevronRight className="rotate-180" size={24} />
            </button>
          </header>

          <div className="grid gap-6 mb-24">
            {causes.map((cause, i) => (
              <div key={i} className="bg-wellness-card p-8 rounded-[32px] border border-white/5">
                <h3 className="text-2xl font-bold text-smoothPurple mb-4">{cause.title}</h3>
                <p className="text-xl text-wellness-white/80 leading-relaxed">{cause.description}</p>
              </div>
            ))}
          </div>

          <div className="fixed bottom-12 left-0 right-0 md:left-64 px-8 pointer-events-none">
            <div className="max-w-4xl mx-auto flex justify-end">
              <Link href="/diet" className="pointer-events-auto">
                <button className="bg-healthGreen hover:bg-green-700 text-white px-12 py-6 rounded-2xl font-bold text-xl shadow-2xl shadow-green-900/40 transition-all active:scale-95">
                  Get Personalized Diet Plan
                </button>
              </Link>
            </div>
          </div>
        </motion.div>
      ) : null}
    </div>
  );
}

