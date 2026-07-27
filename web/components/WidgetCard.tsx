"use client";

import { LucideIcon } from "lucide-react";
import { motion } from "framer-motion";
import Link from "next/link";

interface WidgetCardProps {
  title: string;
  icon: LucideIcon;
  href: string;
  color?: string;
}

export default function WidgetCard({ title, icon: Icon, href, color = "text-richOrange" }: WidgetCardProps) {
  return (
    <Link href={href}>
      <motion.div
        whileHover={{ scale: 1.03, y: -4 }}
        whileTap={{ scale: 0.98 }}
        className="glass-panel glass-card-hover p-6 rounded-3xl flex flex-col items-center justify-center aspect-square gap-4 cursor-pointer group"
      >
        <div className={`p-5 rounded-2xl bg-white/5 group-hover:bg-white/10 group-hover:scale-110 transition-all ${color}`}>
          <Icon size={36} />
        </div>
        <span className="font-bold text-lg text-wellness-white group-hover:text-white transition-colors">{title}</span>
      </motion.div>
    </Link>
  );
}
