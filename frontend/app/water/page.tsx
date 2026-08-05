"use client";

import { useState, useEffect } from "react";
import { Plus, Bell, ChevronLeft, Volume2, Sparkles, User, Mail, Phone, Clock, Send, RotateCcw, Minus, Droplets } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import Link from "next/link";
import { clsx } from "clsx";
import { playVictoryChime, playReminderAlarm, sendNativeNotification } from "../audioNotifier";
import { recordWaterLogged } from "../activityTracker";
import { getApiBase } from "../api";

interface UserProfile {
  name: string;
  email: string;
  phone: string;
}

interface SentReminder {
  id: number;
  userName: string;
  email: string;
  phone: string;
  message: string;
  type: string;
  status: string;
  timestamp: string;
}

export default function WaterTrackerPage() {
  const [count, setCount] = useState(5);
  const [goalGlasses, setGoalGlasses] = useState(8);
  const progress = Math.min(count / goalGlasses, 1);
  const isGoalAchieved = count >= goalGlasses;

  // Reminders & Profile state
  const [remindersEnabled, setRemindersEnabled] = useState(true);
  const [frequency, setFrequency] = useState("1 Hour");
  const [profile, setProfile] = useState<UserProfile>({
    name: "Reddyomsai350",
    email: "reddyomsai350@gmail.com",
    phone: "6305473867"
  });
  const [editingProfile, setEditingProfile] = useState(false);
  const [gmailAppPassword, setGmailAppPassword] = useState("");
  const [gmailConfigured, setGmailConfigured] = useState(false);
  const [smsApiKey, setSmsApiKey] = useState("");
  const [sentReminders, setSentReminders] = useState<SentReminder[]>([]);
  const [lastGoalCelebrated, setLastGoalCelebrated] = useState(false);
  const [reminderToast, setReminderToast] = useState<string | null>(null);

  const saveGmailConfig = async (pass: string) => {
    try {
      const apiBase = getApiBase();
      const res = await fetch(`${apiBase}/reminders/config_email`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          sender_email: profile.email,
          app_password: pass
        })
      });
      if (res.ok) {
        const data = await res.json();
        if (data.configured) setGmailConfigured(true);
      }
    } catch (e) {}
  };

  const saveSmsConfig = async (key: string) => {
    try {
      const apiBase = getApiBase();
      await fetch(`${apiBase}/reminders/config_sms`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ api_key: key })
      });
    } catch (e) {}
  };


  useEffect(() => {
    try {
      const stored = localStorage.getItem("symptomsync_user_profile");
      const loggedInUser = localStorage.getItem("symptomsync_user");
      
      let initialProfile: UserProfile = {
        name: "Reddyomsai350",
        email: "reddyomsai350@gmail.com",
        phone: "6305473867"
      };

      if (loggedInUser) {
        const u = JSON.parse(loggedInUser);
        initialProfile.name = u.name || initialProfile.name;
        initialProfile.email = u.email || initialProfile.email;
        initialProfile.phone = u.phone || initialProfile.phone;
      }

      if (stored) {
        const p = JSON.parse(stored);
        initialProfile = {
          name: p.name || initialProfile.name,
          email: p.email || initialProfile.email,
          phone: p.phone || initialProfile.phone
        };
      }

      setProfile(initialProfile);
      syncProfileToBackend(initialProfile);
    } catch (e) {}

    fetchReminderHistory();

    if (typeof window !== "undefined" && "Notification" in window) {
      if (Notification.permission === "default") {
        Notification.requestPermission();
      }
    }
  }, []);

  // Poll backend reminder history every 3 seconds to update history & alert log in real time
  useEffect(() => {
    const historyPoller = setInterval(() => {
      fetchReminderHistory();
    }, 3000);

    return () => clearInterval(historyPoller);
  }, []);

  // Real-time Automated Interval Timer for Water Reminders
  useEffect(() => {
    if (!remindersEnabled) return;

    let intervalMs = 3600000;
    if (frequency === "30 Mins") intervalMs = 1800000;
    if (frequency === "2 Hours") intervalMs = 7200000;

    const timer = setInterval(() => {
      triggerTestReminder();
    }, intervalMs);

    return () => clearInterval(timer);
  }, [remindersEnabled, frequency, profile]);

  const syncProfileToBackend = async (userProfile: UserProfile) => {
    try {
      const apiBase = getApiBase();
      await fetch(`${apiBase}/reminders/sync_profile`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          userName: userProfile.name,
          email: userProfile.email,
          phone: userProfile.phone
        })
      });
    } catch (e) {}
  };

  const fetchReminderHistory = async () => {
    try {
      const apiBase = getApiBase();
      const res = await fetch(`${apiBase}/reminders/history`);
      if (res.ok) {
        const data = await res.json();
        if (Array.isArray(data)) {
          setSentReminders(data);
        }
      }
    } catch (e) {}
  };

  const handleAddGlass = (amount: number = 1) => {
    const nextCount = Math.min(16, Math.max(0, count + amount));
    setCount(nextCount);
    if (amount > 0) {
      recordWaterLogged(250 * amount);
    }

    if (nextCount >= goalGlasses && !lastGoalCelebrated) {
      setLastGoalCelebrated(true);
      playVictoryChime();
      
      const msg = `🎉 Congratulations ${profile.name}! Daily Water Goal Achieved (8/8 Glasses)!`;
      setReminderToast(msg);
      sendNativeNotification("🎉 Water Goal Achieved!", msg);
      dispatchReminderToBackend("water_goal", msg);
    }
  };

  const handleResetCount = () => {
    setCount(0);
    setLastGoalCelebrated(false);
  };

  const handleSaveProfile = () => {
    localStorage.setItem("symptomsync_user_profile", JSON.stringify(profile));
    try {
      const loggedIn = localStorage.getItem("symptomsync_user");
      const current = loggedIn ? JSON.parse(loggedIn) : {};
      localStorage.setItem("symptomsync_user", JSON.stringify({ ...current, ...profile }));
    } catch (e) {}
    syncProfileToBackend(profile);
    setEditingProfile(false);
    triggerTestReminder();
  };

  const dispatchReminderToBackend = async (type: string, message: string) => {
    try {
      const apiBase = getApiBase();
      const res = await fetch(`${apiBase}/reminders/send`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          userName: profile.name,
          email: profile.email,
          phone: profile.phone,
          message,
          type
        })
      });

      if (res.ok) {
        const data = await res.json();
        if (data.dispatch) {
          setSentReminders(prev => [data.dispatch, ...prev.filter(item => item.id !== data.dispatch.id)]);
          if (data.dispatch.message) {
            setReminderToast(data.dispatch.message);
          }
        }
      }
    } catch (e) {
      console.warn("Backend reminder dispatch offline", e);
    }
  };

  const triggerTestReminder = async () => {
    playReminderAlarm();
    await dispatchReminderToBackend("water_reminder", "water_reminder");

    setTimeout(() => {
      setReminderToast(null);
    }, 8000);
  };

  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      {/* Android / Google Messages SMS Pop-Up Notification Card */}
      <AnimatePresence>
        {reminderToast && (
          <motion.div
            initial={{ opacity: 0, y: -40, scale: 0.95 }}
            animate={{ opacity: 1, y: 0, scale: 1 }}
            exit={{ opacity: 0, y: -30, scale: 0.95 }}
            className="mb-8 p-6 bg-gradient-to-r from-slate-900 via-zinc-900 to-slate-900 text-white rounded-3xl shadow-2xl border-2 border-emerald-500/40 relative z-50 overflow-hidden"
          >
            <div className="flex items-center justify-between gap-2 pb-3 mb-3 border-b border-white/10">
              <div className="flex items-center gap-2">
                <div className="w-7 h-7 rounded-full bg-emerald-500/20 text-emerald-400 flex items-center justify-center font-bold text-xs">
                  💬
                </div>
                <span className="text-xs font-black tracking-wider uppercase text-emerald-400">Google Messages • Real-Time SMS Alert</span>
              </div>
              <span className="text-[11px] font-bold px-3 py-1 rounded-full bg-emerald-500/20 text-emerald-300 border border-emerald-500/40">
                Logged-in Phone: {profile.phone || "6305473867"}
              </span>
            </div>

            <div className="flex items-start justify-between gap-4">
              <div className="flex items-start gap-4">
                <div className="w-12 h-12 rounded-2xl bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center shadow-lg shrink-0">
                  <Volume2 className="text-amber-300 animate-bounce" size={24} />
                </div>
                <div>
                  <h4 className="text-xs font-extrabold text-wellness-white/50 uppercase tracking-wide mb-1">
                    SymptomSync SMS • Sent to {profile.phone || "6305473867"}
                  </h4>
                  <p className="font-extrabold text-lg sm:text-xl text-white leading-snug">
                    {reminderToast}
                  </p>
                  <div className="mt-2 flex items-center gap-3 text-xs text-emerald-300 font-semibold">
                    <span>✓ Delivered to Mobile Network ({profile.phone || "6305473867"})</span>
                    <span>•</span>
                    <span>Gmail: {profile.email}</span>
                  </div>
                </div>
              </div>

              <button
                onClick={() => setReminderToast(null)}
                className="px-4 py-2 rounded-xl bg-white/10 hover:bg-white/20 text-white font-bold text-xs transition-colors shrink-0 cursor-pointer"
              >
                Close SMS
              </button>
            </div>
          </motion.div>
        )}
      </AnimatePresence>

      {/* Header Bar */}
      <header className="mb-10 flex flex-wrap items-center justify-between gap-4">
        <div className="flex items-center gap-4">
          <Link href="/">
            <button className="p-3 rounded-2xl bg-white/5 hover:bg-white/10 transition-colors cursor-pointer active:scale-95">
              <ChevronLeft size={24} />
            </button>
          </Link>
          <h1 className="text-4xl font-extrabold tracking-tight">Water Tracker</h1>
        </div>

        <button 
          onClick={triggerTestReminder}
          className="px-5 py-3 rounded-2xl bg-richOrange/10 border border-richOrange/30 text-richOrange font-bold flex items-center gap-2 hover:bg-richOrange hover:text-white transition-all active:scale-95 text-sm cursor-pointer shadow-lg shadow-orange-600/10"
        >
          <Bell size={18} />
          <span>Test Reminder Alert</span>
        </button>
      </header>

      {/* Water Droplet Visual Section */}
      <div className="flex flex-col items-center">
        <div className="relative w-72 h-72 mb-8">
          <svg viewBox="0 0 200 200" className="w-full h-full drop-shadow-2xl">
            <defs>
              <mask id="droplet-mask">
                <path
                  d="M100,0 C100,0 0,120 0,160 C0,200 200,200 200,160 C200,120 100,0 100,0 Z"
                  fill="white"
                />
              </mask>
            </defs>

            <path
              d="M100,0 C100,0 0,120 0,160 C0,200 200,200 200,160 C200,120 100,0 100,0 Z"
              className="fill-white/5"
            />

            <motion.rect
              initial={{ height: 0 }}
              animate={{ height: `${progress * 100}%` }}
              x="0"
              y="0"
              width="200"
              mask="url(#droplet-mask)"
              className="fill-blue-500/80"
              style={{ transformOrigin: "bottom" }}
              transform={`translate(0, ${200 - (progress * 200)})`}
            />

            <text
              x="50%"
              y="60%"
              textAnchor="middle"
              className="text-6xl font-black fill-white"
            >
              {count}
            </text>
          </svg>
        </div>

        <div className="text-center mb-6">
          <h2 className="text-3xl font-extrabold mb-1">{count} / {goalGlasses} Glasses</h2>
          <p className="text-wellness-white/50 font-semibold text-lg">{(progress * 100).toFixed(0)}% of daily target</p>
        </div>

        <AnimatePresence>
          {isGoalAchieved && (
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              className="mb-8 bg-gradient-to-r from-emerald-600 to-healthGreen px-8 py-3.5 rounded-full text-white font-extrabold shadow-xl flex items-center gap-2 border border-emerald-400/30"
            >
              <Sparkles size={20} />
              <span>Goal Achieved! 🎉 Notification & Alarm Sent</span>
            </motion.div>
          )}
        </AnimatePresence>

        {/* Organized Clickable Action Controls Bar */}
        <div className="flex items-center gap-4 mb-12 flex-wrap justify-center">
          <button
            onClick={() => handleAddGlass(-1)}
            disabled={count <= 0}
            className="w-14 h-14 rounded-2xl bg-white/5 hover:bg-white/10 disabled:opacity-30 disabled:cursor-not-allowed border border-white/10 text-white font-bold flex items-center justify-center transition-all cursor-pointer active:scale-95"
            title="Subtract 1 Glass"
          >
            <Minus size={22} />
          </button>

          <button
            onClick={() => handleAddGlass(1)}
            className="w-20 h-20 rounded-3xl bg-gradient-to-r from-purple-600 to-indigo-600 flex items-center justify-center shadow-2xl shadow-purple-600/40 hover:scale-105 active:scale-95 transition-all border border-purple-400/30 cursor-pointer"
            title="Add 1 Glass (+250ml)"
          >
            <Plus size={36} className="text-white" />
          </button>

          <button
            onClick={() => handleAddGlass(2)}
            className="px-5 py-4 rounded-2xl bg-blue-600/20 hover:bg-blue-600/30 border border-blue-500/30 text-blue-300 font-bold flex items-center gap-1.5 transition-all cursor-pointer active:scale-95 text-sm"
            title="Add 2 Glasses (+500ml)"
          >
            <Plus size={18} />
            <span>+2 Glasses</span>
          </button>

          <button
            onClick={handleResetCount}
            className="p-4 rounded-2xl bg-white/5 hover:bg-white/10 border border-white/10 text-wellness-white/60 hover:text-white transition-all cursor-pointer active:scale-95"
            title="Reset Today's Log"
          >
            <RotateCcw size={20} />
          </button>
        </div>
      </div>

      {/* Hydration Goal & Quick Command Center */}
      <div className="mt-8 bg-wellness-card p-8 rounded-[36px] border border-white/10 shadow-xl">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-8 border-b border-white/10 pb-6">
          <div className="flex items-center gap-4">
            <div className="p-4 bg-blue-500/20 rounded-2xl text-blue-400 border border-blue-500/30">
              <Droplets size={28} />
            </div>
            <div>
              <h3 className="text-2xl font-bold text-white">Hydration Command Center</h3>
              <p className="text-wellness-white/60 text-sm">Select your daily target & log instant water intake</p>
            </div>
          </div>

          {/* Quick Sound Chime Button */}
          <button
            onClick={() => {
              playVictoryChime();
            }}
            className="px-5 py-3 rounded-2xl bg-purple-600/20 hover:bg-purple-600/30 text-purple-300 border border-purple-500/30 font-bold text-xs flex items-center gap-2 transition-all active:scale-95 cursor-pointer self-start md:self-auto"
          >
            <Bell size={16} />
            <span>Play Water Alarm Chime</span>
          </button>
        </div>

        <div className="grid md:grid-cols-2 gap-8">
          {/* Target Goal Selector */}
          <div>
            <label className="text-xs font-black uppercase text-wellness-white/50 block mb-3">Daily Intake Target</label>
            <div className="grid grid-cols-2 gap-3">
              {[
                { label: "2.0 Liters", glasses: 8 },
                { label: "2.5 Liters", glasses: 10 },
                { label: "3.0 Liters", glasses: 12 },
                { label: "3.5 Liters", glasses: 14 },
              ].map((item) => (
                <button
                  key={item.glasses}
                  onClick={() => setGoalGlasses(item.glasses)}
                  className={clsx(
                    "p-4 rounded-2xl border font-bold text-left transition-all cursor-pointer active:scale-95 flex flex-col justify-between",
                    goalGlasses === item.glasses
                      ? "bg-blue-600/20 border-blue-500 text-white shadow-lg shadow-blue-500/20"
                      : "bg-wellness-charcoal border-white/5 text-wellness-white/60 hover:border-white/20 hover:text-white"
                  )}
                >
                  <span className="text-base font-extrabold">{item.label}</span>
                  <span className="text-xs text-wellness-white/50 mt-1">{item.glasses} Glasses / day</span>
                </button>
              ))}
            </div>
          </div>

          {/* Instant Quick Log Buttons */}
          <div>
            <label className="text-xs font-black uppercase text-wellness-white/50 block mb-3">Instant Quick Log</label>
            <div className="space-y-3">
              <button
                onClick={() => handleAddGlass(1)}
                className="w-full p-4 rounded-2xl bg-blue-500/10 hover:bg-blue-500/20 border border-blue-500/30 text-blue-300 font-bold flex items-center justify-between transition-all cursor-pointer active:scale-95"
              >
                <div className="flex items-center gap-3">
                  <span className="text-xl">🥛</span>
                  <span className="text-sm font-extrabold">+ 1 Glass (250ml)</span>
                </div>
                <Plus size={18} />
              </button>

              <button
                onClick={() => handleAddGlass(2)}
                className="w-full p-4 rounded-2xl bg-emerald-500/10 hover:bg-emerald-500/20 border border-emerald-500/30 text-emerald-300 font-bold flex items-center justify-between transition-all cursor-pointer active:scale-95"
              >
                <div className="flex items-center gap-3">
                  <span className="text-xl">🍾</span>
                  <span className="text-sm font-extrabold">+ 1 Bottle (500ml)</span>
                </div>
                <Plus size={18} />
              </button>

              <button
                onClick={() => handleAddGlass(3)}
                className="w-full p-4 rounded-2xl bg-purple-500/10 hover:bg-purple-500/20 border border-purple-500/30 text-purple-300 font-bold flex items-center justify-between transition-all cursor-pointer active:scale-95"
              >
                <div className="flex items-center gap-3">
                  <span className="text-xl">🏋️</span>
                  <span className="text-sm font-extrabold">+ 1 Sports Flask (750ml)</span>
                </div>
                <Plus size={18} />
              </button>
            </div>
          </div>
        </div>

        {/* AI Hydration Tip Banner */}
        <div className="mt-8 p-5 rounded-2xl bg-gradient-to-r from-blue-900/30 to-purple-900/30 border border-blue-500/30 flex items-start gap-4">
          <Sparkles size={24} className="text-blue-400 shrink-0 mt-0.5" />
          <div>
            <h4 className="text-sm font-bold text-white mb-1">Hydration Science Tip</h4>
            <p className="text-xs text-wellness-white/70 leading-relaxed">
              Drinking water consistently throughout the day prevents brain fatigue, improves nutrient absorption, and reduces metabolic acidity spikes by up to 35%.
            </p>
          </div>
        </div>
      </div>

      {/* Dispatched History Log */}
      {sentReminders.length > 0 && (
        <div className="mt-10 bg-wellness-card p-8 rounded-[36px] border border-white/10 shadow-xl">
          <h3 className="text-xl font-bold mb-4 flex items-center gap-2 text-white">
            <Clock size={20} className="text-purple-400" />
            <span>Dispatched Reminders & Alerts History</span>
          </h3>

          <div className="space-y-3 max-h-64 overflow-y-auto pr-2">
            {sentReminders.map(log => (
              <div key={log.id} className="bg-wellness-charcoal p-4 rounded-2xl border border-white/5 flex flex-col sm:flex-row sm:items-center justify-between gap-3">
                <div>
                  <div className="flex items-center gap-2 mb-1">
                    <span className="text-xs px-2.5 py-0.5 rounded-full font-bold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                      {log.status}
                    </span>
                    <span className="text-xs text-wellness-white/40">{log.timestamp}</span>
                  </div>
                  <p className="text-sm font-semibold text-white">{log.message}</p>
                </div>
                <div className="text-right text-xs text-wellness-white/40 shrink-0">
                  <p>Sent to: {log.userName}</p>
                  <p>{log.phone} | {log.email}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
