import {BASE_URL} from 'config';

const apiPrefix = '/api';

const getAuthHeader = () => {
  const jwt = localStorage.getItem('jwt');
  return jwt ? {Authorization: `Bearer ${jwt}`} : {};
};

const getContentTypeHeader = (contentType) => {
  return contentType ? {'Content-Type': contentType} : {};
};

const getDefaultOptions = (contentType) => ({
  headers: {
    ...getAuthHeader(),
    ...getContentTypeHeader(contentType)
  }
});

const request = async (url, method, data = {}) => {
  const response = await fetch(BASE_URL + apiPrefix + '/' + url, {
    ...getDefaultOptions('application/json'),
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

export const postFormData = (url, formData = {}) => {
  return fetch(BASE_URL + apiPrefix + '/' + url, {
    ...getDefaultOptions(),
    method: 'POST',
    body: formData
  });
};

export const put = async (url, data = {}) =>
  request(url, 'PUT', data);

export const get = async url => {
  const response = await fetch(BASE_URL + apiPrefix + '/' + url, getDefaultOptions());

  return response.json();
};
