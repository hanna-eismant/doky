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

import React from 'react';
import {Box, Button, Container, Typography} from '@mui/material';
import {useNavigate, useRouteError} from 'react-router-dom';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import HomeIcon from '@mui/icons-material/Home';
import BackIcon from '@mui/icons-material/ArrowBack';

const ErrorPage = () => {
  const error = useRouteError();
  const navigate = useNavigate();

  return (
    <Container maxWidth="sm">
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          minHeight: '100vh',
          textAlign: 'center',
          gap: 2
        }}
      >
        <ErrorOutlineIcon sx={{fontSize: 80, color: 'error.main'}}/>
        <Typography variant="h4" component="h1" gutterBottom>
          Oops! Something went wrong
        </Typography>
        <Typography variant="body1" color="text.secondary" gutterBottom>
          Don&apos;t worry, these things happen! Try going back to the previous page or return to the homepage.
        </Typography>
        <Box sx={{display: 'flex', gap: 2}}>
          <Button
            startIcon={<BackIcon/>}
            variant="outlined"
            onClick={() => navigate(-1)}>
            Go Back
          </Button>
          <Button
            startIcon={<HomeIcon/>}
            onClick={() => navigate('/')}>
            Go Home
          </Button>
        </Box>
        {__DEV__ && error?.stack && (
          <Box sx={{mt: 3, textAlign: 'left', width: '100%'}}>
            <Typography variant="body1" color="text.secondary" gutterBottom>
              {error?.statusText || error?.message || 'An unexpected error occurred'}
            </Typography>
            <Typography variant="h6" gutterBottom>
              Error Details:
            </Typography>
            <Typography
              variant="body2"
              component="pre"
              sx={{
                backgroundColor: '#f5f5f5',
                padding: 2,
                borderRadius: 1,
                overflow: 'auto',
                fontSize: '0.875rem'
              }}
            >
              {error.stack}
            </Typography>
          </Box>
        )}
      </Box>
    </Container>
  );
};

export default ErrorPage;
