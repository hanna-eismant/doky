import React from 'react';
import EditUserProfileForm from './EditUserProfileForm';
import {useQuery} from '../../hooks/useQuery';
import {getCurrentUser} from '../../api/users';

const UserProfile = () => {
  const {isLoading, data} = useQuery(getCurrentUser);

  return (
    <>
      <div
        className="d-flex flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 className="h3">
          <span className="align-middle">Your Profile</span>
        </h1>
      </div>
      <div>
        {!isLoading && <EditUserProfileForm user={data}/>}
      </div>
    </>
  );
};

export default UserProfile;
