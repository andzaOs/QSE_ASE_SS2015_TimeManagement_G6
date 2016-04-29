'use strict';

import Rx from 'rx';

export default class DaoBase {
  constructor() {
    this.base = 'http://localhost:8080/web/rest/';
  }

  req(method, url, data) {
    return Rx.Observable.fromPromise($.ajax({
      url: this.base + url,
      contentType: 'application/json; charset=utf-8',
      dataType: 'json',
      data: JSON.stringify(data),
      method: method
    }));
  }

  get(url) {
    return this.req('get', url);
  }

  post(url, data) {
    return this.req('post', url, data);
  }
}
