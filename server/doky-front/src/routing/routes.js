import BasePage from '../components/BasePage.jsx';
import MainPage from '../components/MainPage.jsx';
import Documents from '../pages/Documents/Documents.jsx';
import CreateDocumentPage from '../pages/Documents/CreateDocumentPage.jsx';
import EditDocumentPage from '../pages/Documents/EditDocumentPage.jsx';
import UserProfile from '../pages/UserProfile';
import Login from '../pages/Login';
import Register from '../pages/Register';
import {NotFoundPage} from '../pages/NotFoundPage.jsx';

import Home from '../pages/Home';
import {authPageLoader, mainPageLoader} from './loaders';
import ResetPassword from '../pages/ResetPassword';

export const mainPageRoute = {
  id: 'main', // used for accessing data fetched by this route loader
  element: <MainPage />,
  path: '/',
  loader: mainPageLoader,
  children: [
    { index: true, element: <Home /> },
    { path: 'documents', element: <Documents /> },
    { path: 'documents/new', element: <CreateDocumentPage/> },
    { path: 'documents/edit/:id', element: <EditDocumentPage/> },
    { path: 'profile', element: <UserProfile  /> }
  ]
};

export const routes = [
  {
    path: '/',
    element: <BasePage />,
    children: [
      mainPageRoute,
      {
        path: 'login',
        element: <Login />,
        loader: authPageLoader
      },
      {
        path: 'register',
        element: <Register />,
        loader: authPageLoader
      },
      {
        path: 'reset-password',
        element: <ResetPassword/>,
        loader: authPageLoader
      }
    ]
  },
  { path: '*', element: NotFoundPage }
];
