import { Bookmark, Heart } from 'lucide-react'
import ListingGrid from '../components/listings/ListingGrid'

export default function SavedListingsView({ listings, onSelect }) {
  return <main className="saved-page"><div className="saved-heading"><div><span className="eyebrow"><Bookmark size={14} /> Your collection</span><h1>Saved homes.</h1><p>Keep the places that feel like a possibility close by.</p></div><span className="saved-count">{listings.length} {listings.length === 1 ? 'home' : 'homes'}</span></div>{listings.length ? <ListingGrid listings={listings} onSelect={onSelect} /> : <div className="empty-state saved-empty"><Heart size={28} /><h3>Your saved homes will appear here</h3><p>Tap the heart on any listing you want to come back to.</p></div>}</main>
}
