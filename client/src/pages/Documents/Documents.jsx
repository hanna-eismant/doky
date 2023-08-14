import React from 'react';
import {Link} from 'react-router-dom';
import useDocumentsQuery from "./useDocumentsQuery";

export default () => {
  const {isLoading, data} = useDocumentsQuery();

  return (
    <>
      <div
        className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 className="h3">Documents</h1>
      </div>
      <div className="table-responsive">
        <table className="table table-striped table-hover">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Name</th>
          </tr>
          </thead>
          <tbody>
          {!isLoading ? data.map?.((document) => (
            <tr key={document.id}>
              <td>{document.id}</td>
              <td>{document.name}</td>
            </tr>
          )) : 'Loading'}
          </tbody>
        </table>
      </div>
    </>
  );
};
