import {get, post, put} from './request';

const RESOURCE_NAME = 'users';

export const getCurrentUser = () => get(`${RESOURCE_NAME}/current`);

export const updateCurrentUser = payload =>
  put(`${RESOURCE_NAME}/current`, payload);

export const login = (uid, password) =>
  post('login', {uid, password});

export const register = payload => post('register', payload);

export const requestResetPassword = payload => post('password/reset', payload);

export const updatePassword = payload => post('password/update', payload);
