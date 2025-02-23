import React, {useCallback} from 'react';
import {noop} from '../../utils';
// import classNames from 'classnames';
import { FormControl, FormLabel, TextField } from '@mui/material';

const FormInput = ({label, id, type, value = '', onChange = noop, errors}) => {

  const handleOnChange = useCallback(event => {
    event.preventDefault();
    onChange(event.target.value);
  }, [onChange]);

  const hasErrors = Boolean(errors && errors.length > 0);

  // const inputClassesList = classNames('form-control', {
  //   'is-invalid': hasErrors
  // });

  return (
    <FormControl>
      <FormLabel htmlFor={id}>{label}:</FormLabel>
      <TextField
        id={id} type={type} value={value} onChange={handleOnChange}
        variant='outlined'
        fullWidth
        aria-describedby={'validation' + id + 'Feedback'}/>
      {hasErrors ?
        <div id={'validation' + id + 'Feedback'} className='invalid-feedback'>
          {errors.map((message) => (<div key={message}>{message}</div>))}
        </div>
        : null}
    </FormControl>
  );
};

export default FormInput;
