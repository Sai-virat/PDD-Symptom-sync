"use client";

import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { User, Bell, Sliders, Server } from "lucide-react";

const API_BASE = "http://localhost:8000";

export default function SettingsPage() {
  const [userName, setUserName] = useState("John Doe");
  const [userEmail, setUserEmail] = useState("john.doe@example.com");
  const [backendStatus, setBackendStatus] = useState("Checking backend connection...");
  const [isBackendHealthy, setIsBackendHealthy] = useState(false);

  useEffect(() => {
    try {
      const storedUser = localStorage.getItem("symptomsync_user");
      if (storedUser) {
        const u = JSON.parse(storedUser);
        if (u.name) setUserName(u.name);
        if (u.email) setUserEmail(u.email);
      }
    } catch (e) {}

    fetch(`${API_BASE}/health`)
      .then(res => res.json())
      .then(data => {
        if (data.status === "healthy") {
          setIsBackendHealthy(true);
          setBackendStatus(`Connected to FastAPI (Firebase ${data.firebase_connected ? "Active" : "Fallback Dataset Mode"})`);
        }
      })
      .catch(() => {
        setIsBackendHealthy(false);
        setBackendStatus("Backend API Offline");
      });
  }, []);

  return (
    <div className="max-w-4xl mx-auto py-8">
      <motion.header initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="mb-10">
        <h1 className="text-4xl font-bold mb-4 flex items-center gap-3 text-white">
          <User className="text-healthGreen" size={36} />
          Settings & Profile
        </h1>
        <p className="text-wellness-white/60">Manage your health preferences, notifications, and backend sync status</p>
      </motion.header>

      <div className="space-y-6">
        <div className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex items-center gap-4">
          <div className="bg-smoothPurple/20 p-4 rounded-2xl text-smoothPurple border border-smoothPurple/30">
            <User size={24} />
          </div>
          <div className="flex-1">
            <h3 className="text-xl font-bold text-white">Profile Details</h3>
            <p className="text-wellness-white/60 text-sm">{userName} ({userEmail})</p>
          </div>
        </div>

        <div className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex items-center gap-4">
          <div className="bg-richOrange/20 p-4 rounded-2xl text-richOrange border border-richOrange/30">
            <Sliders size={24} />
          </div>
          <div className="flex-1">
            <h3 className="text-xl font-bold text-white">Dietary Preferences</h3>
            <p className="text-wellness-white/60 text-sm">Low Histamine, Gluten-Free, High Fiber</p>
          </div>
        </div>

        <div className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex items-center gap-4">
          <div className="bg-blue-500/20 p-4 rounded-2xl text-blue-400 border border-blue-500/30">
            <Bell size={24} />
          </div>
          <div className="flex-1">
            <h3 className="text-xl font-bold text-white">Notifications</h3>
            <p className="text-wellness-white/60 text-sm">Smart water intake reminders active</p>
          </div>
        </div>

        <div className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex items-center gap-4">
          <div className={`p-4 rounded-2xl border ${isBackendHealthy ? "bg-emerald-500/20 text-emerald-400 border-emerald-500/30" : "bg-red-500/20 text-red-400 border-red-500/30"}`}>
            <Server size={24} />
          </div>
          <div className="flex-1">
            <h3 className="text-xl font-bold text-white">Backend Connection</h3>
            <p className="text-wellness-white/60 text-sm">{backendStatus}</p>
          </div>
        </div>
      </div>
    </div>
  );
}
