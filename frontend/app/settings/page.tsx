"use client";

import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { User, Bell, Sliders, Server, LogOut, Check, RefreshCw } from "lucide-react";
import { getApiBase } from "../api";

export default function SettingsPage() {
  const [userName, setUserName] = useState("User");
  const [userEmail, setUserEmail] = useState("user@example.com");
  const [userPhone, setUserPhone] = useState("6305473867");
  const [isEditingProfile, setIsEditingProfile] = useState(false);
  const [editName, setEditName] = useState("");
  const [editEmail, setEditEmail] = useState("");
  const [editPhone, setEditPhone] = useState("");
  const [smsApiKey, setSmsApiKey] = useState("");
  const [gmailAppPassword, setGmailAppPassword] = useState("");

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

  const saveGmailConfig = async (pass: string) => {
    try {
      const apiBase = getApiBase();
      await fetch(`${apiBase}/reminders/config_email`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          sender_email: userEmail,
          app_password: pass
        })
      });
    } catch (e) {}
  };

  const [dietaryOptions, setDietaryOptions] = useState({
    lowHistamine: true,
    glutenFree: true,
    highFiber: true,
    dairyFree: false,
    lowFodmap: false,
  });

  const [notificationsActive, setNotificationsActive] = useState(true);
  const [backendStatus, setBackendStatus] = useState("Checking backend connection...");
  const [isBackendHealthy, setIsBackendHealthy] = useState(false);
  const [pingLatency, setPingLatency] = useState<number | null>(null);
  const [testingPing, setTestingPing] = useState(false);

  useEffect(() => {
    try {
      const storedUser = localStorage.getItem("symptomsync_user");
      if (storedUser) {
        const u = JSON.parse(storedUser);
        if (u.name) {
          setUserName(u.name);
          setEditName(u.name);
        }
        if (u.email) {
          setUserEmail(u.email);
          setEditEmail(u.email);
        }
        if (u.phone) {
          setUserPhone(u.phone);
          setEditPhone(u.phone);
        } else {
          setUserPhone("6305473867");
          setEditPhone("6305473867");
        }
      }
    } catch (e) {}

    checkBackendHealth();
  }, []);

  const checkBackendHealth = async () => {
    setTestingPing(true);
    const start = Date.now();
    try {
      const apiBase = getApiBase();
      const res = await fetch(`${apiBase}/health`);
      const latency = Date.now() - start;
      setPingLatency(latency);
      if (res.ok) {
        const data = await res.json();
        setIsBackendHealthy(true);
        setBackendStatus(`Connected to FastAPI (Firebase ${data.firebase_connected ? "Active" : "Fallback Dataset Mode"}) - ${latency}ms`);
      } else {
        setIsBackendHealthy(false);
        setBackendStatus("Backend API returned non-200 status");
      }
    } catch (err) {
      setIsBackendHealthy(false);
      setBackendStatus("Backend API Offline");
    } finally {
      setTestingPing(false);
    }
  };

  const handleSaveProfile = (e: React.FormEvent) => {
    e.preventDefault();
    setUserName(editName);
    setUserEmail(editEmail);
    setUserPhone(editPhone);
    const updatedUser = { name: editName, email: editEmail, phone: editPhone };
    localStorage.setItem("symptomsync_user", JSON.stringify(updatedUser));
    localStorage.setItem("symptomsync_user_profile", JSON.stringify(updatedUser));
    setIsEditingProfile(false);
  };

  const handleSignOut = () => {
    localStorage.clear();
    sessionStorage.clear();
    window.location.href = "/login";
  };

  const toggleDietOption = (key: keyof typeof dietaryOptions) => {
    setDietaryOptions(prev => ({ ...prev, [key]: !prev[key] }));
  };

  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      <motion.header initial={{ opacity: 0, y: -10 }} animate={{ opacity: 1, y: 0 }} className="mb-10">
        <h1 className="text-4xl font-bold mb-4 flex items-center gap-3 text-white">
          <User className="text-healthGreen" size={36} />
          Settings & Profile
        </h1>
        <p className="text-wellness-white/60">Manage your health preferences, notifications, and backend sync status</p>
      </motion.header>

      <div className="space-y-6">
        {/* Profile Card */}
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="bg-wellness-card p-6 rounded-3xl border border-white/10 shadow-xl">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-2">
            <div className="flex items-start gap-3.5 min-w-0">
              <div className="bg-smoothPurple/20 p-3.5 rounded-2xl text-smoothPurple border border-smoothPurple/30 shrink-0">
                <User size={24} />
              </div>
              <div className="min-w-0 flex-1">
                <h3 className="text-xl font-bold text-white">Profile Details</h3>
                <p className="text-wellness-white/80 text-sm font-semibold truncate">{userName}</p>
                <p className="text-wellness-white/50 text-xs truncate">{userEmail}</p>
                <p className="text-emerald-400 text-xs font-bold mt-0.5">📱 SMS: {userPhone}</p>
              </div>
            </div>

            <button
              onClick={() => setIsEditingProfile(!isEditingProfile)}
              className="w-full sm:w-auto px-4 py-2.5 bg-white/10 hover:bg-white/20 text-white rounded-xl font-bold transition-all text-xs cursor-pointer active:scale-95 border border-white/10 text-center shrink-0"
            >
              {isEditingProfile ? "Cancel" : "Edit Profile"}
            </button>
          </div>

          {isEditingProfile && (
            <form onSubmit={handleSaveProfile} className="mt-4 pt-4 border-t border-white/10 space-y-4">
              <div>
                <label className="text-xs text-wellness-white/60 block mb-1">Full Name</label>
                <input
                  type="text"
                  value={editName}
                  onChange={e => setEditName(e.target.value)}
                  className="w-full bg-wellness-charcoal border border-white/10 rounded-xl p-3 text-white focus:outline-none focus:border-richOrange"
                />
              </div>
              <div>
                <label className="text-xs text-wellness-white/60 block mb-1">Email Address</label>
                <input
                  type="email"
                  value={editEmail}
                  onChange={e => setEditEmail(e.target.value)}
                  className="w-full bg-wellness-charcoal border border-white/10 rounded-xl p-3 text-white focus:outline-none focus:border-richOrange"
                />
              </div>
              <div>
                <label className="text-xs font-bold text-emerald-400 block mb-1">Mobile Phone Number (SMS Alerts)</label>
                <input
                  type="tel"
                  value={editPhone}
                  onChange={e => setEditPhone(e.target.value)}
                  className="w-full bg-wellness-charcoal border border-emerald-500/30 rounded-xl p-3 text-white focus:outline-none focus:border-emerald-400 text-sm font-semibold"
                  placeholder="6305473867"
                />
              </div>

              <button
                type="submit"
                className="bg-richOrange hover:bg-orange-600 text-white px-6 py-3 rounded-xl font-bold transition-all cursor-pointer shadow-lg shadow-orange-600/20"
              >
                Save Changes
              </button>
            </form>
          )}
        </motion.div>

        {/* Dietary Preferences Card */}
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.1 }} className="bg-wellness-card p-6 rounded-3xl border border-white/10 shadow-xl">
          <div className="flex items-center gap-4 mb-4">
            <div className="bg-richOrange/20 p-4 rounded-2xl text-richOrange border border-richOrange/30">
              <Sliders size={24} />
            </div>
            <div>
              <h3 className="text-xl font-bold text-white">Dietary Preferences</h3>
              <p className="text-wellness-white/60 text-sm">Select options to tailor your personalized meal plan</p>
            </div>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-3 gap-3 pt-2">
            {[
              { key: "lowHistamine", label: "Low Histamine" },
              { key: "glutenFree", label: "Gluten-Free" },
              { key: "highFiber", label: "High Fiber" },
              { key: "dairyFree", label: "Dairy-Free" },
              { key: "lowFodmap", label: "Low FODMAP" },
            ].map(item => {
              const active = dietaryOptions[item.key as keyof typeof dietaryOptions];
              return (
                <button
                  key={item.key}
                  onClick={() => toggleDietOption(item.key as keyof typeof dietaryOptions)}
                  className={`flex items-center justify-between p-3 rounded-2xl border transition-all cursor-pointer font-medium text-sm ${
                    active ? "bg-richOrange/20 border-richOrange text-white" : "bg-wellness-charcoal border-white/10 text-wellness-white/60 hover:border-white/20"
                  }`}
                >
                  <span>{item.label}</span>
                  {active && <Check size={16} className="text-richOrange" />}
                </button>
              );
            })}
          </div>
        </motion.div>

        {/* Notifications Card */}
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.2 }} className="bg-wellness-card p-6 rounded-3xl border border-white/10 shadow-xl flex items-center justify-between">
          <div className="flex items-center gap-4">
            <div className="bg-blue-500/20 p-4 rounded-2xl text-blue-400 border border-blue-500/30">
              <Bell size={24} />
            </div>
            <div>
              <h3 className="text-xl font-bold text-white">Notifications & Reminders</h3>
              <p className="text-wellness-white/60 text-sm">Water intake reminders and symptom tracking prompts</p>
            </div>
          </div>

          <button
            onClick={() => setNotificationsActive(!notificationsActive)}
            className={`w-14 h-8 flex items-center rounded-full p-1 transition-colors cursor-pointer ${
              notificationsActive ? "bg-healthGreen justify-end" : "bg-white/20 justify-start"
            }`}
          >
            <div className="w-6 h-6 rounded-full bg-white shadow-md" />
          </button>
        </motion.div>

        {/* Backend Connection Card */}
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.3 }} className="bg-wellness-card p-6 rounded-3xl border border-white/10 shadow-xl flex items-center justify-between">
          <div className="flex items-center gap-4">
            <div className={`p-4 rounded-2xl border ${isBackendHealthy ? "bg-emerald-500/20 text-emerald-400 border-emerald-500/30" : "bg-red-500/20 text-red-400 border-red-500/30"}`}>
              <Server size={24} />
            </div>
            <div>
              <h3 className="text-xl font-bold text-white">Backend Server Status</h3>
              <p className="text-wellness-white/60 text-sm">{backendStatus}</p>
            </div>
          </div>

          <button
            onClick={checkBackendHealth}
            disabled={testingPing}
            className="flex items-center gap-2 px-4 py-2.5 bg-white/10 hover:bg-white/20 text-white rounded-xl font-medium transition-all cursor-pointer text-sm"
          >
            <RefreshCw size={16} className={testingPing ? "animate-spin" : ""} />
            <span>Test Ping</span>
          </button>
        </motion.div>

        {/* Sign Out Card at Bottom of Page */}
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.4 }} className="pt-4">
          <button
            onClick={handleSignOut}
            className="w-full flex items-center justify-center gap-3 px-6 py-4 bg-red-500/15 hover:bg-red-500/25 text-red-400 border border-red-500/30 rounded-3xl font-extrabold transition-all active:scale-95 cursor-pointer shadow-xl shadow-red-500/10 text-base"
          >
            <LogOut size={22} />
            <span>Sign Out of SymptomSync</span>
          </button>
        </motion.div>
      </div>
    </div>
  );
}
