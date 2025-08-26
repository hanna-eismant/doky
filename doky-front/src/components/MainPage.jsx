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
import {Outlet, useLocation, useNavigate} from 'react-router-dom';
import {Avatar, Box, Container, Divider, Drawer, Menu, MenuItem} from '@mui/material';

import DocumentsIcon from '@mui/icons-material/ContentPaste';
import DashboardIcon from '@mui/icons-material/Dashboard';
import LogoIcon from './LogoIcon';
import {deleteJWT} from '../services/storage';

const MainPage = () => {
  const navigate = useNavigate();
  const drawerWidth = 61;

  const location = useLocation();
  const isDashboard = location.pathname === '/';
  const isDocuments = location.pathname.startsWith('/documents');

  const [anchorEl, setAnchorEl] = React.useState(null);
  const menuOpen = Boolean(anchorEl);

  const handleMenuItemClick = (path) => {
    navigate(path);
  };

  const handleAvatarClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const gotoProfile = () => {
    handleMenuClose();
    navigate('/profile');
  };

  const handleLogout = () => {
    handleMenuClose();
    deleteJWT();
    navigate('/login');
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
        <Box sx={{display: 'flex', flexDirection: 'column', height: '100%'}}>
          <Box>
            <LogoIcon size={35}/>
            <Divider color={'#FAFAFA'}/>
            <MenuItem name="Dashboard" path="/" onClick={() => handleMenuItemClick('/')} selected={isDashboard} sx={{
              '&.Mui-selected': {backgroundColor: 'rgba(0,0,0,0.2)'},
              '&.Mui-selected:hover': {backgroundColor: 'rgba(0,0,0,0.25)'}
            }}>
              <DashboardIcon sx={{color: '#FAFAFA', width: 28, height: 28, cursor: 'pointer'}}/>
            </MenuItem>
            <MenuItem name="Documents" path="/documents" onClick={() => handleMenuItemClick('/documents')}
                      selected={isDocuments} sx={{
              '&.Mui-selected': {backgroundColor: 'rgba(0,0,0,0.2)'},
              '&.Mui-selected:hover': {backgroundColor: 'rgba(0,0,0,0.25)'}
            }}>
              <DocumentsIcon sx={{color: '#FAFAFA', width: 28, height: 28, cursor: 'pointer'}}/>
            </MenuItem>
          </Box>
          <Box sx={{mt: 'auto', mb: 1, display: 'flex', justifyContent: 'center'}}>
            <Avatar
              sx={{bgcolor: '#FAFAFA', color: '#07689F', width: 40, height: 40, cursor: 'pointer'}}
              onClick={handleAvatarClick}
              alt="User"
            />
            <Menu
              anchorEl={anchorEl}
              open={menuOpen}
              onClose={handleMenuClose}
              anchorOrigin={{vertical: 'top', horizontal: 'right'}}
              transformOrigin={{vertical: 'top', horizontal: 'left'}}
            >
              <MenuItem onClick={gotoProfile}>Profile</MenuItem>
              <MenuItem onClick={handleLogout}>Logout</MenuItem>
            </Menu>
          </Box>
        </Box>
      </Drawer>
      <Outlet style={{width: '100%'}}/>
    </Container>
  );
};

export default MainPage;
