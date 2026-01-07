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

describe('login', () => {

  beforeEach(() => {
    cy.visit('/login');
  });

  it('should login when valid credentials', () => {
    const userUid = 'hkurh4@outlook.com';
    const password = '6OiUl^BdDU';

    cy.get('[data-cy=login-uid]', {timeout: 10000}).should('be.visible');

    cy.get('[data-cy=login-uid]').type(userUid);
    cy.get('[data-cy=login-password]').type(password);
    cy.get('[data-cy=login-submit]').click();

    cy.location().should((location) => {
      expect(location.pathname).to.eq('/');
    });
  });

  it('should show error message when invalid credentials', () => {
    const userUid = 'hkurh4@outlook.com';
    const password = 'password';

    cy.get('[data-cy=login-uid]', {timeout: 10000}).should('be.visible');

    cy.get('[data-cy=login-uid]').type(userUid);
    cy.get('[data-cy=login-password]').type(password);
    cy.get('[data-cy=login-submit]').click();

    cy.location().should((location) => {
      expect(location.pathname).to.eq('/login');
    });
    cy.get('[data-cy=global-snackbar]')
      .should('be.visible')
      .and('have.text', 'Incorrect credentials');
  });

  it('should show error message when empty credentials', () => {
    cy.get('[data-cy=login-uid]', {timeout: 10000}).should('be.visible');

    cy.get('[data-cy=login-submit]').click();

    cy.location().should((location) => {
      expect(location.pathname).to.eq('/login');
    });
    cy.get('[data-cy=global-snackbar]')
      .should('be.visible')
      .and('have.text', 'Validation failed');

    cy.get('[data-cy=uid-errors]')
      .should('be.visible')
      .then((feedback) => {
        const messages = feedback.text();
        expect(messages).to.include('Should be an valid email address');
        expect(messages).to.include('Length should be from 4 to 32 characters');
        expect(messages).to.include('Email is required');
      });

    cy.get('[data-cy=password-errors]')
      .should('be.visible')
      .then((feedback) => {
        const messages = feedback.text();
        expect(messages).to.include('Password is required');
        expect(messages).to.include('Length should be from 8 to 32 characters');
      });
  });
});
