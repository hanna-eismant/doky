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
import {Link, useNavigate } from 'react-router-dom';
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

const globalSearchParams = {
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

const extractDocumentsSearchParams = (urlSearchParams) => ({
  query: urlSearchParams.get('query') ?? globalSearchParams.query,
  sort: urlSearchParams.get('sort') ?? globalSearchParams.sort.property,
  dir: urlSearchParams.get('dir') ?? globalSearchParams.sort.direction,
  page: parseInt(urlSearchParams.get('page') || '0', 10)
});

const useDocumentsSearchParams = () => {
  return useMemo(() => {
    const initialSearchParams = extractDocumentsSearchParams(new URLSearchParams(location.search));
    const urlSearchParams = new URLSearchParams(initialSearchParams);
    history.replaceState(null, '', '?' + urlSearchParams.toString());

    return {
      searchParams: initialSearchParams,
      setQuery: query => {
        globalSearchParams.query = query;
        urlSearchParams.set('query', query);
        history.replaceState(null, '', '?' + urlSearchParams.toString());
      },
      setSort: (field, direction) => {
        globalSearchParams.sort.property = field;
        globalSearchParams.sort.direction = direction;
        urlSearchParams.set('field', field);
        urlSearchParams.set('dir', direction);
        history.replaceState(null, '',  '?' + urlSearchParams.toString());
      },
      setPage: page => {
        globalSearchParams.page.number = page;
        urlSearchParams.set('page', page);
        history.replaceState(null, '',  '?' + urlSearchParams.toString());
      }
    };
  }, []);
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

  const { searchParams, setSort, setQuery, setPage } = useDocumentsSearchParams();

  // Initialize state from URL params or saved state
  const initialQuery = searchParams.query;
  const initialSort = searchParams.sort;
  const initialDir = searchParams.dir;
  const initialPage = searchParams.page;

  const {fields: {query}} = useFormData({ query: initialQuery });
  const [debouncedQuery, setDebouncedQuery] = useState(initialQuery);

  const [paginationModel, setPaginationModel] = useState({
    page: initialPage,
    pageSize: 0
  });

  const onPaginationModelChange = model => {
    // can be triggered with same values possibly due several layout changes bc of autoPageSize is true
    if (model.pageSize !== paginationModel.pageSize || model.page !== paginationModel.page) {
      setPage(model.page);
      setPaginationModel(model);
    }
  };

  const [sortModel, setSortModel] = useState([
    {
      field: initialSort,
      sort: initialDir.toLowerCase()
    }
  ]);

  const handleSetSortModel = model => {
    setSortModel(model);
    const [{ field, sort } ] = model;
    setSort(field, sort);
  };

  const debouncedSetQuery = useMemo(() =>
    debounce((value) => {
      setDebouncedQuery(value);
      setQuery(value);
    }, 500),
  [setQuery]
  );

  const handleSearchChange = (e) => {
    const value = e.target.value;
    query.setValue(value);
    debouncedSetQuery(value);
  };

  const search = useCallback(() => {
    const sort =  {
      property: sortModel[0].field,
      direction: sortModel[0].sort.toUpperCase()
    };

    const page = {
      number: paginationModel.page,
      size: paginationModel.pageSize
    };

    return searchDocuments({ query: debouncedQuery, page, sort});
  }, [debouncedQuery, paginationModel, sortModel]);

  const {isLoading, data} = useQuery(search);

  const navigate = useNavigate();

  const goToCreateDocument = useCallback(() => {
    navigate('/documents/create');
  }, [navigate]);

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
        onPaginationModelChange={onPaginationModelChange}
        sortModel={sortModel}
        onSortModelChange={handleSetSortModel}
        sortingOrder={['asc', 'desc']}
        disableColumnFilter
        autoPageSize
        pageSizeOptions={[0]}
      />
    </Stack>
  );
};

export default Documents;
