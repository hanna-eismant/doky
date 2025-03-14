import {useMutation} from '../../hooks/useMutation';
import {updatePassword} from '../../api/users.js';

export const useUpdatePassword = (onDone, token) => {

  const [updatePasswordMutation] = useMutation(
    form => updatePassword({password: form.password, token: token}),
    onDone
  );

  return updatePasswordMutation;
};
