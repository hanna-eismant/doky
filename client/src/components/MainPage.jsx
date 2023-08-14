import React, { useMemo } from 'react';
import { Link, useLocation } from 'react-router-dom';
import classNames from 'classnames';

const menuItems = [
  {
    name: 'Home',
    path: '/'
  },
  {
    name: 'Documents',
    path: '/documents'
  }
];

const MenuItem = ({ name, path, isActive }) => {
  const linkClassName = useMemo(() => classNames('nav-link', {
    active: isActive
  }), [ isActive ]);

  return (
    <li className="nav-item">
      <Link to={path} className={linkClassName}>{name}</Link>
    </li>
  );
};

export default ({children}) => {
  const location  = useLocation();
  return (
    <div className="container-fluid">
      <div className="row">
        <nav className="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
          <div className="position-sticky pt-3">
            <ul className="nav flex-column nav-pills">
             {menuItems.map(({ name, path }) => <MenuItem name={name} path={path} key={name} isActive={path === location.pathname} />)}
            </ul>
          </div>
        </nav>
        <main className="ms-sm-auto col-lg-10">
          <div className="row">{children}</div>
        </main>
      </div>
    </div>
  );
};