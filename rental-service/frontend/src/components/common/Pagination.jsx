import { ChevronLeft, ChevronRight } from 'lucide-react'

export default function Pagination({ page, totalPages, onChange }) {
  if (totalPages <= 1) return null
  return <nav className="pagination" aria-label="Pagination"><button disabled={page === 1} onClick={() => onChange(page - 1)} aria-label="Previous page"><ChevronLeft size={16} /></button>{Array.from({ length: totalPages }, (_, index) => index + 1).map((number) => <button key={number} className={page === number ? 'active' : ''} onClick={() => onChange(number)}>{number}</button>)}<button disabled={page === totalPages} onClick={() => onChange(page + 1)} aria-label="Next page"><ChevronRight size={16} /></button></nav>
}
