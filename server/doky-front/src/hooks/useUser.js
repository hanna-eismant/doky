import { useEffect } from 'react';
import { useOutletContext, useNavigate } from 'react-router-dom';

export const useUser = () => {
  const navigate = useNavigate();
  const { user } = useOutletContext();

  useEffect(() => {
    if (!user) {
      navigate('/login');
    }
  }, [navigate, user]);

  return user;
};
