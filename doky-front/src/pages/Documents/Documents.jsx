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
import {useNavigate} from 'react-router-dom';
import {useQuery} from '../../hooks/useQuery';
import {getDocuments} from '../../api/documents';

const Documents = () => {
  const {isLoading, data} = useQuery(getDocuments);

  const navigate = useNavigate();

  const goToCreateDocument = useCallback(() => {
    navigate('/documents/new');
  }, [navigate]);

  const getGoToEditDocumentHandler = id => () => {
    navigate(`/documents/edit/${id}`);
  };

  return (
    <>
      <div
        className="d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 className="h3 me-3 align-middle">Documents</h1>
        <button type="button" className="btn btn-outline-primary" onClick={goToCreateDocument}>
          <i className="bi bi-file-earmark-plus me-1"></i><span>Create</span>
        </button>
      </div>
      <div className="table-responsive">
        <table className="table table-striped table-hover">
          <thead>
            <tr>
              <th scope="col">Actions</th>
              <th scope="col">Name</th>
              <th scope="col">Modified Date</th>
            </tr>
          </thead>
          <tbody>
            {!isLoading ? data.map?.((document) => (
              <tr key={document.id}>
                <td>
                  <button
                    type="button" className="btn btn-outline-primary"
                    onClick={getGoToEditDocumentHandler(document.id)}>
                    <i className="bi bi-file-earmark-plus me-1"></i><span>Edit</span>
                  </button>
                </td>
                <td>{document.name}</td>
                <td>{document.modifiedDate}</td>
              </tr>
            )) : 'Loading'}
          </tbody>
        </table>
      </div>
    </>
  );
};

export default Documents;
