/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
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

export const download = async (url, token) => {
  const { body } = await fetch(BASE_URL + apiPrefix + '/' + url, {
    ...getDefaultOptions('application/json'),
    method: 'POST',
    body: JSON.stringify({ token })
  });

  return body;
};
