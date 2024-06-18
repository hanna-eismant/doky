import {useMutation} from '../../hooks/useMutation';
import {getCurrentUser, login} from '../../api/users';
import { useStore } from '../../hooks/useStore';

export const useLogin = () => {
  const { setUser } = useStore();

  const [loginMutation] = useMutation(
    creds => login(creds.uid, creds.password),
    async data => {
      if (data.token) {
        localStorage.setItem('jwt', data.token);
        const user = await getCurrentUser();
        setUser(user);
      }
    }
  );

  return loginMutation;
};
