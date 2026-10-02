export default function Modal({ open, title, onClose, children }) {
  if (!open) return null
  return <div className="modal-backdrop" role="presentation" onClick={onClose}><section className="modal" role="dialog" aria-modal="true" aria-label={title} onClick={(event) => event.stopPropagation()}><div className="modal-heading"><h2>{title}</h2><button onClick={onClose} aria-label="Close dialog">×</button></div>{children}</section></div>
}
