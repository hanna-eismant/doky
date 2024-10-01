const path = require('path');

const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const autoprefixer = require('autoprefixer');

module.exports = (env, argv) => {
  const beEnv = env['be-env'] || 'auto';
  return {
    mode: argv.mode,
    devtool: argv.mode === 'development'
      ? 'source-map'
      : false,
    plugins: [
      new MiniCssExtractPlugin(),
      new HtmlWebpackPlugin({template: 'src/index.html'}),
      new CopyWebpackPlugin({
        patterns: [{from: 'static'}]
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
      port: 10001
    },
    resolve: {
      alias: {
        config: `./config.${beEnv}.js`
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
              loader: argv.mode === 'development'
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
