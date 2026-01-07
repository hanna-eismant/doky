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

const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const autoprefixer = require('autoprefixer');
const webpack = require('webpack');

module.exports = (env, argv) => {
  const beEnv = env['be-env'] || 'auto';
  const appVersion = process.env.APP_VERSION || '0.2.0-local-1234567890abcdef';
  const isDev = argv.mode === 'development';

  return {
    mode: argv.mode,
    devtool: isDev
      ? 'eval-cheap-module-source-map'
      : (env && env.sourcemap ? 'hidden-source-map' : false),
    plugins: [
      new MiniCssExtractPlugin(),
      new HtmlWebpackPlugin({template: 'src/index.html'}),
      new CopyWebpackPlugin({
        patterns: [{from: 'static'}]
      }),
      new webpack.DefinePlugin({
        '__APP_VERSION__': JSON.stringify(appVersion),
        '__DEV__': JSON.stringify(isDev)
      })
    ],
    entry: './src/index.js',
    output: {
      publicPath: '/',
      path: path.resolve(__dirname, 'dist'),
      filename: 'bundle.js'
    },
    devServer: {
      historyApiFallback: true,
      port: 10010
    },
    resolve: {
      alias: {
        config: path.resolve(__dirname, `src/config/config.${beEnv}.js`)
      }
    },
    module: {
      rules: [
        {
          test: /\.(jsx?)$/,
          exclude: /(node_modules)/,
          loader: 'babel-loader',
          options: {
            presets: [['@babel/preset-react', {runtime: 'automatic'}]]
          }
        },
        {
          test: /\.(scss|css)$/,
          use: [
            {
              loader: isDev
                ? 'style-loader'
                : MiniCssExtractPlugin.loader
            },
            {
              // Interprets `@import` and `url()` like `import/require()` and will resolve them
              loader: 'css-loader'
            },
            {
              // Loader for webpack to process CSS with PostCSS
              loader: 'postcss-loader',
              options: {
                postcssOptions: {
                  plugins: [
                    autoprefixer
                  ]
                }
              }
            },
            {
              // Loads a SASS/SCSS file and compiles it to CSS
              loader: 'sass-loader'
            }
          ]
        }
      ]
    }
  };
};
