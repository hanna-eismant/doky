import React, {useCallback} from 'react';
import {useNavigate, useOutletContext} from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();
  const { user } = useOutletContext();

  const logout = useCallback(() => {
    localStorage.removeItem('jwt');
    navigate('/login');
  }, [navigate]);

  return (
    <>
      <div
        className="d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
      </div>
      <div>
        Hello, <strong>{user.name}</strong>
      </div>
      <div>
        <a href="#" onClick={logout}>Logout</a>
      </div>
    </>
  );
};

export default Home;
