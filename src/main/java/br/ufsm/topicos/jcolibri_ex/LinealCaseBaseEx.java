package br.ufsm.topicos.jcolibri_ex;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;

import java.util.Collection;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 06/12/2018.
 */


public class LinealCaseBaseEx {

    private DataBaseConnectorEx connector;
    private Collection<CBRCase> cases;

    public LinealCaseBaseEx() {
    }

    public void init(DataBaseConnectorEx connector) {
        this.connector = connector;
        this.cases = this.connector.retrieveAllCases();
    }

    public void close() {
        this.connector.close();
    }

    public void forgetCases(Collection<CBRCase> cases) {
    }

    public Collection<CBRCase> getCases() {
        return this.cases;
    }

    public Collection<CBRCase> getCases(CaseBaseFilter filter) {
        return null;
    }

    public void learnCases(Collection<CBRCase> cases) {
        this.connector.storeCases(cases);
        this.cases.addAll(cases);
    }

}
