import { useState } from 'react'
import { Send } from 'lucide-react'
import Button from '../common/Button'
import InputField from '../common/InputField'
import { inquiryApi } from '../../services/api'

export default function BookingForm({ property, onDone }) {
  const [form, setForm] = useState({ name: '', email: '', message: '' })
  const [sent, setSent] = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState('')
  const update = (key) => (event) => setForm({ ...form, [key]: event.target.value })
  const submit = async (event) => {
    event.preventDefault()
    setSubmitting(true)
    setError('')
    try {
      await inquiryApi.create({ propertyId: property?.id, ...form })
      setSent(true)
      onDone?.()
    } catch (cause) {
      const message = cause instanceof Error ? cause.message : ''
      setError(message === 'Failed to fetch'
        ? 'Could not reach the rental service. Check your connection and API settings, then try again.'
        : message || 'We could not send your inquiry. Please try again.')
    } finally {
      setSubmitting(false)
    }
  }
  if (sent) return <div className="success-box"><span>✓</span><h3>Inquiry saved</h3><p>Your inquiry has been recorded for this home. We’ll keep you posted at {form.email}.</p></div>
  return <form className="inquiry-form" onSubmit={submit}><div><span className="eyebrow">Make it yours</span><h2>Ask about this home</h2><p>Share a little about yourself and your move.</p></div><InputField label="Your name" value={form.name} onChange={update('name')} placeholder="Alex Morgan" required /><InputField label="Email address" type="email" value={form.email} onChange={update('email')} placeholder="alex@example.com" required /><label className="field"><span className="field-label">Message</span><textarea value={form.message} onChange={update('message')} placeholder={`Hi, I'm interested in ${property?.title || 'this home'}...`} rows="4" /></label>{error && <p className="form-error" role="alert">{error}</p>}<Button icon={Send} type="submit" disabled={submitting}>{submitting ? 'Sending...' : 'Send inquiry'}</Button></form>
}
