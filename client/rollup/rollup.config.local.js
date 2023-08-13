import commonConfig from './rollup.config.common';
import serve from 'rollup-plugin-serve';
import livereload from "rollup-plugin-livereload";

export default args => {
  const config = commonConfig(args['be-env']);
  return {
    ...config,
    plugins: [
      ...config.plugins,

      serve({
        // Launch in browser (default: false)
        open: true,
        // Page to navigate to when opening the browser.
        // Remember to start with a slash.
        openPage: '/',
  
        contentBase: [ 'dist' ],
  
        // return index.html (200) instead of error page (404)
        historyApiFallback: true
      }),
      livereload({ watch: "dist" })
    ]
  };
}
