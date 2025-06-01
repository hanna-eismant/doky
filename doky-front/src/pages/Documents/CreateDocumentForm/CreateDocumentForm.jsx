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
import {useForm} from '../../../hooks/useForm.js';
import HorizontalFormInput from '../../../components/formComponents/HorizontalFormInput.jsx';
import HorizontalFormText from '../../../components/formComponents/HorizontalFormText.jsx';
import {useMutation} from '../../../hooks/useMutation.js';
import {createDocument} from '../../../api/documents.js';
import AlertError from '../../../components/AlertError.jsx';

const initialFormData = {
  name: '',
  description: ''
};

const CreateDocumentForm = ({onCreated}) => {
  const [documentMutation] = useMutation(createDocument);

  const {data, fields: {name, description}, handleSubmit, globalError} = useForm(
    initialFormData,
    documentMutation,
    () => {
      onCreated();
    }
  );

  return (
    <>
      {globalError ? <AlertError message={globalError}/> : null}
      <form onSubmit={handleSubmit} className="mt-3">
        <HorizontalFormInput
          id="name" label="Name" type="text" value={data.name} onChange={name.setValue}
          errors={name.errors}/>
        <HorizontalFormText
          id="description" label="Description" value={data.description}
          onChange={description.setValue}/>
        <div className="d-flex justify-content-between py-2">
          <input type="submit" value="Create" className="btn btn-primary mb-3 float-right"/>
        </div>
      </form>
    </>
  );
};

export default CreateDocumentForm;
