import { useCallback } from 'react';

import { replace, useSearchParams } from 'react-router-dom';

const defaultSearchParams = {
  query: '',
  sort: 'createdDate',
  dir: 'DESC',
  page: 0
};

export const searchParamsNormalizer = ({ request }) => {
  const rawSearchParams = new URL(request.url).searchParams;
  const searchParams = Object.fromEntries(rawSearchParams.entries());

  const searchParamsKeys = Object.keys(searchParams);

  // if all params exists in url no need to normalize them
  if (Object.keys(defaultSearchParams).every(key => searchParamsKeys.includes(key))) {
    Object.assign(defaultSearchParams, searchParams, { page: 0 });
    return null;
  }

  Object.assign(defaultSearchParams, searchParams, { page: 0 });
  return replace(`?${new URLSearchParams(defaultSearchParams)}`);
};

export const useDocumentsSearchParams = () => {
  const [rawSearchParams, setRawSearchParams] = useSearchParams();

  const searchParams = Object.fromEntries(rawSearchParams.entries());

  const setSearchParams = useCallback (params => {
    setRawSearchParams({ ...searchParams, ...params });
  } ,[searchParams, setRawSearchParams]);

  return [
    searchParams,
    setSearchParams
  ];
};