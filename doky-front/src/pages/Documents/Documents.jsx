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

import React, {useCallback, useMemo, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {useQuery} from '../../hooks/useQuery';
import {searchDocuments} from '../../api/documents';
import {
  Button,
  CircularProgress,
  Divider,
  InputAdornment,
  Stack,
  TextField
} from '@mui/material';
import {debounce} from '@mui/material/utils';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import HomeIcon from '@mui/icons-material/Home';
import Typography from '@mui/material/Typography';
import DocumentsIcon from '@mui/icons-material/ContentPaste';
import SearchIcon from '@mui/icons-material/Search';
import AddIcon from '@mui/icons-material/Add';
import {useFormData} from '../../hooks/useFormData';
import {DataGrid} from '@mui/x-data-grid';

const searchPayload = {
  query: '',
  page: {
    number: 0,
    size: 10
  },
  sort: {
    property: 'createdDate',
    direction: 'DESC'
  }
};

const columns = [
  {
    field: 'name',
    headerName: 'Name',
    flex: 2,
    renderHeader: () => (
      <strong data-cy="documents-th-name">
        Name
      </strong>
    ),
  },
  {
    field: 'fileName',
    headerName: 'File',
    flex: 1,
    renderHeader: () => (
      <strong data-cy="documents-th-file">
        File
      </strong>
    ),
  },
  {
    field: 'createdDate',
    headerName: 'Created',
    minWidth: 200,
    renderHeader: () => (
      <strong data-cy="documents-th-created">
        Created
      </strong>
    ),
  },
  {
    field: 'modifiedDate',
    headerName: 'Updated',
    minWidth: 200,
    renderHeader: () => (
      <strong  data-cy="documents-th-updated">
        Updated
      </strong>
    ),
  }
];

const Documents = () => {

  const {fields: {query}} = useFormData(searchPayload);
  const [debouncedQuery, setDebouncedQuery] = useState('');
  const [sortModel, setSortModel] = useState([
    {
      field: searchPayload.sort.property,
      sort: searchPayload.sort.direction.toLowerCase()
    }
  ]);

  const debouncedSetQuery = useMemo(() =>
    debounce((value) => {
      setDebouncedQuery(value);
    }, 500),
  []
  );

  const handleSearchChange = (e) => {
    const value = e.target.value;
    query.setValue(value);
    debouncedSetQuery(value);
  };

  const handleSortModelChange = useCallback((newSortModel) => {
    setSortModel(newSortModel);
  }, []);

  const search = useCallback(() => {
    const sort = sortModel.length > 0
      ? {
        property: sortModel[0].field,
        direction: sortModel[0].sort.toUpperCase()
      }
      : searchPayload.sort;

    return searchDocuments({...searchPayload, query: debouncedQuery, sort});
  }, [debouncedQuery, sortModel]);

  const {isLoading, data} = useQuery(search);

  const navigate = useNavigate();

  const goToCreateDocument = useCallback(() => {
    navigate('/documents/new');
  }, [navigate]);

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
        value={query.value}
        onChange={handleSearchChange}
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
      <DataGrid
        loading={isLoading}
        sx={{
          width: '100%',
          '& .MuiDataGrid-row': {
            cursor: 'pointer'
          }
        }}
        columns={columns}
        rows={data.documents}
        onRowClick={(params) => navigate(`/documents/edit/${params.id}`)}
        sortModel={sortModel}
        onSortModelChange={handleSortModelChange}
        disableColumnFilter
        initialState={{
          pagination: {
            paginationModel: {
              pageSize: searchPayload.page.size,
            },
          },
        }}
      />
    </Stack>
  );
};

export default Documents;
