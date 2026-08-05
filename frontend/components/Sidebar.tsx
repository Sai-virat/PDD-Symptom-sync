"use client";

import { usePathname } from "next/navigation";
import { LayoutDashboard, Activity, Utensils, Droplets, History, User, MessageSquareHeart, LogOut } from "lucide-react";
import { clsx } from "clsx";

const navItems = [
  { name: "Dashboard", href: "/", icon: LayoutDashboard },
  { name: "Analyze", href: "/analyze", icon: Activity },
  { name: "Diet Plan", href: "/diet", icon: Utensils },
  { name: "Water Tracker", href: "/water", icon: Droplets },
  { name: "History", href: "/history", icon: History },
  { name: "Settings", href: "/settings", icon: User },
  { name: "Feedback", href: "/feedback", icon: MessageSquareHeart },
];

const mobileNavItems = [
  { name: "Dashboard", href: "/", icon: LayoutDashboard },
  { name: "Analyze", href: "/analyze", icon: Activity },
  { name: "Water", href: "/water", icon: Droplets },
  { name: "Diet", href: "/diet", icon: Utensils },
  { name: "History", href: "/history", icon: History },
  { name: "Settings", href: "/settings", icon: User },
];

export default function Sidebar() {
  const pathname = usePathname();

  if (pathname === "/login" || pathname === "/login.html") return null;

  const navigateTo = (href: string) => {
    window.location.href = href;
  };

  const handleSignOut = () => {
    localStorage.clear();
    sessionStorage.clear();
    window.location.href = "/login";
  };

  return (
    <>
      {/* Desktop Sidebar Navigation */}
      <aside className="hidden md:flex flex-col w-64 glass-panel border-r border-white/10 z-20 min-h-screen shrink-0">
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
            const isActive = pathname === item.href || (item.href !== "/" && pathname.startsWith(item.href));
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

      {/* Mobile Top Header Navigation Bar (Pushed down for Android Status Bar) */}
      <div className="md:hidden fixed top-0 left-0 right-0 pt-7 pb-2.5 px-4 bg-slate-950/95 backdrop-blur-2xl border-b border-white/10 flex items-center justify-between z-40 shadow-2xl">
        <h1 
          onClick={() => navigateTo("/")}
          className="text-lg font-extrabold text-gradient-orange tracking-tight cursor-pointer"
        >
          SymptomSync
        </h1>
        <div className="flex items-center gap-2">
          <button
            onClick={() => navigateTo("/settings")}
            className="p-2 rounded-xl bg-white/10 text-wellness-white hover:text-white border border-white/10 active:scale-95 flex items-center gap-1.5 text-xs font-bold"
            title="Settings"
          >
            <User size={16} />
            <span className="text-[11px]">Settings</span>
          </button>
          <button
            onClick={() => navigateTo("/feedback")}
            className="p-2 rounded-xl bg-purple-500/20 text-purple-300 border border-purple-500/30 active:scale-95 flex items-center gap-1.5 text-xs font-bold"
            title="Feedback"
          >
            <MessageSquareHeart size={16} />
          </button>
        </div>
      </div>

      {/* Mobile Bottom Dock Navigation Bar */}
      <div className="md:hidden fixed bottom-0 left-0 right-0 bg-slate-950/95 backdrop-blur-2xl border-t border-white/10 py-1.5 px-1 flex items-center justify-around z-40 shadow-2xl">
        {mobileNavItems.map((item) => {
          const isActive = pathname === item.href || (item.href !== "/" && pathname.startsWith(item.href));
          return (
            <button
              key={item.name}
              onClick={() => navigateTo(item.href)}
              className={clsx(
                "flex flex-col items-center gap-0.5 px-2 py-1 rounded-xl font-bold transition-all active:scale-95 cursor-pointer",
                isActive
                  ? "text-richOrange bg-richOrange/10 border border-richOrange/20"
                  : "text-wellness-white/60 hover:text-white"
              )}
            >
              <item.icon size={18} />
              <span className="text-[9px] tracking-tight">{item.name}</span>
            </button>
          );
        })}
      </div>
    </>
  );
}
