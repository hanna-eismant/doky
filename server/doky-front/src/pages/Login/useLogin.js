import { useNavigate } from 'react-router-dom';
import {useMutation} from '../../hooks/useMutation';
import {login} from '../../api/users';
import { setJWT } from '../../services/storage';

export const useLogin = () => {

  const navigate = useNavigate();

  const [loginMutation] = useMutation(
    creds => login(creds.uid, creds.password),
    data => {
      if (data.token) {
        setJWT(data.token);
        navigate('/');
      }
    }
  );

  return loginMutation;
};
