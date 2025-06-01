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
import {Link} from 'react-router-dom';
import { Typography, Card, Box, Button, Container, Alert } from '@mui/material';

import {FormInput} from '../../components';
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
    <Container className='AuthContainer'>
      <Box sx={{ minHeight: '64px', marginBottom: 2 }}>
        {globalError && <Alert severity="error">{globalError}</Alert>}
      </Box>
      <Card variant='outlined' className='AuthFormCard'>
        <Logo/>
        <Box
          component="form"
          onSubmit={handleSubmit}
          className='AuthForm'>
          <Typography component="h4" variant="h4">Sign in</Typography>
          <FormInput id='uid' label='Email' type='text' value={data.uid} onChange={uid.setValue}
            errors={uid.errors}/>
          <FormInput id='password' label='Password' type='password' value={data.password} onChange={password.setValue}
            errors={password.errors}/>
          <Button
            type="submit"
            fullWidth
            variant="contained"
          >
            Sign in
          </Button>
          <Typography className='CenterText'>
            <Link to='/reset-password'>Reset password</Link>
          </Typography>
          <Typography className='CenterText'>
            Don&apos;t have an account?{' '}
            <Link to="/register">Sign up</Link>
          </Typography>
        </Box>
      </Card>
    </Container>
  );
};

export default Login;
