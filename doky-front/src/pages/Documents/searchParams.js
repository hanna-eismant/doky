import { useCallback } from 'react';

import { replace, useSearchParams } from 'react-router-dom';

const staticSearchParams = {
  page: 0
};

const defaultSearchParams = {
  query: '',
  sort: 'createdDate',
  dir: 'DESC',
  ...staticSearchParams
};

/**
 * Normalizes search parameters in the request URL.
 * - If all required parameters are present in the URL, no normalization is needed.
 * - If any parameter is missing, merges defaults and static params, and returns a redirect URL with normalized params.
 */
export const searchParamsNormalizer = ({ request }) => {
  const rawSearchParams = new URL(request.url).searchParams;
  const searchParams = Object.fromEntries(rawSearchParams.entries());
  const searchParamsKeys = Object.keys(searchParams);

  // If all expected parameters are present in the URL, update defaults and return null (no redirect needed)
  if (Object.keys(defaultSearchParams).every(key => searchParamsKeys.includes(key))) {
    Object.assign(defaultSearchParams, searchParams, staticSearchParams);
    return null;
  }

  // If any parameter is missing, merge defaults and static params, and return a redirect URL with normalized params
  Object.assign(defaultSearchParams, searchParams, staticSearchParams);
  return replace(`?${new URLSearchParams(defaultSearchParams)}`);
};

export const useDocumentsSearchParams = () => {
  const [rawSearchParams, setRawSearchParams] = useSearchParams();

  const searchParams = Object.fromEntries(rawSearchParams.entries());

  const setSearchParams = useCallback(params => {
    setRawSearchParams({ ...searchParams, ...params });
  }, [searchParams, setRawSearchParams]);

  return [
    searchParams,
    setSearchParams
  ];
};
