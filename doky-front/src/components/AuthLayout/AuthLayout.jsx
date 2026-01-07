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
import {Card, CardContent, Container, Typography} from '@mui/material';
import Logo from '../Logo/Logo.jsx';

const AuthLayout = ({title, children}) => {
  return (
    <Container className='AuthContainer' maxWidth={false}>
      <Card variant='outlined' className='AuthFormCard'>
        <CardContent><Logo/></CardContent>
        {title && (
          <Typography component="h5" variant="h5" className='CenterText'>
            {title}
          </Typography>
        )}
        {children}
      </Card>
    </Container>
  );
};

export default AuthLayout;
