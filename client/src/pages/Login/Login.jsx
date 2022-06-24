import React, { useCallback } from 'react';

import FormField from './FormField.jsx';
import useFormData from './useFormData.js';

export default () => {

  const { data, fields: { login, password } } = useFormData();

  const onSubmit = useCallback(event => {
    event.preventDefault();
    alert(JSON.stringify(data, null, '  '));
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