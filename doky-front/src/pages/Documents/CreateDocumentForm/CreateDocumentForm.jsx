/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
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
import {useForm} from '../../../hooks/useForm.js';
import {useMutation} from '../../../hooks/useMutation.js';
import {createDocument} from '../../../api/documents.js';
import {Alert, Box, Button, Stack, TextField} from '@mui/material';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Cancel';
import {useNavigate} from 'react-router-dom';

const initialFormData = {
  name: '',
  description: ''
};

const CreateDocumentForm = ({onCreated}) => {
  const [documentMutation] = useMutation(createDocument);
  const navigate = useNavigate();

  const {data, fields: {name, description}, handleSubmit, globalError} = useForm(
    initialFormData,
    documentMutation,
    () => {
      onCreated();
    }
  );

  return (
    <form onSubmit={handleSubmit} style={{width: '100%'}}>
      {globalError && <Alert severity="error">{globalError}</Alert>}
      <Stack spacing={2} width="100%">
        <TextField
          fullWidth
          label="Name"
          id="name"
          size="small"
          value={data.name}
          onChange={name.setValue}
          error={name.errors && name.errors.length > 0}
          helperText={name.errors && name.errors.length > 0 ? name.errors[0] : ''}
        />

        <TextField
          fullWidth
          label="Description"
          id="description"
          size="small"
          value={data.description}
          onChange={description.setValue}
          multiline
          rows={5}
        />

        <Box sx={{display: 'flex', gap: 2, mt: 2}}>
          <Button
            variant="contained"
            color="secondary"
            startIcon={<CancelIcon/>}
            onClick={() => navigate('/documents')}
            disableElevation
          >
            Cancel
          </Button>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            startIcon={<SaveIcon/>}
            disableElevation
          >
            Create
          </Button>
        </Box>
      </Stack>
    </form>
  );
};

export default CreateDocumentForm;
