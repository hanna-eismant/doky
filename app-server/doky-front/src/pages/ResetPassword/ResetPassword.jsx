import AlertError from '../../components/AlertError.jsx';
import {FormInput} from '../../components';
import {Link} from 'react-router-dom';
import React, {useState} from 'react';
import {useForm} from '../../hooks/useForm.js';
import {useRequestResetPassword} from './useRequestResetPassword.js';

const initialFormData = {
  email: ''
};

const ResetPassword = () => {
  const [isSent, setIsSent] = useState(false);

  const onSent = () => {
    setIsSent(true);
  };

  const requestRestorePassword = useRequestResetPassword(onSent);

  const {
    data,
    fields: {email},
    globalError,
    handleSubmit
  } = useForm(initialFormData, requestRestorePassword);

  return (
    <>
      {globalError ? <AlertError message={globalError}/> : ''}
      <div className='d-flex align-items-center justify-content-center'>
        <div className='col-3'>
          <img alt='doky logo' className='mb-3 mt-3 img-fluid' src='/logo-color-bg.svg'/>

          {isSent && !globalError ? (
            <div className='mt-3 row text-center'>
              <div>If user with email <strong>{data.email}</strong> exists, we send a password reset link to your email
                  address.
                  Please check your inbox and follow the instructions to reset your password.
                  If you don’t see the email, be sure to check your spam or junk folder. Thank you!
              </div>
              <Link to='/login' className='mt-3'>Return to login</Link>
            </div>
          )
            : (
              <form onSubmit={handleSubmit}>
                <FormInput id='email' label='Email' type='text' value={data.email} onChange={email.setValue}
                  errors={email.errors}/>
                <div className='mt-3 row'>
                  <input type='submit' value='Send' className='btn btn-primary mb-3'/>
                  <Link to='/login' className='m-3'>Return to login</Link>
                </div>
              </form>
            )
          }

        </div>
      </div>
    </>
  );
};

export default ResetPassword;

