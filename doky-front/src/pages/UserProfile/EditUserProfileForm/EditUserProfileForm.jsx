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
import {useRevalidator} from 'react-router-dom';
import {useMutation} from '../../../hooks/useMutation.js';
import {updateCurrentUser} from '../../../api/users.js';
import {useForm} from '../../../hooks/useForm.js';
import {Box, Button, Stack} from '@mui/material';
import {FormInput} from '../../../components';
import SaveIcon from '@mui/icons-material/Save';
import {useGlobalSnackbar} from '../../../components/GlobalSnackbar/GlobalSnackbarProvider.jsx';

const EditUserProfileForm = ({user}) => {
  const [editUserProfile, {isLoading}] = useMutation(updateCurrentUser);
  const revalidator = useRevalidator();

  const submitUpdate = (payload) => editUserProfile({name: payload.name});

  const {showSuccess} = useGlobalSnackbar();

  const {data, fields: {uid, name}, handleSubmit} = useForm(user, submitUpdate, () => {
    revalidator.revalidate();
    showSuccess('Your name has been updated successfully');
  });

  return (
    <form onSubmit={handleSubmit} style={{width: '50%'}}>
      <Stack width="100%">
        <Stack spacing={2} sx={{flexGrow: 1}}>
          <FormInput id="uid"
            label="Email"
            type="text"
            value={data.uid}
            disabled={true}
            onChange={uid.setValue}
          />
          <FormInput id="name"
            label="Name"
            type="text"
            value={data.name}
            onChange={name.setValue}
            errors={name.errors}
          />
          <Box sx={{display: 'flex', gap: 2, mt: 2}}>
            <Button
              type="submit"
              color="primary"
              startIcon={<SaveIcon/>}
              loading={isLoading}
            >
              Save Changes
            </Button>
          </Box>
        </Stack>

      </Stack>
    </form>
  );
};

export default EditUserProfileForm;
