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

import {useCallback, useState} from 'react';
import {useFormData} from './useFormData';
import {noop} from '../utils';

export const useForm = (initialData, mutation, onSuccess = noop) => {
  const form = useFormData(initialData);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = useCallback(async event => {
    event.preventDefault();
    setIsSubmitting(true);
    const response = await mutation(form.data);
    if (response?.error) {
      form.setGlobalError(response.error.message);
      form.setFieldsErrors(response.fields);
    } else {
      form.setGlobalError(null);
      onSuccess();
    }
    setIsSubmitting(false);
  }, [form, mutation, onSuccess]);

  return {
    ...form,
    handleSubmit,
    isSubmitting
  };
};
