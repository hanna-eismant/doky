/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
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

import {useContext, useEffect} from 'react';
import {ToastsContext} from './ToastsContext.js';

export const useToastsContext = () => useContext(ToastsContext);

export const useAddToast = () => {
  const {toasts, setToasts} = useToastsContext();

  useEffect(() => {
    if (toasts.length > 0) {
      const id = setTimeout(() => {
        setToasts(toasts.toSpliced(-1));
      }, 1500);

      return () => clearTimeout(id);
    }
  }, [setToasts, toasts]);

  return toast => {
    setToasts([...toasts, toast]);
  };
};
