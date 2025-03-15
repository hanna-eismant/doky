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
