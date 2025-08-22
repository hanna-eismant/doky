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

import React, {useState} from 'react';
import {useForm} from '../../hooks/useForm.js';
import {useRequestResetPassword} from './useRequestResetPassword.js';
import Logo from '../../components/Logo/Logo.jsx';
import {Button, Card, CardContent, Container, Stack, Typography} from '@mui/material';
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
    handleSubmit
  } = useForm(initialFormData, requestRestorePassword);

  return (
    <Container maxWidth={false}
               sx={{
                 display: 'flex',
                 marginTop: '10%',
                 justifyContent: 'center',
                 padding: 0,
               }}
    >

      <Card variant='outlined'
            sx={{maxWidth: 400, mt: 1}}
      >
        <CardContent>
          <Logo/>
        </CardContent>
        {isSent && !globalError ? (
            <Stack sx={{m: 1}} spacing={2}>
            <Typography>
              If user with email <strong>{data.email}</strong> exists, we send a password reset link to your email
              address.
              Please check your inbox and follow the instructions to reset your password.
              If you don’t see the email, be sure to check your spam or junk folder. Thank you!
            </Typography>
            <Link to='/login'>Return to login</Link>
            </Stack>
        )
          : (
            <Stack sx={{m: 1}} spacing={2}>
              <FormInput id='email' label='Email' type='text' value={data.email} onChange={email.setValue}
                errors={email.errors}/>
              <Button onClick={handleSubmit} disableElevation variant="contained">Send</Button>
              <Link to='/login'>Return to login</Link>
            </Stack>
          )
        }
      </Card>

    </Container>
  );
};

export default ResetPassword;
