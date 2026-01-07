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
import {useForm} from '../../../hooks/useForm.js';
import {useMutation} from '../../../hooks/useMutation.js';
import {createDocument} from '../../../api/documents.js';
import {Box, Button, Stack} from '@mui/material';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Cancel';
import {useNavigate} from 'react-router-dom';
import {FormInput} from '../../../components/index.js';
import {emitGlobalSuccess} from '../../../components/GlobalSnackbar/snackbarBus.js';

const initialFormData = {
  name: '',
  description: ''
};

const CreateDocumentForm = ({onCreated}) => {
  const [documentMutation] = useMutation(createDocument);
  const navigate = useNavigate();

  const {data, fields: {name, description}, handleSubmit, isSubmitting} = useForm(
    initialFormData,
    documentMutation,
    () => {
      emitGlobalSuccess('Document created successfully');
      onCreated();
    }
  );

  return (
    <form onSubmit={handleSubmit} style={{width: '100%'}}>
      <Stack spacing={2} width="100%">
        <FormInput
          label="Name"
          id="name"
          value={data.name}
          onChange={name.setValue}
          errors={name.errors}
          inputProps={{'data-cy': 'doc-name-input'}}
        />

        <FormInput
          label="Description"
          id="description"
          value={data.description}
          onChange={description.setValue}
          multiline
          rows={5}
          inputProps={{'data-cy': 'doc-description-input'}}
        />

        <Box sx={{display: 'flex', gap: 2, mt: 2}}>
          <Button
            color="secondary"
            startIcon={<CancelIcon/>}
            onClick={() => navigate('/documents')}
            data-cy="create-doc-cancel"
          >
            Cancel
          </Button>
          <Button
            type="submit"
            color="primary"
            startIcon={<SaveIcon/>}
            data-cy="create-doc-submit"
            loading={isSubmitting}
          >
            Create
          </Button>
        </Box>
      </Stack>
    </form>
  );
};

export default CreateDocumentForm;
