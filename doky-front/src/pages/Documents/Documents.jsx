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
import {Link, useNavigate} from 'react-router-dom';
import {useQuery} from '../../hooks/useQuery';
import {getDocuments} from '../../api/documents';
import {
  Button,
  CircularProgress,
  Divider,
  InputAdornment,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField
} from '@mui/material';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import HomeIcon from '@mui/icons-material/Home';
import Typography from '@mui/material/Typography';
import DocumentsIcon from '@mui/icons-material/ContentPaste';
import SearchIcon from '@mui/icons-material/Search';
import AddIcon from '@mui/icons-material/Add';

const Documents = () => {
  const {isLoading, data} = useQuery(getDocuments);

  const navigate = useNavigate();

  const goToCreateDocument = useCallback(() => {
    navigate('/documents/new');
  }, [navigate]);

  const getGoToEditDocumentHandler = id => () => {
    navigate(`/documents/edit/${id}`);
  };

  return (
    <Stack spacing={2}
      sx={{
        width: '100%',
        padding: 2,
        alignItems: 'flex-start'
      }}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" width="100%">
        <Breadcrumbs aria-label="breadcrumb" data-cy="breadcrumb">
          <Link underline="hover" to={'/'} sx={{display: 'flex', alignItems: 'center'}} data-cy="breadcrumb-home">
            <HomeIcon sx={{mr: 0.5}} fontSize="inherit"/>
            Home
          </Link>
          <Typography sx={{display: 'flex', alignItems: 'center'}} data-cy="breadcrumb-documents">
            <DocumentsIcon sx={{mr: 0.5}} fontSize="inherit"/>
            Documents
          </Typography>
        </Breadcrumbs>
        <Button color="primary" onClick={goToCreateDocument} size="small"
          data-cy="documents-create-btn">
          <AddIcon sx={{mr: 0.5}} fontSize="inherit"/>
          Create
        </Button>
      </Stack>

      <Divider flexItem sx={{borderColor: 'rgba(0, 0, 0, 0.3)', borderBottomWidth: 1}}/>

      <TextField
        fullWidth
        label="Search"
        id="outlined-size-small"
        size="small"
        inputProps={{'data-cy': 'documents-search-input'}}
        InputLabelProps={{'data-cy': 'documents-search-label'}}
        slotProps={{
          input: {
            endAdornment: (
              <InputAdornment position="end">
                <SearchIcon/>
              </InputAdornment>
            ),
          },
        }}
      />

      {isLoading ? (
        <Stack alignItems="center" width="100%" padding={4}>
          <CircularProgress/>
        </Stack>
      ) : (
        <TableContainer>
          <Table stickyHeader data-cy="documents-table">
            <TableHead data-cy="documents-table-head">
              <TableRow>
                <TableCell data-cy="documents-th-name"><Typography variant="subtitle1"
                  fontWeight="bold">Name</Typography></TableCell>
                <TableCell data-cy="documents-th-file"><Typography variant="subtitle1"
                  fontWeight="bold">File</Typography></TableCell>
                <TableCell data-cy="documents-th-tags"><Typography variant="subtitle1"
                  fontWeight="bold">Tags</Typography></TableCell>
                <TableCell data-cy="documents-th-created"><Typography variant="subtitle1"
                  fontWeight="bold">Created</Typography></TableCell>
                <TableCell data-cy="documents-th-updated"><Typography variant="subtitle1"
                  fontWeight="bold">Updated</Typography></TableCell>
              </TableRow>
            </TableHead>
            <TableBody data-cy="documents-table-body">
              {Array.isArray(data) && data.map((document) => (
                <TableRow
                  key={document.id}
                  sx={{
                    '&:last-child td, &:last-child th': {border: 0},
                    cursor: 'pointer',
                    '&:hover': {backgroundColor: 'rgba(0, 0, 0, 0.04)'}
                  }}
                  onClick={getGoToEditDocumentHandler(document.id)}
                >
                  <TableCell component="th" scope="row">
                    {document.name}
                  </TableCell>
                  <TableCell>{document.fileName}</TableCell>
                  <TableCell></TableCell>
                  <TableCell>{document.createdDate}</TableCell>
                  <TableCell>{document.modifiedDate}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

    </Stack>
  );
};

export default Documents;
