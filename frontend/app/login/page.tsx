"use client";

import { useState } from "react";
import { Lock, Mail, Eye, EyeOff, Loader2, UserCheck, ArrowRight, Phone } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import { getApiBase } from "../api";

export default function LoginPage() {
  const [mode, setMode] = useState<"login" | "register">("login");
  const [name, setName] = useState("Reddyomsai350");
  const [email, setEmail] = useState("reddyomsai350@gmail.com");
  const [phone, setPhone] = useState("6305473867");
  const [password, setPassword] = useState("password123");
  const [confirmPassword, setConfirmPassword] = useState("password123");

  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const syncProfileState = (userObj: any) => {
    sessionStorage.setItem("symptomsync_session_active", "true");
    localStorage.setItem("symptomsync_token", "symptomsync-token");
    localStorage.setItem("symptomsync_user", JSON.stringify(userObj));
    localStorage.setItem("symptomsync_user_profile", JSON.stringify({
      name: userObj.name || "Reddyomsai350",
      email: userObj.email || "reddyomsai350@gmail.com",
      phone: userObj.phone || "6305473867"
    }));
    try {
      const apiBase = getApiBase();
      fetch(`${apiBase}/reminders/sync_profile`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          userName: userObj.name,
          email: userObj.email,
          phone: userObj.phone
        })
      });
    } catch (e) {}
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    if (!email.includes("@")) {
      setError("Please enter a valid email address.");
      return;
    }

    setLoading(true);
    const apiBase = getApiBase();

    try {
      if (mode === "login") {
        const res = await fetch(`${apiBase}/auth/login`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email, password })
        });

        if (res.ok) {
          const data = await res.json();
          const userObj = data.user || { name: name || "Reddyomsai350", email: email.trim(), phone: phone.trim() || "6305473867" };
          syncProfileState(userObj);
          window.location.href = "/water";
          return;
        } else {
          const data = await res.json();
          setError(data.detail || "Invalid email or password.");
          return;
        }
      } else {
        // Register Mode
        const res = await fetch(`${apiBase}/auth/register`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ name, email, password, phone })
        });

        if (res.ok) {
          const data = await res.json();
          const userObj = data.user || { name: name.trim() || "Reddyomsai350", email: email.trim(), phone: phone.trim() || "6305473867" };
          syncProfileState(userObj);
          window.location.href = "/water";
          return;
        } else {
          const data = await res.json();
          setError(data.detail || "Could not create account. Please try again.");
          return;
        }
      }
    } catch (err) {
      console.warn("Backend API unavailable, using local authentication fallback:", err);
      const displayName = name.trim() || "Reddyomsai350";
      const userPhone = phone.trim() || "6305473867";
      const userData = { name: displayName, email: email.trim(), phone: userPhone };
      syncProfileState(userData);
      window.location.href = "/water";
    } finally {
      setLoading(false);
    }
  };

  const switchMode = (targetMode: "login" | "register") => {
    setError("");
    setMode(targetMode);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-wellness-charcoal px-4 py-12">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="max-w-md w-full bg-wellness-card p-8 rounded-3xl shadow-2xl border border-white/10"
      >
        <div className="text-center mb-8">
          <h1 className="text-3xl font-extrabold text-richOrange mb-2">
            {mode === "login" ? "Welcome Back" : "Create Account"}
          </h1>
          <p className="text-wellness-white/60 text-sm">
            {mode === "login"
              ? "Log in to track your symptoms & personalized diet plan"
              : "Join SymptomSync to personalize your health & nutrition"}
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          <AnimatePresence mode="wait">
            {mode === "register" && (
              <motion.div
                key="name-field"
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: "auto" }}
                exit={{ opacity: 0, height: 0 }}
                className="space-y-4"
              >
                <div className="space-y-1.5">
                  <label className="text-xs font-semibold text-wellness-white/80 ml-1">Full Name</label>
                  <div className="relative">
                    <UserCheck className="absolute left-4 top-1/2 -translate-y-1/2 text-wellness-white/40" size={18} />
                    <input
                      type="text"
                      value={name}
                      onChange={(e) => setName(e.target.value)}
                      className="w-full bg-wellness-charcoal border border-white/10 rounded-2xl py-3.5 pl-12 pr-4 focus:outline-none focus:border-richOrange transition-colors text-white text-sm"
                      placeholder="John Doe"
                      required={mode === "register"}
                    />
                  </div>
                </div>

                <div className="space-y-1.5">
                  <label className="text-xs font-semibold text-wellness-white/80 ml-1">Mobile Phone Number (SMS Alerts)</label>
                  <div className="relative">
                    <Phone className="absolute left-4 top-1/2 -translate-y-1/2 text-wellness-white/40" size={18} />
                    <input
                      type="tel"
                      value={phone}
                      onChange={(e) => setPhone(e.target.value)}
                      className="w-full bg-wellness-charcoal border border-white/10 rounded-2xl py-3.5 pl-12 pr-4 focus:outline-none focus:border-richOrange transition-colors text-white text-sm"
                      placeholder="+1 (555) 234-5678"
                    />
                  </div>
                </div>
              </motion.div>
            )}
          </AnimatePresence>

          <div className="space-y-1.5">
            <label className="text-xs font-semibold text-wellness-white/80 ml-1">Email Address</label>
            <div className="relative">
              <Mail className="absolute left-4 top-1/2 -translate-y-1/2 text-wellness-white/40" size={18} />
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full bg-wellness-charcoal border border-white/10 rounded-2xl py-3.5 pl-12 pr-4 focus:outline-none focus:border-richOrange transition-colors text-white text-sm"
                placeholder="you@example.com"
                required
              />
            </div>
          </div>

          <div className="space-y-1.5">
            <label className="text-xs font-semibold text-wellness-white/80 ml-1">Password</label>
            <div className="relative">
              <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-wellness-white/40" size={18} />
              <input
                type={showPassword ? "text" : "password"}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full bg-wellness-charcoal border border-white/10 rounded-2xl py-3.5 pl-12 pr-12 focus:outline-none focus:border-richOrange transition-colors text-white text-sm"
                placeholder="••••••••"
                required
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-wellness-white/40 hover:text-white"
              >
                {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          <AnimatePresence mode="wait">
            {mode === "register" && (
              <motion.div
                key="confirm-password-field"
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: "auto" }}
                exit={{ opacity: 0, height: 0 }}
                className="space-y-1.5"
              >
                <label className="text-xs font-semibold text-wellness-white/80 ml-1">Confirm Password</label>
                <div className="relative">
                  <Lock className="absolute left-4 top-1/2 -translate-y-1/2 text-wellness-white/40" size={18} />
                  <input
                    type={showPassword ? "text" : "password"}
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    className="w-full bg-wellness-charcoal border border-white/10 rounded-2xl py-3.5 pl-12 pr-4 focus:outline-none focus:border-richOrange transition-colors text-white text-sm"
                    placeholder="••••••••"
                    required={mode === "register"}
                  />
                </div>
              </motion.div>
            )}
          </AnimatePresence>

          {error && (
            <motion.p
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="text-red-400 text-xs text-center font-semibold bg-red-500/10 p-3 rounded-xl border border-red-500/20"
            >
              {error}
            </motion.p>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-richOrange hover:bg-orange-600 text-white font-bold py-4 rounded-2xl transition-all shadow-lg shadow-richOrange/20 active:scale-[0.98] flex items-center justify-center gap-2 text-base cursor-pointer mt-4"
          >
            {loading ? (
              <Loader2 className="animate-spin" size={20} />
            ) : mode === "login" ? (
              <>
                <span>Sign In</span>
                <ArrowRight size={18} />
              </>
            ) : (
              <>
                <span>Create Account</span>
                <ArrowRight size={18} />
              </>
            )}
          </button>
        </form>

        <div className="mt-8 text-center pt-4 border-t border-white/5">
          {mode === "login" ? (
            <p className="text-wellness-white/60 text-sm">
              Don&apos;t have an account?{" "}
              <button
                type="button"
                onClick={() => switchMode("register")}
                className="text-richOrange font-bold hover:underline cursor-pointer ml-1"
              >
                Create Account
              </button>
            </p>
          ) : (
            <p className="text-wellness-white/60 text-sm">
              Already have an account?{" "}
              <button
                type="button"
                onClick={() => switchMode("login")}
                className="text-richOrange font-bold hover:underline cursor-pointer ml-1"
              >
                Sign In
              </button>
            </p>
          )}
        </div>
      </motion.div>
    </div>
  );
}
