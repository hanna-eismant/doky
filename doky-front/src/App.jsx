import React from 'react';
import {RouterProvider} from 'react-router-dom';

import {router} from './routing';

const App = () =>
  <RouterProvider
    router={router}
    fallbackElement={<span>Loading...</span>}
  />;

export default App;
