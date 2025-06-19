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

/* eslint-env jest */
// Test for document update functionality
import {updateDocument} from '../api/documents';
import {put} from '../api/request';

// Mock the put function from the request module
jest.mock('../api/request', () => ({
  put: jest.fn().mockResolvedValue({ok: true})
}));

describe('updateDocument function', () => {
  it('should send only name and description fields to the backend', async () => {
    // Create a test document with id, name, and description
    const testDocument = {
      id: 123,
      name: 'Test Document',
      description: 'This is a test document',
      fileName: 'test.txt',
      createdDate: '2023-01-01',
      modifiedDate: '2023-01-02'
    };

    // Call the updateDocument function with the test document
    await updateDocument(testDocument);

    // Verify that put was called with the correct arguments
    expect(put).toHaveBeenCalledWith(
      'documents/123',
      {name: 'Test Document', description: 'This is a test document'}
    );

    // Verify that only name and description were sent in the request body
    const requestBody = put.mock.calls[0][1];
    expect(Object.keys(requestBody).length).toBe(2);
    expect(requestBody).toHaveProperty('name');
    expect(requestBody).toHaveProperty('description');
    expect(requestBody).not.toHaveProperty('id');
    expect(requestBody).not.toHaveProperty('fileName');
    expect(requestBody).not.toHaveProperty('createdDate');
    expect(requestBody).not.toHaveProperty('modifiedDate');
  });
});
