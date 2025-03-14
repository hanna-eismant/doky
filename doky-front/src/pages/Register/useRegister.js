import {useMutation} from '../../hooks/useMutation';
import {register} from '../../api/users';
import {setJWT} from '../../services/storage';

export const useRegister = () => {
  const [registerMutation] = useMutation(
    creds => register(creds),
    data => {
      if (data.token) {
        setJWT(data.token);
      }
    }
  );

  return registerMutation;
};
