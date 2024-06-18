import React, {useCallback} from 'react';
import { useNavigate} from 'react-router-dom';
import { useStore } from '../../hooks/useStore';

const Home = () => {

  const navigate = useNavigate();

  const { user, setUser } = useStore();

  const logout = useCallback((e) => {
    e.preventDefault();
    localStorage.removeItem('jwt');
    setUser(null);
    navigate('/login');
  }, [navigate, setUser]);

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
