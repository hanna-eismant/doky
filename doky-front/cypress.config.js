/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
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

const {defineConfig} = require('cypress');
const https = require('https');
const http = require('http');

// Simple "profile" switcher for Cypress runs
// Usage:
//  - Default (local): npx cypress run
//  - Dev profile: CYPRESS_PROFILE=dev npx cypress run
//  - You can also pass via Cypress env: npx cypress run --env profile=dev
const profile = process.env.CYPRESS_PROFILE || process.env.PROFILE || process.env.npm_config_profile || 'local';

const isDev = (process.env.CYPRESS_profile || '').toLowerCase() === 'dev' || (profile || '').toLowerCase() === 'dev';

const FRONTEND_BASE_URL = isDev ? 'https://doky.azurewebsites.net' : 'http://localhost:10010/';
const BACKEND_PING_URL = isDev
  ? 'https://doky.azure-api.net/v1/api/users/current'
  : 'http://localhost:8080/api/users/current';

module.exports = defineConfig({
  projectId: 'n2cgvk',
  retries: 2,
  e2e: {
    baseUrl: FRONTEND_BASE_URL,
    setupNodeEvents(on) {
      on('before:run', async () => {
        console.log(`[Cypress] Profile: ${isDev ? 'dev' : 'local'}`);
        console.log('Pinging frontend...');
        await pingServer(FRONTEND_BASE_URL); // Ping frontend

        console.log('Pinging backend...');
        await pingServer(BACKEND_PING_URL, 401); // Expect 401 from backend unauthenticated
      });
    },
  },
});

function pingServer(url, expectedStatus = 200) {
  return new Promise((resolve, reject) => {
    try {
      const parsed = new URL(url);
      const client = parsed.protocol === 'https:' ? https : http;

      const req = client.get(url, (res) => {
        console.log(`Server (${url}) responded with status: ${res.statusCode}`);
        if (res.statusCode === expectedStatus) {
          resolve();
        } else {
          reject(
            new Error(
              `Unexpected status from ${url}: Expected ${expectedStatus}, got ${res.statusCode}`
            )
          );
        }
      });

      req.on('error', (err) => {
        console.error(`Error pinging server (${url}):`, err.message);
        reject(err);
      });

      req.end();
    } catch (e) {
      reject(e);
    }
  });
}
