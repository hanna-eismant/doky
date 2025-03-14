import React from 'react';
import {Link, useNavigate} from 'react-router-dom';

import {useRegister} from './useRegister.js';
import {useForm} from '../../hooks/useForm.js';
import {FormInput} from '../../components';
import AlertError from '../../components/AlertError.jsx';
import Logo from '../../components/Logo/Logo.jsx';

const initialFormData = {
  uid: '',
  password: ''
};

const Register = () => {
  const register = useRegister();
  const navigate = useNavigate();

  const {
    data,
    fields: {uid, password},
    globalError,
    handleSubmit
  } = useForm(initialFormData, register, () => {
    navigate('/');
  });

  return (
    <>
      {globalError ? <AlertError message={globalError}/> : ''}
      <div className="d-flex align-items-center justify-content-center">
        <form onSubmit={handleSubmit} className="col-3">
          <Logo/>
          <FormInput id="uid" label="Email" type="text" value={data.uid} onChange={uid.setValue}
            errors={uid.errors}/>
          <FormInput id="password" label="Password" type="password" value={data.password} onChange={password.setValue}
            errors={password.errors}/>
          <div className="mt-3 row">
            <input type="submit" value="Register" className="btn btn-primary mb-3"/>
            <Link to="/login">Login</Link>
          </div>
        </form>
      </div>
    </>
  );
};

export default Register;
