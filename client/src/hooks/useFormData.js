import { useState, useCallback, useMemo } from 'react';

export default (formData = {}) => {
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
  }), [data])
};
