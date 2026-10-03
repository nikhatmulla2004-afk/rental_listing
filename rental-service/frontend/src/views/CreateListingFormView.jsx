import { ArrowLeft, Check, ImagePlus, Plus } from 'lucide-react'
import { useState } from 'react'
import Button from '../components/common/Button'
import InputField from '../components/common/InputField'
import ProgressBar from '../components/common/ProgressBar'

const initial = { title: '', description: '', type: 'Apartment', city: '', address: '', rent: '', deposit: '', bedrooms: 2, bathrooms: 1 }
const amenityOptions = ['Wi-Fi', 'Parking', 'Air conditioning', 'Laundry', 'Pet friendly', 'Furnished']

export default function CreateListingFormView({ onBack, onSave }) {
  const [form, setForm] = useState(initial)
  const [photos, setPhotos] = useState([])
  const [amenities, setAmenities] = useState([])
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)
  const update = (key) => (event) => setForm({ ...form, [key]: event.target.value })
  const addPhotos = (event) => {
    const selected = Array.from(event.target.files || [])
    if (photos.length + selected.length > 5) {
      setError('Choose no more than five photos for one listing.')
      return
    }
    if (selected.some((file) => file.size > 10 * 1024 * 1024)) {
      setError('Each photo must be 10 MB or smaller.')
      return
    }
    setError('')
    setPhotos((current) => [...current, ...selected.map((file) => ({ file, name: file.name, url: URL.createObjectURL(file) }))])
    event.target.value = ''
  }
  const toggleAmenity = (amenity) => setAmenities((current) => current.includes(amenity) ? current.filter((item) => item !== amenity) : [...current, amenity])
  const save = async (event) => {
    event.preventDefault()
    if (saving) return
    if (!form.title || !form.city || Number(form.rent) <= 0) { setError('Add a title, city, and monthly rent before publishing.'); return }
    setError('')
    setSaving(true)
    try {
      await onSave({ ...form, rent: Number(form.rent), deposit: Number(form.deposit || 0), bedrooms: Number(form.bedrooms), bathrooms: Number(form.bathrooms), amenities, photos: photos.map((photo) => photo.file), available: true })
    } catch (cause) {
      const message = cause instanceof Error ? cause.message : ''
      setError(message === 'Failed to fetch'
        ? 'Could not reach the rental service. Your listing was not published; check your connection and retry.'
        : message || 'We could not publish your listing. Please try again.')
    } finally {
      setSaving(false)
    }
  }
  return <main className="create-page"><button className="back-button" onClick={onBack}><ArrowLeft size={16} /> Cancel</button><div className="create-heading"><div><span className="eyebrow">For thoughtful hosts</span><h1>List your place.</h1><p>A few details now, a great tenant later.</p></div><ProgressBar value={form.title && form.city && form.rent ? 100 : form.title || form.city ? 50 : 15} label="Listing progress" /></div><form className="listing-form" onSubmit={save}><section className="form-section"><div className="form-intro"><span>01</span><div><h2>The essentials</h2><p>Tell people what makes this place feel like home.</p></div></div><div className="form-fields"><InputField label="Listing title" value={form.title} onChange={update('title')} placeholder="e.g. The Courtyard House" /><label className="field"><span className="field-label">Description</span><textarea value={form.description} onChange={update('description')} placeholder="What should guests know about this space?" rows="4" /></label><fieldset className="field radio-field"><legend className="field-label">Property type</legend><div className="radio-options">{['Apartment', 'Villa', 'Studio', 'House'].map((type) => <label key={type}><input type="radio" name="propertyType" value={type} checked={form.type === type} onChange={update('type')} />{type}</label>)}</div></fieldset><InputField label="City" value={form.city} onChange={update('city')} placeholder="Pune" /></div></section><section className="form-section"><div className="form-intro"><span>02</span><div><h2>Make it tangible</h2><p>Photos and amenities help people picture their life here.</p></div></div><div className="form-fields"><label className="upload-box"><ImagePlus size={28} /><strong>{photos.length ? `${photos.length} photo${photos.length > 1 ? 's' : ''} selected` : 'Drop photos here'}</strong><span>or browse from your device · JPG, PNG up to 10MB</span><input type="file" accept="image/png,image/jpeg" multiple onChange={addPhotos} /></label>{photos.length > 0 && <div className="photo-previews">{photos.map((photo) => <img key={photo.url} src={photo.url} alt={photo.name} />)}</div>}<fieldset className="field checkbox-field"><legend className="field-label">Amenities</legend><div className="checkbox-options">{amenityOptions.map((amenity) => <label key={amenity}><input type="checkbox" checked={amenities.includes(amenity)} onChange={() => toggleAmenity(amenity)} />{amenity}</label>)}</div></fieldset><div className="split-fields"><InputField label="Bedrooms" type="number" min="0" value={form.bedrooms} onChange={update('bedrooms')} /><InputField label="Bathrooms" type="number" min="0" value={form.bathrooms} onChange={update('bathrooms')} /></div></div></section><section className="form-section"><div className="form-intro"><span>03</span><div><h2>Set your price</h2><p>Keep it clear and competitive.</p></div></div><div className="form-fields"><div className="split-fields"><InputField label="Monthly rent (₹)" type="number" min="1" value={form.rent} onChange={update('rent')} placeholder="28500" /><InputField label="Security deposit (₹)" type="number" min="0" value={form.deposit} onChange={update('deposit')} placeholder="57000" /></div></div></section>{error && <p className="form-error">{error}</p>}<div className="form-actions"><Button type="button" variant="outline" onClick={onBack}>Save draft</Button><Button type="submit" icon={Plus}>Publish listing</Button></div></form></main>
}
