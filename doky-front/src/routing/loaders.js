import {replace} from 'react-router-dom';
import {getCurrentUser} from '../api/users';
import {getJWT} from '../services/storage';

export const mainPageLoader = async () =>
  getJWT()
    ? { user: await getCurrentUser() }
    : replace('login');

export const authPageLoader = async () =>
  getJWT() ? replace('/') : null;
