"use client";

import { useEffect, useMemo, useState } from "react";
import { Download, Share2, Smartphone } from "lucide-react";

type InstallPromptEvent = Event & {
  prompt: () => Promise<void>;
  userChoice: Promise<{ outcome: "accepted" | "dismissed" }>;
};

export function PwaInstaller() {
  const [prompt, setPrompt] = useState<InstallPromptEvent | null>(null);
  const [isIos, setIsIos] = useState(false);
  const [installed, setInstalled] = useState(false);

  useEffect(() => {
    const standalone = window.matchMedia("(display-mode: standalone)").matches;
    setInstalled(standalone);
    setIsIos(/iphone|ipad|ipod/i.test(window.navigator.userAgent));

    const onBeforeInstall = (event: Event) => {
      event.preventDefault();
      setPrompt(event as InstallPromptEvent);
    };

    window.addEventListener("beforeinstallprompt", onBeforeInstall);
    window.addEventListener("appinstalled", () => setInstalled(true));
    return () => window.removeEventListener("beforeinstallprompt", onBeforeInstall);
  }, []);

  const label = useMemo(() => {
    if (installed) return "App instalado";
    if (isIos) return "Instalar no iPhone";
    return "Instalar App";
  }, [installed, isIos]);

  async function install() {
    if (!prompt) return;
    await prompt.prompt();
    const choice = await prompt.userChoice;
    if (choice.outcome === "accepted") setInstalled(true);
    setPrompt(null);
  }

  return (
    <div className="rounded-lg border border-white/15 bg-white/10 p-3 text-white shadow-soft backdrop-blur">
      <button
        className="flex w-full items-center justify-center gap-2 rounded-md bg-limefuel px-4 py-3 text-sm font-bold text-ink transition hover:bg-[#bada65] disabled:cursor-default disabled:opacity-70"
        disabled={installed || (!prompt && !isIos)}
        onClick={install}
      >
        {isIos ? <Share2 size={18} /> : <Download size={18} />}
        {label}
      </button>
      {isIos ? (
        <p className="mt-3 flex gap-2 text-xs leading-relaxed text-white/80">
          <Smartphone className="mt-0.5 h-4 w-4 shrink-0" />
          No Safari: Compartilhar → Adicionar à Tela Inicial.
        </p>
      ) : (
        <p className="mt-3 text-xs leading-relaxed text-white/75">
          Funciona no Android, desktop e navegador com cache parcial offline.
        </p>
      )}
    </div>
  );
}
