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

import {replace} from 'react-router-dom';
import {getCurrentUser} from '../api/users';
import {getJWT, deleteJWT} from '../services/storage';
import {datadogLogs} from '@datadog/browser-logs';
import {datadogRum} from '@datadog/browser-rum';

export const mainPageLoader = async () => {
  if (!getJWT()) {
    return replace('login');
  }

  try {
    const user = await getCurrentUser();
    if (user?.error) {
      deleteJWT();
      return replace('login');
    }
    datadogRum.setUser(user);
    datadogLogs.setUser(user);
    datadogLogs.logger.debug('User loaded');
    return { user };
  } catch (error) {
    datadogLogs.logger.error('Error occurred', {}, error);
    deleteJWT();
    return replace('login');
  }
};

export const authPageLoader = async () =>
  getJWT() ? replace('/') : null;
