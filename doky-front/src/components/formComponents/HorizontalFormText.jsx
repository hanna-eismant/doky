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

import React, {useCallback, useMemo} from 'react';
import {clamp, getRowsCount, noop} from '../../utils';

const getClampedRowsCount = (str, min, max) => {
  const rowsCount = getRowsCount(str);
  return clamp(rowsCount, min, max);
};

const HorizontalFormText = ({
  label,
  id,
  minRows = 3,
  maxRows = 10,
  value = '',
  onChange = noop
}) => {

  const handleOnChange = useCallback(event => {
    event.preventDefault();
    onChange(event.target.value);
  }, [onChange]);

  const rowsCount = useMemo(
    () => getClampedRowsCount(value, minRows, maxRows),
    [value, minRows, maxRows]
  );

  return (
    <div className="mt-4">
      <label className="form-label" htmlFor={id}>{label}:</label>
      <textarea className="form-control" rows={rowsCount} id={id} value={value} onChange={handleOnChange}/>
    </div>
  );
};

export default HorizontalFormText;
