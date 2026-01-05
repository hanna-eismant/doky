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

import { datadogRum } from '@datadog/browser-rum';
import { reactPlugin } from '@datadog/browser-rum-react';
import {BASE_URL} from 'config';

export const initializeDatadog = (config) => {
  if (!config?.applicationId || !config?.clientToken) {
    console.warn('Datadog configuration missing. Monitoring will not be initialized.');
    return;
  }

  datadogRum.init({
    applicationId: config.applicationId,
    clientToken: config.clientToken,
    site: config.site,
    service: config.service,
    env: config.env,
    version: config.version,
    sessionReplaySampleRate: 100,
    traceSampleRate:100,
    trackUserInteractions: true,
    trackResources: true,
    trackLongTasks: true,
    defaultPrivacyLevel: 'mask-user-input',
    startSessionReplayRecordingManually: false,
    plugins: [reactPlugin({ router: true })],
    allowedTracingUrls: [
      (url) => url.startsWith(BASE_URL)
    ],
    allowUntrustedEvents: true
  });

  console.log('Datadog monitoring initialized');
};
