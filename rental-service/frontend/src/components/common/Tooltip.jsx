export default function Tooltip({ label, children }) {
  return <span className="tooltip-wrap"><span className="tooltip-target" aria-label={label}>{children}</span><span className="tooltip-text" role="tooltip">{label}</span></span>
}
