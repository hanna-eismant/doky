import React from 'react';
import { Route, Routes} from 'react-router-dom';
import MainPage from '../../components/MainPage.jsx';
import Home from '../Home';
import Documents from '../Documents/Documents.jsx';

export default () => {
  return (
    <MainPage>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path='documents' element={<Documents />} />
      </Routes>
    </MainPage>
  );
};
