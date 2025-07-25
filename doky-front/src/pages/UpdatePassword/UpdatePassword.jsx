/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
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
import {Link, useNavigate, useSearchParams} from 'react-router-dom';
import React from 'react';
import {useForm} from '../../hooks/useForm.js';
import {useUpdatePassword} from './useUpdatePassword.js';
import Logo from '../../components/Logo/Logo.jsx';

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
          <Logo/>
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
