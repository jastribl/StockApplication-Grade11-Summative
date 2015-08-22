// Generated by CoffeeScript 1.9.3
(function() {
  var app, bodyParser, controller, express, logger, session;

  express = require('express');

  logger = require('morgan');

  bodyParser = require('body-parser');

  session = require('client-sessions');

  controller = require('./controllers/index');

  app = express();

  app.use(session({
    cookieName: 'session',
    secret: 'JustinStriblingsSecretString',
    duration: 60 * 60 * 1000,
    activeDuration: 5 * 60 * 1000
  }));

  app.set('view engine', 'jade');

  app.use(logger('dev'));

  app.use(bodyParser.json());

  app.use(bodyParser.urlencoded({
    extended: false
  }));

  app.use(express["static"]('public'));

  app.use(controller);

  if (app.get('env') === 'development') {
    app.use(function(err, req, res, next) {
      res.status(err.status || 500);
      return res.render('error', {
        message: err.message,
        error: err
      });
    });
  }

  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    return res.render('error', {
      message: err.message,
      error: {}
    });
  });

  module.exports = app;

}).call(this);
