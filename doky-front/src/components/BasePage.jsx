import React from 'react';
import {Link, Outlet} from 'react-router-dom';

const BasePage = () => (
  <>
    <header className="navbar bg-primary text-white bg-gradient sticky-top">
      <div className="container-fluid">
        <Link className="navbar-brand text-white" to="/">
          <img height="40px" src="/logo-white-no-bg.svg" alt="Doky Logo"/>
        </Link>
      </div>
    </header>
    <Outlet />
  </>
);

export default BasePage;
