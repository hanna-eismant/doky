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

import React, {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import {CssBaseline} from '@mui/material';
import { ErrorBoundary } from '@datadog/browser-rum-react';

import './index.scss';

import App from './App.jsx';
import GlobalSnackbarProvider from './components/GlobalSnackbar/GlobalSnackbarProvider.jsx';
import {initializeDatadog} from './services/datadog.js';
import {DATADOG_CONFIG} from 'config';

if (!__DEV__) {
  initializeDatadog({
    ...DATADOG_CONFIG,
    applicationId: '0ba4cfc8-b927-4961-9eef-a02bd07595ea',
    clientToken: 'pub4ba59a23137eb19900270e4f1b686acf',
    site: 'us3.datadoghq.com',
    service: 'doky.front',
    version: __APP_VERSION__
  });
}

const theme = createTheme({
  palette: {
    primary: {
      main: '#07689f'
    }
  },
  components: {
    MuiButton: {
      defaultProps: {
        size: 'small',
        variant: 'contained',
        disableElevation: true,
        loadingPosition: 'center'
      }
    }
  }
});

function ErrorFallback({ resetError, error }) {
  return (
    <p>
      Oops, something went wrong! <strong>{String(error)}</strong> <button onClick={resetError}>Retry</button>
    </p>
  );
}

const container = document.getElementById('root');
const root = createRoot(container);

root.render(
  <StrictMode>
    <ErrorBoundary fallback={ErrorFallback}>
      <ThemeProvider theme={theme}>
        <CssBaseline/>
        <GlobalSnackbarProvider>
          <App/>
        </GlobalSnackbarProvider>
      </ThemeProvider>
    </ErrorBoundary>
  </StrictMode>
);
