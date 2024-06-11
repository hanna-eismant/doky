import {useState, useCallback, useMemo} from 'react';

export const useFormData = initialData => {
  const [data, setData] = useState(initialData);
  const [globalError, setGlobalError] = useState(null);

  const setValue = useCallback((key, value) => {
    setData(data => ({...data, [key]: value}));
  }, [setData]);

  const fields = useMemo(() => Object.keys(data).reduce((form, key) => ({
    ...form,
    [key]: {
      value: data[key],
      setValue: value => setValue(key, value)
    },
  }), {}), [data, setValue]);

  const setFieldsErrors = useCallback(errors => {
    for (const error of errors) {
      fields[error.field].errors = error.messages;
    }
  }, [fields]);

  return {fields, data, setFieldsErrors, globalError, setGlobalError};
};
