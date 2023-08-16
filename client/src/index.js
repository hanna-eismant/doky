import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/js/index.esm.js';
import 'bootstrap-icons/font/bootstrap-icons.css';
import React, {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';

import App from './App.jsx';

const container = document.getElementById('root');
const root = createRoot(container);

root.render(
  <StrictMode>
    <App/>
  </StrictMode>
)
