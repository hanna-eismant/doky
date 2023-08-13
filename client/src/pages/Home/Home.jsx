import React, {useCallback} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import useUserInfoQuery from './useUserInfoQuery';

export default () => {
  const {isLoading, data} = useUserInfoQuery();

  const navigate = useNavigate();

  const logout = useCallback(() => {
    localStorage.removeItem('jwt');
    navigate('/login');
  }, [navigate]);

  return (
    <div className="container-fluid">
      <div className="row">
        <nav className="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
          <div className="position-sticky pt-3">
            <ul className="nav flex-column nav-pills">
              <li className="nav-item">
                <a className="nav-link active" href="#">
                  <span data-feather="home"></span>
                  Home
                </a>
              </li>
              <li className="nav-item">
                <a aria-current="page" className="nav-link" href="#">
                  <span data-feather="documents"></span>
                  Documents
                </a>
              </li>
            </ul>
          </div>
        </nav>
        <main className="ms-sm-auto col-lg-10">
          <div className="row">
            <div
              className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
              <h1 className="h3">Documents</h1>
            </div>
            Hello {isLoading ? '-' : data.userUid}
            {data.userUid
              ? <a href="#" onClick={logout}>Logout</a>
              : <Link to="login">Login</Link>
            }
          </div>
        </main>
      </div>
    </div>
  );
};
