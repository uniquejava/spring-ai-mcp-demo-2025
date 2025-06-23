import { Outlet } from 'react-router';
import './index.less';

export default function Layout() {
  return (
    <div className="navs">
      <Outlet />
    </div>
  );
}
