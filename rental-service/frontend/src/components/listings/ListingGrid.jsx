import ListingCard from './ListingCard'

export default function ListingGrid({ listings, loading, onSelect, savedIds, onToggleSave }) {
  if (loading) return <div className="listing-grid">{[1, 2, 3, 4].map((item) => <div className="skeleton-card" key={item}><div className="skeleton-image" /><div className="skeleton-line long" /><div className="skeleton-line" /><div className="skeleton-line short" /></div>)}</div>
  if (!listings.length) return <div className="empty-state"><span>⌂</span><h3>No listings match your criteria</h3><p>Try widening your search or exploring another neighborhood.</p></div>
  return <div className="listing-grid">{listings.map((property, index) => <ListingCard key={property.id || index} property={property} index={index} onSelect={onSelect} isSaved={savedIds?.has(property.id)} onToggleSave={onToggleSave} />)}</div>
}
