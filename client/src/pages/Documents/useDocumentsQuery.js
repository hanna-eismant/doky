import {useEffect, useState} from "react";
import {get} from "../../api/request";

export default () => {
  const [isLoading, setIsLoading] = useState(true);
  const [data, setData] = useState([]);

  useEffect(() => {
    get('documents').then(documents => {
      setData(documents);
      setIsLoading(false);
    })
  }, []);

  return {data, isLoading};
}
