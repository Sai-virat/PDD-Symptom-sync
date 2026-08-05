"use client";

import { getApiBase } from "./api";

export interface RealtimeHistoryEntry {
  id: string;
  timestamp: string; // e.g. "Today at 2:35 PM"
  rawTime: number;
  type: "analysis" | "diet" | "water";
  title: string;
  symptoms: string[];
  severity: "High" | "Medium" | "Low";
  cause: string;
  details?: string;
}

export function loadHistoryLogs(): RealtimeHistoryEntry[] {
  if (typeof window === "undefined") return [];
  try {
    const raw = localStorage.getItem("symptomsync_realtime_history_v2");
    if (raw) {
      return JSON.parse(raw);
    }
  } catch (e) {
    console.warn("Failed to load history logs", e);
  }
  return [];
}

export function saveHistoryLogs(logs: RealtimeHistoryEntry[]): void {
  if (typeof window === "undefined") return;
  try {
    localStorage.setItem("symptomsync_realtime_history_v2", JSON.stringify(logs));
  } catch (e) {
    console.warn("Failed to save history logs", e);
  }
}

export function recordHistoryEntry(entry: Omit<RealtimeHistoryEntry, "id" | "timestamp" | "rawTime">): void {
  const existing = loadHistoryLogs();
  const now = new Date();
  
  const timeStr = now.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
  const dateStr = now.toLocaleDateString([], { month: "short", day: "numeric", year: "numeric" });
  const formattedTimestamp = `${dateStr} at ${timeStr}`;

  const newEntry: RealtimeHistoryEntry = {
    id: `log_${Date.now()}_${Math.random().toString(36).substr(2, 4)}`,
    timestamp: formattedTimestamp,
    rawTime: now.getTime(),
    ...entry
  };

  const updated = [newEntry, ...existing];
  saveHistoryLogs(updated);

  // Sync to backend API asynchronously
  syncHistoryToBackend(newEntry);
}

async function syncHistoryToBackend(entry: RealtimeHistoryEntry): Promise<void> {
  try {
    const apiBase = getApiBase();
    await fetch(`${apiBase}/history/log`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(entry)
    });
  } catch (e) {
    // Ignore backend offline
  }
}

export function clearAllHistory(): void {
  if (typeof window === "undefined") return;
  try {
    localStorage.removeItem("symptomsync_realtime_history_v2");
  } catch (e) {}
}
