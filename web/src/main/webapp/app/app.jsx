'use strict';

import 'babel-core/polyfill';
import './scss/app.scss';
import './index.html';

import React from 'react';
import Router from 'react-router';
import routes from './routes';
import UserStore from './stores/UserStore';

import Login from './components/Login/Login.jsx';

// <development>
let userOverride = /userOverride=([^&]+)/.exec(location.search);
if (userOverride !== null && userOverride.length >= 1) {
  UserStore.login(userOverride[1], userOverride[1]).subscribe(u => UserStore.setCurrentUser(u));
}
// </development>

UserStore.currentUser().subscribe(user => {
  if (user === null) {
    $(document.body).addClass('login-page');
    $(document.body).removeClass('skin-blue');

    React.render(<Login/>, document.getElementById('app'));
  } else {
    $(document.body).removeClass('login-page');
    $(document.body).addClass('skin-blue');

    Router.run(routes, function (Handler) {
      React.render(<Handler/>, document.getElementById('app'));
    });
  }
});

