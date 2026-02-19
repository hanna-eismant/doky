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

import React, {useCallback} from 'react';
import EditDocumentForm from './EditDocumentForm/EditDocumentForm.jsx';
import {Link, useNavigate, useParams} from 'react-router-dom';
import {useQuery} from '../../hooks/useQuery.js';
import {getDocument} from '../../api/documents.js';
import {Box, Button, CircularProgress, Stack} from '@mui/material';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Typography from '@mui/material/Typography';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import AddIcon from '@mui/icons-material/Add';

const DocumentPage = () => {
  const params = useParams();
  const navigate = useNavigate();
  const getCurrentDocument = useCallback(() => getDocument(params.id), [params.id]);

  const {data, isLoading, refetch} = useQuery(getCurrentDocument);

  const hasError = !isLoading && (!data || data.error);

  const goToCreateDocument = useCallback(() => {
    navigate('/documents/create');
  }, [navigate]);

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
          <Link underline="hover" to={'/documents'} sx={{display: 'flex', alignItems: 'center'}}>
            Documents
          </Link>
          <Typography sx={{display: 'flex', alignItems: 'center', fontSize: 'inherit'}}>
            Edit
          </Typography>
        </Breadcrumbs>
        <Button color="primary" onClick={goToCreateDocument} size="small" data-cy="documents-create-btn">
          <AddIcon sx={{mr: 0.5}} fontSize="inherit"/>
          Create
        </Button>
      </Stack>

      {isLoading ? (
        <Stack alignItems="center" width="100%" padding={4}>
          <CircularProgress/>
        </Stack>
      ) : hasError ? (
        <Stack alignItems="center" width="100%" padding={4} spacing={2}>
          <ErrorOutlineIcon sx={{fontSize: 64, color: 'error.main'}}/>
          <Typography variant="h5">Document Not Found</Typography>
          <Typography variant="body1" color="text.secondary">
            The document you are trying to edit does not exist or has been deleted.
          </Typography>
          <Button variant="contained" onClick={() => navigate('/documents')}>
            Back to Documents
          </Button>
        </Stack>
      ) : (
        <Box width="100%">
          <EditDocumentForm document={data} onSaveSuccess={refetch}/>
        </Box>
      )}

    </Stack>
  );
};

export default DocumentPage;
