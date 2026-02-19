/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
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
import {useUser} from '../../hooks/useUser';
import {useQuery} from '../../hooks/useQuery';
import {searchDocuments} from '../../api/documents';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Typography from '@mui/material/Typography';
import {Box, Button, Card, CardActionArea, CardContent, Skeleton, Stack} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import AttachFileIcon from '@mui/icons-material/AttachFile';

const searchPayload = {
  query: '',
  page: {
    number: 0,
    size: 5
  },
  sort: {
    property: 'modifiedDate',
    direction: 'DESC'
  }
};

const Home = () => {

  const navigate = useNavigate();

  const user = useUser();

  const search = useCallback(() => {
    return searchDocuments(searchPayload);
  }, []);

  const {isLoading, data} = useQuery(search);

  const goToCreateDocument = useCallback(() => {
    navigate('/documents/create');
  }, [navigate]);

  const goToDocument = useCallback((id) => {
    navigate(`/documents/${id}`);
  }, [navigate]);

  const goToDocuments = useCallback(() => {
    const params = new URLSearchParams({
      query: searchPayload.query,
      sort: searchPayload.sort.property,
      dir: searchPayload.sort.direction
    });
    navigate(`/documents?${params.toString()}`);
  }, [navigate]);

  return (
    <Stack spacing={5}
      sx={{
        width: '100%',
        height: '100vh',
        padding: 2,
        alignItems: 'flex-start',
        bgcolor: 'grey.100'
      }}>
      <Stack direction="row" justifyContent="space-between" alignItems="center" width="100%">
        <Breadcrumbs aria-label="breadcrumb" data-cy="breadcrumb">
          <Typography sx={{display: 'flex', alignItems: 'center', fontSize: 'inherit'}} data-cy="breadcrumb-documents">
            Home
          </Typography>
        </Breadcrumbs>
        <Button color="primary" onClick={goToCreateDocument} size="small" data-cy="documents-create-btn">
          <AddIcon sx={{mr: 0.5}} fontSize="inherit"/>
          Create
        </Button>
      </Stack>
      <Typography variant="h6" component="div" sx={{textAlign: 'center'}}>
        Welcome back, {user.name}!
      </Typography>
      <Stack direction="row" spacing={2} width="100%">
        <Card sx={{width: '25%'}} elevation={2} data-cy="home-card-documents">
          <CardContent>
            <Typography variant="overline" gutterBottom>
              Last Updates
            </Typography>
            <Stack spacing={1}>
              {isLoading ? (
                Array.from({length: 5}).map((_, index) => (
                  <Card key={index} variant="outlined">
                    <CardContent>
                      <Skeleton variant="text" width="80%" height={28}/>
                      <Skeleton variant="text" width="100%"/>
                      <Skeleton variant="text" width="60%"/>
                    </CardContent>
                  </Card>
                ))
              ) : data.documents?.length === 0 ? (
                <>
                  <Typography variant="body2" color="text.secondary" sx={{textAlign: 'center', py: 2}}>
                    There is no documents yet.
                  </Typography>
                  <Button color="primary" variant="outlined" onClick={goToCreateDocument} size="small">
                    Try to create a new one
                  </Button>
                </>
              ) : (
                data.documents?.map((doc) => (
                  <Card key={doc.id} variant="outlined">
                    <CardActionArea onClick={() => goToDocument(doc.id)}>
                      <CardContent>
                        <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start'}}>
                          <Box sx={{flex: 1, pr: 1}}>
                            <Typography variant="subtitle1" component="div" gutterBottom>
                              {doc.name}
                            </Typography>
                            <Typography
                              variant="body2"
                              color="text.secondary"
                              sx={{
                                display: '-webkit-box',
                                WebkitLineClamp: 2,
                                WebkitBoxOrient: 'vertical',
                                overflow: 'hidden',
                                textOverflow: 'ellipsis'
                              }}
                            >
                              {doc.description ? (
                                doc.description
                              ) : (
                                <Typography color="text.secondary" fontStyle="italic">
                                  No description provided
                                </Typography>
                              )}
                            </Typography>
                          </Box>
                          {doc.fileName && (
                            <AttachFileIcon color="action" fontSize="small"/>
                          )}
                        </Box>
                      </CardContent>
                    </CardActionArea>
                  </Card>
                ))
              )}
              {!isLoading && data.documents?.length > 0 && (
                <Button
                  variant="text"
                  onClick={goToDocuments}
                  sx={{ mt: 2 }}
                  fullWidth
                >
                  See more
                </Button>
              )}
            </Stack>
          </CardContent>
        </Card>
      </Stack>
    </Stack>
  );
};

export default Home;
