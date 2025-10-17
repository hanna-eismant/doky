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

/*
 * Global Snackbar Provider for Doky frontend
 * Provides a consistent top-centered Snackbar with MUI Alert across the app
 */
import React, {createContext, useCallback, useContext, useEffect, useMemo, useState} from 'react';
import {Alert, Snackbar} from '@mui/material';
import {registerSnackbarHandlers} from './snackbarBus.js';
import {router} from '../../routing';

const GlobalSnackbarContext = createContext({
  show: () => {
  },
  showError: () => {
  },
  showSuccess: () => {
  },
  showWarning: () => {
  },
  showInfo: () => {
  },
  hide: () => {
  }
});

export const useGlobalSnackbar = () => useContext(GlobalSnackbarContext);

const DEFAULT_AUTO_HIDE = 5000; // ms
const DEFAULT_CLEAR_ON_ROUTE_CHANGE = {error: true, warning: false, info: false, success: false};

const GlobalSnackbarProvider = ({
                                  children,
                                  autoHideDuration = DEFAULT_AUTO_HIDE,
                                  clearOnRouteChange = DEFAULT_CLEAR_ON_ROUTE_CHANGE
                                }) => {
  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState('');
  const [severity, setSeverity] = useState('info'); // 'error' | 'warning' | 'info' | 'success'

  const hide = useCallback(() => setOpen(false), []);

  const show = useCallback((msg, sev = 'info') => {
    if (!msg) return;
    setMessage(String(msg));
    setSeverity(sev);
    setOpen(true);
  }, []);

  const showError = useCallback((msg) => show(msg, 'error'), [show]);
  const showSuccess = useCallback((msg) => show(msg, 'success'), [show]);
  const showWarning = useCallback((msg) => show(msg, 'warning'), [show]);
  const showInfo = useCallback((msg) => show(msg, 'info'), [show]);

  const value = useMemo(() => ({
    show,
    showError,
    showSuccess,
    showWarning,
    showInfo,
    hide
  }), [show, showError, showSuccess, showWarning, showInfo, hide]);

  // Register handlers so non-React modules (e.g., API layer) can emit global messages
  useEffect(() => {
    registerSnackbarHandlers({showError, showInfo, showWarning, showSuccess});
  }, [showError, showInfo, showWarning, showSuccess]);

  const handleClose = useCallback((event, reason) => {
    // ignore clickaway to avoid accidental dismiss when user clicks elsewhere
    if (reason === 'clickaway') return;
    hide();
  }, [hide]);

  // Close snackbar on route change depending on severity configuration
  useEffect(() => {
    if (!router || !router.subscribe) return;
    const unsubscribe = router.subscribe((state) => {
      // state has { location, navigation, ... }
      if (open && clearOnRouteChange && clearOnRouteChange[severity]) {
        setOpen(false);
      }
    });
    return () => {
      if (typeof unsubscribe === 'function') unsubscribe();
    };
  }, [open, severity, clearOnRouteChange]);

  return (
    <GlobalSnackbarContext.Provider value={value}>
      {children}
      <Snackbar
        anchorOrigin={{vertical: 'top', horizontal: 'center'}}
        open={open}
        autoHideDuration={autoHideDuration}
        onClose={handleClose}
      >
        <Alert
          elevation={6}
          // variant="filled"
          onClose={handleClose}
          severity={severity}
          sx={{width: '100%'}}
          data-cy="global-snackbar"
        >
          {message}
        </Alert>
      </Snackbar>
    </GlobalSnackbarContext.Provider>
  );
};

export default GlobalSnackbarProvider;
