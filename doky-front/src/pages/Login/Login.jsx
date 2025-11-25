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
import {Link} from 'react-router-dom';
import {Button, Stack, Typography} from '@mui/material';
import AuthLayout from '../../components/AuthLayout/AuthLayout.jsx';

import {FormInput} from '../../components';
import {useLogin} from './useLogin.js';
import {useForm} from '../../hooks/useForm.js';

const initialFormData = {
  uid: '',
  password: ''
};

const Login = () => {

  const login = useLogin();

  const {
    data,
    fields: {uid, password},
    handleSubmit,
    isSubmitting
  } = useForm(initialFormData, login);

  return (
    <AuthLayout title="Sign In">
      <Stack sx={{m: 2}} spacing={2} onSubmit={handleSubmit} component="form">
        <FormInput id='uid' label='Email' type='text' value={data.uid} onChange={uid.setValue} errors={uid.errors}
          data-cy="login-uid"/>
        <FormInput id='password' label='Password' type='password' value={data.password} onChange={password.setValue}
          errors={password.errors} data-cy="login-password"/>
        <Typography variant="caption">
          Forget password?{' '}
          <Link to='/password/reset'>Reset</Link>
        </Typography>
        <Button type="submit" data-cy="login-submit" loading={isSubmitting}>Sign in</Button>
        <Typography variant="caption">
          Don&apos;t have an account?{' '}
          <Link to="/register">Sign Up</Link>
        </Typography>
      </Stack>
    </AuthLayout>
  );
};

export default Login;
