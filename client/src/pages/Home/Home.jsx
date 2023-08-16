import React, {useCallback} from 'react';
import useUserInfoQuery from "./useUserInfoQuery";
import {Link, useNavigate} from "react-router-dom";

export default () => {
  const {isLoading, data} = useUserInfoQuery();

  const navigate = useNavigate();

  const logout = useCallback(() => {
    localStorage.removeItem('jwt');
    navigate('/login');
  }, [navigate]);

  return (
    <>
      <div
        className="d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 className="h3">Home</h1>
      </div>
      <div>
        Hello {isLoading ? '-' : data.userUid}
      </div>
      <div>
        {data.userUid
          ? <a href="#" onClick={logout}>Logout</a>
          : <Link to="login">Login</Link>
        }
      </div>
    </>
  );
};
