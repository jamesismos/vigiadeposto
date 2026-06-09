import { MetadataRoute } from "next";

export default function sitemap(): MetadataRoute.Sitemap {
  const base = process.env.NEXT_PUBLIC_SITE_URL || "https://vigiadeposto.vercel.app";

  return [
    { url: base, lastModified: new Date(), changeFrequency: "daily", priority: 1 },
    { url: `${base}/admin`, lastModified: new Date(), changeFrequency: "weekly", priority: 0.5 },
  ].map((entry) => ({
    ...entry,
    changeFrequency: entry.changeFrequency as "daily" | "weekly",
  }));
}
