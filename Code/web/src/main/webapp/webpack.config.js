var path = require('path');
var util = require('util');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var webpack = require('webpack');
var autoprefixer = require('autoprefixer-core');
var pkg = require('./package.json');

var DEBUG = process.env.NODE_ENV !== 'production';

var cssBundle = path.join('css', util.format('[name].css'));
var jsBundle = path.join('js', util.format('[name].js'));

var entry = {
  app: ['./app.jsx']
};

var config = {
  context: path.join(__dirname, 'app'),
  cache: DEBUG,
  debug: DEBUG,
  target: 'web',
  devtool: DEBUG ? 'inline-source-map' : false,
  entry: entry,
  output: {
    path: path.resolve(pkg.config.publicDir),
    publicPath: '/',
    filename: jsBundle,
    pathinfo: false
  },
  module: {
    loaders: getLoaders()
  },
  postcss: [
    autoprefixer
  ],
  plugins: getPlugins(),
  resolve: {
    extensions: ['', '.js', '.json', '.jsx']
  }
};

function getPlugins() {
  var plugins = [
    new webpack.optimize.OccurenceOrderPlugin()
  ];
  if (!DEBUG) {
    plugins.push(
        new ExtractTextPlugin(cssBundle, {
          allChunks: true
        }),
        new webpack.optimize.UglifyJsPlugin({
          compress: {
            warnings: false
          }
        }),
        new webpack.optimize.DedupePlugin(),
        new webpack.DefinePlugin({
          'process.env': {
            NODE_ENV: JSON.stringify('production')
          }
        }),
        new webpack.NoErrorsPlugin()
    );
  }
  return plugins;
}

function getLoaders() {
  var jsxLoader;
  var sassLoader;
  var cssLoader;
  var fileLoader = 'file-loader?name=[path][name].[ext]';
  var eslintLoader = 'eslint-loader';
  var htmlLoader = [
    'file-loader?name=[path][name].[ext]',
    'template-html-loader?' + [
      'raw=true',
      'engine=lodash',
      'version=' + pkg.version,
      'title=' + pkg.name,
      'debug=' + DEBUG
    ].join('&')
  ].join('!');
  var jsonLoader = ['json-loader'];

  var sassParams = [
    'outputStyle=expanded',
    'includePaths[]=' + path.resolve(__dirname, './app/scss'),
    'includePaths[]=' + path.resolve(__dirname, './node_modules')
  ];
  jsxLoader = ['babel-loader?optional=runtime'];

  if (DEBUG) {
    sassParams.push('sourceMap', 'sourceMapContents=true');
    sassLoader = [
      'style-loader',
      'css-loader?sourceMap',
      'postcss-loader',
      'sass-loader?' + sassParams.join('&')
    ].join('!');
    cssLoader = [
      'style-loader',
      'css-loader?sourceMap',
      'postcss-loader'
    ].join('!');
  } else {
    sassLoader = ExtractTextPlugin.extract('style-loader', [
      'css-loader',
      'postcss-loader',
      'sass-loader?' + sassParams.join('&')
    ].join('!'));
    cssLoader = ExtractTextPlugin.extract('style-loader', [
      'css-loader',
      'postcss-loader'
    ].join('!'));
  }

  return [
    {
      test: /\.jsx?$/,
      exclude: /node_modules/,
      loaders: jsxLoader
    },
    {
      test: /\.css$/,
      loader: cssLoader
    },
    {
      test: /\.jpe?g$|\.gif$|\.png$|\.ico|\.svg$|\.woff$|\.ttf$/,
      loader: fileLoader
    },
    {
      test: /\.json$/,
      exclude: /node_modules/,
      loaders: jsonLoader
    },
    {
      test: /\.html$/,
      loader: htmlLoader
    },
    {
      test: /\.scss$/,
      loader: sassLoader
    },
    {
      test: /\.jsx?$/,
      exclude: /node_modules/,
      loader: eslintLoader
    }
  ];
}

module.exports = config;
