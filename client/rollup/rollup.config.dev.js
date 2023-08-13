import commonConfig from './rollup.config.common';
import terser from '@rollup/plugin-terser';

const config = commonConfig();

export default {
  ...config,
  plugins: [
    ...config.plugins,
    terser()
  ]
};

