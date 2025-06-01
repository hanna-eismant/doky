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

import React, {useEffect, useRef, useState} from 'react';
import {useMutation} from '../../../hooks/useMutation.js';
import {downloadDocument, updateDocument, uploadDocument} from '../../../api/documents.js';
import {useForm} from '../../../hooks/useForm.js';
import {saveFile} from '../../../services/save-file.js';
import {Alert, Box, Button, Divider, Stack, TextField, Typography} from '@mui/material';
import CloudDownloadIcon from '@mui/icons-material/CloudDownload';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Cancel';
import {useNavigate} from 'react-router-dom';
import CircularProgressWithLabel from '../../../components/CircularProgressWithLabel.jsx';

const EditDocumentForm = ({document, onSaveSuccess}) => {
  const [editDocument] = useMutation(updateDocument);
  const [uploadStatus, setUploadStatus] = useState({loading: false, success: false, error: null});
  const [saveStatus, setSaveStatus] = useState({loading: false, success: false, error: null});
  const [downloadStatus, setDownloadStatus] = useState({loading: false, progress: 0});
  const fileInputRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (uploadStatus.success || uploadStatus.error) {
      const timer = setTimeout(() => {
        setUploadStatus({loading: false, success: false, error: null});
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [uploadStatus.success, uploadStatus.error]);

  useEffect(() => {
    if (saveStatus.success || saveStatus.error) {
      const timer = setTimeout(() => {
        setSaveStatus({loading: false, success: false, error: null});
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [saveStatus.success, saveStatus.error]);

  const {fields: {name, description}, handleSubmit, isSubmitting} = useForm(
    document,
    editDocument,
    () => {
      setSaveStatus({loading: false, success: true, error: null});
      if (onSaveSuccess) {
        onSaveSuccess();
      }
    },
    (error) => {
      setSaveStatus({loading: false, success: false, error: error.message || 'Failed to save changes'});
    }
  );

  useEffect(() => {
    if (isSubmitting) {
      setSaveStatus({loading: true, success: false, error: null});
    }
  }, [isSubmitting]);

  const onUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    setUploadStatus({loading: true, success: false, error: null});

    try {
      const response = await uploadDocument(document.id, formData);
      if (response.ok) {
        setUploadStatus({loading: false, success: true, error: null});
        if (onSaveSuccess) {
          onSaveSuccess();
        }
      } else {
        const errorData = await response.json();
        const errorMessage = errorData?.error?.message || 'Failed to upload file';
        setUploadStatus({loading: false, success: false, error: errorMessage});
        console.error(response);
      }
    } catch (error) {
      setUploadStatus({loading: false, success: false, error: error.message || 'An error occurred during upload'});
      console.error(error);
    }
  };

  const handleDownload = async () => {
    try {
      setDownloadStatus({loading: true, progress: 0});

      const body = await downloadDocument(document.id, (progress) => {
        setDownloadStatus({loading: true, progress});
      });

      saveFile(body, document.fileName);

      // Reset download status after a short delay
      setTimeout(() => {
        setDownloadStatus({loading: false, progress: 0});
      }, 1000);
    } catch (error) {
      console.error('Error downloading file:', error);
      setDownloadStatus({loading: false, progress: 0});
    }
  };

  const handleFileButtonClick = () => {
    fileInputRef.current.click();
  };

  return (

    <form onSubmit={handleSubmit} style={{width: '100%'}}>
      {uploadStatus.success && <Alert severity="success">File uploaded successfully!</Alert>}
      {uploadStatus.error && <Alert severity="error">{uploadStatus.error}</Alert>}
      <Stack width="100%">
        <Stack direction="row" spacing={2} width="100%"
          divider={<Divider orientation="vertical" flexItem/>}
        >
          <Stack spacing={2} sx={{flexGrow: 1, width: 'calc(100% - 270px)'}}>
            <TextField
              fullWidth
              label="Name"
              id="outlined-size-small"
              size="small"
              value={name.value}
              onChange={name.setValue}
            />

            <TextField
              fullWidth
              label="Description"
              id="outlined-size-small"
              size="small"
              value={description.value}
              onChange={description.setValue}
              multiline
              rows={5}
            />

            {document.createdBy && document.createdDate && (
              <Typography variant="caption" color="text.secondary">
                Created by: {document.createdBy} on {document.createdDate}
              </Typography>
            )}

            {document.modifiedBy && document.modifiedDate && (
              <Typography variant="caption" color="text.secondary">
                Last modified by: {document.modifiedBy} on {document.modifiedDate}
              </Typography>
            )}

          </Stack>
          <Stack width="250px" flexShrink={0}>
            <Typography variant="subtitle1" gutterBottom>
              {document.fileName}
            </Typography>
            <Stack spacing={2}>
              {document.fileName && (
                downloadStatus.loading ? (
                  <Box display="flex" alignItems="center" justifyContent="center" p={1}>
                    <CircularProgressWithLabel value={downloadStatus.progress}/>
                  </Box>
                ) : (
                  <Button
                    variant="outlined"
                    color="primary"
                    startIcon={<CloudDownloadIcon/>}
                    onClick={handleDownload}
                    fullWidth
                  >
                    Download
                  </Button>
                )
              )}

              <Button
                variant="contained"
                color="primary"
                startIcon={<CloudUploadIcon/>}
                onClick={handleFileButtonClick}
                loading={uploadStatus.loading}
                loadingPosition="start"
                fullWidth
                disableElevation
              >
                Upload New File
              </Button>
              <input
                type="file"
                ref={fileInputRef}
                style={{display: 'none'}}
                onChange={onUpload}
              />
            </Stack>
          </Stack>
        </Stack>

        <Box sx={{display: 'flex', gap: 2, mt: 2}}>
          <Button
            variant="contained"
            color="secondary"
            startIcon={<CancelIcon/>}
            onClick={() => navigate('/documents')}
            disableElevation
          >
            Cancel
          </Button>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            startIcon={<SaveIcon/>}
            loading={isSubmitting}
            loadingPosition="start"
            disableElevation
          >
            Save Changes
          </Button>
        </Box>
      </Stack>
    </form>
  );
};

export default EditDocumentForm;
