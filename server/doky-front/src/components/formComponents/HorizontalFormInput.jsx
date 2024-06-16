import React, {useCallback} from 'react';
import {noop} from '../../utils';
import classNames from 'classnames';

const HorizontalFormInput = ({label, id, type, value = '', disabled = false, onChange = noop, errors}) => {
  const handleOnChange = useCallback(event => {
    event.preventDefault();
    onChange(event.target.value);
  }, [onChange]);

  const hasErrors = Boolean(errors && errors.length > 0);

  const inputClassesList = classNames('form-control', {
    'is-invalid': hasErrors
  });

  return (
    <div>
      <label className="form-label" htmlFor={id}>{label}:</label>
      <div className="has-validation">
        <input
          className={inputClassesList} id={id} type={type} value={value} disabled={disabled}
          onChange={handleOnChange}
          aria-describedby={'validation' + id + 'Feedback'} />
        {hasErrors ?
          <div id={'validation' + id + 'Feedback'} className="invalid-feedback">
            {errors.map((message) => (<div key={message}>{message}</div>))}
          </div>
          : null}
      </div>
    </div>
  );
};

export default HorizontalFormInput;
