"use client";

import { useState, useEffect } from "react";
import { Search, ChevronRight, AlertCircle, X, Loader2, Sparkles, Plus, CheckCircle2 } from "lucide-react";
import { motion } from "framer-motion";
import { clsx } from "clsx";
import Link from "next/link";
import { getApiBase } from "../api";
import { recordHistoryEntry } from "../historyTracker";

const DEFAULT_SYMPTOMS = [
  "Migraine", "Bloating", "Joint Pain", "Anxiety", "Insomnia",
  "Fatigue", "Acidity", "Cough", "Nausea", "Headache", "Fever"
];

export default function AnalyzePage() {
  const [symptomsList, setSymptomsList] = useState<string[]>(DEFAULT_SYMPTOMS);
  const [step, setStep] = useState(1);
  const [query, setQuery] = useState("");
  const [selected, setSelected] = useState<string[]>([]);
  const [results, setResults] = useState<{name: string, severity: string, impact?: string}[]>([]);
  const [causes, setCauses] = useState<{title: string, description: string}[]>([]);
  const [aiBadge, setAiBadge] = useState<string>("✨ AI-Powered Analysis");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const apiBase = getApiBase();
    fetch(`${apiBase}/symptoms`)
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data) && data.length > 0) {
          // Merge unique symptoms
          const combined = Array.from(new Set([...DEFAULT_SYMPTOMS, ...data]));
          setSymptomsList(combined);
        }
      })
      .catch(err => {
        console.warn("Backend API unreachable, using default dataset:", err);
      });
  }, []);

  const filtered = symptomsList.filter(s => s.toLowerCase().includes(query.toLowerCase()));

  const toggleSymptom = (s: string) => {
    setSelected(prev => prev.includes(s) ? prev.filter(item => item !== s) : [...prev, s]);
  };

  const handleAddCustom = () => {
    if (query.trim() && !selected.includes(query.trim())) {
      const custom = query.trim();
      setSymptomsList(prev => [custom, ...prev]);
      setSelected(prev => [...prev, custom]);
      setQuery("");
    }
  };

  const handleContinue = async () => {
    if (selected.length === 0) return;
    setLoading(true);

    try {
      const apiBase = getApiBase();
      const res = await fetch(`${apiBase}/analyze`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ symptoms: selected })
      });

      if (res.ok) {
        const data = await res.json();
        
        setAiBadge(data.aiBadgeText || (data.isAiGenerated ? "✨ AI-Powered Gemini Analysis" : "💡 Smart Clinical Knowledge Base"));
        
        if (data.analysis) {
          setResults(data.analysis.map((item: any) => ({
            name: item.name,
            severity: item.severity || "Medium",
            impact: item.impact
          })));
        } else {
          setResults(selected.map(s => ({ name: s, severity: "Medium" })));
        }

        setCauses(data.possibleCauses || []);
        
        // Cache complete payload for Diet Plan Page
        localStorage.setItem("symptomsync_analysisData", JSON.stringify(data));
        if (data.dietPlan) {
          localStorage.setItem("symptomsync_dietPlan", JSON.stringify(data.dietPlan));
        }
        if (data.foodsToAvoid) {
          localStorage.setItem("symptomsync_foodsToAvoid", JSON.stringify(data.foodsToAvoid));
        }

        // Record real-time log in History
        const primaryCause = (data.possibleCauses && data.possibleCauses[0]) ? data.possibleCauses[0].title : "Metabolic/Dietary Triggers";
        recordHistoryEntry({
          type: "analysis",
          title: `Analysis: ${selected.join(", ")}`,
          symptoms: selected,
          severity: "Medium",
          cause: primaryCause,
          details: `Generated 4-meal full-day diet plan for ${selected.length} symptom(s)`
        });
      } else {
        throw new Error("Backend error");
      }
    } catch (err) {
      console.warn("Using fallback analysis:", err);
      setAiBadge("💡 Offline Fallback Engine");
      setResults(selected.map(s => ({ name: s, severity: "Medium" })));
      setCauses([
        { title: "Metabolic & Dietary Factors", description: "Sensitivities to specific ingredients, dehydration, or irregular meal timing." },
        { title: "Inflammatory Triggers", description: "Elevated cytokine markers associated with selected physical symptoms." }
      ]);
    } finally {
      setLoading(false);
      setStep(2);
    }
  };

  const updateSeverity = (name: string, sev: string) => {
    setResults(prev => prev.map(r => r.name === name ? { ...r, severity: sev } : r));
  };

  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      {step === 1 ? (
        <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }}>
          <header className="mb-8">
            <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-smoothPurple/10 border border-smoothPurple/30 text-smoothPurple font-bold text-sm mb-4">
              <Sparkles size={16} />
              <span>AI Symptom-Based Full-Day Diet Planner</span>
            </div>
            <h1 className="text-4xl font-extrabold mb-3">What symptoms are you experiencing?</h1>
            <p className="text-wellness-white/60 text-lg">Select one or multiple symptoms to generate a customized full-day meal schedule.</p>
          </header>

          <div className="relative mb-8">
            <Search className="absolute left-6 top-1/2 -translate-y-1/2 text-wellness-white/30" size={24} />
            <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="Type or search symptoms (e.g. Migraine, Acidity, Eczema)..."
              className="w-full bg-wellness-card border border-white/10 rounded-3xl py-5 pl-16 pr-32 text-lg focus:outline-none focus:border-smoothPurple transition-colors text-white"
            />
            {query.trim() && (
              <button
                onClick={handleAddCustom}
                className="absolute right-4 top-1/2 -translate-y-1/2 bg-smoothPurple hover:bg-indigo-600 text-white px-4 py-2 rounded-2xl font-bold text-sm flex items-center gap-1.5 transition-all"
              >
                <Plus size={16} />
                <span>Add</span>
              </button>
            )}
          </div>

          <div className="flex flex-wrap gap-3 mb-24">
            {filtered.map(s => {
              const isActive = selected.includes(s);
              return (
                <button
                  key={s}
                  onClick={() => toggleSymptom(s)}
                  className={clsx(
                    "px-6 py-3.5 rounded-2xl font-bold transition-all active:scale-95 border flex items-center gap-2 text-base",
                    isActive
                      ? "bg-smoothPurple border-smoothPurple text-white shadow-lg shadow-smoothPurple/30"
                      : "bg-wellness-card border-white/10 text-wellness-white/70 hover:border-white/20 hover:text-white"
                  )}
                >
                  {isActive && <CheckCircle2 size={18} />}
                  <span>{s}</span>
                </button>
              );
            })}
          </div>

          <div className="fixed bottom-14 left-0 right-0 md:left-64 px-4 py-3 bg-slate-950/95 backdrop-blur-2xl border-t border-white/10 z-30 shadow-2xl">
            <div className="max-w-4xl mx-auto flex items-center justify-between gap-4">
              <span className="text-xs font-bold text-wellness-white/70 hidden sm:inline">
                {selected.length > 0 ? `${selected.length} symptoms selected` : "Select symptoms to proceed"}
              </span>
              <button
                disabled={selected.length === 0 || loading}
                onClick={handleContinue}
                className="w-full sm:w-auto bg-gradient-to-r from-smoothPurple to-indigo-600 hover:from-purple-600 hover:to-indigo-700 disabled:opacity-40 disabled:cursor-not-allowed text-white px-6 py-3.5 rounded-2xl font-bold flex items-center justify-center gap-2 shadow-xl shadow-smoothPurple/30 transition-all active:scale-95 text-base"
              >
                {loading ? (
                  <>
                    <Loader2 className="animate-spin" size={20} />
                    <span>AI Synthesizing Plan...</span>
                  </>
                ) : (
                  <>
                    <span>Generate AI Diet Plan ({selected.length})</span>
                    <ChevronRight size={20} />
                  </>
                )}
              </button>
            </div>
          </div>
        </motion.div>
      ) : step === 2 ? (
        <motion.div initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }}>
          <header className="mb-10 flex items-center justify-between">
            <div>
              <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-smoothPurple/20 border border-smoothPurple/40 text-smoothPurple font-bold text-sm mb-3">
                <span>{aiBadge}</span>
              </div>
              <h1 className="text-4xl font-extrabold mb-2">Symptom Severity & Impact</h1>
              <p className="text-wellness-white/60">Review detected symptoms and customize intensity levels.</p>
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
              <div key={res.name} className="bg-wellness-card p-6 rounded-3xl border border-white/10 flex flex-col md:flex-row md:items-center justify-between gap-6 shadow-lg">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <h3 className="text-2xl font-bold text-white">{res.name}</h3>
                    <span className="text-xs px-3 py-1 rounded-full font-bold bg-white/5 border border-white/10 text-wellness-white/70">
                      Intensity: {res.severity}
                    </span>
                  </div>
                  {res.impact && (
                    <p className="text-wellness-white/70 text-sm leading-relaxed">{res.impact}</p>
                  )}
                </div>

                <div className="flex bg-wellness-charcoal p-1.5 rounded-2xl border border-white/5 shrink-0">
                  {["Low", "Medium", "High"].map(level => (
                    <button
                      key={level}
                      onClick={() => updateSeverity(res.name, level)}
                      className={clsx(
                        "px-5 py-2.5 rounded-xl text-sm font-bold transition-all",
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

          <div className="fixed bottom-12 left-0 right-0 md:left-64 px-8 pointer-events-none z-30">
            <div className="max-w-4xl mx-auto flex flex-col md:flex-row justify-end gap-4">
              <button
                onClick={() => setStep(3)}
                className="pointer-events-auto bg-wellness-card border border-white/10 hover:border-white/30 text-white px-8 py-5 rounded-2xl font-bold transition-all active:scale-95"
              >
                View Causes
              </button>
              <Link href="/diet" className="pointer-events-auto">
                <button className="w-full bg-gradient-to-r from-healthGreen to-emerald-600 hover:from-green-600 hover:to-emerald-700 text-white px-10 py-5 rounded-2xl font-bold shadow-xl shadow-green-900/30 transition-all active:scale-95 flex items-center gap-2">
                  <Sparkles size={20} />
                  <span>View Full-Day Diet Plan</span>
                </button>
              </Link>
            </div>
          </div>
        </motion.div>
      ) : step === 3 ? (
        <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }}>
          <header className="mb-10 flex items-center justify-between">
            <div>
              <h1 className="text-4xl font-extrabold mb-3">Root Causes & Triggers</h1>
              <p className="text-wellness-white/60">Understanding why these symptoms manifest together.</p>
            </div>
            <button
              onClick={() => setStep(2)}
              className="p-3 rounded-2xl bg-white/5 hover:bg-white/10 transition-colors"
            >
              <ChevronRight className="rotate-180" size={24} />
            </button>
          </header>

          <div className="grid gap-6 mb-24">
            {causes.map((cause: any, i: number) => {
              const symTag = cause.targetSymptom || (selected[i % Math.max(1, selected.length)]) || "General Symptom";
              const conditionTag = cause.associatedCondition || `Condition: ${symTag} Underlying Mechanism`;
              const dietTag = cause.recommendedDiet || `Recommended: ${symTag} Recovery & Anti-Inflammatory Diet`;
              const avoidList: string[] = Array.isArray(cause.foodsToAvoid) && cause.foodsToAvoid.length > 0
                ? cause.foodsToAvoid
                : ["Ultra-Processed Foods", "Refined Sugars", "Excessive Caffeine"];

              return (
                <div key={i} className="bg-wellness-card p-8 rounded-[32px] border border-white/10 shadow-lg space-y-4">
                  {/* Disease & Symptom Header Pill */}
                  <div className="flex flex-wrap items-center gap-2">
                    <span className="px-3.5 py-1.5 rounded-full bg-purple-500/20 text-purple-300 border border-purple-500/30 text-xs font-black uppercase tracking-wider flex items-center gap-1.5">
                      <span>🩺 FOR SYMPTOM:</span>
                      <span className="text-white font-extrabold">{symTag}</span>
                    </span>
                    <span className="px-3 py-1 rounded-full bg-blue-500/10 text-blue-300 border border-blue-500/20 text-xs font-bold">
                      {conditionTag}
                    </span>
                  </div>

                  {/* Trigger Title & Description */}
                  <div>
                    <span className="text-xs font-bold uppercase tracking-wider text-wellness-white/50 block mb-1">⚡ Trigger / Cause:</span>
                    <h3 className="text-2xl font-bold text-smoothPurple mb-2">{cause.title}</h3>
                    <p className="text-base text-wellness-white/80 leading-relaxed">{cause.description}</p>
                  </div>

                  {/* Prescribed Diet Mapping */}
                  <div className="pt-3 border-t border-white/10 flex items-center gap-3">
                    <div className="p-2.5 bg-emerald-500/20 rounded-xl text-emerald-400 border border-emerald-500/30 shrink-0">
                      <Sparkles size={20} />
                    </div>
                    <div>
                      <span className="text-xs font-black text-emerald-400 uppercase tracking-wider block">🥗 Prescribed Diet for {symTag}</span>
                      <span className="text-sm font-extrabold text-white">{dietTag}</span>
                    </div>
                  </div>

                  {/* Foods to Avoid for this Cause/Symptom */}
                  <div className="pt-3 border-t border-white/10">
                    <span className="text-xs font-black text-red-400 uppercase tracking-wider block mb-2">
                      🚫 Foods to Avoid for {symTag} ({cause.title}):
                    </span>
                    <div className="flex flex-wrap gap-2">
                      {avoidList.map((food, fIdx) => (
                        <span key={fIdx} className="px-3 py-1.5 rounded-xl bg-red-500/10 text-red-300 border border-red-500/20 text-xs font-bold flex items-center gap-1.5">
                          <AlertCircle size={14} className="text-red-400" />
                          <span>{food}</span>
                        </span>
                      ))}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>

          <div className="fixed bottom-12 left-0 right-0 md:left-64 px-8 pointer-events-none z-30">
            <div className="max-w-4xl mx-auto flex justify-end">
              <Link href="/diet" className="pointer-events-auto">
                <button className="bg-gradient-to-r from-healthGreen to-emerald-600 hover:from-green-600 hover:to-emerald-700 text-white px-12 py-6 rounded-2xl font-bold text-xl shadow-2xl shadow-green-900/40 transition-all active:scale-95 flex items-center gap-3">
                  <Sparkles size={24} />
                  <span>Open Personalized Full-Day Diet Plan</span>
                </button>
              </Link>
            </div>
          </div>
        </motion.div>
      ) : null}
    </div>
  );
}
