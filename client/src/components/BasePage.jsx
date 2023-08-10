import React from 'react';

export default ({children}) => {
  return (
    <>
      <header className="navbar bg-primary text-white bg-gradient sticky-top">
        <div className="container-fluid">
          <a className="navbar-brand text-white" href="#"><img height="40px" src="logo-white-no-bg.svg"/></a>
        </div>
      </header>
      {children}
    </>
  );
}
