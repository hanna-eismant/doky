import React, {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';

import './index.scss';

import App from './App.jsx';

const container = document.getElementById('root');
const root = createRoot(container);

root.render(
  <StrictMode>
    <App/>
  </StrictMode>
);
