import {useState, useCallback} from 'react';

import {noop} from '../utils';

export const useMutation = (request, onDone = noop) => {
  const [isLoading, setIsLoading] = useState(false);
  const [data, setData] = useState({});

  const fetch = useCallback(async payload => {
    setIsLoading(true);
    const data = await request(payload);
    onDone(data);
    setData(data);
    setIsLoading(false);
    return data;
  }, [request, onDone]);

  return [fetch, {data, isLoading}];
};
