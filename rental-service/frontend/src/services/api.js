const API_BASE = (import.meta.env.VITE_API_BASE_URL || '/api').replace(/\/+$/, '')

async function request(path = '', options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options
  })

  if (!response.ok) {
    throw new Error(`Request failed with status ${response.status}`)
  }

  return response.status === 204 ? null : response.json()
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
