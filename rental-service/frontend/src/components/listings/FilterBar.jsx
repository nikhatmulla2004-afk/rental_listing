import { CalendarDays, ChevronDown, MapPin, SlidersHorizontal, Users } from 'lucide-react'
import Button from '../common/Button'

export default function FilterBar({ filters, setFilters, onSearch }) {
  const update = (key) => (event) => setFilters({ ...filters, [key]: event.target.value })
  return <section className="filter-panel">
    <div className="filter-heading"><div><span className="eyebrow">Find your next address</span><h1>Spaces with a little more soul.</h1></div><button className="filter-more"><SlidersHorizontal size={16} /> All filters</button></div>
    <div className="filter-fields">
      <label className="filter-control wide"><MapPin size={17} /><span><small>Where</small><input value={filters.city} onChange={update('city')} placeholder="City or neighborhood" /></span></label>
      <label className="filter-control"><CalendarDays size={17} /><span><small>Move-in</small><input type="date" value={filters.date} onChange={update('date')} /></span></label>
      <label className="filter-control"><Users size={17} /><span><small>Guests</small><select value={filters.guests} onChange={update('guests')}><option>1 guest</option><option>2 guests</option><option>3+ guests</option></select></span><ChevronDown size={15} /></label>
      <label className="filter-control price-control"><span><small>Max monthly rent</small><input type="range" min="10000" max="100000" step="5000" value={filters.maxPrice || 100000} onChange={update('maxPrice')} /><strong>₹{Number(filters.maxPrice || 100000).toLocaleString('en-IN')}</strong></span></label>
      <Button onClick={onSearch}>Search homes</Button>
    </div>
    <div className="filter-row"><span className="filter-label">Popular near you</span>{['Apartment', 'Villa', 'Studio', 'House'].map((type) => <button key={type} className={filters.type === type ? 'chip selected' : 'chip'} onClick={() => setFilters({ ...filters, type: filters.type === type ? '' : type })}>{type}</button>)}<span className="result-count">{filters.city || filters.type ? 'Filtered results' : 'Curated for you'}</span></div>
  </section>
}
