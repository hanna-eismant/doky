/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

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
