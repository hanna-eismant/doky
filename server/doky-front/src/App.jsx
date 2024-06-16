import React from 'react';
import {Route, RouterProvider, createRoutesFromElements, createBrowserRouter} from 'react-router-dom';

import Login from './pages/Login';
import Register from './pages/Register';
import BasePage from './components/BasePage.jsx';
import MainRoutes from './pages/MainRoutes/MainRoutes.jsx';
import { getCurrentUser } from './api/users.js';

const userLoader = async() => {
  const rawUser = await getCurrentUser();

  return {
    user: rawUser.error ? null : rawUser
  };
};

const router = createBrowserRouter(createRoutesFromElements(
  <Route path="/" Component={BasePage} loader={userLoader}>
    {MainRoutes()}
    <Route path="login" element={<Login/>} loader={params => { console.log(params); return null; }} />
    <Route path="register" element={<Register/>}/>
  </Route>
));

const App = () => {
  return <RouterProvider router={router} />;
};


export default App;
