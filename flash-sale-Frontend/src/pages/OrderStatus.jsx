import { useState, useEffect } from 'react';
import { orderApi } from '../api';
import { RefreshCcw } from 'lucide-react';

const OrderStatus = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isPolling, setIsPolling] = useState(false);

  const fetchOrders = async () => {
    try {
      const response = await orderApi.get('');
      setOrders(response.data);
      return response.data;
    } catch (error) {
      console.error('Error fetching orders:', error);
      return [];
    } finally {
      if (loading) setLoading(false);
    }
  };

  useEffect(() => {
    let intervalId = null;

    const setupPolling = async () => {
      // Fetch initial data
      const currentOrders = await fetchOrders();
      
      // Check if any order is PENDING
      const hasPending = currentOrders.some(order => order.status === 'PENDING');
      
      if (hasPending) {
        setIsPolling(true);
        intervalId = setInterval(async () => {
          const updatedOrders = await fetchOrders();
          const stillHasPending = updatedOrders.some(order => order.status === 'PENDING');
          
          if (!stillHasPending) {
            setIsPolling(false);
            clearInterval(intervalId);
          }
        }, 3000);
      } else {
        setIsPolling(false);
      }
    };

    setupPolling();

    // CLEANUP: If user navigates away, stop polling to prevent memory leak
    return () => {
      if (intervalId) {
        console.log('Cleaning up polling interval on unmount');
        clearInterval(intervalId);
      }
    };
  }, []);

  return (
    <div className="animate-fade-in">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <div>
          <h2 className="card-title">Your Orders</h2>
          <p style={{ color: 'var(--text-secondary)' }}>Track your recent purchases</p>
        </div>
        
        {isPolling && (
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--accent-color)' }}>
            <RefreshCcw size={16} className="animate-spin" style={{ animation: 'spin 2s linear infinite' }} />
            <span style={{ fontSize: '0.875rem', fontWeight: 500 }}>Live updates active</span>
            <style>{`
              @keyframes spin { 100% { transform: rotate(360deg); } }
            `}</style>
          </div>
        )}
      </div>

      <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
        {loading ? (
          <div style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
            Loading orders...
          </div>
        ) : orders.length === 0 ? (
          <div style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
            You haven't placed any orders yet.
          </div>
        ) : (
          <div className="table-container">
            <table className="orders-table">
              <thead>
                <tr>
                  <th>Order ID</th>
                  <th>Product</th>
                  <th>QTY</th>
                  <th>Price</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {orders.map(order => (
                  <tr key={order.id}>
                    <td style={{ fontFamily: 'monospace' }}>#{order.id}</td>
                    <td>{order.productId}</td>
                    <td>{order.quantity}</td>
                    <td>${order.price.toFixed(2)}</td>
                    <td>
                      <span className={`status-badge status-${order.status}`}>
                        {order.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default OrderStatus;
