"use client";

import { useEffect } from "react";
import { recordAppActiveTime } from "@/app/activityTracker";

export default function GlobalTracker() {
  useEffect(() => {
    // Record initial visit immediately
    recordAppActiveTime(1);

    // Track active screen time every second when tab is visible
    const timer = setInterval(() => {
      if (typeof document !== "undefined" && !document.hidden) {
        recordAppActiveTime(1);
      }
    }, 1000);

    return () => clearInterval(timer);
  }, []);

  return null;
}
