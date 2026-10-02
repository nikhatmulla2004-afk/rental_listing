import { Compass, Camera, MessageCircle } from 'lucide-react'

export default function Footer() {
  return <footer className="footer"><div className="footer-brand"><span className="brand-mark"><Compass size={17} /></span><strong>havenly</strong><span>Thoughtful spaces, made simple.</span></div><div className="footer-meta"><span>© 2026 Havenly</span><span>Privacy</span><span>Terms</span><Camera size={16} /><MessageCircle size={16} /></div></footer>
}
