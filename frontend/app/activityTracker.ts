"use client";

import { getApiBase } from "./api";

export interface DayActivity {
  day: string; // "Mon", "Tue", etc.
  dateStr: string; // YYYY-MM-DD
  activeSeconds: number;
  waterLoggedMl: number;
  symptomsCount: number;
  dietLoggedCount: number;
  adherencePct: number;
}

const DAYS_OF_WEEK = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

export function getTodayKey(): string {
  const now = new Date();
  return now.toISOString().split("T")[0];
}

export function formatTimeSpent(seconds: number): string {
  if (!seconds || seconds <= 0) return "0m";
  const hrs = Math.floor(seconds / 3600);
  const mins = Math.floor((seconds % 3600) / 60);
  const secs = seconds % 60;

  if (hrs > 0) {
    return mins > 0 ? `${hrs}h ${mins}m` : `${hrs}h`;
  }
  if (mins > 0) {
    return `${mins}m`;
  }
  return `${secs}s`;
}

export function loadLocalActivity(): Record<string, DayActivity> {
  if (typeof window === "undefined") return {};
  try {
    const raw = localStorage.getItem("symptomsync_realtime_activity_v2");
    if (raw) {
      return JSON.parse(raw);
    }
  } catch (e) {
    console.warn("Failed to load local activity", e);
  }
  return {};
}

export function saveLocalActivity(data: Record<string, DayActivity>): void {
  if (typeof window === "undefined") return;
  try {
    localStorage.setItem("symptomsync_realtime_activity_v2", JSON.stringify(data));
  } catch (e) {
    console.warn("Failed to save local activity", e);
  }
}

export function recordAppActiveTime(addSeconds: number): void {
  const data = loadLocalActivity();
  const todayKey = getTodayKey();
  const date = new Date();
  const dayName = DAYS_OF_WEEK[date.getDay()];

  if (!data[todayKey]) {
    data[todayKey] = {
      day: dayName,
      dateStr: todayKey,
      activeSeconds: 0,
      waterLoggedMl: 0,
      symptomsCount: 0,
      dietLoggedCount: 0,
      adherencePct: 0
    };
  }

  data[todayKey].activeSeconds += addSeconds;
  calculateAdherence(data[todayKey]);
  saveLocalActivity(data);

  // Sync to backend periodically
  if (data[todayKey].activeSeconds % 10 === 0) {
    syncActivityToBackend(data[todayKey]);
  }
}

export function recordWaterLogged(amountMl: number): void {
  const data = loadLocalActivity();
  const todayKey = getTodayKey();
  const date = new Date();
  const dayName = DAYS_OF_WEEK[date.getDay()];

  if (!data[todayKey]) {
    data[todayKey] = {
      day: dayName,
      dateStr: todayKey,
      activeSeconds: 0,
      waterLoggedMl: 0,
      symptomsCount: 0,
      dietLoggedCount: 0,
      adherencePct: 0
    };
  }

  data[todayKey].waterLoggedMl += amountMl;
  calculateAdherence(data[todayKey]);
  saveLocalActivity(data);
  syncActivityToBackend(data[todayKey]);
}

export function recordSymptomLogged(): void {
  const data = loadLocalActivity();
  const todayKey = getTodayKey();
  const date = new Date();
  const dayName = DAYS_OF_WEEK[date.getDay()];

  if (!data[todayKey]) {
    data[todayKey] = {
      day: dayName,
      dateStr: todayKey,
      activeSeconds: 0,
      waterLoggedMl: 0,
      symptomsCount: 0,
      dietLoggedCount: 0,
      adherencePct: 0
    };
  }

  data[todayKey].symptomsCount += 1;
  calculateAdherence(data[todayKey]);
  saveLocalActivity(data);
  syncActivityToBackend(data[todayKey]);
}

function calculateAdherence(day: DayActivity): void {
  // Score formula based on real usage:
  // Active Time: 60 points max (target 30 mins)
  // Water: 20 points max (target 2500ml)
  // Actions: 20 points max
  const timeMins = day.activeSeconds / 60;
  const timeScore = Math.min(60, (timeMins / 30) * 60);
  const waterScore = Math.min(20, (day.waterLoggedMl / 2500) * 20);
  const logScore = Math.min(20, (day.symptomsCount * 10) + (day.dietLoggedCount * 10));

  day.adherencePct = Math.min(100, Math.round(timeScore + waterScore + logScore));
}

async function syncActivityToBackend(dayData: DayActivity): Promise<void> {
  try {
    const apiBase = getApiBase();
    await fetch(`${apiBase}/activity/log`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        ...dayData,
        activeMinutes: Math.round((dayData.activeSeconds / 60) * 10) / 10
      })
    });
  } catch (e) {
    // Ignore backend sync error
  }
}

export interface DayReport {
  day: string;
  dateStr: string;
  activeSeconds: number;
  formattedTime: string;
  hoursFloat: number;
  pct: number;
  waterMl: number;
}

export function getWeeklyReport(): DayReport[] {
  const localData = loadLocalActivity();
  const days = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  
  // Calculate current week's Monday date
  const now = new Date();
  const currentDayOfWeek = now.getDay(); // 0 is Sun, 1 is Mon
  const distToMon = (currentDayOfWeek === 0 ? -6 : 1 - currentDayOfWeek);
  
  const monday = new Date(now);
  monday.setDate(now.getDate() + distToMon);

  return days.map((dayName, idx) => {
    const d = new Date(monday);
    d.setDate(monday.getDate() + idx);
    const key = d.toISOString().split("T")[0];

    const record = localData[key];
    if (record && record.activeSeconds > 0) {
      const hoursFloat = Math.round((record.activeSeconds / 3600) * 100) / 100;
      return {
        day: dayName,
        dateStr: key,
        activeSeconds: record.activeSeconds,
        formattedTime: formatTimeSpent(record.activeSeconds),
        hoursFloat: hoursFloat,
        pct: record.adherencePct || Math.min(100, Math.round((record.activeSeconds / 1800) * 100)),
        waterMl: record.waterLoggedMl || 0
      };
    }

    return {
      day: dayName,
      dateStr: key,
      activeSeconds: 0,
      formattedTime: "0m",
      hoursFloat: 0,
      pct: 0,
      waterMl: 0
    };
  });
}
