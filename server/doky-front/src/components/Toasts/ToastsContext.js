import React, {useState, createContext} from 'react';
import {Toasts} from './Toasts.jsx';

export const ToastsContext = createContext();

export const ToastContextProvider = ({children}) => {
  const [toasts, setToasts] = useState([]);

  return (
    <ToastsContext.Provider value={{toasts, setToasts}}>
      <Toasts/>
      {children}
    </ToastsContext.Provider>
  );
};
