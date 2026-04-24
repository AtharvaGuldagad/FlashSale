import { useState } from 'react';
import { inventoryApi } from '../api';
import { Plus } from 'lucide-react';

const AdminDashboard = () => {
  const [productId, setProductId] = useState('PROD-123');
  const [quantity, setQuantity] = useState(100);
  const [submitting, setSubmitting] = useState(false);
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setSubmitting(true);
      setMessage('');
      await inventoryApi.post('', {
        productId,
        availableQuantity: parseInt(quantity, 10)
      });
      setMessage(`Successfully added/updated stock for ${productId}`);
    } catch (error) {
      console.error('Error adding stock:', error);
      setMessage('Failed to add stock.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="animate-fade-in" style={{ maxWidth: '600px', margin: '0 auto' }}>
      <div className="card">
        <div className="card-header">
          <h2 className="card-title">Inventory Management</h2>
          <p style={{ color: 'var(--text-secondary)' }}>Add new stock to the inventory system.</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Product ID</label>
            <input 
              type="text" 
              className="form-input" 
              value={productId}
              onChange={(e) => setProductId(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">Available Quantity</label>
            <input 
              type="number" 
              className="form-input" 
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
              min="1"
              required
            />
          </div>

          <button type="submit" className="btn btn-primary" disabled={submitting} style={{ width: '100%' }}>
            <Plus size={18} />
            {submitting ? 'Adding...' : 'Add Stock'}
          </button>
        </form>

        {message && (
          <div style={{ 
            marginTop: '1.5rem', 
            padding: '1rem', 
            borderRadius: '8px',
            backgroundColor: message.includes('Failed') ? 'rgba(239, 68, 68, 0.1)' : 'rgba(16, 185, 129, 0.1)',
            color: message.includes('Failed') ? 'var(--danger)' : 'var(--success)'
          }}>
            {message}
          </div>
        )}
      </div>
    </div>
  );
};

export default AdminDashboard;
