import { useState } from 'react'
import { Apple, ArrowRight, Check, Eye, EyeOff, Mail, ShieldCheck, X } from 'lucide-react'
import Button from './Button'

export default function AuthModal({ open, onClose, onAuthenticated }) {
  const [mode, setMode] = useState('signin')
  const [showPassword, setShowPassword] = useState(false)
  const [form, setForm] = useState({ name: '', email: '', password: '' })
  const [error, setError] = useState('')
  if (!open) return null
  const update = (key) => (event) => setForm({ ...form, [key]: event.target.value })
  const submit = (event) => {
    event.preventDefault()
    if (!form.email || !form.password || (mode === 'signup' && !form.name)) {
      setError(mode === 'signup' ? 'Add your name, email, and password to continue.' : 'Enter your email and password to continue.')
      return
    }
    onAuthenticated({ name: form.name || form.email.split('@')[0], email: form.email })
    onClose()
  }
  return <div className="auth-backdrop" onClick={onClose}><section className="auth-modal" role="dialog" aria-modal="true" aria-labelledby="auth-title" onClick={(event) => event.stopPropagation()}><button className="auth-close" onClick={onClose} aria-label="Close sign in"><X size={18} /></button><div className="auth-mark"><ShieldCheck size={22} /></div><span className="eyebrow">A better way to move</span><h2 id="auth-title">{mode === 'signin' ? 'Welcome back.' : 'Make yourself at home.'}</h2><p className="auth-subtitle">{mode === 'signin' ? 'Sign in to save homes, manage trips, and message hosts.' : 'Create an account to keep your rental search in one place.'}</p><div className="auth-tabs"><button className={mode === 'signin' ? 'active' : ''} onClick={() => { setMode('signin'); setError('') }}>Sign in</button><button className={mode === 'signup' ? 'active' : ''} onClick={() => { setMode('signup'); setError('') }}>Create account</button></div><form className="auth-form" onSubmit={submit}>{mode === 'signup' && <label className="field"><span className="field-label">Full name</span><input value={form.name} onChange={update('name')} placeholder="Alex Morgan" autoComplete="name" /></label>}<label className="field"><span className="field-label">Email address</span><span className="input-icon"><Mail size={16} /><input type="email" value={form.email} onChange={update('email')} placeholder="alex@example.com" autoComplete="email" /></span></label><label className="field"><span className="field-label">Password</span><span className="input-icon"><input type={showPassword ? 'text' : 'password'} value={form.password} onChange={update('password')} placeholder="At least 8 characters" autoComplete={mode === 'signin' ? 'current-password' : 'new-password'} /><button type="button" onClick={() => setShowPassword(!showPassword)} aria-label={showPassword ? 'Hide password' : 'Show password'}>{showPassword ? <EyeOff size={16} /> : <Eye size={16} />}</button></span></label>{mode === 'signin' && <div className="auth-options"><label><input type="checkbox" /> Remember me</label><button type="button">Forgot password?</button></div>}{error && <p className="auth-error">{error}</p>}<Button type="submit" icon={ArrowRight}>{mode === 'signin' ? 'Sign in securely' : 'Create my account'}</Button></form><div className="auth-divider"><span>or continue with</span></div><div className="social-buttons"><button><Apple size={16} /> Apple</button><button><span className="google-mark">G</span> Google</button></div><p className="auth-legal">By continuing, you agree to Havenly’s Terms and Privacy Policy.</p><div className="auth-trust"><Check size={14} /> Your details are encrypted and never shared with hosts</div></section></div>
}
