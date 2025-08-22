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
import {Button, Card, CardContent, Container, Stack, Typography} from '@mui/material';

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
    handleSubmit
  } = useForm(initialFormData, login);

  return (
    <Container className='AuthContainer' maxWidth={false}>
      <Card variant='outlined' className='AuthFormCard'>
        <CardContent><Logo/></CardContent>
        <Typography component="h5" variant="h5" className='CenterText'>Sign In</Typography>
        <Stack sx={{m: 1}} spacing={2} onSubmit={handleSubmit} component="form">
          <FormInput id='uid' label='Email' type='text' value={data.uid} onChange={uid.setValue} errors={uid.errors}/>
          <FormInput id='password' label='Password' type='password' value={data.password} onChange={password.setValue}
                     errors={password.errors}/>
          <Typography variant="caption">
            Forget password?{' '}
            <Link to='/password/reset'>Reset</Link>
          </Typography>
          <Button type="submit" disableElevation variant="contained">Sign in</Button>
          <Typography variant="caption">
            Don&apos;t have an account?{' '}
            <Link to="/register">Sign Up</Link>
          </Typography>
        </Stack>
      </Card>
    </Container>
  );
};

export default Login;
