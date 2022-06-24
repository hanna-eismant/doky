import React, { useCallback } from 'react';
import { noop } from '../../utils';

export default ({ label, id, type, value = '', onChange = noop }) => {
  const handleOnChange = useCallback(event => {
    event.preventDefault();
    onChange(event.target.value);
  }, [onChange]);

  return (
    <div>
      <label for={id}>{label}:</label>
      <input type={type} value={value} onChange={handleOnChange} />
    </div>
  );
}
