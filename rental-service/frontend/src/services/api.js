const API_BASE = (import.meta.env.VITE_API_BASE_URL || (import.meta.env.DEV ? '/api' : '')).replace(/\/+$/, '')

async function request(path = '', options = {}) {
  if (!API_BASE) {
    throw new Error('The rental API URL is not configured. Set VITE_API_BASE_URL to your backend URL ending in /api, then redeploy.')
  }

  const response = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options
  })

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
  create: (property) => request('/properties', { method: 'POST', body: JSON.stringify(property) }),
  update: (id, property) => request(`/properties/${id}`, { method: 'PUT', body: JSON.stringify(property) }),
  remove: (id) => request(`/properties/${id}`, { method: 'DELETE' })
}

export const inquiryApi = {
  create: (inquiry) => request('/inquiries', { method: 'POST', body: JSON.stringify(inquiry) })
}
