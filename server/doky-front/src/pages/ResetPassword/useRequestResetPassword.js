import {useMutation} from '../../hooks/useMutation';
import {requestResetPassword} from '../../api/users';

export const useRequestResetPassword = (onDone) => {

  const [requestResetPasswordMutation] = useMutation(
    form => requestResetPassword({email: form.email}),
    onDone
  );

  return requestResetPasswordMutation;
};
