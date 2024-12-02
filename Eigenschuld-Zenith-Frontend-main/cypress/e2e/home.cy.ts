describe('Home Page', () => {
    beforeEach(() => {
        cy.intercept('GET', 'http://localhost:8080/api/v1/clientprogress/client/1', { fixture: 'exerciseProgress.json' }).as('getClientExerciseProgress');

        cy.login();
        cy.visit('http://localhost:4200/home');
    });

    it('should display exercises', () => {
        cy.wait('@getClientExerciseProgress');

        cy.get('.progress-box').should('have.length', 4);

    });

    it('should navigate to the correct route on click', () => {
        cy.wait('@getClientExerciseProgress');

        cy.get('.progress-box').eq(1).click();
        cy.url().should('include', 'grafiek-gevoel/0');

        cy.visit('http://localhost:4200/home');
        cy.wait('@getClientExerciseProgress');

        cy.get('.progress-box').eq(2).click();
        cy.url().should('include', 'questions/1');

        cy.visit('http://localhost:4200/home');
        cy.wait('@getClientExerciseProgress');

        cy.get('.progress-box').eq(3).click();
        cy.url().should('include', 'questions/2');
    });
});
