/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

import React, {useMemo} from 'react';
import {Link, Outlet, useLocation} from 'react-router-dom';
import classNames from 'classnames';
import {ToastContextProvider} from './Toasts/ToastsContext';

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
            <div className="position-sticky pt-3">
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
            </div>
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
