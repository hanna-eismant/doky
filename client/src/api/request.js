import { BASE_URL } from "config";

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
  mode: 'cors',
  headers: getHeaders()
});

export const post = async (url, data = {}) => {
  const response = await fetch(BASE_URL + '/' + url, {
    ...getDefaultOptions(),
    method: 'POST',
    body: JSON.stringify(data)
  });

  // TODO ?
  const contentType = response.headers.get('content-type');
  if (contentType !== null && contentType.includes('application/json')) {
    return response.json();
  }
};

export const get = async url => {
  const response = await fetch(BASE_URL + '/' + url, getDefaultOptions());

  return response.json();
};
