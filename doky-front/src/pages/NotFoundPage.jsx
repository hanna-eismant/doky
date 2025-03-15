import React from 'react';
import {Link} from 'react-router-dom';

export const NotFoundPage = (

  <div className='min-vh-100 d-flex flex-column'>
    <div className='row flex-grow-1 m-0'>
      <div className='col-sm-12 col-md-6 align-content-center text-center'>
        <img className='img-fluid mw-100' src='/404.svg' alt='not found'/>
      </div>
      <div className='col-sm-12 col-md-6 align-content-center'>
        <p className='text-center fs-4 m-3'>It looks like you’ve taken a wrong turn.
          Don’t worry, it happens to the best of us. Let’s get you back on track</p>
        <div className='text-center'>
          <Link to={'/'} className='btn btn-primary m-3 text-center' role='button'>Return to Home</Link>
        </div>
      </div>
    </div>
  </div>
);
