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

import React from 'react';
import {Link, useNavigate} from 'react-router-dom';
import { Container } from '@mui/material';

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
    <Container className='AuthContainer'>
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
    </Container>
  );
};

export default Register;
