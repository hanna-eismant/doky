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
 * Simple event bus for triggering global Snackbar from non-React code
 */
let showErrorHandler = null;
let showInfoHandler = null;
let showWarningHandler = null;
let showSuccessHandler = null;

export const registerSnackbarHandlers = ({showError, showInfo, showWarning, showSuccess}) => {
  showErrorHandler = showError || showErrorHandler;
  showInfoHandler = showInfo || showInfoHandler;
  showWarningHandler = showWarning || showWarningHandler;
  showSuccessHandler = showSuccess || showSuccessHandler;
};

export const emitGlobalError = (message) => {
  if (showErrorHandler && message) {
    showErrorHandler(String(message));
  }
};

export const emitGlobalInfo = (message) => {
  if (showInfoHandler && message) {
    showInfoHandler(String(message));
  }
};

export const emitGlobalWarning = (message) => {
  if (showWarningHandler && message) {
    showWarningHandler(String(message));
  }
};

export const emitGlobalSuccess = (message) => {
  if (showSuccessHandler && message) {
    showSuccessHandler(String(message));
  }
};
