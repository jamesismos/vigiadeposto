export function calculateFairPriceIndex(params: {
  userPrice: number;
  stateAverage: number;
  anpAverage: number;
  stationScore: number;
  complaintRate: number;
}) {
  const reference = (params.stateAverage * 0.6) + (params.anpAverage * 0.4);
  const priceDelta = ((params.userPrice - reference) / reference) * 100;
  const trustPenalty = Math.max(0, 4.5 - params.stationScore) * 6;
  const complaintPenalty = params.complaintRate * 24;
  const score = Math.max(0, Math.min(100, 78 - priceDelta - trustPenalty - complaintPenalty));

  if (priceDelta > 12 || complaintPenalty > 14) return { score, label: "suspeito" };
  if (priceDelta > 6) return { score, label: "acima da média" };
  if (priceDelta < -5) return { score, label: "abaixo da média" };
  return { score, label: "preço justo" };
}
