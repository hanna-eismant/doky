import React, {useCallback, useMemo} from 'react';
import {noop, getRowsCount, clamp} from '../../utils';

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
