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
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import BackIcon from '@mui/icons-material/ArrowBack';
import HomeIcon from '@mui/icons-material/Home';
import {useNavigate} from 'react-router-dom';

const NotFoundPage = () => {
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
          404 NOT FOUND
        </Typography>
        <Typography variant="body1" color="text.secondary" gutterBottom>
          Looks like you&apos;ve ventured into uncharted territory. This page doesn&apos;t exist, but we can help you find your
          way back.
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
      </Box>
    </Container>
  );
};

export default NotFoundPage;

