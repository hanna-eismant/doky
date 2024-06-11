import {useContext, useEffect} from 'react';
import {ToastsContext} from './ToastsContext.js';

export const useToastsContext = () => useContext(ToastsContext);

export const useAddToast = () => {
  const {toasts, setToasts} = useToastsContext();

  useEffect(() => {
    if (toasts.length > 0) {
      const id = setTimeout(() => {
        setToasts(toasts.toSpliced(-1));
      }, 1500);

      return () => clearTimeout(id);
    }
  }, [setToasts, toasts]);

  return toast => {
    setToasts([...toasts, toast]);
  };
};
