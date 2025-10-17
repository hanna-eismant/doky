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

import React from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {Button, Stack, Typography} from '@mui/material';
import AuthLayout from '../../components/AuthLayout/AuthLayout.jsx';

import {useRegister} from './useRegister.js';
import {useForm} from '../../hooks/useForm.js';
import {FormInput} from '../../components';

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
    handleSubmit
  } = useForm(initialFormData, register, () => {
    navigate('/');
  });

  return (
    <AuthLayout title="Register">
      <Stack sx={{m: 2}} spacing={2} onSubmit={handleSubmit} component="form">
        <FormInput id="uid" label="Email" type="text" value={data.uid} onChange={uid.setValue} errors={uid.errors}
                   data-cy="register-uid"/>
        <FormInput id="password" label="Password" type="password" value={data.password} onChange={password.setValue}
                   errors={password.errors} data-cy="register-password"/>
        <Button type="submit" disableElevation variant="contained" data-cy="register-submit">Register</Button>
        <Typography variant="caption">
          Already have an account?{' '}
          <Link to="/login">Log In</Link>
        </Typography>
      </Stack>
    </AuthLayout>
  );
};

export default Register;
