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

const {defineConfig} = require("cypress");
const https = require("https");


module.exports = defineConfig({
  projectId: 'n2cgvk',
  retries: 2,
  e2e: {
    baseUrl: 'https://doky.azurewebsites.net',
    setupNodeEvents(on, config) {
      on('before:run', async () => {
        console.log('Pinging frontend...');
        await pingServer(config.baseUrl); // Ping frontend

        console.log('Pinging backend...');
        await pingServer('https://doky.azure-api.net/v1/api/users/current', 401); // Ping backend
      });
    }
  }
});

function pingServer(url, expectedStatus = 200) {
  return new Promise((resolve, reject) => {
    const req = https.get(url, (res) => {
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
  });
}
