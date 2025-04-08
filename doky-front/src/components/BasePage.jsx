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

import React from 'react';
import {Link, Outlet} from 'react-router-dom';

const BasePage = () => (
  <>
    <header className="navbar bg-primary text-white bg-gradient sticky-top">
      <div className="container-fluid">
        <Link className="navbar-brand text-white" to="/">
          <img height="40px" src="/logo-white-no-bg.svg" alt="Doky Logo"/>
        </Link>
      </div>
    </header>
    <Outlet />
  </>
);

export default BasePage;
