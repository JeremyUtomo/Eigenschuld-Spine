describe('Login', () => {
    beforeEach(() => {
        cy.intercept('POST', 'http://localhost:8080/api/v1/auth/login', { fixture: 'loginResponse.json' }).as('loginRequest');
        cy.intercept('GET', 'http://localhost:8080/api/v1/user/getRole/1', { role: 'CLIENT' }).as('getRoleRequest');
        cy.visit('http://localhost:4200/');
    });

    it('should successfully log in', () => {
        cy.get('#email').type('test@example.com');
        cy.get('#password').type('password');

        cy.get('button[type="login"]').click();

        cy.wait('@loginRequest').then((interception) => {
            assert.isNotNull(interception.response.body, 'Login API heeft een response');

            cy.getCookie('token').should('exist').and('have.property', 'value', 'mock-token');
            cy.getCookie('userId').should('exist').and('have.property', 'value', '1');
        });

        cy.wait('@getRoleRequest').then((interception) => {
            assert.isNotNull(interception.response.body, 'getRole API heeft een response');
            assert.equal(interception.response.body.role, 'CLIENT', 'De rol is CLIENT');
        });

        cy.url({ timeout: 10000 }).should('include', '/home');
    });

    it('should display error message on failed login', () => {
        cy.intercept('POST', 'http://localhost:8080/api/v1/auth/login', {
            statusCode: 401,
            body: { message: 'wrong credentials' },
        }).as('loginRequestFailed');

        cy.get('#email').type('test@example.com');
        cy.get('#password').type('wrongpassword');

        cy.get('button[type="login"]').click();

        cy.wait('@loginRequestFailed');
        cy.get('.error-message').should('contain', 'wrong credentials');
    });
});
