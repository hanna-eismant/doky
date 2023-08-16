import React, {useCallback} from 'react';
import useFormData from "../../../hooks/useFormData";
import useCreateDocumentQuery from "./useCreateDocumentQuery.js";
import HorizontalFormInput from "../../../components/formComponents/HorizontalFormInput.jsx";
import HorizontalFormText from "../../../components/formComponents/HorizontalFormText.jsx";

const initialFormData = {
  name: '',
  description: ''
};

export default ({onCreated}) => {
  const {data, fields: {name, description}} = useFormData(initialFormData);
  const createDocument = useCreateDocumentQuery();

  const onSubmit = useCallback(async event => {
    event.preventDefault();

    const response = await createDocument(data);
    if (response?.error) {
      alert(response.error.message);
    } else {
      onCreated();
    }
  });

  return (
    <form onSubmit={onSubmit} className="mt-3">
      <HorizontalFormInput id="name" label="Name" type="text" value={data.name} onChange={name.setValue}/>
      <HorizontalFormText id="description" label="Description" value={data.description}
                          onChange={description.setValue}/>
      <div className="d-flex justify-content-between py-2">
        <input type="submit" value="Create" className="btn btn-primary mb-3 float-right"/>
      </div>
    </form>
  );
};
