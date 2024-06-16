import React from 'react';
import EditUserProfileForm from './EditUserProfileForm';
import { useOutletContext } from 'react-router-dom';

const UserProfile = () => {
  const { user } = useOutletContext();

  return (
    <>
      <div
        className="d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 className="h3">
          <span className="align-middle">Your Profile</span>
        </h1>
      </div>
      <div>
        <EditUserProfileForm user={user} />
      </div>
    </>
  );
};

export default UserProfile;
