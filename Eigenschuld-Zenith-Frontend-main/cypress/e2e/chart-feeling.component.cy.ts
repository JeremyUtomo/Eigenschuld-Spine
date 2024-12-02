describe('ChartFeelingComponent Tests', () => {
    beforeEach(() => {
        cy.intercept('GET', 'http://localhost:8080/api/v1/clientprogress/client/1', { fixture: 'exerciseProgress.json' }).as('getClientExerciseProgress');
    });

    it('should not allow access if not logged in', () => {
        cy.visit('http://localhost:4200/grafiek-gevoel/1');
        cy.url().should('eq', 'http://localhost:4200/');
    });

    it('should load the page correctly when logged in', () => {
        cy.login();
        cy.visit('http://localhost:4200/grafiek-gevoel/1');
        
        cy.get('app-navbar').should('be.visible'); 
        cy.get('canvas#chart', { timeout: 10000 }).should('be.visible');
    });

    it('should delete a slice', () => {
        cy.login();
    
        cy.visit('http://localhost:4200/grafiek-gevoel/1');
    
    
        const newSliceLabel = 'Test';
        cy.get('#involved-input-mobile').type(newSliceLabel);
        cy.get('#add-button-mobile').click();
    
        cy.get('.involved-row').contains('Test').should('be.visible');
    
        cy.get('.involved-row').contains('Test').parent().find('#remove-slice').click();
    
        cy.get('.involved-row').should('not.exist');
    });
    
    it('should adjust slice percentage when "+" button is clicked', () => {
        cy.login();
    
        cy.visit('http://localhost:4200/grafiek-gevoel/1');
        
        cy.get('#involved-input-mobile').type('Slice 1');
        cy.get('#add-button-mobile').click();
    
        cy.get('.involved-row').contains('Slice 1').should('be.visible');
    
        cy.get('.involved-row').contains('Slice 1').parent().find('#increase-slice').click();
    
        cy.get('.involved-row').contains('Slice 1').parent().find('p').eq(1).should('contain', '5');
    });

    it('shouldnt adjust slice percentage when "+" button is clicked and the total is already 100', () => {
        cy.login();
    
        cy.visit('http://localhost:4200/grafiek-gevoel/1');
        
        cy.get('#involved-input-mobile').type('Slice 1');
        cy.get('#add-button-mobile').click();
    
        cy.get('.involved-row').contains('Slice 1').should('be.visible');
    
        for (let i = 0; i < 21; i++) {
            cy.get('.involved-row').contains('Slice 1').parent().find('#increase-slice').click();
        }
    
        cy.get('.involved-row').contains('Slice 1').parent().find('p').eq(1).should('not.contain', '105');
    });    
    
    it('should render the chart', () => {
        cy.login();
        cy.visit('http://localhost:4200/grafiek-gevoel/1');

        cy.get('canvas#chart', { timeout: 10000 }).should('be.visible');
    });

    it('should add a slice', () => {
        cy.login();

        cy.visit('http://localhost:4200/grafiek-gevoel/1');   

        const newSliceLabel = 'Test';
        cy.get('#involved-input-mobile').type(newSliceLabel);
        cy.get('#add-button-mobile').click();

        cy.get('.involved-row').contains('Test').should('be.visible');
    });

    it('should not add a slice without a name', () => {
        cy.login();

        cy.visit('http://localhost:4200/grafiek-gevoel/1');

        cy.get('#add-button-mobile').click();

        cy.get('.involved-row').should('have.length', 0);
    });

    it('should not save when total percentage is not 100', () => {
        cy.login();

        cy.visit('http://localhost:4200/grafiek-gevoel/1');

        cy.get('#involved-input-mobile').type('Slice 1');
        cy.get('#add-button-mobile').click();
        cy.get('#involved-input-mobile').type('Slice 2');
        cy.get('#add-button-mobile').click();

        cy.get('.mobile-next-button').should('be.enabled');

        cy.get('.mobile-next-button').click();

        cy.url().should('include', '/grafiek-gevoel/1');
    });   
});
