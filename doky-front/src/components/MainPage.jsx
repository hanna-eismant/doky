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

import React from 'react';
import {Outlet, useLocation, useNavigate} from 'react-router-dom';
import {
  Avatar,
  Box,
  Container,
  Divider,
  Drawer,
  IconButton,
  Menu,
  MenuItem,
  Typography
} from '@mui/material';

import DocumentsIcon from '@mui/icons-material/ContentPaste';
import DashboardIcon from '@mui/icons-material/Dashboard';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {deleteJWT} from '../services/storage';
import {useUser} from '../hooks/useUser';
import DrawerLogo from './DrawerLogo';
import DrawerMenuItem from './DrawerMenuItem';

const DRAWER_STATE_KEY = 'doky-drawer-open';

const MainPage = () => {
  const navigate = useNavigate();
  const user = useUser();
  const collapsedDrawerWidth = 61;
  const expandedDrawerWidth = 240;

  const location = useLocation();
  const isDashboard = location.pathname === '/';
  const isDocuments = location.pathname.startsWith('/documents');

  // Initialize drawer state from localStorage, default to true if not found
  const [drawerOpen, setDrawerOpen] = React.useState(() => {
    const savedState = localStorage.getItem(DRAWER_STATE_KEY);
    return savedState !== null ? JSON.parse(savedState) : true;
  });
  const [anchorEl, setAnchorEl] = React.useState(null);
  const menuOpen = Boolean(anchorEl);

  const handleDrawerToggle = () => {
    setDrawerOpen(prevState => {
      const newState = !prevState;
      localStorage.setItem(DRAWER_STATE_KEY, JSON.stringify(newState));
      return newState;
    });
  };

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
    <Container maxWidth={false} sx={{display: 'flex', padding: 0, position: 'relative', minHeight: '100vh'}}>
      <Drawer
        sx={{
          width: drawerOpen ? expandedDrawerWidth : collapsedDrawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: drawerOpen ? expandedDrawerWidth : collapsedDrawerWidth,
            boxSizing: 'border-box',
            background: '#07689F',
            color: '#FAFAFA',
            transition: 'width 0.3s',
            overflowX: 'hidden',
            borderRight: 'none',
          },
        }}
        variant="permanent"
        anchor="left"
      >
        <Box sx={{display: 'flex', flexDirection: 'column', height: '100%'}}>
          <Box>
            <Box sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              px: drawerOpen ? 1 : 0
            }}>
              <DrawerLogo isExpanded={drawerOpen}/>
            </Box>
            <Divider color={'#FAFAFA'}/>
            <DrawerMenuItem
              icon={DashboardIcon}
              label="Dashboard"
              isExpanded={drawerOpen}
              selected={isDashboard}
              onClick={() => handleMenuItemClick('/')}
              dataCy="nav-dashboard"
            />
            <DrawerMenuItem
              icon={DocumentsIcon}
              label="Documents"
              isExpanded={drawerOpen}
              selected={isDocuments}
              onClick={() => handleMenuItemClick('/documents')}
              dataCy="nav-documents"
            />
          </Box>
          <Box sx={{mt: 'auto', mb: 1}}>
            <Box
              onClick={handleAvatarClick}
              sx={{
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                justifyContent: drawerOpen ? 'flex-start' : 'center',
                gap: drawerOpen ? 1.5 : 0,
                px: drawerOpen ? 2 : 0,
                py: 1.5
              }}>
              <Avatar
                sx={{bgcolor: '#FAFAFA', color: '#07689F', width: 40, height: 40}}

                alt={user?.name || 'User'}
                data-cy="user-avatar"
              >
                {user?.name?.[0]?.toUpperCase() || 'U'}
              </Avatar>
              {drawerOpen && (
                <>
                  <Typography variant="body2" sx={{color: '#FAFAFA', fontWeight: 500}}>
                    {user?.name || 'User'}
                  </Typography>
                  <ChevronRightIcon/>
                </>
              )}
            </Box>
            <Menu
              anchorEl={anchorEl}
              open={menuOpen}
              onClose={handleMenuClose}
              anchorOrigin={{vertical: 'top', horizontal: 'right'}}
              transformOrigin={{vertical: 'top', horizontal: 'left'}}
            >
              <MenuItem onClick={gotoProfile} data-cy="menu-profile">Profile</MenuItem>
              <MenuItem onClick={handleLogout} data-cy="menu-logout">Logout</MenuItem>
            </Menu>
          </Box>
        </Box>
      </Drawer>
      <IconButton
        onClick={handleDrawerToggle}
        sx={{
          position: 'absolute',
          left: drawerOpen ? expandedDrawerWidth - 16 : collapsedDrawerWidth - 16,
          top: '50%',
          transform: 'translateY(-50%)',
          zIndex: 1300,
          backgroundColor: '#FAFAFA',
          border: '1px solid #E0E0E0',
          width: 32,
          height: 32,
          transition: 'left 0.3s',
          '&:hover': {
            backgroundColor: '#F5F5F5',
          },
        }}
        data-cy="drawer-toggle"
      >
        {drawerOpen ? <ChevronLeftIcon sx={{fontSize: 20}}/> : <ChevronRightIcon sx={{fontSize: 20}}/>}
      </IconButton>
      <Box component="main" sx={{flexGrow: 1, width: 0}}>
        <Outlet/>
      </Box>
    </Container>
  );
};

export default MainPage;
