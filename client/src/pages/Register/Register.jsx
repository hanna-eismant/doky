import React, {useCallback} from 'react';
import {Link, useNavigate} from 'react-router-dom';

import {FormInput} from '../../components';
import useFormData from '../../hooks/useFormData';
import useRegisterQuery from './useRegisterQuery';

const initialFormData = {
  username: '',
  password: ''
};

export default () => {

  const {data, fields: {username, password}} = useFormData(initialFormData);
  const [register] = useRegisterQuery();
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
    <div className="d-flex align-items-center justify-content-center">
      <form onSubmit={onSubmit} className="mt-3">
        <img className="mb-3 mt-5" height="100px" src="logo-color-bg.svg"/>
        <FormInput id="username" label="Username" type="text" value={data.username} onChange={username.setValue}/>
        <FormInput id="password" label="Password" type="password" value={data.password} onChange={password.setValue}/>
        <div className="d-flex justify-content-between py-2">
          <Link to="/login">Login</Link>
          <input type="submit" value="Register" className="btn btn-primary mb-3 float-end"/>
        </div>
      </form>
    </div>
  )
}
