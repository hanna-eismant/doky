import {useMutation} from '../../hooks/useMutation';
import {login} from '../../api/users';

export const useLogin = () => {
  const [loginMutation] = useMutation(
    creds => login(creds.uid, creds.password),
    data => {
      if (data.token) {
        localStorage.setItem('jwt', data.token);
      }
    }
  );

  return loginMutation;
};
