import {get, post, put} from './request';

const RESOURCE_NAME = 'documents';

export const getDocuments = () => get(RESOURCE_NAME);

export const getDocument = id => get(`${RESOURCE_NAME}/${id}`);

export const createDocument = payload => post(RESOURCE_NAME, payload);

export const updateDocument = payload => put(`${RESOURCE_NAME}/${payload.id}`, payload);
