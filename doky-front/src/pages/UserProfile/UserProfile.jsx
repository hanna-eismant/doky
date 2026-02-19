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
import EditUserProfileForm from './EditUserProfileForm';
import {useUser} from '../../hooks/useUser';
import {Stack} from '@mui/material';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import {Link} from 'react-router-dom';
import Typography from '@mui/material/Typography';

const UserProfile = () => {
  const user = useUser();

  return (
    <Stack spacing={5} sx={{
      width: '100%',
      padding: 2,
      alignItems: 'flex-start'
    }}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" width="100%">
        <Breadcrumbs aria-label="breadcrumb">
          <Link underline="hover" to={'/'} sx={{display: 'flex', alignItems: 'center'}}>
            Home
          </Link>
          <Typography sx={{display: 'flex', alignItems: 'center', fontSize: 'inherit'}}>
            Profile
          </Typography>
        </Breadcrumbs>
      </Stack>

      <EditUserProfileForm user={user}/>

    </Stack>
  );
};

export default UserProfile;
