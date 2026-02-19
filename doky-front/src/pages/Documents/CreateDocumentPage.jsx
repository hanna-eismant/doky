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
import CreateDocumentForm from './CreateDocumentForm/CreateDocumentForm.jsx';
import {Link, useNavigate} from 'react-router-dom';
import {Box, Stack} from '@mui/material';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Typography from '@mui/material/Typography';
import {datadogLogs} from '@datadog/browser-logs';

const CreateDocumentPage = () => {
  const navigate = useNavigate();

  const goBack = useCallback((response) => {
    const location = response?.location;
    if (location) {
      const documentId = location.split('/').pop();
      datadogLogs.logger.debug(`Navigate to new document with id [${documentId}]`);
      navigate(`/documents/${documentId}`);
    } else {
      navigate('/documents');
    }
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
            Create Document
          </Typography>
        </Breadcrumbs>
      </Stack>

      <Box width="100%">
        <CreateDocumentForm onCreated={goBack}/>
      </Box>
    </Stack>
  );
};

export default CreateDocumentPage;
