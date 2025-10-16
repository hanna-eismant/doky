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

import {FormInput} from '../../components';
import {Link, useNavigate, useSearchParams} from 'react-router-dom';
import React from 'react';
import {useForm} from '../../hooks/useForm.js';
import {useUpdatePassword} from './useUpdatePassword.js';
import {Button, Stack, Typography} from '@mui/material';
import AuthLayout from '../../components/AuthLayout/AuthLayout.jsx';
import {useGlobalSnackbar} from '../../components/GlobalSnackbar/GlobalSnackbarProvider.jsx';

const initialFormData = {
  password: '',
  confirmPassword: '',
  token: ''
};

const UpdatePassword = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');

  const navigate = useNavigate();
  const {showSuccess} = useGlobalSnackbar();
  const onSent = (data) => {
    if (!data || !data.error) {
      showSuccess('Your password has been updated successfully');
      navigate('/login');
    }
  };

  const updatePassword = useUpdatePassword(onSent, token);

  const {
    data,
    fields: {password, confirmPassword},
    handleSubmit
  } = useForm(initialFormData, updatePassword);

  return (
    <AuthLayout title="Update Password">
      <Stack sx={{m: 2}} spacing={2} onSubmit={handleSubmit} component="form">
        <FormInput id='password' label='Password' type='password' value={data.password} onChange={password.setValue}
                   errors={password.errors}/>
        <FormInput id='confirmPassword' label='Confirm password' type='password' value={data.confirmPassword}
                   onChange={confirmPassword.setValue} errors={confirmPassword.errors}/>
        <Button type="submit" disableElevation variant="contained">Confirm</Button>
        <Typography variant="caption">
          <Link to='/login'>Return to Log In</Link>
        </Typography>
      </Stack>
    </AuthLayout>
  );
};

export default UpdatePassword;
