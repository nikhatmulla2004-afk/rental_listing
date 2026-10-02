import { ChevronRight, Home } from 'lucide-react'

export default function Breadcrumbs({ items = [] }) {
  return <nav className="breadcrumbs" aria-label="Breadcrumb"><button aria-label="Home"><Home size={14} /></button>{items.map((item, index) => <span key={item}><ChevronRight size={13} /><button className={index === items.length - 1 ? 'current' : ''}>{item}</button></span>)}</nav>
}
