export function StationSchema({
  name,
  address,
  city,
  state,
  lat,
  lng,
  flag,
  reputation,
  price,
}: {
  name: string;
  address: string;
  city: string;
  state: string;
  lat: number;
  lng: number;
  flag?: string | null;
  reputation: number;
  price?: number;
}) {
  const schema = {
    "@context": "https://schema.org",
    "@type": "GasStation",
    name: flag ? `${name} (${flag})` : name,
    description: `Posto ${name} em ${city}, ${state}. Reputacao da comunidade: ${reputation.toFixed(1)}.`,
    address: {
      "@type": "PostalAddress",
      streetAddress: address,
      addressLocality: city,
      addressRegion: state,
      addressCountry: "BR",
    },
    geo: {
      "@type": "GeoCoordinates",
      latitude: lat,
      longitude: lng,
    },
    aggregateRating: {
      "@type": "AggregateRating",
      ratingValue: reputation.toFixed(1),
      bestRating: 5,
      reviewCount: 1,
    },
    ...(price && {
      makesOffer: {
        "@type": "Offer",
        price: price.toFixed(2),
        priceCurrency: "BRL",
      },
    }),
  };

  return (
    <script
      type="application/ld+json"
      dangerouslySetInnerHTML={{ __html: JSON.stringify(schema) }}
    />
  );
}
