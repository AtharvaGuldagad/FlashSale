import { Link, useLocation } from 'react-router-dom';
import { PackageOpen, Activity, LayoutDashboard } from 'lucide-react';

const Navbar = () => {
  const location = useLocation();

  return (
    <nav className="navbar">
      <div className="nav-brand">
        <PackageOpen size={24} color="var(--accent-color)" />
        <div>Flash<span>Sale</span></div>
      </div>
      <div className="nav-links">
        <Link to="/" className={`nav-link ${location.pathname === '/' ? 'active' : ''}`}>
          Storefront
        </Link>
        <Link to="/orders" className={`nav-link ${location.pathname === '/orders' ? 'active' : ''}`}>
          <Activity size={18} />
          Orders
        </Link>
        <Link to="/admin" className={`nav-link ${location.pathname === '/admin' ? 'active' : ''}`}>
          <LayoutDashboard size={18} />
          Admin
        </Link>
      </div>
    </nav>
  );
};

export default Navbar;
