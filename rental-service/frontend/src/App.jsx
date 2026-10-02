import { useEffect, useState } from 'react'
import Navbar from './components/common/Navbar'
import Footer from './components/common/Footer'
import HomeListingView from './views/HomeListingView'
import ListingDetailView from './views/ListingDetailView'
import CreateListingView from './views/CreateListingFormView'
import ProfileView from './views/ProfileView'
import SavedListingsView from './views/SavedListingsView'
import AuthModal from './components/common/AuthModal'
import { propertyApi } from './services/api'

const demoListings = [
  { id: 1, title: 'The Courtyard House', city: 'Pune', state: 'MH', type: 'Apartment', rent: 28500, bedrooms: 2, bathrooms: 2, description: 'A quiet, sun-washed home tucked into one of the city’s most walkable neighborhoods.' },
  { id: 2, title: 'A little house in the trees', city: 'Bengaluru', state: 'KA', type: 'House', rent: 42000, bedrooms: 3, bathrooms: 2, description: 'A leafy hideaway with generous rooms and a slow morning kind of light.' },
  { id: 3, title: 'The blue door studio', city: 'Mumbai', state: 'MH', type: 'Studio', rent: 31000, bedrooms: 1, bathrooms: 1, description: 'Compact, calm, and close to all the good things in Bandra.' },
  { id: 4, title: 'Sunset over the old city', city: 'Jaipur', state: 'RJ', type: 'Villa', rent: 56000, bedrooms: 3, bathrooms: 3, description: 'An airy villa where afternoon light moves across terracotta floors.' }
]

export default function App() {
  const [view, setView] = useState('home')
  const [listings, setListings] = useState([])
  const [selected, setSelected] = useState(null)
  const [loading, setLoading] = useState(true)
  const [toast, setToast] = useState('')
  const [filters, setFilters] = useState({ city: '', type: '', date: '', guests: '1 guest', maxPrice: 100000 })
  const [savedIds, setSavedIds] = useState(new Set())
  const [page, setPage] = useState(1)
  const [user, setUser] = useState(() => JSON.parse(localStorage.getItem('havenly-user') || 'null'))
  const [authOpen, setAuthOpen] = useState(false)
  const [pendingView, setPendingView] = useState(null)

  useEffect(() => {
    propertyApi.list().then((data) => setListings(data.length ? data : demoListings)).catch(() => { setListings(demoListings); setToast('Showing curated homes while the API is unavailable.') }).finally(() => setLoading(false))
  }, [])
  useEffect(() => { if (!toast) return undefined; const timer = setTimeout(() => setToast(''), 4500); return () => clearTimeout(timer) }, [toast])

  const visible = listings.filter((property) => (!filters.city || `${property.city} ${property.address}`.toLowerCase().includes(filters.city.toLowerCase())) && (!filters.type || property.type === filters.type) && Number(property.rent || 0) <= Number(filters.maxPrice || 100000))
  const navigate = (next) => { setView(next); window.scrollTo({ top: 0, behavior: 'smooth' }) }
  const searchHomes = () => { setPage(1); setToast(visible.length ? `${visible.length} homes found.` : 'No homes match those filters.') }
  const selectPlace = (place) => { const city = place.address?.city || place.address?.town || place.address?.municipality || place.display_name.split(',')[0]; setFilters((current) => ({ ...current, city })); setPage(1); navigate('home'); setToast(`Searching homes near ${city}.`) }
  const authenticate = (account) => { setUser(account); localStorage.setItem('havenly-user', JSON.stringify(account)); setToast(`Welcome to Havenly, ${account.name}.`); if (pendingView) { navigate(pendingView); setPendingView(null) } }
  const updateUser = (account) => { setUser(account); localStorage.setItem('havenly-user', JSON.stringify(account)); setToast('Your profile was updated.') }
  const signOut = () => { setUser(null); localStorage.removeItem('havenly-user'); setToast('You have been signed out.'); navigate('home') }
  const requestNavigation = (next) => { if ((next === 'profile' || next === 'create') && !user) { setPendingView(next); setAuthOpen(true); return } navigate(next) }
  const toggleSave = (property) => {
    setSavedIds((current) => {
      const next = new Set(current)
      if (next.has(property.id)) {
        next.delete(property.id)
        setToast('Removed from saved homes.')
      } else {
        next.add(property.id)
        setToast('Saved to your collection.')
      }
      return next
    })
  }
  const createListing = async (property) => { try { const created = await propertyApi.create(property); setListings([created, ...listings]); setToast('Your listing is live.'); navigate('home') } catch { setListings([{ ...property, id: Date.now() }, ...listings]); setToast('Saved locally. Connect the API to publish it remotely.'); navigate('home') } }
  const pageContent = view === 'detail' ? <ListingDetailView property={selected} onBack={() => navigate('home')} /> : view === 'create' ? <CreateListingView onBack={() => navigate('home')} onSave={createListing} /> : view === 'profile' ? <ProfileView user={user} onUpdate={updateUser} onSignOut={signOut} /> : view === 'saved' ? <SavedListingsView listings={listings.filter((property) => savedIds.has(property.id))} onSelect={(property) => { setSelected(property); navigate('detail') }} /> : <HomeListingView listings={visible} loading={loading} filters={filters} setFilters={setFilters} onSearch={searchHomes} page={page} onPageChange={setPage} onSelect={(property) => { setSelected(property); navigate('detail') }} savedIds={savedIds} onToggleSave={toggleSave} />
  return <><Navbar view={view} onNavigate={requestNavigation} user={user} onSignIn={() => setAuthOpen(true)} onPlaceSelect={selectPlace} />{pageContent}<Footer />{toast && <div className="toast">{toast}</div>}<AuthModal open={authOpen} onClose={() => { setAuthOpen(false); setPendingView(null) }} onAuthenticated={authenticate} /></>
}
