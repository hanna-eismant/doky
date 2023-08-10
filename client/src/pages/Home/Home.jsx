import React, { useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import useUserInfoQuery from './useUserInfoQuery';

export default () => {
  const { isLoading, data } = useUserInfoQuery();

  const navigate = useNavigate();

  const logout = useCallback(() => {
    localStorage.removeItem('jwt');
    navigate('/login');
  }, [navigate]);

  return (
    <>
      <h1>Hello { isLoading ? '-' : data.userUid}</h1>
      {data.userUid
        ? <a href="#" onClick={logout}>Logout</a>
        : <Link to="login">Login</Link>
      }
    </>
  );
};
