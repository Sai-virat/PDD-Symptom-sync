"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { LayoutDashboard, Activity, Utensils, Droplets, History, User, LogOut } from "lucide-react";
import { clsx } from "clsx";

const navItems = [
  { name: "Dashboard", href: "/", icon: LayoutDashboard },
  { name: "Analyze", href: "/analyze", icon: Activity },
  { name: "Diet Plan", href: "/diet", icon: Utensils },
  { name: "Water Tracker", href: "/water", icon: Droplets },
  { name: "History", href: "/history", icon: History },
  { name: "Settings", href: "/settings", icon: User },
];

export default function Sidebar() {
  const pathname = usePathname();

  if (pathname === "/login") return null;

  return (
    <aside className="hidden md:flex flex-col w-64 glass-panel border-r border-white/10 z-20">
      <div className="p-6">
        <h1 className="text-3xl font-extrabold text-gradient-orange tracking-tight">SymptomSync</h1>
      </div>

      <nav className="flex-1 px-4 space-y-2">
        {navItems.map((item) => (
          <Link
            key={item.name}
            href={item.href}
            className={clsx(
              "flex items-center gap-3 px-4 py-3.5 rounded-2xl font-bold transition-all",
              pathname === item.href
                ? "btn-glow-purple text-white"
                : "text-wellness-white/60 hover:bg-white/5 hover:text-white"
            )}
          >
            <item.icon size={20} />
            <span className="font-semibold text-base">{item.name}</span>
          </Link>
        ))}
      </nav>

      <div className="p-4 border-t border-white/5">
        <button className="flex items-center gap-3 px-4 py-3 w-full text-wellness-white/60 hover:text-white transition-colors">
          <LogOut size={20} />
          <span className="font-medium">Sign Out</span>
        </button>
      </div>
    </aside>
  );
}
