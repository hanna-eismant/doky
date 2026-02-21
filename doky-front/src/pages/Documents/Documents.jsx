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

import React, {useCallback, useEffect, useMemo, useState} from 'react';
import {Link, useNavigate, useSearchParams} from 'react-router-dom';
import {useQuery} from '../../hooks/useQuery';
import {searchDocuments} from '../../api/documents';
import {Button, InputAdornment, Stack, TextField} from '@mui/material';
import {debounce} from '@mui/material/utils';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Typography from '@mui/material/Typography';
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
      <strong data-cy="documents-th-updated">
        Updated
      </strong>
    ),
  }
];

const Documents = () => {

  const [searchParams, setSearchParams] = useSearchParams();

  // Initialize state from URL params or saved state
  const initialQuery = searchParams.get('query') || '';
  const initialSort = searchParams.get('sort') || searchPayload.sort.property;
  const initialDir = searchParams.get('dir') || searchPayload.sort.direction;
  const initialPage = parseInt(searchParams.get('page') || '0', 10);

  const {fields: {query}} = useFormData({...searchPayload, query: initialQuery});
  const [debouncedQuery, setDebouncedQuery] = useState(initialQuery);
  const [paginationModel, setPaginationModel] = useState({
    page: initialPage,
    pageSize: 0
  });
  const [sortModel, setSortModel] = useState([
    {
      field: initialSort,
      sort: initialDir.toLowerCase()
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

  const search = useCallback(() => {
    const sort = sortModel.length > 0
      ? {
        property: sortModel[0].field,
        direction: sortModel[0].sort.toUpperCase()
      }
      : searchPayload.sort;

    const page = {
      number: paginationModel.page,
      size: paginationModel.pageSize
    };

    return searchDocuments({...searchPayload, query: debouncedQuery, page, sort});
  }, [debouncedQuery, paginationModel, sortModel]);

  const {isLoading, data} = useQuery(search);

  const navigate = useNavigate();

  const goToCreateDocument = useCallback(() => {
    navigate('/documents/create');
  }, [navigate]);

  // Sync URL with state changes
  useEffect(() => {
    const params = new URLSearchParams();

    if (debouncedQuery) {
      params.set('query', debouncedQuery);
    }

    if (sortModel.length > 0) {
      params.set('sort', sortModel[0].field);
      params.set('dir', sortModel[0].sort.toUpperCase());
    }

    if (paginationModel.page > 0) {
      params.set('page', paginationModel.page.toString());
    }

    setSearchParams(params, { replace: true });
  }, [debouncedQuery, sortModel, paginationModel, setSearchParams]);

  return (
    <Stack spacing={5}
      sx={{
        width: '100%',
        height: '100vh',
        padding: 2,
        alignItems: 'flex-start'
      }}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" width="100%">
        <Breadcrumbs aria-label="breadcrumb" data-cy="breadcrumb">
          <Link underline="hover" to={'/'} sx={{display: 'flex', alignItems: 'center'}} data-cy="breadcrumb-home">
            Home
          </Link>
          <Typography sx={{display: 'flex', alignItems: 'center', fontSize: 'inherit'}} data-cy="breadcrumb-documents">
            Documents
          </Typography>
        </Breadcrumbs>
        <Button color="primary" onClick={goToCreateDocument} size="small" data-cy="documents-create-btn">
          <AddIcon sx={{mr: 0.5}} fontSize="inherit"/>
          Create
        </Button>
      </Stack>

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
          flex: 1,
          '& .MuiDataGrid-row': {
            cursor: 'pointer'
          }
        }}
        columns={columns}
        rows={data.documents}
        rowCount={data.totalCount}
        onRowClick={(params) => navigate(`/documents/${params.id}`)}
        paginationMode="server"
        paginationModel={paginationModel}
        onPaginationModelChange={setPaginationModel}
        sortModel={sortModel}
        onSortModelChange={setSortModel}
        sortingOrder={['asc', 'desc']}
        disableColumnFilter
        autoPageSize
        pageSizeOptions={[0]}
      />
    </Stack>
  );
};

export default Documents;
