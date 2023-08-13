## Run Client

### Prerequisite

LTS version of [Node.js](https://nodejs.org) should be installed. nvm is recommended. 

### Commands to Run Client

* `npm i` - install dependencies. Need to execute before first run or when dependencies updated.
* `npm start` - run client. Will open new tab in browser. Life reloading is working only for js changes.
  To apply other changes re-run this command. By default, api pointing to **dev** (remote) back-end
* `npm run start:local`- run client with api pointing to **local** back-end
* `npm run start:dev` - run client with api pointing to **dev** (remote) back-end.
* `npm run serve` - run local for serving static from dist folder. Useful for checking dev configured builds locally
