"use client";

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

  if (pathname === "/login" || pathname === "/login.html") return null;

  const navigateTo = (href: string) => {
    if (href === "/") {
      window.location.href = "/";
    } else {
      window.location.href = `${href}.html`;
    }
  };

  const handleSignOut = () => {
    localStorage.clear();
    sessionStorage.clear();
    window.location.href = "/login.html";
  };

  return (
    <aside className="hidden md:flex flex-col w-64 glass-panel border-r border-white/10 z-20 min-h-screen">
      <div className="p-6">
        <h1 
          onClick={() => navigateTo("/")}
          className="text-3xl font-extrabold text-gradient-orange tracking-tight cursor-pointer"
        >
          SymptomSync
        </h1>
      </div>

      <nav className="flex-1 px-4 space-y-2">
        {navItems.map((item) => {
          const isActive = pathname === item.href || pathname === `${item.href}.html`;
          return (
            <button
              key={item.name}
              onClick={() => navigateTo(item.href)}
              className={clsx(
                "flex items-center gap-3 px-4 py-3.5 w-full text-left rounded-2xl font-bold transition-all cursor-pointer",
                isActive
                  ? "btn-glow-purple text-white shadow-lg"
                  : "text-wellness-white/60 hover:bg-white/5 hover:text-white"
              )}
            >
              <item.icon size={20} />
              <span className="font-semibold text-base">{item.name}</span>
            </button>
          );
        })}
      </nav>

      <div className="p-4 border-t border-white/5">
        <button
          type="button"
          onClick={handleSignOut}
          className="flex items-center gap-3 px-4 py-3.5 w-full text-wellness-white/70 hover:text-red-400 bg-red-500/10 hover:bg-red-500/20 border border-red-500/20 rounded-2xl transition-all cursor-pointer font-bold shadow-md"
        >
          <LogOut size={20} className="text-red-400" />
          <span className="font-semibold text-base text-red-400">Sign Out</span>
        </button>
      </div>
    </aside>
  );
}
