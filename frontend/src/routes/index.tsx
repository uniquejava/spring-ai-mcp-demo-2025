import {useRoutes} from 'react-router';

import {adminRoutes} from './routes.tsx';

export const Router = () => {
  return useRoutes(adminRoutes);
};
