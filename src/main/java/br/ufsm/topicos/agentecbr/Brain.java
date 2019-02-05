package br.ufsm.topicos.agentecbr;

import br.ufsm.topicos.cbr.TrucoDescription;
import br.ufsm.topicos.enuns.StatesDecision;
import br.ufsm.topicos.jcolibri_ex.LinealCaseBaseEx;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;

import java.util.Collection;
import java.util.Iterator;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 25/01/2019.
 */


public interface Brain {

    public Collection<RetrievalResult> getMostSimilarCases(LinealCaseBaseEx _caseBase, TrucoDescription gameStateQuery, NNConfig simConfig);

    public NNConfig getSimConfigTruco(TrucoDescription gameStateQuery);

    public NNConfig getSimConfigPoints(TrucoDescription gameStateQuery);

    public NNConfig getSimConfigPlayCard(TrucoDescription gameStateQuery);

    public NNConfig getSimConfigFlor(TrucoDescription gameStateQuery);

    public TrucoDescription getQueryPoints(int who, GameState gameState, StatesDecision statesDecision);

    public TrucoDescription getQueryShowPoints(int who, GameState gameState);

    public TrucoDescription getQueryTruco(int who, GameState gameState, StatesDecision statesDecision);

    public TrucoDescription getQueryPlayCard(int who, GameState gameState, StatesDecision statesDecision);

    public TrucoDescription getQueryFlor(int who, GameState gameState, StatesDecision statesDecision);

    default void printRetrievedCases(int who, Collection<RetrievalResult> retrievalResults, String phase) {

        retrievalResults.forEach(rr -> {
            if (who == 1) {
                System.out.println("::" + phase + ":: Robô --> Eval: " + rr.getEval() + " - CaseId: " + rr.get_case().getID());
            } else {
                System.out.println("::" + phase + ":: Humano --> Eval: " + rr.getEval() + " - CaseId: " + rr.get_case().getID());
            }
        });
    }


}



