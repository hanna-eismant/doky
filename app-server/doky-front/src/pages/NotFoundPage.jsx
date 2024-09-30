import React from 'react';
import {Link} from 'react-router-dom';

export const NotFoundPage = (

  <div className='min-vh-100 d-flex flex-column'>
    <div className='row'>
      <div className='col'>
        <header className='navbar bg-primary text-white bg-gradient sticky-top'>
          <div className='container-fluid'>
            <Link className='navbar-brand text-white' to='/'>
              <img height='40px' src='/logo-white-no-bg.svg' alt='Doky'/>
              <span className='version-badge'>
                <img
                  alt='Endpoint Badge'
                  src='https://img.shields.io/endpoint?url=https%3A%2F%2Fserver.blackfield-1e13811b.westeurope.azurecontainerapps.io%2Fapi%2Fapp-version'
                />
              </span>
            </Link>
          </div>
        </header>
      </div>
    </div>
    <div className='row flex-grow-1'>
      <div className='col align-content-center text-center'>
        <img className='img-fluid mw-100' src='/404.svg' alt='not found'/>
      </div>
      <div className='col align-content-center'>
        <p className='text-center fs-4 m-3'>It looks like you’ve taken a wrong turn.
          Don’t worry, it happens to the best of us. Let’s get you back on track</p>
        <div className='text-center'>
          <Link to={'/'} className='btn btn-primary m-3 text-center' role='button'>Return to Home</Link>
        </div>
      </div>
    </div>
  </div>
);
