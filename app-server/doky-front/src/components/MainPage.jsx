import React, {useMemo} from 'react';
import {Link, Outlet, useLocation} from 'react-router-dom';
import classNames from 'classnames';
import {ToastContextProvider} from './Toasts/ToastsContext';
import Logo from './Logo';

const menuItems = [
  {
    name: 'Home',
    path: '/'
  },
  {
    name: 'Documents',
    // TODO
    path: '/documents',
    hasSubRoutes: true
  },
  {
    name: 'User Profile',
    path: '/profile'
  }
];

const MenuItem = ({name, path, isActive}) => {
  const linkClassName = useMemo(() => classNames('nav-link', {
    active: isActive
  }), [isActive]);

  return (
    <li className="nav-item">
      <Link to={path} className={linkClassName}>{name}</Link>
    </li>
  );
};

const MainPage = () => {
  const location = useLocation();

  return (
    <div className="container-fluid">
      <ToastContextProvider>
        <div className="row">
          <nav className="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
            <Logo />
            <ul className="nav flex-column nav-pills">
              {menuItems.map(({name, path, hasSubRoutes}) => {
                const isActive = hasSubRoutes ? location.pathname.startsWith(path) : path === location.pathname;
                return (
                  <MenuItem
                    name={name}
                    path={path}
                    key={name}
                    isActive={isActive}
                  />
                );
              })}
            </ul>
          </nav>
          <main className="ms-sm-auto col-lg-10">
            <div className="row">
              <Outlet/>
            </div>
          </main>
        </div>
      </ToastContextProvider>
    </div>
  );
};

export default MainPage;
