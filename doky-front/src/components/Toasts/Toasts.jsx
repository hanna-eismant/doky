import React from 'react';
import {useToastsContext} from './Toasts.hooks.js';

export const Toasts = () => {
  const {toasts} = useToastsContext();

  return (
    <div className="position-absolute m-2 end-0">
      {toasts.map((toast, index) =>
        <div key={index} className="p-2 m-2 text-bg-success rounded">
          <i className="bi bi-info-circle me-1"/>
          {toast}
        </div>)}
    </div>
  );
};
