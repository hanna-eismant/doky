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

import React, {useCallback} from 'react';
import {useNavigate} from 'react-router-dom';
import {deleteJWT} from '../../services/storage';
import {useUser} from '../../hooks/useUser';

const Home = () => {

  const navigate = useNavigate();

  const user = useUser();

  const logout = useCallback((e) => {
    e.preventDefault();
    deleteJWT();
    navigate('/login');
  }, [navigate]);

  return (
    <>
      <div
        className="d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
      </div>
      <div>
        Hello, <strong>{user.name}</strong>
      </div>
      <div>
        <a href="#" onClick={logout}>Logout</a>
      </div>
    </>
  );
};

export default Home;
