import { ChevronRight, Sparkles } from 'lucide-react'
import FilterBar from '../components/listings/FilterBar'
import ListingGrid from '../components/listings/ListingGrid'
import Breadcrumbs from '../components/common/Breadcrumbs'
import Pagination from '../components/common/Pagination'

export default function HomeListingView({ listings, loading, filters, setFilters, onSelect, onSearch, savedIds, onToggleSave, page, onPageChange }) {
  const pageSize = 2
  const pagedListings = listings.slice((page - 1) * pageSize, page * pageSize)
  return <main><FilterBar filters={filters} setFilters={setFilters} onSearch={onSearch} /><section className="content-section"><Breadcrumbs items={['Explore homes']} /><div className="section-heading"><div><span className="eyebrow"><Sparkles size={14} /> Handpicked homes</span><h2>Places worth staying for.</h2></div><button className="text-button">See all <ChevronRight size={16} /></button></div><ListingGrid listings={pagedListings} loading={loading} onSelect={onSelect} savedIds={savedIds} onToggleSave={onToggleSave} /><Pagination page={page} totalPages={Math.max(1, Math.ceil(listings.length / pageSize))} onChange={onPageChange} /></section><section className="host-banner"><div><span className="eyebrow">Have a space to share?</span><h2>Good homes deserve good tenants.</h2><p>List your property with Havenly and meet people who care about where they live.</p></div><button className="button button-light">Become a host <ChevronRight size={16} /></button></section></main>
}
