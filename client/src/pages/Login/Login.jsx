import React, { useCallback } from 'react';
import { useNavigate, Link } from 'react-router-dom';

import { FormField } from '../../components';
import useFormData from '../../hooks/useFormData.js';
import useLoginQuery from './useLoginQuery.js';

const initialFormData = {
  username: '',
  password: ''
};

export default () => {

  const { data, fields: { username, password } } = useFormData(initialFormData);
  const [ login ] = useLoginQuery();
  const navigate = useNavigate();

  const onSubmit = useCallback(async event => {
    event.preventDefault();
    const response = await login(data);
    if (response.error) {
      alert(response.error.message)
    } else {
      navigate('/');
    }
  });

  return (
    <>
      <form onSubmit={onSubmit}>
        <FormField id="username" label="Username" type="text" value={data.username} onChange={username.setValue} />
        <FormField id="password" label="Password" type="password" value={data.password} onChange={password.setValue} />
        <div>
          <input type="submit" value="Login" class="btn btn-primary mb-3" />
        </div>
      </form>
      <Link to="/register">Register</Link>
    </>
  )
}