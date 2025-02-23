import React from 'react';
import {Link} from 'react-router-dom';
import { Typography, Card, Box, Button, Container } from '@mui/material';

import {FormInput} from '../../components';
import AlertError from '../../components/AlertError.jsx';
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
      {globalError ? <AlertError message={globalError}/> : ''}
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
