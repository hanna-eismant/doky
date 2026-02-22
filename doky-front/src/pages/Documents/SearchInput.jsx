import React, { useMemo } from 'react';

import { debounce, InputAdornment, TextField} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';

export const SearchInput = React.memo(function SearchInput({ defaultValue, onValueChange }) {

  const debouncedOnValueChange = useMemo(() => debounce(e => {
    onValueChange(e.target.value);
  }), [onValueChange]);

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

