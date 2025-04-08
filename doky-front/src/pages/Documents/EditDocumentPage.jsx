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

import React, {useCallback} from 'react';
import EditDocumentForm from './EditDocumentForm/EditDocumentForm.jsx';
import {useNavigate, useParams} from 'react-router-dom';
import {useQuery} from '../../hooks/useQuery.js';
import {getDocument} from '../../api/documents.js';

const EditDocumentPage = () => {
  const navigate = useNavigate();
  const params = useParams();

  const goBack = useCallback(() => {
    navigate('/documents');
  }, [navigate]);

  const getCurrentDocument = useCallback(() => getDocument(params.id), [params.id]);

  const {data, isLoading} = useQuery(getCurrentDocument);

  return (
    <>
      <div
        className="d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 className="h3">
          <button className="btn btn-outline-primary me-3" onClick={goBack}>
            <i className="bi bi-arrow-left"></i>
          </button>
          <span className="align-middle">Edit Document</span>
        </h1>
      </div>
      <div>
        {!isLoading && <EditDocumentForm document={data}/>}
      </div>
    </>
  );
};

export default EditDocumentPage;
