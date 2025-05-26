/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

import React from 'react';
import {useMutation} from '../../../hooks/useMutation.js';
import {downloadDocument, updateDocument, uploadDocument} from '../../../api/documents.js';
import {useForm} from '../../../hooks/useForm.js';
import AlertError from '../../../components/AlertError.jsx';
import HorizontalFormInput from '../../../components/formComponents/HorizontalFormInput.jsx';
import HorizontalFormText from '../../../components/formComponents/HorizontalFormText.jsx';
import FileUploader from '../../../components/formComponents/FileUploader.jsx';
import {saveFile} from '../../../services/save-file.js';

const EditDocumentForm = ({document}) => {
  const [editDocument, {isLoading}] = useMutation(updateDocument);

  const {data, fields: {name, description}, handleSubmit, globalError} = useForm(document, editDocument, () => {

  });

  const onUpload = async (formData) => {

    try {
      const response = await uploadDocument(document.id, formData);
      if (response.ok) {

      } else {
        console.error(response);
      }
    } catch (error) {
      console.error(error);
    }

  };

  const handleDownload = async () => {
    const body = await downloadDocument(document.id);
    saveFile(body, document.fileName);
  };

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
                <button type="button" className="btn btn-outline-primary me-2" disabled={!document.fileName} onClick={handleDownload}>
                  <i className="bi bi-cloud-download me-1"></i><span>Download</span>
                </button>
                <FileUploader onUpload={onUpload}/>
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
