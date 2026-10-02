const NOMINATIM_URL = 'https://nominatim.openstreetmap.org'
const OVERPASS_URL = 'https://overpass-api.de/api/interpreter'

export async function searchPlaces(query) {
  if (!query.trim()) return []
  const response = await fetch(`${NOMINATIM_URL}/search?format=jsonv2&addressdetails=1&limit=5&q=${encodeURIComponent(query)}`, { headers: { 'Accept-Language': 'en' } })
  if (!response.ok) throw new Error('Place search failed')
  return response.json()
}

export async function geocodePlace(query) {
  const results = await searchPlaces(query)
  return results[0] || null
}

export async function nearbyPlaces(latitude, longitude) {
  const query = `[out:json];(nwr[amenity](around:1400,${latitude},${longitude});nwr[shop](around:1400,${latitude},${longitude}););out center tags 12;`
  const response = await fetch(`${OVERPASS_URL}?data=${encodeURIComponent(query)}`)
  if (!response.ok) throw new Error('Nearby place search failed')
  const data = await response.json()
  return data.elements.filter((place) => place.tags?.name).slice(0, 8).map((place) => ({
    id: place.id,
    name: place.tags.name,
    type: place.tags.amenity || place.tags.shop || 'place'
  }))
}

export function mapEmbedUrl(place) {
  if (!place) return ''
  const latitude = Number(place.lat)
  const longitude = Number(place.lon)
  const delta = 0.025
  return `https://www.openstreetmap.org/export/embed.html?bbox=${longitude - delta}%2C${latitude - delta}%2C${longitude + delta}%2C${latitude + delta}&layer=mapnik&marker=${latitude}%2C${longitude}`
}
