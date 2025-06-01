describe('doky login', () => {

  beforeEach(() => {
    cy.visit('/login');
  });

  it('should login when valid credentials', () => {
    const userUid = 'hkurh4@outlook.com';
    const password = '6OiUl^BdDU';

    cy.get('#uid', {timeout: 10000}).should('be.visible');

    cy.get('#uid').type(userUid);
    cy.get('#password').type(password);
    cy.get('.btn.btn-primary.mb-3').click();

    cy.wait(2000);
    cy.location().should((location) => {
      expect(location.pathname).to.eq('/');
    });
  });

  it('should show error message when invalid credentials', () => {
    const userUid = 'hkurh4@outlook.com';
    const password = 'password';

    cy.get('#uid', {timeout: 10000}).should('be.visible');

    cy.get('#uid').type(userUid);
    cy.get('#password').type(password);
    cy.get('.btn.btn-primary.mb-3').click();

    cy.wait(2000);
    cy.location().should((location) => {
      expect(location.pathname).to.eq('/login');
    });
    cy.get('.alert.alert-danger')
      .should('be.visible')
      .and('have.text', 'Incorrect credentials');
  });

  it('should show error message when empty credentials', () => {
    cy.get('#uid', {timeout: 10000}).should('be.visible');

    cy.get('.btn.btn-primary.mb-3').click();

    cy.wait(2000);
    cy.location().should((location) => {
      expect(location.pathname).to.eq('/login');
    });
    cy.get('.alert.alert-danger')
      .should('be.visible')
      .and('have.text', 'Validation failed');

    cy.get('#validationuidFeedback')
      .should('be.visible')
      .then((feedback) => {
        const messages = feedback.text();
        expect(messages).to.include('Should be an valid email address');
        expect(messages).to.include('Length should be from 4 to 32 characters');
        expect(messages).to.include('Email is required');
      });

    cy.get('#validationpasswordFeedback')
      .should('be.visible')
      .then((feedback) => {
        const messages = feedback.text();
        expect(messages).to.include('Password is required');
        expect(messages).to.include('Length should be from 8 to 32 characters');
      });
  });
});
