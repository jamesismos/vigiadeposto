const CACHE_NAME = "vigia-de-posto-v2";
const OSM_CACHE = "osm-tiles-v1";
const APP_SHELL = ["/", "/offline", "/manifest.json", "/icon-192.svg", "/icon-512.svg"];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(APP_SHELL))
  );
  self.skipWaiting();
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(keys.filter((key) => key !== CACHE_NAME && key !== OSM_CACHE).map((key) => caches.delete(key)))
    )
  );
  self.clients.claim();
});

self.addEventListener("fetch", (event) => {
  const request = event.request;
  if (request.method !== "GET") return;
  const url = new URL(request.url);

  if (url.hostname.includes("tile.openstreetmap.org")) {
    event.respondWith(
      caches.open(OSM_CACHE).then((cache) =>
        cache.match(request).then((cached) => {
          const fetchPromise = fetch(request).then((response) => {
            if (response.ok) {
              cache.put(request, response.clone());
            }
            return response;
          });
          return cached || fetchPromise;
        })
      )
    );
    return;
  }

  if (url.pathname.startsWith("/rest/v1/")) {
    event.respondWith(
      fetch(request)
        .then((response) => {
          const copy = response.clone();
          caches.open(CACHE_NAME).then((cache) =>
            cache.put(request, copy).catch(() => {})
          );
          return response;
        })
        .catch(() => caches.match(request) || Response.error())
    );
    return;
  }

  event.respondWith(
    fetch(request)
      .then((response) => {
        const copy = response.clone();
        caches.open(CACHE_NAME).then((cache) => cache.put(request, copy));
        return response;
      })
      .catch(async () => {
        const cached = await caches.match(request);
        if (cached) return cached;
        if (request.mode === "navigate") return caches.match("/offline");
        return Response.error();
      })
  );
});
