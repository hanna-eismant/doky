/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
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

function generateTestDocumentName() {
  const random6 = Math.floor(100000 + Math.random() * 900000);
  return `test.cy.${random6}`;
}

describe('documents', () => {
  it('should see empty list with search, create button and breadcrumbs', () => {
    cy.registerNewUser({navigateToDocuments: true}).then(() => {
      // We should be on /documents after navigation
      cy.location('pathname', {timeout: 10000}).should('eq', '/documents');

      // Breadcrumbs: Home > Documents
      cy.get('[data-cy=breadcrumb]', {timeout: 10000})
        .should('be.visible')
        .within(() => {
          cy.get('[data-cy=breadcrumb-home]').should('be.visible');
          cy.get('[data-cy=breadcrumb-documents]').should('be.visible');
        });

      // Search field (TextField with label "Search")
      cy.get('[data-cy=documents-search-input]', {timeout: 10000}).should('be.visible');
      // Label should be present too
      cy.get('[data-cy=documents-search-label]').should('be.visible');

      // Create button visible
      cy.get('[data-cy=documents-create-btn]').should('be.visible');

      // Wait until possible loading spinner disappears
      cy.get('[role="progressbar"]').should('not.exist');

      // Table headers exist
      cy.get('[data-cy=documents-th-name]').should('be.visible');
      cy.get('[data-cy=documents-th-file]').should('be.visible');
      cy.get('[data-cy=documents-th-created]').should('be.visible');
      cy.get('[data-cy=documents-th-updated]').should('be.visible');

      // Expect empty table body (no rows) for a new user
      cy.get('[data-cy=documents-table-body] tr').should('have.length', 0);
    });
  });

  it('creates a document and is redirected to edit page with success snackbar', () => {
    const docName = generateTestDocumentName();
    const docDescription = 'Some description';

    // Register and land on Documents page
    cy.registerNewUser({navigateToDocuments: true}).then(() => {
      cy.location('pathname', {timeout: 10000}).should('eq', '/documents');

      // Spy on real API calls (no stubbing)
      cy.intercept('POST', '**/api/documents').as('createDocument');

      // Go to create form
      cy.get('[data-cy=documents-create-btn]').click();
      cy.location('pathname', {timeout: 10000}).should('eq', '/documents/new');

      // Fill the form
      cy.get('[data-cy=doc-name-input]').should('be.visible').type(docName);
      cy.get('[data-cy=doc-description-input]').should('be.visible').type(docDescription);

      // Submit
      cy.get('[data-cy=create-doc-submit]').click();

      // Assert creation call returned 2xx and get document ID
      cy.wait('@createDocument').then((interception) => {
        expect(interception.response.statusCode).to.be.within(200, 299);
        const documentId = interception.response.headers.location.split('/').pop();

        // We should be redirected to edit page for the newly created document
        cy.location('pathname', {timeout: 10000}).should('eq', `/documents/edit/${documentId}`);

        // Success snackbar is shown
        cy.get('[data-cy=global-snackbar]')
          .should('be.visible')
          .and('contain.text', 'Document created successfully');
      });
    });
  });
});
