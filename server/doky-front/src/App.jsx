import React from 'react';
import {BrowserRouter, Navigate,  Route, Routes} from 'react-router-dom';

import Login from './pages/Login';
import Register from './pages/Register';
import BasePage from './components/BasePage.jsx';
import { useQuery } from './hooks/useQuery.js';
import { getCurrentUser } from './api/users.js';
import { useStore } from './hooks/useStore.js';

import MainPage from './components/MainPage.jsx';
import Home from './pages/Home';
import Documents from './pages//Documents/Documents.jsx';
import CreateDocumentPage from './pages/Documents/CreateDocumentPage.jsx';
import EditDocumentPage from './pages/Documents/EditDocumentPage.jsx';
import UserProfile from './pages/UserProfile';

const useAuth = () => {
  const { setUser, user: prevUser } = useStore();

  const skipQuery = !localStorage.getItem('jwt') || prevUser;

  const { data, isLoading } = useQuery(getCurrentUser, {
    skip: skipQuery
  });

  const user = data && !data.error ? data : null;

  if (!skipQuery && user) {
    setUser(user);
  }

  return {
    isAuth: !!prevUser,
    isLoading
  };
};

const getUnAuthRoutes = isAuth =>
  isAuth
    ? (
      <>
        <Route path="login" element={<Navigate to="/"/>}/>
        <Route path="register" element={<Navigate to="/"/>}/>
      </>
    )
    : (
      <>
        <Route path="login" element={<Login />}/>
        <Route path="register" element={<Register />}/>
      </>
    );

const getAuthRoutes = isAuth =>
  isAuth
    ? (
      <Route path="/" element={<MainPage/>}>
        <Route path="" element={<Home />}/>
        <Route path='documents' element={<Documents/>}/>
        <Route path='documents/new' element={<CreateDocumentPage/>}/>
        <Route path='documents/edit/:id' element={<EditDocumentPage/>}/>
        <Route path='profile' element={<UserProfile  />}/>
      </Route>
    ) : <Route path="*" element={<Navigate to="/login" />} />;

const App = () => {

  const { isLoading, isAuth } = useAuth();

  return !isLoading && (
    <BrowserRouter>
      <Routes>
        <Route
          element={<BasePage />}
          path="/"
        >
          {getUnAuthRoutes(isAuth)}
          {getAuthRoutes(isAuth)}
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default App;
