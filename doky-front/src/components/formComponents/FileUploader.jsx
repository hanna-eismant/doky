import React, {useId, useState} from 'react';

const FileUploader = ({onUpload}) => {

  const [file, setFile] = useState(null);
  const id = useId();
  const inputId = `file-input-${id}`;

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = () => {
    const formData = new FormData();
    formData.append('file', file);
    onUpload(formData);
  };

  return (
    <>
      <div className="mt-4">
        <label htmlFor={inputId} className="form-label">Select file to upload:</label>
        <input className="form-control" type="file" id={inputId} onChange={handleFileChange}/>
      </div>
      {file &&
        <button type="button" className="btn btn-outline-primary me-2 mt-2" onClick={handleUpload}>
          <i className="bi bi-cloud-upload me-1"></i><span>Upload</span>
        </button>
      }
    </>
  );
};

export default FileUploader;
