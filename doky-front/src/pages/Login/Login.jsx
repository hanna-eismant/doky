import React from 'react';
import {Link} from 'react-router-dom';

import {FormInput} from '../../components';
import AlertError from '../../components/AlertError.jsx';
import {useLogin} from './useLogin.js';
import {useForm} from '../../hooks/useForm.js';
import Logo from '../../components/Logo/Logo.jsx';

const initialFormData = {
  uid: '',
  password: ''
};

const Login = () => {

  const login = useLogin();

  const {
    data,
    fields: {uid, password},
    globalError,
    handleSubmit
  } = useForm(initialFormData, login);

  return (
    <>
      {globalError ? <AlertError message={globalError}/> : ''}
      <div className='d-flex align-items-center justify-content-center'>
        <form onSubmit={handleSubmit} className='col-3'>
          <Logo/>
          <FormInput id='uid' label='Email' type='text' value={data.uid} onChange={uid.setValue}
            errors={uid.errors}/>
          <FormInput id='password' label='Password' type='password' value={data.password} onChange={password.setValue}
            errors={password.errors}/>
          <Link to='/reset-password'>Reset password</Link>
          <div className='mt-3 row'>
            <input type='submit' value='Login' className='btn btn-primary mb-3'/>
            <Link to='/register'>Register</Link>
          </div>
        </form>
      </div>
    </>
  );
};

export default Login;
