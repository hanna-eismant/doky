import React, { useCallback, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import FormField from './FormField.jsx';

const initialFormData = {
  login: '',
  password: ''
};

const useFormData = (formData = initialFormData) => {
  const [ data, setData ] = useState(formData);

  const setValue = useCallback((key, value) => {
    setData(data =>({ ...data, [ key ]: value }));
  }, [setData]);

  return useMemo(() => ({
    fields: Object.keys(data).reduce((form, key) => ({
      ...form,
      [ key ]: {
        value: formData[key],
        setValue: value => setValue(key, value)
      }
    }), {}),
    data
  }), [data]);
}

export default () => {

  const { data, fields: { login, password } } = useFormData();
  const navigate = useNavigate();

  const onSubmit = useCallback(event => {
    event.preventDefault();
    alert(JSON.stringify(data, null, '  '));
    navigate('/');
  });

  return (
    <form onSubmit={onSubmit}>
      <FormField id="login" label="Login" type="text" value={data.login} onChange={login.setValue} />
      <FormField id="password" label="Password" type="password" value={data.password} onChange={password.setValue} />
      <div>
        <input type="submit" value="Login" />
      </div>
    </form>
  )
}