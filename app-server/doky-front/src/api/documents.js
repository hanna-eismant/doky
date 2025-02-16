import {download, get, post, postFormData, put} from './request';

const RESOURCE_NAME = 'documents';

export const getDocuments = () => get(RESOURCE_NAME);

export const getDocument = id => get(`${RESOURCE_NAME}/${id}`);

export const createDocument = payload => post(RESOURCE_NAME, payload);

export const updateDocument = payload => put(`${RESOURCE_NAME}/${payload.id}`, payload);

export const uploadDocument = (documentId, formData) => postFormData(`${RESOURCE_NAME}/${documentId}/upload`, formData);

export const downloadDocument = async documentId => {
  const token = await getDownloadToken(documentId);
  return download(`${RESOURCE_NAME}/${documentId}/download`, token);
};

export const getDownloadToken = async documentId => {
  const { token } = await get(`${RESOURCE_NAME}/${documentId}/download/token`);
  return token;
};

