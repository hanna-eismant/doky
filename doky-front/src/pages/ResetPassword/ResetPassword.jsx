/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

import AlertError from '../../components/AlertError.jsx';
import {FormInput} from '../../components';
import {Link} from 'react-router-dom';
import React, {useState} from 'react';
import {useForm} from '../../hooks/useForm.js';
import {useRequestResetPassword} from './useRequestResetPassword.js';
import Logo from '../../components/Logo/Logo.jsx';

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
          <Logo/>

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
