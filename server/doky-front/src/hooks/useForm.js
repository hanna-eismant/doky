import {useCallback, useState} from 'react';
import {useFormData} from './useFormData';

export const useForm = (initialData, mutation, onSuccess) => {
  const form = useFormData(initialData);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = useCallback(async event => {
    event.preventDefault();
    setIsSubmitting(true);
    const response = await mutation(form.data);
    if (response?.error) {
      form.setGlobalError(response.error.message);
      form.setFieldsErrors(response.fields);
    } else {
      onSuccess();
    }
    setIsSubmitting(false);
  }, [form, mutation, onSuccess]);

  return {
    ...form,
    handleSubmit,
    isSubmitting
  };
};
