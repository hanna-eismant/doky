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

import React, {useCallback} from 'react';
import {noop} from '../../utils';
import classNames from 'classnames';

const FormInput = ({label, id, type, value = '', onChange = noop, errors}) => {

  const handleOnChange = useCallback(event => {
    event.preventDefault();
    onChange(event.target.value);
  }, [onChange]);

  const hasErrors = Boolean(errors && errors.length > 0);

  const inputClassesList = classNames('form-control', {
    'is-invalid': hasErrors
  });

  return (
    <div className='row mb-3'>
      <label className='col-form-label' htmlFor={id}>{label}:</label>
      <div className='input-group has-validation'>
        <input
          className={inputClassesList} id={id} type={type} value={value} onChange={handleOnChange}
          aria-describedby={'validation' + id + 'Feedback'}/>
        {hasErrors ?
          <div id={'validation' + id + 'Feedback'} className='invalid-feedback'>
            {errors.map((message) => (<div key={message}>{message}</div>))}
          </div>
          : null}
      </div>
    </div>
  );
};

export default FormInput;
