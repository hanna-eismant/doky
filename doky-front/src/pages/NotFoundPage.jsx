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
import {CardContent, Container, Stack, Typography} from '@mui/material';
import {Link} from 'react-router-dom';

export const NotFoundPage = (

  <Container className='AuthContainer' maxWidth={false}>
    <div className='NotFoundCard'>
      <Stack sx={{m: 2}} spacing={2} component="form">
        <CardContent>
          <img className='AuthLogo' alt="Not Found" src='/404.svg'/>
        </CardContent>
        <Typography component="h5" variant="h5" className='CenterText'>
          It looks like you’ve taken a wrong turn.
          Don’t worry, it happens to the best of us. Let’s get you back on track.
        </Typography>
        <Typography component="h5" variant="h5" className='CenterText'>
          <Link to="/">Return to Home </Link>
        </Typography>
      </Stack>
    </div>
  </Container>
);
