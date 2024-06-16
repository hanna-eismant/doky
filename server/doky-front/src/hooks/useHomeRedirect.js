import { useEffect } from 'react';
import { useOutletContext, useNavigate } from 'react-router-dom';

export const useHomeRedirect = () => {
  const navigate = useNavigate();
  const { user } = useOutletContext();

  useEffect(() => {
    if (user) {
      navigate('/');
    }
  }, [navigate, user]);
};
