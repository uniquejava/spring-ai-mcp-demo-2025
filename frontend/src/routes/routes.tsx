import { Navigate } from 'react-router';
import Chat from '@/pages/chat/Chat.tsx';
import Layout from '@/layouts'; // export const publicRoutes = [

// export const publicRoutes = [
//   {
//     path: '/login',
//     element: <Login />,
//   },
//   {
//     path: '*',
//     element: <Navigate to="/login" />,
//   },
// ];

export const adminRoutes = [
  {
    path: '',
    element: <Layout />,
    children: [
      { path: '/', element: <Chat /> },
      { path: '*', element: <Navigate to="." /> },
    ],
  },
];
