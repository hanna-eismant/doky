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

// ***********************************************
// Custom Cypress commands for Doky
// ***********************************************
// This file defines reusable helpers to keep tests concise and robust.
//
// Usage examples:
//   cy.registerNewUser({ logoutAfter: true }).then(({ email, password }) => {
//     // use creds for login or other flows
//   });
//
// Options:
//   - logoutAfter: when true, logs out after successful registration (default: false)
//   - navigateToDocuments: when true, navigates to /documents after registration (default: false)
//
// The helper generates email in format: test.cy.<6digits>@yopmail.com

function generateTestEmail() {
  const random6 = Math.floor(100000 + Math.random() * 900000);
  return `test.cy.${random6}@yopmail.com`;
}

function defaultPassword() {
  return 'TestPwd#1234';
}

Cypress.Commands.add('registerNewUser', (options = {}) => {
  const {logoutAfter = false, navigateToDocuments = false} = options;
  const email = generateTestEmail();
  const password = defaultPassword();

  // Chainable flow: visit register -> submit -> assert dashboard
  cy.visit('/register');
  cy.get('[data-cy=register-email]', {timeout: 10000}).should('be.visible').type(email);
  cy.get('[data-cy=register-password]').type(password);
  cy.get('[data-cy=register-submit]').click();

  // After successful registration, expect redirect to dashboard
  cy.location('pathname', {timeout: 10000}).should('eq', '/');

  if (navigateToDocuments) {
    cy.then(() => {
      // Use app navigation by clicking the Documents menu in side drawer via data-cy selector.
      cy.get('[data-cy=nav-documents]', {timeout: 10000})
        .should('be.visible')
        .click({force: true});
    });
  }

  if (logoutAfter) {
    cy.get('[data-cy=user-avatar]', {timeout: 10000}).should('be.visible').click();
    cy.get('[data-cy=menu-logout]', {timeout: 10000}).click();
    cy.location('pathname', {timeout: 10000}).should('eq', '/login');
  }

  // Yield the credentials to the next command in the chain
  cy.wrap({email, password});
});

// Export utility generators for direct import if needed
export {generateTestEmail, defaultPassword};
