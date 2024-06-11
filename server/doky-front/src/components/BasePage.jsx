import React from 'react';
import {Link} from 'react-router-dom';

const BasePage = ({children}) => (
  <>
    <header className="navbar bg-primary text-white bg-gradient sticky-top">
      <div className="container-fluid">
        <Link className="navbar-brand text-white" to="/">
          <img height="40px" src="/logo-white-no-bg.svg"/>
          <span className="version-badge">
            <img
              alt="Endpoint Badge"
              src="https://img.shields.io/endpoint?url=https%3A%2F%2Fserver.blackfield-1e13811b.westeurope.azurecontainerapps.io%2Fapi%2Fversion"
            />
          </span>
        </Link>
      </div>
    </header>
    {children}
  </>
);

export default BasePage;
