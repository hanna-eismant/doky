/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
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

import React, {useState} from 'react';
import {useForm} from '../../hooks/useForm.js';
import {useRequestResetPassword} from './useRequestResetPassword.js';
import {Button, Stack, Typography} from '@mui/material';
import AuthLayout from '../../components/AuthLayout/AuthLayout.jsx';
import {Link} from 'react-router-dom';
import FormInput from '../../components/formComponents/FormInput.jsx';

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
    handleSubmit,
    isSubmitting
  } = useForm(initialFormData, requestRestorePassword);

  return (
    <AuthLayout title="Reset Password">
      {isSent && !globalError ? (
        <Stack sx={{m: 2}} spacing={2}>
          <Typography>
            If user with email <strong>{data.email}</strong> exists, we send a password reset link to your email
            address.
            Please check your inbox and follow the instructions to reset your password.
            If you don’t see the email, be sure to check your spam or junk folder. Thank you!
          </Typography>
        </Stack>
      ) : (
        <Stack sx={{m: 2}} spacing={2} onSubmit={handleSubmit} component="form">
          <FormInput id='email' label='Email' type='text' value={data.email} onChange={email.setValue}
            errors={email.errors}/>
          <Button type="submit" loading={isSubmitting}>Send</Button>
          <Typography variant="caption">
            <Link to='/login'>Return to Log In</Link>
          </Typography>
        </Stack>
      )}
    </AuthLayout>
  );
};

export default ResetPassword;
