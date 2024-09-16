import React, {useCallback} from 'react';
import { useNavigate } from 'react-router-dom';
import { deleteJWT } from '../../services/storage';
import { useUser } from '../../hooks/useUser';

const Home = () => {

  const navigate = useNavigate();

  const user = useUser();

  const logout = useCallback((e) => {
    e.preventDefault();
    deleteJWT();
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
