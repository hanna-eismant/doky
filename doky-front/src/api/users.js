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
