import { useState, useEffect } from 'react';
import { inventoryApi, orderApi } from '../api';
import { ShoppingCart } from 'lucide-react';

const PRODUCT = {
  id: 'PROD-123',
  title: 'Quantum Mechanical Keyboard',
  price: 49.99,
  image: '/keyboard.png'
};

const Storefront = () => {
  const [stock, setStock] = useState(null);
  const [loading, setLoading] = useState(true);
  const [buying, setBuying] = useState(false);
  const [message, setMessage] = useState('');

  const fetchStock = async () => {
    try {
      setLoading(true);
      const response = await inventoryApi.get(`/${PRODUCT.id}`);
      setStock(response.data.availableQuantity);
    } catch (error) {
      console.error('Error fetching stock:', error);
      // Fallback if not found or server off
      setStock(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStock();
  }, []);

  const handleBuy = async () => {
    try {
      setBuying(true);
      setMessage('');
      await orderApi.post('', {
        productId: PRODUCT.id,
        quantity: 1,
        price: PRODUCT.price
      });
      setMessage('Order placed successfully! Check Order Status.');
      fetchStock(); // Refresh stock
    } catch (error) {
      console.error('Error placing order:', error);
      setMessage('Failed to place order. Try again.');
    } finally {
      setBuying(false);
    }
  };

  const isOutOfStock = stock !== null && stock <= 0;

  return (
    <div className="animate-fade-in product-grid">
      <div className="product-image-container">
        <img src={PRODUCT.image} alt={PRODUCT.title} className="product-image" />
      </div>
      
      <div className="product-details">
        <div>
          <div className="product-id">{PRODUCT.id}</div>
          <h1 className="product-title">{PRODUCT.title}</h1>
        </div>
        
        <div className="product-price">${PRODUCT.price.toFixed(2)}</div>
        
        <div>
          {loading ? (
            <div className="stock-badge">Checking stock...</div>
          ) : (
            <div className={`stock-badge ${isOutOfStock ? 'out' : ''}`}>
              {isOutOfStock ? 'Out of Stock' : `${stock} in stock`}
            </div>
          )}
        </div>

        <button 
          className="btn btn-primary" 
          onClick={handleBuy} 
          disabled={loading || isOutOfStock || buying}
          style={{ padding: '1rem 2rem', fontSize: '1.125rem', marginTop: '1rem' }}
        >
          <ShoppingCart size={20} />
          {buying ? 'Processing...' : 'Buy Now'}
        </button>

        {message && (
          <div style={{ color: message.includes('Failed') ? 'var(--danger)' : 'var(--success)' }}>
            {message}
          </div>
        )}
      </div>
    </div>
  );
};

export default Storefront;
