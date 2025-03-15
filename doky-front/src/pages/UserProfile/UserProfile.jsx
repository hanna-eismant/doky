import React from 'react';
import EditUserProfileForm from './EditUserProfileForm';
import {useUser} from '../../hooks/useUser';

const UserProfile = () => {
  const user = useUser();

  return (
    <>
      <div
        className="d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 className="h3">
          <span className="align-middle">Your Profile</span>
        </h1>
      </div>
      <div>
        <EditUserProfileForm user={user}/>
      </div>
    </>
  );
};

export default UserProfile;
