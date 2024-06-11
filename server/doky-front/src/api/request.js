import {BASE_URL} from 'config';

const apiPrefix = '/api';

const HEADERS = {
  'Content-Type': 'application/json'
};

const getHeaders = () => {
  const jwt = localStorage.getItem('jwt');
  return jwt ? {
    Authorization: `Bearer ${jwt}`,
    ...HEADERS
  } : HEADERS;
};

const getDefaultOptions = () => ({
  headers: getHeaders()
});

const request = async (url, method, data = {}) => {
  const response = await fetch(BASE_URL + apiPrefix + '/' + url, {
    ...getDefaultOptions(),
    method,
    body: JSON.stringify(data)
  });

  // TODO ?
  const contentType = response.headers.get('content-type');
  if (contentType !== null && contentType.includes('application/json')) {
    return response.json();
  }
};

export const post = (url, data = {}) =>
  request(url, 'POST', data);

export const put = async (url, data = {}) =>
  request(url, 'PUT', data);

export const get = async url => {
  const response = await fetch(BASE_URL + apiPrefix + '/' + url, getDefaultOptions());

  return response.json();
};
