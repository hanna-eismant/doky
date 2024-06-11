import React from 'react';
import {Route, Routes} from 'react-router-dom';
import MainPage from '../../components/MainPage.jsx';
import Home from '../Home';
import Documents from '../Documents/Documents.jsx';
import CreateDocumentPage from '../Documents/CreateDocumentPage.jsx';
import EditDocumentPage from '../Documents/EditDocumentPage.jsx';
import UserProfile from '../UserProfile';

const MainRoutes = () => (
  <Routes>
    <Route path="/" element={<MainPage/>}>
      <Route path="" element={<Home/>}/>
      <Route path='documents' element={<Documents/>}/>
      <Route path='documents/new' element={<CreateDocumentPage/>}/>
      <Route path='documents/edit/:id' element={<EditDocumentPage/>}/>
      <Route path='profile' element={<UserProfile/>}/>
    </Route>
  </Routes>
);

export default MainRoutes;
