import { ArrowUpRight, Heart, MapPin, Star } from 'lucide-react'
import { propertyApi } from '../../services/api'

const images = ['https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?auto=format&fit=crop&w=900&q=85', 'https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea?auto=format&fit=crop&w=900&q=85', 'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=900&q=85', 'https://images.unsplash.com/photo-1600047509807-ba8f99d2cdde?auto=format&fit=crop&w=900&q=85']

export default function ListingCard({ property, index = 0, onSelect, isSaved = false, onToggleSave }) {
  const photoIds = property.photoIds || []
  const image = photoIds.length ? propertyApi.photoUrl(property.id, photoIds[0]) : images[index % images.length]
  const photoCount = photoIds.length || index % 3 + 3
  return <article className="listing-card" onClick={() => onSelect(property)}><div className="card-image"><img src={image} alt={property.title || 'Rental property'} /><span className="verified">Verified</span><button className={`heart ${isSaved ? 'saved' : ''}`} onClick={(event) => { event.stopPropagation(); onToggleSave?.(property) }} aria-label={isSaved ? 'Remove from saved listings' : 'Save listing'} aria-pressed={isSaved}><Heart size={17} fill={isSaved ? 'currentColor' : 'none'} /></button><span className="image-count">{photoCount} {photoCount === 1 ? 'photo' : 'photos'}</span></div><div className="card-body"><div className="card-title-row"><h3>{property.title || 'Light-filled city retreat'}</h3><ArrowUpRight size={18} /></div><p className="location"><MapPin size={14} /> {property.city || 'Pune'}, {property.state || 'India'}</p><div className="specs"><span>{property.bedrooms || 2} beds</span><span>{property.bathrooms || 2} baths</span><span>{property.type || 'Apartment'}</span></div><div className="card-footer"><strong>₹{Number(property.rent || 28500).toLocaleString('en-IN')}<small> / month</small></strong><span className="rating"><Star size={14} fill="currentColor" /> 4.9</span></div></div></article>
}
