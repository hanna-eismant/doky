const JWT_KEY = 'jwt';

export const getJWT = () => localStorage.getItem(JWT_KEY);

export const setJWT = jwt => {
  localStorage.setItem(JWT_KEY, jwt);
};

export const deleteJWT = () => {
  localStorage.removeItem(JWT_KEY);
};
