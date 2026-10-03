import { ArrowLeft, Heart, MapPin, Share2, Sparkles } from 'lucide-react'
import { useEffect, useState } from 'react'
import Button from '../components/common/Button'
import ImageGallery from '../components/listings/ImageGallery'
import PricingCard from '../components/booking/PricingCard'
import BookingForm from '../components/booking/BookingForm'
import { geocodePlace, mapEmbedUrl, nearbyPlaces } from '../services/places'

export default function ListingDetailView({ property, onBack }) {
  const home = property || { title: 'The Courtyard House', city: 'Pune', state: 'MH', type: 'Apartment', bedrooms: 2, bathrooms: 2, rent: 28500, description: 'A quiet, sun-washed home tucked into one of the city’s most walkable neighborhoods.' }
  const [place, setPlace] = useState(null)
  const [nearby, setNearby] = useState([])
  useEffect(() => { let active = true; geocodePlace(`${home.address || ''} ${home.city || ''} ${home.state || ''}`.trim()).then((result) => { if (!active) return; setPlace(result); if (result) nearbyPlaces(result.lat, result.lon).then(setNearby).catch(() => {}) }).catch(() => {}); return () => { active = false } }, [home.address, home.city, home.state])
  return <main className="detail-page"><button className="back-button" onClick={onBack}><ArrowLeft size={16} /> Back to explore</button><ImageGallery property={home} /><div className="detail-layout"><div className="detail-copy"><div className="detail-actions"><span className="verified large">Verified home</span><div><button className="icon-action"><Share2 size={17} /></button><button className="icon-action"><Heart size={17} /></button></div></div><h1>{home.title}</h1><p className="detail-location"><MapPin size={16} /> {home.city}, {home.state || 'India'} · {home.type || 'Apartment'}</p><div className="detail-specs"><div><b>{home.bedrooms || 2}</b><span>Bedrooms</span></div><div><b>{home.bathrooms || 2}</b><span>Bathrooms</span></div><div><b>86 m²</b><span>Living area</span></div></div><div className="detail-description"><span className="eyebrow"><Sparkles size={14} /> A considered stay</span><p>{home.description || 'A bright, considered home with room to settle in and make your own.'}</p></div><div className="map-placeholder">{place ? <iframe title={`Map of ${home.city}`} src={mapEmbedUrl(place)} loading="lazy" /> : <div className="map-loading"><MapPin size={22} /><span>Finding this home on the map...</span></div>}</div>{nearby.length > 0 && <div className="nearby-section"><div><span className="eyebrow">Around the home</span><h2>Nearby essentials</h2></div><div className="nearby-list">{nearby.map((item) => <span key={item.id}><MapPin size={13} /> {item.name}</span>)}</div></div>}<BookingForm property={home} /></div><div className="sticky-widget"><PricingCard property={home} onBook={() => document.querySelector('.inquiry-form')?.scrollIntoView({ behavior: 'smooth' })} /></div></div></main>
}
