import React, { useCallback } from 'react';
import useUserInfoQuery from "./useUserInfoQuery";
import { useNavigate, Link } from "react-router-dom";

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
      className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
      <h1 className="h3">Home</h1>
      </div>
      Hello {isLoading ? '-' : data.userUid}
      {data.userUid
        ? <a href="#" onClick={logout}>Logout</a>
        : <Link to="login">Login</Link>
      }
    </>
  );
};
