# ASP_SmartBeer_GreenHouse

### Node-RED

#### To install

- `cd node-red`
- `npm install`

#### To run

- `cd node-red`
- `npm start`

### Microcontroller

After cloning the repo use call `git submodule update --init --recursive`.

### API testing

Installation of `jq` and `curl` package is required.

- `cd api`
- `./login.sh name@alumnos.upm.es` and enter password. Can be executed again even after successful login.
- Call `./user-info.sh` to display info about the currently logged in user
- Call `./api.sh` to execute generic api requests:
  - List all users: `./api.sh 'users?pageSize=10&page=0' | jq '.data'`
  - Inspect rulechain: `./api.sh 'ruleChain/73069ff0-793e-11ec-9a04-591db17ccd5b/metadata';`
