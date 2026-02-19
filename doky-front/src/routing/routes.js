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

import MainPage from '../components/MainPage.jsx';
import Documents from '../pages/Documents/Documents.jsx';
import CreateDocumentPage from '../pages/Documents/CreateDocumentPage.jsx';
import DocumentPage from '../pages/Documents/DocumentPage.jsx';
import UserProfile from '../pages/UserProfile';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Home from '../pages/Home';
import {authPageLoader, mainPageLoader} from './loaders';
import ResetPassword from '../pages/ResetPassword';
import UpdatePassword from '../pages/UpdatePassword';
import ErrorPage from '../pages/Error';
import NotFoundPage from '../pages/NotFound';

export const mainPageRoute = {
  id: 'main', // used for accessing data fetched by this route loader
  element: <MainPage/>,
  path: '/',
  loader: mainPageLoader,
  errorElement: <ErrorPage/>,
  children: [
    {index: true, element: <Home/>},
    {path: 'documents', element: <Documents/>},
    {path: 'documents/create', element: <CreateDocumentPage/>},
    {path: 'documents/:id', element: <DocumentPage/>},
    {path: 'profile', element: <UserProfile/>}
  ]
};

export const routes = [
  {
    path: '/',
    children: [
      mainPageRoute,
      {
        path: 'login',
        element: <Login/>,
        loader: authPageLoader
      },
      {
        path: 'register',
        element: <Register/>,
        loader: authPageLoader
      },
      {
        path: 'password/reset',
        element: <ResetPassword/>,
        loader: authPageLoader
      },
      {
        path: 'password/update',
        element: <UpdatePassword/>,
        loader: authPageLoader
      }
    ]
  },
  {path: '*', element: <NotFoundPage/>}
];
