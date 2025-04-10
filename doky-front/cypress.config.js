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
