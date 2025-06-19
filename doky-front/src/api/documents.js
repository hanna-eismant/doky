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

import {download, get, post, postFormData, put} from './request';

const RESOURCE_NAME = 'documents';

export const getDocuments = () => get(RESOURCE_NAME);

export const getDocument = id => get(`${RESOURCE_NAME}/${id}`);

export const createDocument = payload => post(RESOURCE_NAME, payload);

export const updateDocument = payload => {
  const {name, description} = payload;
  return put(`${RESOURCE_NAME}/${payload.id}`, {name, description});
};

export const uploadDocument = (documentId, formData) => postFormData(`${RESOURCE_NAME}/${documentId}/upload`, formData);

export const downloadDocument = async documentId => {
  const token = await getDownloadToken(documentId);
  return download(`${RESOURCE_NAME}/${documentId}/download`, token);
};

export const getDownloadToken = async documentId => {
  const { token } = await get(`${RESOURCE_NAME}/${documentId}/download/token`);
  return token;
};
