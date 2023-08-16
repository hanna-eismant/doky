import {useCallback, useState} from "react";
import {post} from "../../../api/request.js";

export default () => {
  const [isLoading, setIsLoading] = useState(true);
  const [data, setData] = useState({});

  const fetch = useCallback(async (formData) => {
    setIsLoading(true);
    const data = await post('documents', formData);
    setData(data);
    setIsLoading(false);
    return data;
  }, [setIsLoading, setData]);

  return fetch;
}
