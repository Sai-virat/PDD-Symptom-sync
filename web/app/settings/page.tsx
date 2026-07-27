"use client";

import { motion } from "framer-motion";
import { User, Bell, Shield, Sliders } from "lucide-react";

export default function SettingsPage() {
  return (
    <div className="max-w-4xl mx-auto py-8">
      <motion.header initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="mb-10">
        <h1 className="text-4xl font-bold mb-4 flex items-center gap-3">
          <User className="text-healthGreen" size={36} />
          Settings & Profile
        </h1>
        <p className="text-wellness-white/60">Manage your health preferences, notifications, and profile details</p>
      </motion.header>

      <div className="space-y-6">
        <div className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex items-center gap-4">
          <div className="bg-smoothPurple/20 p-4 rounded-2xl text-smoothPurple">
            <User size={24} />
          </div>
          <div className="flex-1">
            <h3 className="text-xl font-bold">Profile Details</h3>
            <p className="text-wellness-white/60 text-sm">John Doe (john.doe@example.com)</p>
          </div>
        </div>

        <div className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex items-center gap-4">
          <div className="bg-richOrange/20 p-4 rounded-2xl text-richOrange">
            <Sliders size={24} />
          </div>
          <div className="flex-1">
            <h3 className="text-xl font-bold">Dietary Preferences</h3>
            <p className="text-wellness-white/60 text-sm">Low Histamine, Gluten-Free, High Fiber</p>
          </div>
        </div>

        <div className="bg-wellness-card p-6 rounded-3xl border border-white/5 flex items-center gap-4">
          <div className="bg-blue-500/20 p-4 rounded-2xl text-blue-400">
            <Bell size={24} />
          </div>
          <div className="flex-1">
            <h3 className="text-xl font-bold">Notifications</h3>
            <p className="text-wellness-white/60 text-sm">Water intake reminders active</p>
          </div>
        </div>
      </div>
    </div>
  );
}
