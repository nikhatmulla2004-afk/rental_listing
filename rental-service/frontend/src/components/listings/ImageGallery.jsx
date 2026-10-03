import { propertyApi } from '../../services/api'

const fallbackImages = ['https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?auto=format&fit=crop&w=1200&q=85', 'https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea?auto=format&fit=crop&w=700&q=85', 'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=700&q=85']

export default function ImageGallery({ property }) {
  const uploadedImages = (property?.photoIds || []).map((photoId) => propertyApi.photoUrl(property.id, photoId))
  const images = (uploadedImages.length ? uploadedImages : fallbackImages).slice(0, 3)
  const sideImages = images.slice(1)
  const singleImageStyle = images.length === 1 ? { gridTemplateColumns: '1fr' } : undefined
  const sideStyle = sideImages.length === 1 ? { gridTemplateRows: '1fr' } : undefined

  return <div className="gallery" style={singleImageStyle}><img className="gallery-main" src={images[0]} alt={`${property?.title || 'Rental property'} photo 1`} />{sideImages.length > 0 && <div className="gallery-side" style={sideStyle}>{sideImages.map((image, index) => <img key={image} src={image} alt={`${property?.title || 'Rental property'} photo ${index + 2}`} />)}</div>}<button className="gallery-button">View all photos</button></div>
}
