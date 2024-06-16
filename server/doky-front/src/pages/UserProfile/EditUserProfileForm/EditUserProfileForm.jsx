import React from 'react';

import HorizontalFormInput from '../../../components/formComponents/HorizontalFormInput.jsx';
import {useAddToast} from '../../../components/Toasts';
import {useMutation} from '../../../hooks/useMutation.js';
import {updateCurrentUser} from '../../../api/users.js';
import {useForm} from '../../../hooks/useForm.js';
import AlertError from '../../../components/AlertError.jsx';

const EditUserProfileForm = ({user}) => {
  const [editUserProfile, {isLoading}, globalError] = useMutation(updateCurrentUser);
  const addToast = useAddToast();

  const {data, fields: {uid, name}, handleSubmit} = useForm(user, editUserProfile, () => {
    addToast('Saved');
  });

  return (
    <>
      {globalError ? <AlertError message={globalError}/> : null}
      <form onSubmit={handleSubmit} className="mt-3">
        <HorizontalFormInput id="uid" label="Email" type="text" value={data.uid} disabled={true}
          onChange={uid.setValue}/>
        <HorizontalFormInput id="name" label="Name" type="text" value={data.name} onChange={name.setValue}/>
        <div className="d-flex justify-content-between py-2">
          <input type="submit" value="Save" disabled={isLoading} className="btn btn-primary mb-3 float-right"/>
        </div>
      </form>
    </>
  );
};

export default EditUserProfileForm;
