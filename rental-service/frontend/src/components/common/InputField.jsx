export default function InputField({ label, ...props }) {
  return (
    <label className="field">
      {label && <span className="field-label">{label}</span>}
      <input {...props} />
    </label>
  )
}
