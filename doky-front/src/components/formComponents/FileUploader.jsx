/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
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
