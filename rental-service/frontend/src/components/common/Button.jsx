export default function Button({ children, variant = 'primary', icon: Icon, ...props }) {
  return (
    <button className={`button button-${variant}`} {...props}>
      {Icon && <Icon size={17} strokeWidth={2.2} />}
      {children}
    </button>
  )
}
