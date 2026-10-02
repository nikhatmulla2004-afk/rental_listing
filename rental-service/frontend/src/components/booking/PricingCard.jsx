import { CalendarDays, Check, ShieldCheck } from 'lucide-react'
import Button from '../common/Button'

export default function PricingCard({ property, onBook }) {
  const rent = Number(property?.rent || 28500)
  const serviceFee = Math.round(rent * 0.08)
  return <aside className="pricing-card"><div className="price-head"><div><strong>₹{rent.toLocaleString('en-IN')}</strong><span>/ month</span></div><span className="rating-pill">★ 4.9</span></div><div className="booking-fields"><label><small>MOVE-IN</small><span><CalendarDays size={15} /> 01 Oct 2026</span></label><label><small>LEASE LENGTH</small><select><option>12 months</option><option>6 months</option><option>3 months</option></select></label></div><div className="price-lines"><span>Monthly rent <b>₹{rent.toLocaleString('en-IN')}</b></span><span>Service fee <b>₹{serviceFee.toLocaleString('en-IN')}</b></span><span className="total">Estimated total <b>₹{(rent + serviceFee).toLocaleString('en-IN')}</b></span></div><Button onClick={onBook}>Request to book</Button><p className="no-charge"><ShieldCheck size={14} /> You won't be charged yet</p><div className="trust-row"><Check size={14} /> Identity-verified hosts <Check size={14} /> Secure payments</div></aside>
}
