import { useCallback, useState } from "react";
import { post } from "../../api/request";

export default () => {
  const [ isLoading, setIsLoading ] = useState(true);
  const [ data, setData ] = useState({});

  const fetch = useCallback(async (formData) => {
    setIsLoading(true);
    const data = await post('login', formData);
    if (data.token) {
      localStorage.setItem('jwt', data.token);
    }
    setData(data);
    setIsLoading(false);
    return data;
  }, [setIsLoading, setData]);

  return [ fetch, { data, isLoading } ];
}
