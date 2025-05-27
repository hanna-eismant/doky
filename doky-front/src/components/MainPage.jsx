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
import {Outlet, useNavigate} from 'react-router-dom';
import {Container, Divider, Drawer, MenuItem} from '@mui/material';

import DocumentsIcon from '@mui/icons-material/ContentPaste';
import DashboardIcon from '@mui/icons-material/Dashboard';
import LogoIcon from './LogoIcon';

const MainPage = () => {
  const navigate = useNavigate();
  const drawerWidth = 61;

  const handleMenuItemClick = (path) => {
    navigate(path);
  };

  return (

    <Container maxWidth={false} sx={{display: 'flex', padding: 0}}>
      <Drawer
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: drawerWidth,
            boxSizing: 'border-box',
            background: '#07689F',
            color: '#FAFAFA',
          },
        }}
        variant="permanent"
        anchor="left"
      >
        <LogoIcon/>
        <Divider color={'#FAFAFA'}/>
        <MenuItem name="Dashboard" path="/" onClick={() => handleMenuItemClick('/')}>
          <DashboardIcon fontSize={'large'} sx={{color: '#FAFAFA'}}/>
        </MenuItem>
        <MenuItem name="Documents" path="/documents" onClick={() => handleMenuItemClick('/documents')}>
          <DocumentsIcon fontSize={'large'} sx={{color: '#FAFAFA'}}/>
        </MenuItem>
      </Drawer>
      <Outlet style={{width: '100%'}}/>
    </Container>
  );
};

export default MainPage;
