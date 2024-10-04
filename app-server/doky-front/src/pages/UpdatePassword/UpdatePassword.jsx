import AlertError from '../../components/AlertError.jsx';
import {FormInput} from '../../components';
import {Link, useNavigate, useSearchParams} from 'react-router-dom';
import React from 'react';
import {useForm} from '../../hooks/useForm.js';
import {useUpdatePassword} from './useUpdatePassword.js';

const initialFormData = {
  password: '',
  confirmPassword: '',
  token: ''
};

const UpdatePassword = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');

  const navigate = useNavigate();
  const onSent = (data) => {
    if (!data.error) {
      navigate('/login');
    }
  };

  const updatePassword = useUpdatePassword(onSent, token);

  const {
    data,
    fields: {password, confirmPassword},
    globalError,
    handleSubmit
  } = useForm(initialFormData, updatePassword);

  return (
    <>
      {globalError ? <AlertError message={globalError}/> : ''}
      <div className='d-flex align-items-center justify-content-center'>
        <div className='col-3'>
          <img alt='doky logo' className='mb-3 mt-3 img-fluid' src='/logo-color-bg.svg'/>
          <form onSubmit={handleSubmit}>
            <FormInput id='password' label='Password' type='password' value={data.password}
              onChange={password.setValue}
              errors={password.errors}/>
            <FormInput id='confirmPassword' label='Confirm password' type='password'
              value={data.confirmPassword} onChange={confirmPassword.setValue}
              errors={confirmPassword.errors}/>
            <div className='mt-3 row'>
              <input type='submit' value='Confirm' className='btn btn-primary mb-3'/>
              <Link to='/login' className='m-3'>Return to login</Link>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default UpdatePassword;

