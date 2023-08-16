import babel from '@rollup/plugin-babel';
import copy from 'rollup-plugin-copy';
import replace from '@rollup/plugin-replace';
import {nodeResolve} from '@rollup/plugin-node-resolve';
import commonjs from '@rollup/plugin-commonjs';
import alias from '@rollup/plugin-alias';
import css from "rollup-plugin-import-css";

export default (env = 'dev') => ({
  input: "src/index.js",
  output: {
    file: "dist/bundle.js",
    format: "iife",
    sourcemap: true,
  },
  plugins: [
    css({
      output: 'bundle.css'
    }),
    copy({
      targets: [
        { src: 'static/**/*', dest: 'dist' }, 
        { src: 'node_modules/bootstrap-icons/font/fonts/**/*', dest: 'dist/fonts' }
      ]
    }),
    nodeResolve({
      extensions: [".js"],
    }),
    alias({
      entries: [
        { find: 'config', replacement: `./config.${env}.js` }
      ]
    }),
    replace({
      preventAssignment: true,
      'process.env.NODE_ENV': JSON.stringify( 'development' )
    }),
    babel({
      presets: ["@babel/preset-react"]
    }),
    commonjs()
  ]
});
