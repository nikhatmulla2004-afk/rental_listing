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
  photoUrl: (propertyId, photoId) => `${API_BASE}/properties/${propertyId}/photos/${photoId}`,
  update: (id, property) => request(`/properties/${id}`, { method: 'PUT', body: JSON.stringify(property) }),
  remove: (id) => request(`/properties/${id}`, { method: 'DELETE' })
}

export const inquiryApi = {
  create: (inquiry) => request('/inquiries', { method: 'POST', body: JSON.stringify(inquiry) })
}
