const API_BASE = (import.meta.env.VITE_API_BASE_URL || '/api').replace(/\/+$/, '')

async function request(path = '', options = {}) {
  if (!API_BASE) {
    throw new Error('The rental API URL is not configured. Set VITE_API_BASE_URL to your backend URL ending in /api, then redeploy.')
  }

  const headers = {
    ...(options.body instanceof FormData ? {} : { 'Content-Type': 'application/json' }),
    ...options.headers
  }
  const response = await fetch(`${API_BASE}${path}`, { ...options, headers })

  if (!response.ok) {
    if (response.status === 502 || response.status === 503) {
      throw new Error('The rental backend is not responding. Start it from the rental-service folder with .\\mvnw.cmd spring-boot:run, then try again.')
    }
    throw new Error(`The rental service returned HTTP ${response.status}. Please try again.`)
  }

  if (response.status === 204) return null

  if (!response.headers.get('content-type')?.includes('application/json')) {
    throw new Error('The rental API returned a webpage instead of JSON. Check VITE_API_BASE_URL and redeploy the frontend.')
  }

  return response.json()
}

export const propertyApi = {
  list: () => request('/properties'),
  get: (id) => request(`/properties/${id}`),
  create: (property) => {
    const { photos = [], ...details } = property
    if (!photos.length) return request('/properties', { method: 'POST', body: JSON.stringify(details) })

    const body = new FormData()
    body.append('property', new Blob([JSON.stringify(details)], { type: 'application/json' }))
    photos.forEach((photo) => body.append('photos', photo))
    return request('/properties', { method: 'POST', body })
  },
  photos: (id) => request(`/properties/${id}/photos`),
  addPhotos: (id, photos) => {
    const body = new FormData()
    photos.forEach((photo) => body.append('photos', photo))
    return request(`/properties/${id}/photos`, { method: 'POST', body })
  },
  photoUrl: (propertyId, photoId) => `${API_BASE}/properties/${propertyId}/photos/${photoId}`,
  removePhoto: (propertyId, photoId) => request(`/properties/${propertyId}/photos/${photoId}`, { method: 'DELETE' }),
  update: (id, property) => request(`/properties/${id}`, { method: 'PUT', body: JSON.stringify(property) }),
  remove: (id) => request(`/properties/${id}`, { method: 'DELETE' })
}

export const inquiryApi = {
  list: () => request('/inquiries'),
  get: (id) => request(`/inquiries/${id}`),
  byProperty: (propertyId) => request(`/inquiries/property/${propertyId}`),
  create: (inquiry) => request('/inquiries', { method: 'POST', body: JSON.stringify(inquiry) }),
  remove: (id) => request(`/inquiries/${id}`, { method: 'DELETE' })
}

export const bookingApi = {
  list: () => request('/bookings'),
  get: (id) => request(`/bookings/${id}`),
  byProperty: (propertyId) => request(`/bookings/property/${propertyId}`),
  byTenant: (tenantId) => request(`/bookings/tenant/${tenantId}`),
  create: (booking) => request('/bookings', { method: 'POST', body: JSON.stringify(booking) }),
  recordPayment: (id, amount) => request(`/bookings/${id}/payment`, {
    method: 'PATCH',
    body: JSON.stringify({ amount })
  }),
  updateStatus: (id, status) => request(`/bookings/${id}/status`, {
    method: 'PATCH',
    body: JSON.stringify({ status })
  }),
  remove: (id) => request(`/bookings/${id}`, { method: 'DELETE' })
}
