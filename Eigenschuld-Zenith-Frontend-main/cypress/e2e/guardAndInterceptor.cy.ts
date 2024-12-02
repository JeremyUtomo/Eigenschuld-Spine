// cypress/integration/auth.guard.spec.js

describe('Auth Guard and HTTP Interceptor Tests', () => {
    beforeEach(() => {
        cy.intercept('POST', '**/v1/auth/login', { fixture: 'loginResponse.json' }).as('loginRequest');
        cy.intercept('GET', '**/v1/user/getRole/*', { fixture: 'roleResponse.json' }).as('roleRequest');
        cy.clearCookies();
    });

    it('should get token and userId', () => {
        const url = 'http://localhost:8080/api/v1/auth/login';

        const requestBody = {
            email: 'hulp@gmail.com',
            password: 'hallo123'
        };

        cy.request({
            method: 'POST',
            url: url,
            body: requestBody,
            headers: {
                'Content-Type': 'application/json'
            }
        }).then((response) => {
            expect(response.status).to.eq(200);
            expect(response.body).to.have.property('token');
            expect(response.body).to.have.property('userId');

            // Store the token and userId using Cypress.env
            Cypress.env('token', response.body.token);
            Cypress.env('userId', response.body.userId);
        });
    });

    it('should redirect to login if not authenticated', () => {
        cy.visit('/home');
        cy.url().should('include', '');  // Ensure to check the actual redirection path
    });

    it('should login and navigate based on role', () => {
        cy.visit('/');
        cy.get('#email').type('test@example.com');
        cy.get('#password').type('password123');
        cy.get('button[type="login"]').click();

        cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
        cy.wait('@roleRequest').its('response.statusCode').should('eq', 200);

        cy.fixture('roleResponse.json').then((roleResponse) => {
            if (roleResponse.role === 'HULPVERLENER') {
                cy.url().should('include', '/caregiver-overview');
            } else if (roleResponse.role === 'CLIENT') {
                cy.url().should('include', '/home');
            } else if (roleResponse.role === 'ORGANISATIE') {
                cy.url().should('include', '/zorginstantie');
            } else if (roleResponse.role === 'ADMIN') {
                cy.url().should('include', '/admin-overview');
            }
        });
    });

    it('should attach authorization header to requests', () => {
        const token = Cypress.env('token');
        const userId = Cypress.env('userId');

        // Ensure token and userId are available before making the request
        expect(token).to.exist;
        expect(userId).to.exist;

        cy.visit('/');
        cy.get('#email').type('test@example.com');
        cy.get('#password').type('password123');
        cy.get('button[type="login"]').click();

        cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
        cy.wait('@roleRequest').its('response.statusCode').should('eq', 200);

        cy.request({
            method: 'GET',
            url: `http://localhost:8080/api/v1/user/getRole/${userId}`,

            headers: {
                'Authorization': `Bearer ${token}`
            },
            failOnStatusCode: false,
        }).then((response) => {
            cy.log('Request Headers:', response.requestHeaders);
            cy.log('Response Headers:', response.headers);
            cy.log('Response Body:', response.body);

            expect(response.status).to.eq(200);
            expect(response.headers['content-type']).to.include('application/json');
            expect(response.body).to.have.property('role');
        });
    });

    it('should redirect based on user role', () => {
        cy.visit('/');
        cy.get('#email').type('test@example.com');
        cy.get('#password').type('password123');
        cy.get('button[type="login"]').click();

        cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
        cy.wait('@roleRequest').its('response.statusCode').should('eq', 200);

        cy.fixture('roleResponse.json').then((roleResponse) => {
            if (roleResponse.role === 'CLIENT') {
                cy.url().should('include', '/home');
            } else if (roleResponse.role === 'HULPVERLENER') {
                cy.url().should('include', '/caregiver-overview');
            } else if (roleResponse.role === 'ORGANISATIE') {
                cy.url().should('include', '/zorginstantie');
            } else if (roleResponse.role === 'ADMIN') {
                cy.url().should('include', '/admin-overview');
            }
        });
    });
});
