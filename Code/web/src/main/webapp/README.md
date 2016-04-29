Porr time management client side app
===================================

The project is built with React which is written by Facebook. They propose to use the Flux pattern in favor of MVC.
You may get a brief introduction of React and Flux at https://facebook.github.io/react/docs/getting-started.html.

All async stuff is handled with the reactive pattern (http://reactivex.io/, Rx.JS library).

### Building

Compiling of the client side app is handled by node.js you have to install it in order to compile the client side project (https://nodejs.org/).
Goto `web/src/main/webapp` directory, here is the npm project root.

To install the dependencies run `npm install`.

Run `npm build to build` a production version.

### Development workflow

Goto `web/src/main/webapp` directory, here is the npm project root.

* run `npm run start` to start the dev server.
* Begin to edit files, changes are automatically compiled and copied to the public/ dir.
* Be sure to run `npm run lint` before committing to check for errors.

Be sure to turn off caching when reloading the page.

The developer tools may be helpful when developing (`F12` in chrome) - also the React Developer extension for chrome may be helpful.


### Tasks

To run npm tasks goto `web/src/main/webapp` directory, here is the npm project root.

* ./gradlew web:war - creates the war
* npm build - builds a production version
* npm run start - runs a local development webserver on port 8081 (serves the files under public/ and recompiles on changes)
* npm run lint - lints (checks) the source code for common errors

