## Run Client

### Prerequisite

LTS version of [Node.js](https://nodejs.org) should be installed. nvm is recommended.

### Commands to Run Client

* `npm install` - install dependencies. Need to execute before first run or when dependencies updated.
* `npm start` - run client. URL to open in browser will be printed on console. By default, api pointing to **dev** (
  remote) back-end
* `npm run start:local`- run client with api pointing to **local** back-end.
* `npm run start:remote` - run client with api pointing to **dev** (remote) back-end.
* `npm run build` - assembling prod version. All assets will be saved in *dist* folder
* `npm run build:sourcemap` - assembling prod version with sourcemaps. All assets will be saved in *dist* folder
* `npm run serve` - run node server for serving static from *dist* folder. Useful for checking dev configured builds
  locally
* `npm run lint` - run eslint check
* `npm run lint:fix` - run eslint check and fix lint issues automatically

### Quality Gates

#### Pre-Commit Hooks

Before each commit eslint check running _locally_. Any eslint error prevent to commit. In this case you can try to fix
lint error automatically by running `npm run lint:fix`. Please pay attention that not all eslint errors can be fixed
automatically. There are errors which can be fixed only manually.

Pre-commit hooks installed automatically when `npm install` runs.

There is possibility to bypass this check by adding `--no-verify` flag (**not recommended**):

`git commit -m <commit_message> --no-verify`

#### Github Action

Please pay attention that eslint check performed by github action as well. So, even you bypassed pre-commit
hooks [github action](.github/workflows/lint.yml) will be failed in case of any lint errors and this prevent you to
merge your PR.
