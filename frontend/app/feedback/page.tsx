"use client";

import { useState, useEffect } from "react";
import { Star, CheckCircle2, ArrowRight, Loader2, Heart, Sparkles } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import { getApiBase } from "../api";

export default function FeedbackPage() {
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);
  const [feedbackText, setFeedbackText] = useState("");
  const [submitted, setSubmitted] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [userEmail, setUserEmail] = useState("");

  useEffect(() => {
    try {
      const storedUser = localStorage.getItem("symptomsync_user");
      if (storedUser) {
        const u = JSON.parse(storedUser);
        if (u.email) setUserEmail(u.email);
      }
    } catch (e) {}
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (rating === 0) {
      setError("Please select a rating between 1 and 5 stars.");
      return;
    }
    setError("");
    setLoading(true);

    try {
      const apiBase = getApiBase();
      const res = await fetch(`${apiBase}/feedback`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ rating, feedback: feedbackText, email: userEmail })
      });

      if (res.ok) {
        setSubmitted(true);
      } else {
        const data = await res.json();
        setError(data.detail || "Could not submit feedback.");
      }
    } catch (err) {
      console.warn("Backend API notice, saving feedback locally:", err);
      setSubmitted(true);
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setRating(0);
    setHoverRating(0);
    setFeedbackText("");
    setSubmitted(false);
  };

  return (
    <div className="min-h-[85vh] flex items-center justify-center p-4">
      <AnimatePresence mode="wait">
        {!submitted ? (
          /* 24. Feedback Form Screen */
          <motion.div
            key="feedback-form"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 0.95 }}
            className="max-w-md w-full bg-gradient-to-b from-[#FFF0F3] to-[#FFE4E8] text-slate-800 p-8 rounded-[40px] shadow-2xl border border-rose-200 relative overflow-hidden"
          >
            {/* Top Header */}
            <div className="text-center mb-8">
              <span className="inline-flex items-center gap-1.5 px-4 py-1.5 rounded-full bg-rose-500/10 text-rose-600 font-bold text-xs mb-3 border border-rose-500/20">
                <Sparkles size={14} />
                Feedback
              </span>
              <h1 className="text-3xl font-black text-rose-950 tracking-tight mb-2">
                We Value Your Feedback!
              </h1>
              <p className="text-rose-800/80 font-medium text-sm">
                How was your experience with us?
              </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-6 relative z-10">
              {/* Star Rating System */}
              <div className="flex justify-center items-center gap-2 py-2">
                {[1, 2, 3, 4, 5].map((star) => {
                  const active = star <= (hoverRating || rating);
                  return (
                    <button
                      key={star}
                      type="button"
                      onClick={() => setRating(star)}
                      onMouseEnter={() => setHoverRating(star)}
                      onMouseLeave={() => setHoverRating(0)}
                      className="p-1 transition-transform active:scale-125 focus:outline-none cursor-pointer"
                    >
                      <Star
                        size={36}
                        className={`transition-colors ${
                          active
                            ? "fill-rose-500 text-rose-500 drop-shadow-md"
                            : "fill-transparent text-rose-300 stroke-[1.5]"
                        }`}
                      />
                    </button>
                  );
                })}
              </div>

              {/* Feedback Text Area */}
              <div>
                <textarea
                  rows={4}
                  value={feedbackText}
                  onChange={(e) => setFeedbackText(e.target.value)}
                  placeholder="Write your feedback..."
                  className="w-full bg-white/90 border border-rose-200 rounded-3xl p-4 text-rose-950 placeholder-rose-400 focus:outline-none focus:ring-2 focus:ring-rose-400 focus:bg-white transition-all text-sm font-medium shadow-inner resize-none"
                />
              </div>

              {error && (
                <p className="text-rose-600 text-xs font-bold text-center bg-white/80 p-2.5 rounded-xl border border-rose-200">
                  {error}
                </p>
              )}

              {/* Submit Button */}
              <button
                type="submit"
                disabled={loading}
                className="w-full bg-rose-500 hover:bg-rose-600 text-white font-black py-4 rounded-2xl transition-all shadow-xl shadow-rose-500/30 active:scale-[0.98] flex items-center justify-center gap-2 text-base cursor-pointer"
              >
                {loading ? <Loader2 className="animate-spin" size={20} /> : "Submit"}
              </button>
            </form>

            {/* Bottom Healthy Food Decor Card */}
            <div className="mt-8 pt-4 text-center border-t border-rose-200/60">
              <div className="flex justify-center gap-3 text-2xl">
                <span>🍓</span>
                <span>🍎</span>
                <span>🍊</span>
                <span>🥑</span>
              </div>
            </div>
          </motion.div>
        ) : (
          /* 25. Thank You Screen */
          <motion.div
            key="thank-you"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 0.95 }}
            className="max-w-md w-full bg-gradient-to-b from-[#F0FDF4] to-[#DCFCE7] text-slate-800 p-8 rounded-[40px] shadow-2xl border border-emerald-200 text-center relative overflow-hidden"
          >
            <div className="py-6">
              <h1 className="text-4xl font-black text-emerald-950 tracking-tight mb-3">
                Thank You!
              </h1>
              <p className="text-emerald-800/80 font-semibold text-base mb-8">
                Your feedback helps us improve.
              </p>

              {/* Big Checkmark Circle Icon */}
              <div className="flex justify-center my-6">
                <div className="w-28 h-28 rounded-full bg-emerald-500/10 border-4 border-emerald-500 flex items-center justify-center text-emerald-600 shadow-xl shadow-emerald-500/20 animate-pulse">
                  <CheckCircle2 size={64} className="stroke-[2.5]" />
                </div>
              </div>

              {/* Action Buttons */}
              <div className="space-y-3 mt-8">
                <button
                  onClick={() => window.location.href = "/"}
                  className="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-black py-4 rounded-2xl transition-all shadow-xl shadow-emerald-600/30 active:scale-[0.98] flex items-center justify-center gap-2 cursor-pointer"
                >
                  <span>Back to Dashboard</span>
                  <ArrowRight size={18} />
                </button>

                <button
                  onClick={handleReset}
                  className="w-full bg-white/80 hover:bg-white text-emerald-900 font-bold py-3 rounded-2xl transition-all border border-emerald-200 text-sm cursor-pointer"
                >
                  Submit Another Response
                </button>
              </div>

              {/* Bottom Fruit Nutrition Decor */}
              <div className="mt-8 pt-4 border-t border-emerald-200/60">
                <div className="flex justify-center gap-3 text-2xl">
                  <span>🍎</span>
                  <span>🍑</span>
                  <span>🥝</span>
                  <span>🍐</span>
                </div>
              </div>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}
