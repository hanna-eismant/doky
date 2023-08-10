import React, { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

import { FormField } from '../../components';
import useFormData from '../../hooks/useFormData';
import useRegisterQuery from './useRegisterQuery';

const initialFormData = {
  username: '',
  password: ''
};

export default () => {

  const { data, fields: { username, password } } = useFormData(initialFormData);
  const [ register ] = useRegisterQuery();
  const navigate = useNavigate();

  const onSubmit = useCallback(async event => {
    event.preventDefault();
    const response = await register(data);
    if (response?.error) {
      alert(response.error.message);
    } else {
      navigate('/login');
    }

  });

  return (
    <>
      <form onSubmit={onSubmit}>
        <FormField id="username" label="Username" type="text" value={data.username} onChange={username.setValue} />
        <FormField id="password" label="Password" type="password" value={data.password} onChange={password.setValue} />
        <div className="col-auto">
          <input type="submit" value="Register" class="btn btn-primary mb-3" />
        </div>
      </form>
    </>
  )
}