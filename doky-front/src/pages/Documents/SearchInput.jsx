/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
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

import React, { useMemo } from 'react';

import { debounce, InputAdornment, TextField} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';

export const SearchInput = React.memo(function SearchInput({ defaultValue, onValueChange }) {

  const debouncedOnValueChange = useMemo(() => debounce(e => {
    onValueChange(e.target.value);
  }, 500), [onValueChange]);

  return (
    <TextField
      fullWidth
      label="Search"
      id="outlined-size-small"
      defaultValue={defaultValue}
      onChange={debouncedOnValueChange}
      size="small"
      InputLabelProps={{'data-cy': 'documents-search-label'}}
      slotProps={{
        input: {
          endAdornment: (
            <InputAdornment position="end">
              <SearchIcon/>
            </InputAdornment>
          ),
        },
        htmlInput: {
          'data-cy': 'documents-search-input'
        }
      }}
    />
  );
});

