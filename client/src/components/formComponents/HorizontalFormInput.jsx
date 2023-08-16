import React, {useCallback} from 'react';
import {noop} from '../../utils';

export default ({label, id, type, value = '', onChange = noop}) => {
  const handleOnChange = useCallback(event => {
    event.preventDefault();
    onChange(event.target.value);
  }, [onChange]);

  return (
    <div className="row mb-3">
      <label className="col-sm-2 col-form-label" htmlFor={id}>{label}:</label>
      <div className="col-sm-10">
        <input className="form-control" id={id} type={type} value={value} onChange={handleOnChange}/>
      </div>
    </div>
  );
}
