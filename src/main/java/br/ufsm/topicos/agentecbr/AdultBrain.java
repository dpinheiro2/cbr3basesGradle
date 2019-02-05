package br.ufsm.topicos.agentecbr;

import br.ufsm.topicos.cbr.TrucoDescription;
import br.ufsm.topicos.enuns.StatesDecision;
import br.ufsm.topicos.jcolibri_ex.DataBaseConnectorEx;
import br.ufsm.topicos.jcolibri_ex.LinealCaseBaseEx;
import br.ufsm.topicos.model.Card;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 11/01/2019.
 */


public class AdultBrain {

    private DataBaseConnectorEx _connector;
    private LinealCaseBaseEx _caseBase;

    private ScoutPoints scoutPoints;
    private ScoutTruco scoutTruco;

    private void init() throws InitializingException {
        _caseBase = new LinealCaseBaseEx();
        _connector = new DataBaseConnectorEx();

        _connector.buildSessionFactory();
        _caseBase.init(_connector);
    }

    public AdultBrain() throws InitializingException {
        init();
    }

    public int callPoints(int envidoLevel, boolean isHand, int pontosEnvido, ArrayList<Card> sortedCards,
                          int agentPoints, int opponentPoints, StatesDecision statesDecision) {

        // 0: NÃO CHAMAR 1: ENVIDO; 2: ENVIDO_ENVIDO; 3: REAL_ENVIDO; 4: FALTA_ENVIDO
        int action = 0;

        Collection<RetrievalResult> casesRetrivedRobo = null;
        Collection<RetrievalResult> casesRetrivedHumano = null;

        TrucoDescription queryRobo = getQueryPoints(1, isHand, pontosEnvido, sortedCards, agentPoints, opponentPoints);
        TrucoDescription queryHumano = getQueryPoints(2, isHand, pontosEnvido, sortedCards, agentPoints, opponentPoints);

        casesRetrivedRobo = getMostSimilarCases(_caseBase, queryRobo, getSimConfigPoints(queryRobo));
        casesRetrivedHumano = getMostSimilarCases(_caseBase, queryHumano, getSimConfigPoints(queryHumano));

        casesRetrivedRobo.forEach(rr -> {
            System.out.println("START::Envido::Robô --> Eval: " + rr.getEval() + " - CaseId: " + rr.get_case().getID());
        });

        casesRetrivedHumano.forEach(rr -> {
            System.out.println("START::Envido::Humano --> Eval: " + rr.getEval() + " - CaseId: " + rr.get_case().getID());
        });

        scoutPoints = new ScoutPoints();

        switch (statesDecision) {
            case START:

                sumarizarEnvidoCases(1, casesRetrivedRobo, StatesDecision.START);
                sumarizarEnvidoCases(2, casesRetrivedHumano, StatesDecision.START);

                System.out.println("countCallEnvidoWon: " + scoutPoints.getCountCallEnvidoWon());
                System.out.println("countCallEnvidoLost: " + scoutPoints.getCountCallEnvidoLost());
                System.out.println("countNoCallEnvidoWon: " + scoutPoints.getCountNoCallEnvidoWon());
                System.out.println("countNoCallEnvidoLost: " + scoutPoints.getCountNoCallEnvidoLost());

                System.out.println("countCallEnvido: " + scoutPoints.getCountCallEnvido());
                System.out.println("countCallRealEnvido: " + scoutPoints.getCountCallRealEnvido());
                System.out.println("countCallFaltaEnvido: " + scoutPoints.getCountCallFaltaEnvido());

                action = decisionMakingEnvido(StatesDecision.START);

                break;
        }

        System.out.println("Agent::START::Envido"+ action);


        return action;

    }

    public boolean callTruco(boolean isHand, ArrayList<Card> sortedCards, int agentPoints, int opponentPoints,
                             StatesDecision statesDecision) {

        boolean call = false;

        Collection<RetrievalResult> casesRetrivedRobo = null;
        Collection<RetrievalResult> casesRetrivedHumano = null;

        TrucoDescription queryRobo = getQueryTruco(1, isHand, sortedCards, agentPoints, opponentPoints);
        TrucoDescription queryHumano = getQueryTruco(2, isHand, sortedCards, agentPoints, opponentPoints);

        casesRetrivedRobo = getMostSimilarCases(_caseBase, queryRobo, getSimConfigTruco(queryRobo));
        casesRetrivedHumano = getMostSimilarCases(_caseBase, queryHumano, getSimConfigTruco(queryHumano));

        casesRetrivedRobo.forEach(rr -> {
            System.out.println("START::Truco::Robô --> Eval: " + rr.getEval() + " - CaseId: " + rr.get_case().getID());
        });

        casesRetrivedHumano.forEach(rr -> {
            System.out.println("START::Truco::Humano --> Eval: " + rr.getEval() + " - CaseId: " + rr.get_case().getID());
        });

        scoutTruco = new ScoutTruco();


        switch (statesDecision) {
            case START:

                sumarizarTrucoCases(1, casesRetrivedRobo, StatesDecision.START);
                sumarizarTrucoCases(2, casesRetrivedHumano, StatesDecision.START);

                System.out.println("countCallTrucoRodada1Won: " + scoutTruco.getCountCallTrucoRodada1Won());
                System.out.println("countCallTrucoRodada1Lost: " + scoutTruco.getCountCallTrucoRodada1Lost());
                System.out.println("countNoCallTrucoRodada1Won: " + scoutTruco.getCountNoCallTrucoRodada1Won());
                System.out.println("countNoCallTrucoRodada1Lost: " + scoutTruco.getCountNoCallTrucoRodada1Lost());


                System.out.println("countPlayedCartaAltaWon: " + scoutTruco.getCountPlayedCartaAltaWon());
                System.out.println("countPlayedCartaAltaLost: " + scoutTruco.getCountPlayedCartaAltaLost());
                System.out.println("countPlayedCartaMediaWon: " + scoutTruco.getCountPlayedCartaMediaWon());
                System.out.println("countPlayedCartaMediaLost: " + scoutTruco.getCountPlayedCartaMediaLost());
                System.out.println("countPlayedCartaBaixaWon: " + scoutTruco.getCountPlayedCartaBaixaWon());
                System.out.println("countPlayedCartaBaixaLost: " + scoutTruco.getCountPlayedCartaBaixaLost());

                System.out.println("countWentDeck: " + scoutTruco.getCountWentDeck());

                //call = decisionMakingTruco(StatesDecision.START);

                break;
        }

        //System.out.println("Agent::START::Truco"+ call);

        return call;
    }

    public Card playCard(ArrayList<Card> sortedCards, StatesDecision statesDecision) {

        Card card = null;

        Collection<RetrievalResult> casesRetrivedRobo = null;
        Collection<RetrievalResult> casesRetrivedHumano = null;

        switch (statesDecision) {
            case START:
                card = decisionMakingPlayCard(sortedCards, statesDecision);
                break;
        }

        System.out.println("Agent::START::PlayCard"+ card != null ? card.toString(): "null");

        return card;
    }

    private void sumarizarEnvidoCases(int who, Collection<RetrievalResult> casesRetrived, StatesDecision statesDecision) {


        int countCallEnvidoWon = scoutPoints.getCountCallEnvidoWon();
        int countCallEnvidoLost = scoutPoints.getCountCallEnvidoLost();
        int countNoCallEnvidoWon = scoutPoints.getCountNoCallEnvidoWon();
        int countNoCallEnvidoLost = scoutPoints.getCountNoCallEnvidoLost();

        int countCallEnvidoHonestWon = scoutPoints.getCountCallEnvidoHonestWon();
        int countCallEnvidoHonestLost = scoutPoints.getCountCallEnvidoHonestLost();
        int countNoCallEnvidoHonestWon = scoutPoints.getCountNoCallEnvidoHonestWon();
        int countNoCallEnvidoHonestLost = scoutPoints.getCountNoCallEnvidoHonestLost();
        int countCallEnvidoDeceptiveWon = scoutPoints.getCountCallEnvidoDeceptiveWon();
        int countCallEnvidoDeceptiveLost = scoutPoints.getCountCallEnvidoDeceptiveLost();
        int countNoCallEnvidoDeceptiveWon = scoutPoints.getCountNoCallEnvidoDeceptiveWon();
        int countNoCallEnvidoDeceptiveLost = scoutPoints.getCountNoCallEnvidoDeceptiveLost();

        int countTypeEnvidoDeception1 = scoutPoints.getCountTypeEnvidoDeception1();
        int countTypeEnvidoDeception2 = scoutPoints.getCountTypeEnvidoDeception2();
        int countTypeEnvidoDeception3 = scoutPoints.getCountTypeEnvidoDeception3();
        int countTypeEnvidoDeception4 = scoutPoints.getCountTypeEnvidoDeception4();
        int countTypeEnvidoDeception5 = scoutPoints.getCountTypeEnvidoDeception5();

        int countCallEnvido = scoutPoints.getCountCallEnvido();
        int countCallRealEnvido = scoutPoints.getCountCallRealEnvido();
        int countCallFaltaEnvido = scoutPoints.getCountCallFaltaEnvido();




        for (RetrievalResult rr : casesRetrived) {
            TrucoDescription caso = (TrucoDescription) rr.get_case().getDescription();

            switch (statesDecision) {
                case START:
                    // 0: NÃO CHAMAR 1: ENVIDO; 2: ENVIDO_ENVIDO; 3: REAL_ENVIDO; 4: FALTA_ENVIDO
                    if (who == 1) {

                        if (caso.getQuemPediuEnvido() != null && caso.getQuemPediuEnvido() == 1) {

                            countCallEnvido++;

                            if (caso.getQuemGanhouEnvido() != null && caso.getQuemGanhouEnvido() == 1)
                                countCallEnvidoWon++;
                            else
                                countCallEnvidoLost++;

                           /* if (caso.getRoboMentiuEnvido() != null) {
                                incrementTypeDeception(caso.getRoboMentiuEnvido());
                            }*/

                        } else if (caso.getQuemPediuEnvido() == null && caso.getQuemPediuRealEnvido() != null &&
                                caso.getQuemPediuRealEnvido() == 1) {

                            countCallRealEnvido++;

                            if (caso.getQuemGanhouEnvido() != null && caso.getQuemGanhouEnvido() == 1)
                                countCallEnvidoWon++;
                            else
                                countCallEnvidoLost++;

                           /* if (caso.getRoboMentiuEnvido() != null) {
                                incrementTypeDeception(caso.getRoboMentiuEnvido());
                            }*/

                        } else if (caso.getQuemPediuEnvido() == null && caso.getQuemPediuRealEnvido() == null &&
                                caso.getQuemPediuFaltaEnvido() != null && caso.getQuemPediuFaltaEnvido() == 1) {

                            countCallFaltaEnvido++;

                            if (caso.getQuemGanhouEnvido() != null && caso.getQuemGanhouEnvido() == 1)
                                countCallEnvidoWon++;
                            else
                                countCallEnvidoLost++;

                            /*if (caso.getRoboMentiuEnvido() != null) {
                                incrementTypeDeception(caso.getRoboMentiuEnvido());
                            }*/

                        } else {

                            if (caso.getQuemGanhouEnvido() != null) {
                                if ( caso.getQuemGanhouEnvido() == 1)
                                    countNoCallEnvidoWon++;
                                else
                                    countNoCallEnvidoLost++;
                            }


                            /*if (caso.getRoboMentiuEnvido() != null) {
                                incrementTypeDeception(caso.getRoboMentiuEnvido());
                            }*/

                        }

                    } else {

                        if (caso.getQuemPediuEnvido() != null && caso.getQuemPediuEnvido() == 2) {

                            countCallEnvido++;

                            if (caso.getQuemGanhouEnvido() != null && caso.getQuemGanhouEnvido() == 2)
                                countCallEnvidoWon++;
                            else
                                countCallEnvidoLost++;

                        } else if (caso.getQuemPediuEnvido() == null && caso.getQuemPediuRealEnvido() != null &&
                                caso.getQuemPediuRealEnvido() == 2) {

                            countCallRealEnvido++;

                            if (caso.getQuemGanhouEnvido() != null && caso.getQuemGanhouEnvido() == 2)
                                countCallEnvidoWon++;
                            else
                                countCallEnvidoLost++;

                        } else if (caso.getQuemPediuEnvido() == null && caso.getQuemPediuRealEnvido() == null &&
                                caso.getQuemPediuFaltaEnvido() != null && caso.getQuemPediuFaltaEnvido() == 2) {

                            countCallFaltaEnvido++;

                            if (caso.getQuemGanhouEnvido() != null && caso.getQuemGanhouEnvido() == 2)
                                countCallEnvidoWon++;
                            else
                                countCallEnvidoLost++;

                        } else {

                            if (caso.getQuemGanhouEnvido() != null) {
                                if ( caso.getQuemGanhouEnvido() == 2)
                                    countNoCallEnvidoWon++;
                                else
                                    countNoCallEnvidoLost++;
                            }
                        }

                    }

                    break;
            }

        }

        scoutPoints.setCountCallEnvidoWon(countCallEnvidoWon);
        scoutPoints.setCountCallEnvidoLost(countCallEnvidoLost);
        scoutPoints.setCountNoCallEnvidoWon(countNoCallEnvidoWon);
        scoutPoints.setCountNoCallEnvidoLost(countNoCallEnvidoLost);

        scoutPoints.setCountCallEnvidoHonestWon(countCallEnvidoHonestWon);
        scoutPoints.setCountCallEnvidoHonestLost(countCallEnvidoHonestLost);
        scoutPoints.setCountNoCallEnvidoHonestWon(countNoCallEnvidoHonestWon);
        scoutPoints.setCountNoCallEnvidoHonestLost(countNoCallEnvidoHonestLost);
        scoutPoints.setCountCallEnvidoDeceptiveWon(countCallEnvidoDeceptiveWon);
        scoutPoints.setCountCallEnvidoDeceptiveLost(countCallEnvidoDeceptiveLost);
        scoutPoints.setCountNoCallEnvidoDeceptiveWon(countNoCallEnvidoDeceptiveWon);
        scoutPoints.setCountNoCallEnvidoDeceptiveLost(countNoCallEnvidoDeceptiveLost);

        scoutPoints.setCountTypeEnvidoDeception1(countTypeEnvidoDeception1);
        scoutPoints.setCountTypeEnvidoDeception2(countTypeEnvidoDeception2);
        scoutPoints.setCountTypeEnvidoDeception3(countTypeEnvidoDeception3);
        scoutPoints.setCountTypeEnvidoDeception4(countTypeEnvidoDeception4);
        scoutPoints.setCountTypeEnvidoDeception5(countTypeEnvidoDeception5);

        scoutPoints.setCountCallEnvido(countCallEnvido);
        scoutPoints.setCountCallRealEnvido(countCallRealEnvido);
        scoutPoints.setCountCallFaltaEnvido(countCallFaltaEnvido);

    }

    private void sumarizarTrucoCases(int who, Collection<RetrievalResult> casesRetrived, StatesDecision statesDecision) {

        int countCallTrucoRodada1Won = scoutTruco.getCountCallTrucoRodada1Won();
        int countCallTrucoRodada1Lost = scoutTruco.getCountCallTrucoRodada1Lost();
        int countNoCallTrucoRodada1Won = scoutTruco.getCountNoCallTrucoRodada1Won();
        int countNoCallTrucoRodada1Lost = scoutTruco.getCountNoCallTrucoRodada1Lost();

        int countPlayedCartaAltaWon = scoutTruco.getCountPlayedCartaAltaWon();
        int countPlayedCartaAltaLost = scoutTruco.getCountPlayedCartaAltaLost();
        int countPlayedCartaMediaWon = scoutTruco.getCountPlayedCartaMediaWon();
        int countPlayedCartaMediaLost = scoutTruco.getCountPlayedCartaMediaLost();
        int countPlayedCartaBaixaWon = scoutTruco.getCountPlayedCartaBaixaWon();
        int countPlayedCartaBaixaLost = scoutTruco.getCountPlayedCartaBaixaLost();
        int countWentDeck = scoutTruco.getCountWentDeck();

        for (RetrievalResult rr : casesRetrived) {
            TrucoDescription caso = (TrucoDescription) rr.get_case().getDescription();

            switch (statesDecision) {
                case START:
                    if (who == 1) {

                        if (caso.getQuemTruco() != null) {
                            if (caso.getQuemTruco() == 1) {
                                if (caso.getQuandoTruco() != null && caso.getQuandoTruco() == 1) {
                                    if (caso.getQuemGanhouTruco() != null) {
                                        if (caso.getQuemGanhouTruco() == 1) {
                                            countCallTrucoRodada1Won++;
                                        } else {
                                            countCallTrucoRodada1Lost++;
                                        }
                                    }
                                }
                            } else {
                                if (caso.getQuandoTruco() != null && caso.getQuandoTruco() == 1) {
                                    if (caso.getQuemGanhouTruco() != null) {
                                        if (caso.getQuemGanhouTruco() == 1) {
                                            countNoCallTrucoRodada1Won++;
                                        } else {
                                            countNoCallTrucoRodada1Lost++;
                                        }
                                    }
                                }
                            }
                        }

                        if (caso.getPrimeiraCartaRobo() != null) {
                            if (caso.getPrimeiraCartaRobo() == caso.getCartaAltaRobo()) {
                                if (caso.getQuemGanhouTruco() != null) {
                                    if (caso.getQuemGanhouTruco() == 1) {
                                        countPlayedCartaAltaWon++;
                                    } else {
                                        countPlayedCartaAltaLost++;
                                    }
                                }
                            } else if (caso.getPrimeiraCartaRobo() == caso.getCartaMediaRobo()) {
                                if (caso.getQuemGanhouTruco() == 1) {
                                    countPlayedCartaMediaWon++;
                                } else {
                                    countPlayedCartaMediaLost++;
                                }
                            } else if (caso.getPrimeiraCartaRobo() == caso.getCartaBaixaRobo()) {
                                if (caso.getQuemGanhouTruco() == 1) {
                                    countPlayedCartaBaixaWon++;
                                } else {
                                    countPlayedCartaBaixaLost++;
                                }
                            }
                        }

                        if (caso.getQuemBaralho() != null && caso.getQuandoBaralho() != null) {
                            if (caso.getQuemBaralho() == 1 && caso.getQuandoBaralho() == 1 &&
                                    caso.getPrimeiraCartaRobo() == null) {
                                countWentDeck++;
                            }
                        }


                    } else {
                        if (caso.getQuemTruco() != null) {
                            if (caso.getQuemTruco() == 2) {
                                if (caso.getQuandoTruco() != null && caso.getQuandoTruco() == 1) {
                                    if (caso.getQuemGanhouTruco() != null) {
                                        if (caso.getQuemGanhouTruco() == 2) {
                                            countCallTrucoRodada1Won++;
                                        } else {
                                            countCallTrucoRodada1Lost++;
                                        }
                                    }
                                }
                            } else {
                                if (caso.getQuandoTruco() != null && caso.getQuandoTruco() == 1) {
                                    if (caso.getQuemGanhouTruco() != null) {
                                        if (caso.getQuemGanhouTruco() == 2) {
                                            countNoCallTrucoRodada1Won++;
                                        } else {
                                            countNoCallTrucoRodada1Lost++;
                                        }
                                    }
                                }
                            }
                        }

                        if (caso.getPrimeiraCartaHumano() != null) {
                            if (caso.getPrimeiraCartaHumano() == caso.getCartaAltaHumano()) {
                                if (caso.getQuemGanhouTruco() != null) {
                                    if (caso.getQuemGanhouTruco() == 2) {
                                        countPlayedCartaAltaWon++;
                                    } else {
                                        countPlayedCartaAltaLost++;
                                    }
                                }
                            } else if (caso.getPrimeiraCartaHumano() == caso.getCartaMediaHumano()) {
                                if (caso.getQuemGanhouTruco() == 2) {
                                    countPlayedCartaMediaWon++;
                                } else {
                                    countPlayedCartaMediaLost++;
                                }
                            } else if (caso.getPrimeiraCartaHumano() == caso.getCartaBaixaHumano()) {
                                if (caso.getQuemGanhouTruco() == 2) {
                                    countPlayedCartaBaixaWon++;
                                } else {
                                    countPlayedCartaBaixaLost++;
                                }
                            }
                        }

                        if (caso.getQuemBaralho() != null && caso.getQuandoBaralho() != null) {
                            if (caso.getQuemBaralho() == 2 && caso.getQuandoBaralho() == 1 &&
                                    caso.getPrimeiraCartaHumano() == null) {
                                countWentDeck++;
                            }
                        }

                    }
                    break;
            }
        }

        scoutTruco.setCountCallTrucoRodada1Won(countCallTrucoRodada1Won);
        scoutTruco.setCountCallTrucoRodada1Lost(countCallTrucoRodada1Lost);
        scoutTruco.setCountNoCallTrucoRodada1Won(countNoCallTrucoRodada1Won);
        scoutTruco.setCountNoCallTrucoRodada1Lost(countNoCallTrucoRodada1Lost);

        scoutTruco.setCountPlayedCartaAltaWon(countPlayedCartaAltaWon);
        scoutTruco.setCountPlayedCartaAltaLost(countPlayedCartaAltaLost);
        scoutTruco.setCountPlayedCartaMediaWon(countPlayedCartaMediaWon);
        scoutTruco.setCountPlayedCartaMediaLost(countPlayedCartaMediaLost);
        scoutTruco.setCountPlayedCartaBaixaWon(countPlayedCartaBaixaWon);
        scoutTruco.setCountPlayedCartaBaixaLost(countPlayedCartaBaixaLost);
        scoutTruco.setCountWentDeck(countWentDeck);
    }

    private int decisionMakingEnvido(StatesDecision statesDecision) {

        // 0: NÃO CHAMAR 1: ENVIDO; 2: ENVIDO_ENVIDO; 3: REAL_ENVIDO; 4: FALTA_ENVIDO
        int action = 0;
        boolean call = false;

        switch (statesDecision) {
            case START:
                /** Decide a ação mais promissora de acordo com a jogada que mais ganhou */

                if ((scoutPoints.getCountCallEnvidoWon() > scoutPoints.getCountCallEnvidoLost() &&
                scoutPoints.getCountCallEnvidoWon() > scoutPoints.getCountNoCallEnvidoWon()) || (
                        scoutPoints.getCountCallEnvidoWon() < scoutPoints.getCountCallEnvidoLost() &&
                        scoutPoints.getCountCallEnvidoWon() > scoutPoints.getCountNoCallEnvidoWon() &&
                                scoutPoints.getCountCallEnvidoLost() < scoutPoints.getCountNoCallEnvidoLost())) {
                    call = true;
                } else if ((scoutPoints.getCountCallEnvidoWon() == scoutPoints.getCountNoCallEnvidoWon()) ||
                        (scoutPoints.getCountCallEnvidoLost() == scoutPoints.getCountNoCallEnvidoLost())) {
                    Random r = new Random();
                    call = r.nextBoolean();
                } else {
                    call = false;
                }

                if (call) {
                    if (scoutPoints.getCountCallFaltaEnvido() > scoutPoints.getCountCallEnvido() &&
                            scoutPoints.getCountCallFaltaEnvido() > scoutPoints.getCountCallRealEnvido()) {
                        action = 4;
                    } else if (scoutPoints.getCountCallRealEnvido() > scoutPoints.getCountCallEnvido() &&
                            scoutPoints.getCountCallRealEnvido() > scoutPoints.getCountCallFaltaEnvido()) {
                        action = 3;
                    } else {
                        action = 1;
                    }
                } else
                    action = 0;

                break;
        }


        return action;
    }

    private boolean decisionMakingTruco(StatesDecision statesDecision) {

        boolean call = false;

        switch (statesDecision) {
            case START:

                if (((scoutTruco.getCountCallTrucoRodada1Won() + scoutTruco.getCountNoCallTrucoRodada1Won()) > 5) &&
                        (scoutTruco.getCountCallTrucoRodada1Won() > scoutTruco.getCountCallTrucoRodada1Lost() &&
                                scoutTruco.getCountCallTrucoRodada1Won() > scoutTruco.getCountNoCallTrucoRodada1Won()) || (
                        scoutTruco.getCountCallTrucoRodada1Won() < scoutTruco.getCountCallTrucoRodada1Lost() &&
                                scoutTruco.getCountCallTrucoRodada1Won() > scoutTruco.getCountNoCallTrucoRodada1Won() &&
                                scoutTruco.getCountCallTrucoRodada1Lost() < scoutTruco.getCountNoCallTrucoRodada1Lost())) {
                    call = true;
                } else if (((scoutTruco.getCountCallTrucoRodada1Won() + scoutTruco.getCountNoCallTrucoRodada1Won()) > 5) &&
                        (scoutTruco.getCountCallTrucoRodada1Won() == scoutTruco.getCountNoCallTrucoRodada1Won()) ||
                        (scoutTruco.getCountCallTrucoRodada1Lost() == scoutTruco.getCountNoCallTrucoRodada1Lost())) {
                    Random r = new Random();
                    call = r.nextBoolean();
                } else {
                    call = false;
                }

                break;
        }

        return call;
    }

    private Card decisionMakingPlayCard(ArrayList<Card> sortedCards, StatesDecision statesDecision) {


        Card card = null;

        int sumPlayedCards = scoutTruco.getCountPlayedCartaAltaWon() + scoutTruco.getCountPlayedCartaAltaLost() +
                scoutTruco.getCountPlayedCartaMediaWon() + scoutTruco.getCountPlayedCartaMediaLost() +
                scoutTruco.getCountPlayedCartaBaixaWon() + scoutTruco.getCountPlayedCartaBaixaLost();

        int sumPlayedCardsWon = scoutTruco.getCountPlayedCartaAltaWon() + scoutTruco.getCountPlayedCartaMediaWon() +
                scoutTruco.getCountPlayedCartaBaixaWon();

        int sumPlayedCardsLost = scoutTruco.getCountPlayedCartaAltaLost() + scoutTruco.getCountPlayedCartaMediaLost() +
                scoutTruco.getCountPlayedCartaBaixaLost();


        switch (statesDecision) {
            case START:

                if (sumPlayedCardsWon >= sumPlayedCardsLost) {

                    if (scoutTruco.getCountWentDeck() > sumPlayedCardsWon) {
                        card = null;
                    } else {
                        if (scoutTruco.getCountPlayedCartaAltaWon() > scoutTruco.getCountPlayedCartaMediaWon() &&
                                scoutTruco.getCountPlayedCartaAltaWon() > scoutTruco.getCountPlayedCartaBaixaWon()) {
                            card = sortedCards.get(2);
                        } else if (scoutTruco.getCountPlayedCartaMediaWon() > scoutTruco.getCountPlayedCartaBaixaWon()) {
                            card = sortedCards.get(1);
                        } else {
                            card = sortedCards.get(0);
                        }
                    }
                } else {
                    if (sumPlayedCardsLost >= (sumPlayedCards * 0.7)) {
                        card = null;
                    } else {
                        if (scoutTruco.getCountPlayedCartaAltaLost() < scoutTruco.getCountPlayedCartaMediaLost() &&
                                scoutTruco.getCountPlayedCartaAltaLost() < scoutTruco.getCountPlayedCartaBaixaLost()) {
                            card = sortedCards.get(2);
                        } else if (scoutTruco.getCountPlayedCartaMediaLost() < scoutTruco.getCountPlayedCartaBaixaLost()) {
                            card = sortedCards.get(1);
                        } else {
                            card = sortedCards.get(0);
                        }
                    }
                }
                break;
        }

        return card;
    }


    private TrucoDescription getQueryPoints(int who, boolean isHand, int pontosEnvido, ArrayList<Card> sortedCards,
                                            int agentPoints, int opponentPoints) {

        TrucoDescription query = new TrucoDescription();

        // 1-Robo 2-Humano
        if (who == 1) {
            query.setJogadorMao(isHand ? 1 : 2);
            query.setPontosEnvidoRobo(pontosEnvido);
            query.setTentosAnterioresRobo(agentPoints);
            query.setTentosAnterioresHumano(opponentPoints);

        } else {
            query.setJogadorMao(isHand ? 2 : 1);
            query.setPontosEnvidoHumano(pontosEnvido);
            query.setTentosAnterioresHumano(agentPoints);
            query.setTentosAnterioresRobo(opponentPoints);
        }

        return query;
    }

    private TrucoDescription getQueryTruco(int who, boolean isHand, ArrayList<Card> sortedCards,
                                            int agentPoints, int opponentPoints) {

        TrucoDescription query = new TrucoDescription();

        // 1-Robo 2-Humano
        if (who == 1) {
            query.setJogadorMao(isHand ? 1 : 2);
            query.setCartaAltaRobo(sortedCards.get(2).getCbrCode());
            query.setCartaMediaRobo(sortedCards.get(1).getCbrCode());
            query.setCartaBaixaRobo(sortedCards.get(0).getCbrCode());
            query.setTentosAnterioresRobo(agentPoints);
            query.setTentosAnterioresHumano(opponentPoints);

        } else {
            query.setJogadorMao(isHand ? 2 : 1);
            query.setCartaAltaHumano(sortedCards.get(2).getCbrCode());
            query.setCartaMediaHumano(sortedCards.get(1).getCbrCode());
            query.setCartaBaixaHumano(sortedCards.get(0).getCbrCode());
            query.setTentosAnterioresHumano(agentPoints);
            query.setTentosAnterioresRobo(opponentPoints);
        }

        return query;
    }

    private TrucoDescription getQueryPlayCard(int who, boolean isHand, ArrayList<Card> sortedCards,
                                           int agentPoints, int opponentPoints) {

        TrucoDescription query = new TrucoDescription();

        // 1-Robo 2-Humano
        if (who == 1) {
            query.setJogadorMao(isHand ? 1 : 2);
            query.setCartaAltaRobo(sortedCards.get(2).getCbrCode());
            query.setCartaMediaRobo(sortedCards.get(1).getCbrCode());
            query.setCartaBaixaRobo(sortedCards.get(0).getCbrCode());
            query.setTentosAnterioresRobo(agentPoints);
            query.setTentosAnterioresHumano(opponentPoints);

        } else {
            query.setJogadorMao(isHand ? 2 : 1);
            query.setCartaAltaHumano(sortedCards.get(2).getCbrCode());
            query.setCartaMediaHumano(sortedCards.get(1).getCbrCode());
            query.setCartaBaixaHumano(sortedCards.get(0).getCbrCode());
            query.setTentosAnterioresHumano(agentPoints);
            query.setTentosAnterioresRobo(opponentPoints);
        }

        return query;
    }

    private NNConfig getSimConfigPoints(TrucoDescription gameStateQuery) {

        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new Average());

        if (gameStateQuery.getJogadorMao() != null) {
            Attribute jogadorMao = new Attribute("jogadorMao", TrucoDescription.class);
            simConfig.addMapping(jogadorMao, new Equal()); //TODO: caso não encontre como mão fazer nova consulta como pé
            simConfig.setWeight(jogadorMao, 2.0);
        }

        if (gameStateQuery.getPontosEnvidoRobo() != null) {
            Attribute pontosEnvidoRobo = new Attribute("pontosEnvidoRobo", TrucoDescription.class);
            simConfig.addMapping(pontosEnvidoRobo, new Interval(33));
            simConfig.setWeight(pontosEnvidoRobo, 3.0);
        }

        if (gameStateQuery.getCartaAltaRobo() != null) {
            Attribute cartaAltaRobo = new Attribute("cartaAltaRobo", TrucoDescription.class);
            simConfig.addMapping(cartaAltaRobo, new Interval(52));
            simConfig.setWeight(cartaAltaRobo, 1.0);
        }

        if (gameStateQuery.getCartaMediaRobo() != null) {
            Attribute cartaMediaRobo = new Attribute("cartaMediaRobo", TrucoDescription.class);
            simConfig.addMapping(cartaMediaRobo, new Interval(52));
            simConfig.setWeight(cartaMediaRobo, 1.0);
        }

        if (gameStateQuery.getCartaBaixaRobo() != null) {
            Attribute cartaBaixaRobo = new Attribute("cartaBaixaRobo", TrucoDescription.class);
            simConfig.addMapping(cartaBaixaRobo, new Interval(52));
            simConfig.setWeight(cartaBaixaRobo, 1.0);
        }

        if (gameStateQuery.getTentosAnterioresRobo() != null) {
            Attribute tentosAnterioresRobo = new Attribute("tentosAnterioresRobo", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresRobo, new Interval(24));
            simConfig.setWeight(tentosAnterioresRobo, 1.0);
        }

        if (gameStateQuery.getPontosEnvidoHumano() != null) {
            Attribute pontosEnvidoHumano = new Attribute("pontosEnvidoHumano", TrucoDescription.class);
            simConfig.addMapping(pontosEnvidoHumano, new Interval(52));
            simConfig.setWeight(pontosEnvidoHumano, 3.0);
        }

        if (gameStateQuery.getCartaAltaHumano() != null) {
            Attribute cartaAltaHumano = new Attribute("cartaAltaHumano", TrucoDescription.class);
            simConfig.addMapping(cartaAltaHumano, new Interval(52));
            simConfig.setWeight(cartaAltaHumano, 1.0);
        }

        if (gameStateQuery.getCartaMediaHumano() != null) {
            Attribute cartaMediaHumano = new Attribute("cartaMediaHumano", TrucoDescription.class);
            simConfig.addMapping(cartaMediaHumano, new Interval(52));
            simConfig.setWeight(cartaMediaHumano, 1.0);
        }

        if (gameStateQuery.getCartaBaixaHumano() != null) {
            Attribute cartaBaixaHumano = new Attribute("cartaBaixaHumano", TrucoDescription.class);
            simConfig.addMapping(cartaBaixaHumano, new Interval(52));
            simConfig.setWeight(cartaBaixaHumano, 1.0);
        }

        if (gameStateQuery.getTentosAnterioresHumano() != null) {
            Attribute tentosAnterioresHumano = new Attribute("tentosAnterioresHumano", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresHumano, new Interval(24));
            simConfig.setWeight(tentosAnterioresHumano, 1.0);
        }

        return simConfig;

    }

    private NNConfig getSimConfigTruco(TrucoDescription gameStateQuery) {

        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new Average());

        if (gameStateQuery.getJogadorMao() != null) {
            Attribute jogadorMao = new Attribute("jogadorMao", TrucoDescription.class);
            simConfig.addMapping(jogadorMao, new Equal());
            simConfig.setWeight(jogadorMao, 3.0);
        }

        if (gameStateQuery.getCartaAltaRobo() != null) {
            Attribute cartaAltaRobo = new Attribute("cartaAltaRobo", TrucoDescription.class);
            simConfig.addMapping(cartaAltaRobo, new Interval(52));
            simConfig.setWeight(cartaAltaRobo, 2.0);
        }

        if (gameStateQuery.getCartaMediaRobo() != null) {
            Attribute cartaMediaRobo = new Attribute("cartaMediaRobo", TrucoDescription.class);
            simConfig.addMapping(cartaMediaRobo, new Interval(52));
            simConfig.setWeight(cartaMediaRobo, 2.0);
        }

        if (gameStateQuery.getCartaBaixaRobo() != null) {
            Attribute cartaBaixaRobo = new Attribute("cartaBaixaRobo", TrucoDescription.class);
            simConfig.addMapping(cartaBaixaRobo, new Interval(52));
            simConfig.setWeight(cartaBaixaRobo, 2.0);
        }

        if (gameStateQuery.getTentosAnterioresRobo() != null) {
            Attribute tentosAnterioresRobo = new Attribute("tentosAnterioresRobo", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresRobo, new Interval(24));
            simConfig.setWeight(tentosAnterioresRobo, 1.0);
        }

        if (gameStateQuery.getCartaAltaHumano() != null) {
            Attribute cartaAltaHumano = new Attribute("cartaAltaHumano", TrucoDescription.class);
            simConfig.addMapping(cartaAltaHumano, new Interval(52));
            simConfig.setWeight(cartaAltaHumano, 2.0);
        }

        if (gameStateQuery.getCartaMediaHumano() != null) {
            Attribute cartaMediaHumano = new Attribute("cartaMediaHumano", TrucoDescription.class);
            simConfig.addMapping(cartaMediaHumano, new Interval(52));
            simConfig.setWeight(cartaMediaHumano, 2.0);
        }

        if (gameStateQuery.getCartaBaixaHumano() != null) {
            Attribute cartaBaixaHumano = new Attribute("cartaBaixaHumano", TrucoDescription.class);
            simConfig.addMapping(cartaBaixaHumano, new Interval(52));
            simConfig.setWeight(cartaBaixaHumano, 2.0);
        }

        if (gameStateQuery.getTentosAnterioresHumano() != null) {
            Attribute tentosAnterioresHumano = new Attribute("tentosAnterioresHumano", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresHumano, new Interval(24));
            simConfig.setWeight(tentosAnterioresHumano, 1.0);
        }

        return simConfig;

    }

    private NNConfig getSimConfigPlayCard(TrucoDescription gameStateQuery) {

        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new Average());

        if (gameStateQuery.getJogadorMao() != null) {
            Attribute jogadorMao = new Attribute("jogadorMao", TrucoDescription.class);
            simConfig.addMapping(jogadorMao, new Equal());
            simConfig.setWeight(jogadorMao, 3.0);
        }

        if (gameStateQuery.getCartaAltaRobo() != null) {
            Attribute cartaAltaRobo = new Attribute("cartaAltaRobo", TrucoDescription.class);
            simConfig.addMapping(cartaAltaRobo, new Interval(52));
            simConfig.setWeight(cartaAltaRobo, 2.0);
        }

        if (gameStateQuery.getCartaMediaRobo() != null) {
            Attribute cartaMediaRobo = new Attribute("cartaMediaRobo", TrucoDescription.class);
            simConfig.addMapping(cartaMediaRobo, new Interval(52));
            simConfig.setWeight(cartaMediaRobo, 2.0);
        }

        if (gameStateQuery.getCartaBaixaRobo() != null) {
            Attribute cartaBaixaRobo = new Attribute("cartaBaixaRobo", TrucoDescription.class);
            simConfig.addMapping(cartaBaixaRobo, new Interval(52));
            simConfig.setWeight(cartaBaixaRobo, 2.0);
        }

        if (gameStateQuery.getTentosAnterioresRobo() != null) {
            Attribute tentosAnterioresRobo = new Attribute("tentosAnterioresRobo", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresRobo, new Interval(24));
            simConfig.setWeight(tentosAnterioresRobo, 1.0);
        }

        if (gameStateQuery.getCartaAltaHumano() != null) {
            Attribute cartaAltaHumano = new Attribute("cartaAltaHumano", TrucoDescription.class);
            simConfig.addMapping(cartaAltaHumano, new Interval(52));
            simConfig.setWeight(cartaAltaHumano, 2.0);
        }

        if (gameStateQuery.getCartaMediaHumano() != null) {
            Attribute cartaMediaHumano = new Attribute("cartaMediaHumano", TrucoDescription.class);
            simConfig.addMapping(cartaMediaHumano, new Interval(52));
            simConfig.setWeight(cartaMediaHumano, 2.0);
        }

        if (gameStateQuery.getCartaBaixaHumano() != null) {
            Attribute cartaBaixaHumano = new Attribute("cartaBaixaHumano", TrucoDescription.class);
            simConfig.addMapping(cartaBaixaHumano, new Interval(52));
            simConfig.setWeight(cartaBaixaHumano, 2.0);
        }

        if (gameStateQuery.getTentosAnterioresHumano() != null) {
            Attribute tentosAnterioresHumano = new Attribute("tentosAnterioresHumano", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresHumano, new Interval(24));
            simConfig.setWeight(tentosAnterioresHumano, 1.0);
        }

        return simConfig;

    }

    private Collection<RetrievalResult> getMostSimilarCases(LinealCaseBaseEx _caseBase, TrucoDescription gameStateQuery, NNConfig simConfig) {

        Collection<RetrievalResult> results = null;
        CBRQuery query = new CBRQuery();
        query.setDescription(gameStateQuery);

        results = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
        results = SelectCases.selectTopKRR(results, 10);

        return results;
    }


    private void incrementTypeDeception(int typeDeception) {

        int countTypeEnvidoDeception1 = scoutPoints.getCountTypeEnvidoDeception1();
        int countTypeEnvidoDeception2 = scoutPoints.getCountTypeEnvidoDeception2();
        int countTypeEnvidoDeception3 = scoutPoints.getCountTypeEnvidoDeception3();
        int countTypeEnvidoDeception4 = scoutPoints.getCountTypeEnvidoDeception4();
        int countTypeEnvidoDeception5 = scoutPoints.getCountTypeEnvidoDeception5();

        switch (typeDeception) {
            case 1:
                countTypeEnvidoDeception1++;
                break;
            case 2:
                countTypeEnvidoDeception2++;
                break;
            case 3:
                countTypeEnvidoDeception3++;
                break;
            case 4:
                countTypeEnvidoDeception4++;
                break;
            case 5:
                countTypeEnvidoDeception5++;
                break;
        }

        scoutPoints.setCountTypeEnvidoDeception1(countTypeEnvidoDeception1);
        scoutPoints.setCountTypeEnvidoDeception2(countTypeEnvidoDeception2);
        scoutPoints.setCountTypeEnvidoDeception3(countTypeEnvidoDeception3);
        scoutPoints.setCountTypeEnvidoDeception4(countTypeEnvidoDeception4);
        scoutPoints.setCountTypeEnvidoDeception5(countTypeEnvidoDeception5);
    }


}
