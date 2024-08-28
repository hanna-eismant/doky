import React, {useId, useState} from 'react';
import {useAddToast} from '../Toasts';
import {uploadDocument} from '../../api/documents';

const SingleFileUploader = ({documentId}) => {

  const [file, setFile] = useState(null);
  const id = useId();
  const addToast = useAddToast();

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (file) {
      const formData = new FormData();
      formData.append('file', file);

      try {
        const response = await uploadDocument(documentId, formData);
        if (response.ok) {
          addToast('uploaded');
        } else {
          // todo: fetch all document info
        }
      } catch (error) {
        // todo: show message that upload failed
      }
    }
  };

  return (
    <>
      <div className="mt-4">
        <label htmlFor={id} className="form-label">Select file to upload:</label>
        <input className="form-control" type="file" id={id} onChange={handleFileChange}/>
      </div>
      {file &&
        <button type="button" className="btn btn-outline-primary me-2 mt-2" onClick={handleUpload}>
          <i className="bi bi-cloud-upload me-1"></i><span>Upload</span>
        </button>
      }
    </>
  );
};

export default SingleFileUploader;
