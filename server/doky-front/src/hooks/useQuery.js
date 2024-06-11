import {useEffect, useMemo, useState} from 'react';

/*
 * Decorator function that ensures a request does not called multiple times concurrently.
 * Instead, if the request is already in progress (a promise is pending),
 * it returns the same promise to any other calls
 * until the original promise is resolved or rejected.
 */
const dedupeRequest = request => {
  let pendingPromise = null;

  return () => {
    if (!pendingPromise) {
      /*
       * `finally` not `then`
       * cause we nullate promise even it was rejected (error occurred)
       */
      pendingPromise = request().finally(() => {
        pendingPromise = null;
      });
    }

    return pendingPromise;
  };
};

export const useQuery = request => {
  const [isLoading, setIsLoading] = useState(true);
  const [data, setData] = useState({});

  /*
   * Usage of useCallback might looks more obvious
   * `useCallback(dedupeRequest(request), [request])`
   * but leads to multiple calls of dedupeRequest decorator.
   * Resulted function still won't recreated as in applied solution but
   * dedupeRequest(request) will be called multiple times
   * This leads to multiple creation of unused closures
   */
  const dedupedRequest = useMemo(() => dedupeRequest(request), [request]);

  useEffect(() => {
    dedupedRequest().then(data => {
      setData(data); // still called twice but with reference equal data
      setIsLoading(false);
    });
  }, [dedupedRequest]);

  return {data, isLoading};
};
