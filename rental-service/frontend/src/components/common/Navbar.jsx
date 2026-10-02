import { Compass, Heart, LoaderCircle, MapPin, Menu, Plus, Search, UserRound, X } from 'lucide-react'
import { useEffect, useState } from 'react'
import Button from './Button'
import { searchPlaces } from '../../services/places'

export default function Navbar({ view, onNavigate, user, onSignIn, onPlaceSelect }) {
  const [query, setQuery] = useState('')
  const [suggestions, setSuggestions] = useState([])
  const [searching, setSearching] = useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  useEffect(() => {
    if (query.trim().length < 3) { setSuggestions([]); return undefined }
    const timer = setTimeout(() => { setSearching(true); searchPlaces(query).then(setSuggestions).catch(() => setSuggestions([])).finally(() => setSearching(false)) }, 350)
    return () => clearTimeout(timer)
  }, [query])
  const choosePlace = (place) => { setQuery(place.display_name.split(',').slice(0, 2).join(',')); setSuggestions([]); onPlaceSelect?.(place) }
  const navigateFromMenu = (destination) => { setMobileMenuOpen(false); onNavigate(destination) }
  return (
    <header className="navbar">
      <div className="nav-inner">
        <button className="brand" onClick={() => onNavigate('home')} aria-label="Go home">
          <span className="brand-mark"><Compass size={19} /></span>
          <span>havenly</span>
        </button>
        <div className="nav-search-wrap"><div className="nav-search"><Search size={16} /><input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Search by city, neighborhood..." /><span>{searching ? <LoaderCircle className="spin" size={15} /> : <kbd>⌘ K</kbd>}</span></div>{suggestions.length > 0 && <div className="place-suggestions">{suggestions.map((place) => <button key={place.place_id} onClick={() => choosePlace(place)}><MapPin size={15} /><span><strong>{place.display_name.split(',')[0]}</strong><small>{place.display_name.split(',').slice(1, 3).join(',')}</small></span></button>)}</div>}</div>
        <nav className="nav-links">
          <button className={view === 'home' ? 'active' : ''} onClick={() => onNavigate('home')}>Explore</button>
          <button onClick={() => onNavigate('saved')}><Heart size={16} /> Saved</button>
          <button onClick={() => onNavigate('profile')}><UserRound size={16} /> My trips</button>
        </nav>
        {user ? <button className="user-chip" onClick={() => onNavigate('profile')}><span>{user.name.slice(0, 1).toUpperCase()}</span><strong>{user.name}</strong></button> : <button className="signin-button" onClick={onSignIn}><UserRound size={16} /> Sign in</button>}
        <Button variant="dark" icon={Plus} onClick={() => onNavigate('create')}>List your place</Button>
        <button className="mobile-menu" aria-label={mobileMenuOpen ? 'Close menu' : 'Open menu'} aria-expanded={mobileMenuOpen} aria-controls="mobile-navigation" onClick={() => setMobileMenuOpen((open) => !open)}>{mobileMenuOpen ? <X size={20} /> : <Menu size={20} />}</button>
      </div>
      <nav className={`mobile-nav${mobileMenuOpen ? ' open' : ''}`} id="mobile-navigation" aria-label="Mobile navigation">
        <button onClick={() => navigateFromMenu('home')}>Explore</button>
        <button onClick={() => navigateFromMenu('saved')}><Heart size={16} /> Saved homes</button>
        <button onClick={() => navigateFromMenu('profile')}><UserRound size={16} /> My trips</button>
        {!user && <button onClick={() => { setMobileMenuOpen(false); onSignIn() }}><UserRound size={16} /> Sign in</button>}
        <Button variant="dark" icon={Plus} onClick={() => navigateFromMenu('create')}>List your place</Button>
      </nav>
    </header>
  )
}
