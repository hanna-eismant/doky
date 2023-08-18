import React from 'react';

export default ({message}) => {
  return (
    <div className="container mt-3">
      <div className="row no-gutters">
        <div className="col-md-12 m-auto">
          <div className="alert alert-danger fade show alert-dismissible" role="alert">
            {message}
          </div>
        </div>
      </div>
    </div>
  )
}

