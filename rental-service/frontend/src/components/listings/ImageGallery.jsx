const images = ['https://images.unsplash.com/photo-1600607687920-4e2a09cf159d?auto=format&fit=crop&w=1200&q=85', 'https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea?auto=format&fit=crop&w=700&q=85', 'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=700&q=85']

export default function ImageGallery() {
  return <div className="gallery"><img className="gallery-main" src={images[0]} alt="Living room" /><div className="gallery-side"><img src={images[1]} alt="Kitchen" /><img src={images[2]} alt="Exterior" /></div><button className="gallery-button">View all photos</button></div>
}
