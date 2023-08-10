import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Login from './pages/Login';
import Home from './pages/Home';
import Register from './pages/Register';

export default () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
    </Routes>
  </BrowserRouter>
)
