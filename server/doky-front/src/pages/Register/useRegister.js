import {useMutation} from '../../hooks/useMutation';
import {register} from '../../api/users';

export const useRegister = () => {
  const [registerMutation] = useMutation(
    creds => register(creds),
    data => {
      if (data.token) {
        localStorage.setItem('jwt', data.token);
      }
    }
  );

  return registerMutation;
};
