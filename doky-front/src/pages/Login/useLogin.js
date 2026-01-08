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

import {useNavigate} from 'react-router-dom';
import {useMutation} from '../../hooks/useMutation';
import {login} from '../../api/users';
import {setJWT} from '../../services/storage';

export const useLogin = () => {

  const navigate = useNavigate();

  const [loginMutation] = useMutation(
    creds => login(creds.email, creds.password),
    data => {
      if (data?.token) {
        setJWT(data.token);
        navigate('/');
      }
      // Error case is handled globally in api/request.js
    }
  );

  return loginMutation;
};
