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
