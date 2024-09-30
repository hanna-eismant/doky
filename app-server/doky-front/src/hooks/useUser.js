import {useRouteLoaderData} from 'react-router-dom';
import {mainPageRoute} from '../routing';

export const useUser = () => {
  const { user } = useRouteLoaderData(mainPageRoute.id);
  return user;
};
