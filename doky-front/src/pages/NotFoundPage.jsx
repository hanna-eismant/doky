/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
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

import React from 'react';
import {Link} from 'react-router-dom';

export const NotFoundPage = (

  <div className='min-vh-100 d-flex flex-column'>
    <div className='row flex-grow-1 m-0'>
      <div className='col-sm-12 col-md-6 align-content-center text-center'>
        <img className='img-fluid mw-100' src='/404.svg' alt='not found'/>
      </div>
      <div className='col-sm-12 col-md-6 align-content-center'>
        <p className='text-center fs-4 m-3'>It looks like you’ve taken a wrong turn.
          Don’t worry, it happens to the best of us. Let’s get you back on track</p>
        <div className='text-center'>
          <Link to={'/'} className='btn btn-primary m-3 text-center' role='button'>Return to Home</Link>
        </div>
      </div>
    </div>
  </div>
);
