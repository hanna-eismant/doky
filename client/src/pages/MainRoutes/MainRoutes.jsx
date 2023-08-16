import React from 'react';
import {Route, Routes} from 'react-router-dom';
import MainPage from '../../components/MainPage.jsx';
import Home from '../Home';
import Documents from '../Documents/Documents.jsx';
import CreateDocumentPage from '../Documents/CreateDocumentPage.jsx';

export default () => {
  return (
    <MainPage>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path='documents' element={<Documents />} />
        <Route path='documents/new' element={<CreateDocumentPage />} />
      </Routes>
    </MainPage>
  );
};
