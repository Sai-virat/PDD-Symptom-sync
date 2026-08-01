import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Sidebar from "@/components/Sidebar";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "SymptomSync | AI Diet Planner",
  description: "Personalized nutrition based on your symptoms",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <div className="bg-ambient-mesh">
          <div className="ambient-orb-1" />
          <div className="ambient-orb-2" />
          <div className="ambient-orb-3" />
        </div>
        <div className="relative z-10 flex min-h-screen text-wellness-white">
          <Sidebar />
          <main className="flex-1 p-4 md:p-8 overflow-y-auto">
            {children}
          </main>
        </div>
      </body>
    </html>
  );
}
