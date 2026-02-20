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

import React, {memo} from 'react';
import {ListItemIcon, ListItemText, MenuItem} from '@mui/material';

const DrawerMenuItem = ({
  icon: Icon,
  label,
  isExpanded,
  selected,
  onClick,
  dataCy,
}) => (
  <MenuItem
    onClick={onClick}
    selected={selected}
    sx={{
      mx: 1,
      my: 0.5,
      borderRadius: 1.5,
      '&:hover': {backgroundColor: 'rgba(0,0,0,0.15)'},
      '&.Mui-selected': {backgroundColor: 'rgba(0,0,0,0.2)'},
      '&.Mui-selected:hover': {backgroundColor: 'rgba(0,0,0,0.25)'},
      justifyContent: 'center',
      px: isExpanded ? 2 : 0,
    }}
    data-cy={dataCy}
  >
    <ListItemIcon sx={{minWidth: isExpanded ? 40 : 'auto', justifyContent: 'center', display: 'flex'}}>
      <Icon sx={{color: '#FAFAFA', width: 28, height: 28}}/>
    </ListItemIcon>
    {isExpanded && <ListItemText primary={label}/>}
  </MenuItem>
);

export default memo(DrawerMenuItem);
