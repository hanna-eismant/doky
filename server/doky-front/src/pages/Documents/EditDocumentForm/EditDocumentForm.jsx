import React from 'react';
import {useAddToast} from '../../../components/Toasts';
import {useMutation} from '../../../hooks/useMutation.js';
import {updateDocument} from '../../../api/documents.js';
import {useForm} from '../../../hooks/useForm.js';
import AlertError from '../../../components/AlertError.jsx';
import HorizontalFormInput from '../../../components/formComponents/HorizontalFormInput.jsx';
import HorizontalFormText from '../../../components/formComponents/HorizontalFormText.jsx';
import SingleFileUploader from '../../../components/formComponents/SingleFileUploader.jsx';

const EditDocumentForm = ({document}) => {
  const [editDocument, {isLoading}] = useMutation(updateDocument);

  const addToast = useAddToast();
  const {data, fields: {name, description}, handleSubmit, globalError} = useForm(document, editDocument, () => {
    addToast('saved');
  });

  return (
    <div className="container-fluid">
      <div className="row">
        <div className="col">
          {globalError ? <AlertError message={globalError}/> : null}
          <form onSubmit={handleSubmit} className="mt-3">
            <HorizontalFormInput id="name" label="Name" type="text" value={data.name} onChange={name.setValue}
              errors={name.errors}/>
            <HorizontalFormText id="description" label="Description" value={data.description}
              onChange={description.setValue}/>
            <div className="mt-4">
              <div>File:</div>
              <div>{document.fileName}</div>
              <div>
                <button type="button" className="btn btn-outline-primary me-2" disabled={!document.fileName}>
                  <i className="bi bi-cloud-download me-1"></i><span>Download</span>
                </button>
                <SingleFileUploader documentId={document.id}/>
              </div>
            </div>
            <div className="d-flex justify-content-between py-2 mt-5 border-top">
              <input type="submit" value="Save" disabled={isLoading} className="btn btn-primary mb-3 float-right"/>
            </div>
          </form>
        </div>
        <div className="col col-2">
          <div className="mt-3">Created:
            <div>{document.createdDate}</div>
            <div>{document.createdBy}</div>
          </div>
          <div className="mt-3">Updated:
            <div>{document.modifiedDate}</div>
            <div>{document.modifiedBy}</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditDocumentForm;
