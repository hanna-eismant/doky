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

describe('register', () => {
  it('should register a new user and then login successfully', () => {
    cy.registerNewUser({logoutAfter: true}).then(({email, password}) => {
      // Login with the same credentials
      cy.get('[data-cy=login-email]', {timeout: 10000}).should('be.visible').type(email);
      cy.get('[data-cy=login-password]').type(password);
      cy.get('[data-cy=login-submit]').click();

      // Expect redirect to dashboard
      cy.location('pathname', {timeout: 10000}).should('eq', '/');
    });
  });

  it('should show error when trying to register an existing email', () => {
    const existingEmail = 'hkurh4@outlook.com';
    const password = 'TestPwd#1234';

    cy.visit('/register');

    cy.get('[data-cy=register-email]', {timeout: 10000}).should('be.visible').type(existingEmail);
    cy.get('[data-cy=register-password]').type(password);
    cy.get('[data-cy=register-submit]').click();

    // Expect to remain on the register page (successful registration would redirect to '/')
    cy.location('pathname', {timeout: 10000}).should('eq', '/register');

    // A global error snackbar should appear with a message from backend, e.g. "already exists"
    cy.get('[data-cy=global-snackbar]', {timeout: 10000})
      .should('be.visible')
      .invoke('text')
      .then((text) => {
        const lower = text.toLowerCase();
        // Be tolerant to backend wording but ensure it's an "already exists"-type error
        expect(lower).to.satisfy((t) => t.includes('exist') || t.includes('already'));
      });
  });
});
