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
import {Button, Stack } from '@mui/material';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Typography from '@mui/material/Typography';
import AddIcon from '@mui/icons-material/Add';
import {DataGrid} from '@mui/x-data-grid';
import { useDocumentsSearchParams } from './searchParams';
import { SearchInput } from './SearchInput';

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

  const [searchParams, setSearchParams] = useDocumentsSearchParams();

  /* The searchParams object has already been normalized by ../searchParams/searchParamsNormalizer,
    ensuring that all expected parameters are present and set to default values if missing.
    Therefore, we can safely destructure the required parameters without additional existence checks. */
  const { query, sort, dir } = searchParams;
  const page = parseInt(searchParams.page, 10);

  const [ pageSize, setPageSize ] = useState(0);
  const paginationModel = useMemo(() => ({ page, pageSize }), [page, pageSize]);

  const onPaginationModelChange = ({ page: newPage, pageSize: newPageSize }) => {
    // This handler may be triggered multiple times during initial load, sometimes with unchanged values.
    // Only update state if the newPageSize differ from the current one to avoid unnecessary re-renders.
    if (newPage !== page) {
      setSearchParams({ page: newPage });
    }
    setPageSize(newPageSize);
  };

  const onSortModelChange = (model) => {
    if (model.length === 0) {
      return;
    }

    const [ { field, sort }] = model;
    setSearchParams({
      sort: field,
      dir: sort.toUpperCase()
    });
  };

  const sortModel = useMemo(() => [{
    field: sort,
    sort: dir.toLowerCase()
  }], [dir, sort]);

  const handleSearchChange = value => {
    setSearchParams({ query: value });
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

    return searchDocuments({ query: query, page, sort});
  }, [query, paginationModel.page, paginationModel.pageSize, sortModel]);

  // page size calculated dynamically based on layout. Initially it is zero.
  // So, it is needed to skip initial API request
  const {isLoading, data} = useQuery(search, { skip: pageSize === 0 });

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

      {/*
      NOTE: SearchInput is an uncontrolled component.
      This means that updating the 'defaultValue' prop after the initial render
      will NOT update the input's displayed value.
    */}
      <SearchInput
        defaultValue={query}
        onValueChange={handleSearchChange}
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
        onSortModelChange={onSortModelChange}
        sortingOrder={['asc', 'desc']}
        disableColumnFilter
        autoPageSize
        pageSizeOptions={[0]}
      />
    </Stack>
  );
};

export default Documents;
