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

import java.util.Collection;
import java.util.Iterator;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 25/01/2019.
 */


/** Agente recupera somente o caso mais similar e replica a ação*/

public class BabyBrain implements Brain {

    private DataBaseConnectorEx _connector;
    private LinealCaseBaseEx _caseBase;

    private int chosenCase;
    private int cartaVirada;
    private boolean irBaralho;

    private void init() throws InitializingException {
        _caseBase = new LinealCaseBaseEx();
        _connector = new DataBaseConnectorEx();

        _connector.buildSessionFactory();
        _caseBase.init(_connector);
    }

    public BabyBrain() throws InitializingException {
        init();
    }

    public int getCartaVirada() {
        return cartaVirada;
    }

    public boolean isIrBaralho() {
        return irBaralho;
    }

    @Override
    public Collection<RetrievalResult> getMostSimilarCases(LinealCaseBaseEx _caseBase, TrucoDescription gameStateQuery, NNConfig simConfig) {
        Collection<RetrievalResult> results = null;
        CBRQuery query = new CBRQuery();
        query.setDescription(gameStateQuery);

        results = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
        results = SelectCases.selectTopKRR(results, 1);
        SelectCases.selectTopK(results, 1);

        return results;
    }

    @Override
    public NNConfig getSimConfigTruco(TrucoDescription gameStateQuery) {

        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new Average());

        if (gameStateQuery.getJogadorMao() != null) {
            Attribute jogadorMao = new Attribute("jogadorMao", TrucoDescription.class);
            simConfig.addMapping(jogadorMao, new Equal());
            simConfig.setWeight(jogadorMao, 1.0);
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

        if (gameStateQuery.getQuemGanhouTruco() != null) {
            Attribute quemGanhouTruco = new Attribute("quemGanhouTruco", TrucoDescription.class);
            simConfig.addMapping(quemGanhouTruco, new Equal());
            simConfig.setWeight(quemGanhouTruco, 1.0);
        }

        if (gameStateQuery.getQuandoTruco() != null) {
            Attribute quandoTruco = new Attribute("quandoTruco", TrucoDescription.class);
            simConfig.addMapping(quandoTruco, new Equal());
            simConfig.setWeight(quandoTruco, 1.0);
        }

        if (gameStateQuery.getQuemTruco() != null) {
            Attribute quemTruco = new Attribute("quemTruco", TrucoDescription.class);
            simConfig.addMapping(quemTruco, new Equal());
            simConfig.setWeight(quemTruco, 1.0);
        }

        if (gameStateQuery.getQuemRetruco() != null) {
            Attribute quemRetruco = new Attribute("quemRetruco", TrucoDescription.class);
            simConfig.addMapping(quemRetruco, new Equal());
            simConfig.setWeight(quemRetruco, 1.0);
        }

        if (gameStateQuery.getQuemValeQuatro() != null) {
            Attribute quemValeQuatro = new Attribute("quemValeQuatro", TrucoDescription.class);
            simConfig.addMapping(quemValeQuatro, new Equal());
            simConfig.setWeight(quemValeQuatro, 1.0);
        }

        if (gameStateQuery.getGanhadorPrimeiraRodada() != null) {
            Attribute ganhadorPrimeiraRodada = new Attribute("ganhadorPrimeiraRodada", TrucoDescription.class);
            simConfig.addMapping(ganhadorPrimeiraRodada, new Equal());
            simConfig.setWeight(ganhadorPrimeiraRodada, 1.0);
        }

        if (gameStateQuery.getGanhadorSegundaRodada() != null) {
            Attribute ganhadorSegundaRodada = new Attribute("ganhadorSegundaRodada", TrucoDescription.class);
            simConfig.addMapping(ganhadorSegundaRodada, new Equal());
            simConfig.setWeight(ganhadorSegundaRodada, 1.0);
        }

        return simConfig;

    }

    @Override
    public NNConfig getSimConfigPoints(TrucoDescription gameStateQuery) {

        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new Average());

        if (gameStateQuery.getJogadorMao() != null) {
            Attribute jogadorMao = new Attribute("jogadorMao", TrucoDescription.class);
            simConfig.addMapping(jogadorMao, new Equal()); //TODO: caso não encontre como mão fazer nova consulta como pé
            simConfig.setWeight(jogadorMao, 1.0);
        }

        if (gameStateQuery.getPontosEnvidoRobo() != null) {
            Attribute pontosEnvidoRobo = new Attribute("pontosEnvidoRobo", TrucoDescription.class);
            simConfig.addMapping(pontosEnvidoRobo, new Interval(33));
            simConfig.setWeight(pontosEnvidoRobo, 1.0);
        }

        if (gameStateQuery.getTentosAnterioresRobo() != null) {
            Attribute tentosAnterioresRobo = new Attribute("tentosAnterioresRobo", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresRobo, new Interval(24));
            simConfig.setWeight(tentosAnterioresRobo, 1.0);
        }

        if (gameStateQuery.getPontosEnvidoHumano() != null) {
            Attribute pontosEnvidoHumano = new Attribute("pontosEnvidoHumano", TrucoDescription.class);
            simConfig.addMapping(pontosEnvidoHumano, new Interval(52));
            simConfig.setWeight(pontosEnvidoHumano, 1.0);
        }

        if (gameStateQuery.getTentosAnterioresHumano() != null) {
            Attribute tentosAnterioresHumano = new Attribute("tentosAnterioresHumano", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresHumano, new Interval(24));
            simConfig.setWeight(tentosAnterioresHumano, 1.0);
        }

        if (gameStateQuery.getQuemPediuEnvido() != null) {
            Attribute quemPediuEnvido = new Attribute("quemPediuEnvido", TrucoDescription.class);
            simConfig.addMapping(quemPediuEnvido, new Equal());
            simConfig.setWeight(quemPediuEnvido, 1.0);
        }

        if (gameStateQuery.getQuemEnvidoEnvido() != null) {
            Attribute quemEnvidoEnvido = new Attribute("quemEnvidoEnvido", TrucoDescription.class);
            simConfig.addMapping(quemEnvidoEnvido, new Equal());
            simConfig.setWeight(quemEnvidoEnvido, 1.0);
        }

        if (gameStateQuery.getQuemPediuRealEnvido() != null) {
            Attribute quemPediuRealEnvido = new Attribute("quemPediuRealEnvido", TrucoDescription.class);
            simConfig.addMapping(quemPediuRealEnvido, new Equal());
            simConfig.setWeight(quemPediuRealEnvido, 1.0);
        }

        if (gameStateQuery.getQuemPediuFaltaEnvido() != null) {
            Attribute quemPediuFaltaEnvido = new Attribute("quemPediuFaltaEnvido", TrucoDescription.class);
            simConfig.addMapping(quemPediuFaltaEnvido, new Equal());
            simConfig.setWeight(quemPediuFaltaEnvido, 1.0);
        }

        if (gameStateQuery.getQuemGanhouEnvido() != null) {
            Attribute quemGanhouEnvido = new Attribute("quemGanhouEnvido", TrucoDescription.class);
            simConfig.addMapping(quemGanhouEnvido, new Equal());
            simConfig.setWeight(quemGanhouEnvido, 1.0);
        }

        return simConfig;
    }

    @Override
    public NNConfig getSimConfigPlayCard(TrucoDescription gameStateQuery) {

        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new Average());

        if (gameStateQuery.getJogadorMao() != null) {
            Attribute jogadorMao = new Attribute("jogadorMao", TrucoDescription.class);
            simConfig.addMapping(jogadorMao, new Equal());
            simConfig.setWeight(jogadorMao, 1.0);
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

        if (gameStateQuery.getPrimeiraCartaRobo() != null) {
            Attribute primeiraCartaRobo = new Attribute("primeiraCartaRobo", TrucoDescription.class);
            simConfig.addMapping(primeiraCartaRobo, new Interval(52));
            simConfig.setWeight(primeiraCartaRobo, 1.0);
        }

        if (gameStateQuery.getSegundaCartaRobo() != null) {
            Attribute segundaCartaRobo = new Attribute("segundaCartaRobo", TrucoDescription.class);
            simConfig.addMapping(segundaCartaRobo, new Interval(52));
            simConfig.setWeight(segundaCartaRobo, 1.0);
        }

        if (gameStateQuery.getTerceiraCartaRobo() != null) {
            Attribute terceiraCartaRobo = new Attribute("terceiraCartaRobo", TrucoDescription.class);
            simConfig.addMapping(terceiraCartaRobo, new Interval(52));
            simConfig.setWeight(terceiraCartaRobo, 1.0);
        }

        if (gameStateQuery.getTentosAnterioresRobo() != null) {
            Attribute tentosAnterioresRobo = new Attribute("tentosAnterioresRobo", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresRobo, new Interval(24));
            simConfig.setWeight(tentosAnterioresRobo, 1.0);
        }

        if (gameStateQuery.getRoboCartaVirada() != null) {
            Attribute roboCartaVirada = new Attribute("roboCartaVirada", TrucoDescription.class);
            simConfig.addMapping(roboCartaVirada, new Interval(24));
            simConfig.setWeight(roboCartaVirada, 1.0);
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

        if (gameStateQuery.getPrimeiraCartaHumano() != null) {
            Attribute primeiraCartaHumano = new Attribute("primeiraCartaHumano", TrucoDescription.class);
            simConfig.addMapping(primeiraCartaHumano, new Interval(52));
            simConfig.setWeight(primeiraCartaHumano, 1.0);
        }

        if (gameStateQuery.getSegundaCartaHumano() != null) {
            Attribute segundaCartaHumano = new Attribute("segundaCartaHumano", TrucoDescription.class);
            simConfig.addMapping(segundaCartaHumano, new Interval(52));
            simConfig.setWeight(segundaCartaHumano, 1.0);
        }

        if (gameStateQuery.getTerceiraCartaHumano() != null) {
            Attribute terceiraCartaHumano = new Attribute("terceiraCartaHumano", TrucoDescription.class);
            simConfig.addMapping(terceiraCartaHumano, new Interval(52));
            simConfig.setWeight(terceiraCartaHumano, 1.0);
        }

        if (gameStateQuery.getTentosAnterioresHumano() != null) {
            Attribute tentosAnterioresHumano = new Attribute("tentosAnterioresHumano", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresHumano, new Interval(24));
            simConfig.setWeight(tentosAnterioresHumano, 1.0);
        }

        if (gameStateQuery.getHumanoCartaVirada() != null) {
            Attribute humanoCartaVirada = new Attribute("humanoCartaVirada", TrucoDescription.class);
            simConfig.addMapping(humanoCartaVirada, new Interval(24));
            simConfig.setWeight(humanoCartaVirada, 1.0);
        }

        if (gameStateQuery.getQuemGanhouTruco() != null) {
            Attribute quemGanhouTruco = new Attribute("quemGanhouTruco", TrucoDescription.class);
            simConfig.addMapping(quemGanhouTruco, new Equal());
            simConfig.setWeight(quemGanhouTruco, 1.0);
        }


        return simConfig;
    }

    @Override
    public NNConfig getSimConfigFlor(TrucoDescription gameStateQuery) {

        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new Average());

        if (gameStateQuery.getJogadorMao() != null) {
            Attribute jogadorMao = new Attribute("jogadorMao", TrucoDescription.class);
            simConfig.addMapping(jogadorMao, new Equal());
            simConfig.setWeight(jogadorMao, 1.0);
        }

        if (gameStateQuery.getPontosFlorRobo() != null) {
            Attribute pontosFlorRobo = new Attribute("pontosFlorRobo", TrucoDescription.class);
            simConfig.addMapping(pontosFlorRobo, new Interval(33));
            simConfig.setWeight(pontosFlorRobo, 1.0);
        }

        if (gameStateQuery.getTentosAnterioresRobo() != null) {
            Attribute tentosAnterioresRobo = new Attribute("tentosAnterioresRobo", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresRobo, new Interval(24));
            simConfig.setWeight(tentosAnterioresRobo, 1.0);
        }

        if (gameStateQuery.getPontosFlorHumano() != null) {
            Attribute pontosFlorHumano = new Attribute("pontosFlorHumano", TrucoDescription.class);
            simConfig.addMapping(pontosFlorHumano, new Interval(52));
            simConfig.setWeight(pontosFlorHumano, 1.0);
        }

        if (gameStateQuery.getTentosAnterioresHumano() != null) {
            Attribute tentosAnterioresHumano = new Attribute("tentosAnterioresHumano", TrucoDescription.class);
            simConfig.addMapping(tentosAnterioresHumano, new Interval(24));
            simConfig.setWeight(tentosAnterioresHumano, 1.0);
        }

        if (gameStateQuery.getQuemFlor() != null) {
            Attribute quemFlor = new Attribute("quemFlor", TrucoDescription.class);
            simConfig.addMapping(quemFlor, new Equal());
            simConfig.setWeight(quemFlor, 1.0);
        }

        if (gameStateQuery.getQuemFlorFlor() != null) {
            Attribute quemFlorFlor = new Attribute("quemFlorFlor", TrucoDescription.class);
            simConfig.addMapping(quemFlorFlor, new Equal());
            simConfig.setWeight(quemFlorFlor, 1.0);
        }

        if (gameStateQuery.getQuemContraFlor() != null) {
            Attribute quemContraFlor = new Attribute("quemContraFlor", TrucoDescription.class);
            simConfig.addMapping(quemContraFlor, new Equal());
            simConfig.setWeight(quemContraFlor, 1.0);
        }

        if (gameStateQuery.getQuemContraFlorFalta() != null) {
            Attribute quemContraFlorFalta = new Attribute("quemContraFlorFalta", TrucoDescription.class);
            simConfig.addMapping(quemContraFlorFalta, new Equal());
            simConfig.setWeight(quemContraFlorFalta, 1.0);
        }

        if (gameStateQuery.getQuemContraFlorResto() != null) {
            Attribute quemContraFlorResto = new Attribute("quemContraFlorResto", TrucoDescription.class);
            simConfig.addMapping(quemContraFlorResto, new Equal());
            simConfig.setWeight(quemContraFlorResto, 1.0);
        }

        if (gameStateQuery.getQuemGanhouFlor() != null) {
            Attribute quemGanhouFlor = new Attribute("quemGanhouFlor", TrucoDescription.class);
            simConfig.addMapping(quemGanhouFlor, new Equal());
            simConfig.setWeight(quemGanhouFlor, 1.0);
        }

        return simConfig;
    }

    @Override
    public TrucoDescription getQueryPoints(int who, GameState gameState, StatesDecision statesDecision) {

        TrucoDescription query = new TrucoDescription();

        if (gameState.isHand()) {
            query.setJogadorMao(who);
        } else {
            if (who == 1) {
                query.setJogadorMao(2);
            } else {
                query.setJogadorMao(1);
            }
        }

        switch (statesDecision) {
            case ENVIDO:
                if (who == 1) {
                    query.setQuemPediuEnvido(2);
                } else {
                    query.setQuemPediuEnvido(1);
                }
                break;
            case ENVIDO_ENVIDO:
                if (who == 1) {
                    query.setQuemEnvidoEnvido(2);
                } else {
                    query.setQuemEnvidoEnvido(1);
                }
                break;
            case REAL_ENVIDO:
                if (who == 1) {
                    query.setQuemPediuRealEnvido(2);
                } else {
                    query.setQuemPediuRealEnvido(1);
                }
                break;
            case FALTA_ENVIDO:
                if (who == 1) {
                    query.setQuemPediuFaltaEnvido(2);
                } else {
                    query.setQuemPediuFaltaEnvido(1);
                }
                break;
        }

        query.setQuemGanhouEnvido(who);

        // 1-Robo 2-Humano
        if (who == 1) {
            query.setPontosEnvidoRobo(gameState.getEnvidoPoints());
            query.setTentosAnterioresRobo(gameState.getAgentPoints());
            query.setTentosAnterioresHumano(gameState.getOpponentPoints());
        } else {
            query.setPontosEnvidoHumano(gameState.getEnvidoPoints());
            query.setTentosAnterioresHumano(gameState.getAgentPoints());
            query.setTentosAnterioresRobo(gameState.getOpponentPoints());
        }

        return query;

    }

    @Override
    public TrucoDescription getQueryShowPoints(int who, GameState gameState) {

        TrucoDescription query = new TrucoDescription();

        if (gameState.isHand()) {
            query.setJogadorMao(who);
        } else {
            if (who == 1) {
                query.setJogadorMao(2);
            } else {
                query.setJogadorMao(1);
            }
        }

        if (who == 1) {
            query.setPontosEnvidoRobo(gameState.getEnvidoPoints());
            query.setTentosAnterioresRobo(gameState.getAgentPoints());
            query.setTentosAnterioresHumano(gameState.getOpponentPoints());
            query.setPontosEnvidoHumano(gameState.getOpponentEnvidoPoints());
        } else {
            query.setPontosEnvidoHumano(gameState.getEnvidoPoints());
            query.setTentosAnterioresHumano(gameState.getAgentPoints());
            query.setTentosAnterioresRobo(gameState.getOpponentPoints());
            query.setPontosEnvidoRobo(gameState.getOpponentEnvidoPoints());
        }

        return query;
    }

    @Override
    public TrucoDescription getQueryTruco(int who, GameState gameState, StatesDecision statesDecision) {


        TrucoDescription query = new TrucoDescription();

        if (gameState.isHand()) {
            query.setJogadorMao(who);
        } else {
            if (who == 1) {
                query.setJogadorMao(2);
            } else {
                query.setJogadorMao(1);
            }
        }

        switch (statesDecision) {
            case TRUCO:
                if (who == 1) {
                    query.setQuemTruco(2);
                } else {
                    query.setQuemTruco(1);
                }
                break;
            case RETRUCO:
                if (who == 1) {
                    query.setQuemRetruco(2);
                } else {
                    query.setQuemRetruco(1);
                }
                break;
            case VALE4:
                if (who == 1) {
                    query.setQuemValeQuatro(2);
                } else {
                    query.setQuemValeQuatro(1);
                }
                break;
        }

        if (gameState.getCurrentRound() == 2) {
            if (gameState.isWinnerRound1()) {
                query.setGanhadorPrimeiraRodada(who);
            } else {
                if (gameState.isEmpateRound1()) {
                    query.setGanhadorPrimeiraRodada(0);
                } else {
                    if (who == 1) {
                        query.setGanhadorPrimeiraRodada(2);
                    } else {
                        query.setGanhadorPrimeiraRodada(1);
                    }
                }
            }
        } else if (gameState.getCurrentRound() == 3) {

            if (gameState.isWinnerRound1()) {
                query.setGanhadorPrimeiraRodada(who);
            } else {
                if (gameState.isEmpateRound1()) {
                    query.setGanhadorPrimeiraRodada(0);
                } else {
                    if (who == 1) {
                        query.setGanhadorPrimeiraRodada(2);
                    } else {
                        query.setGanhadorPrimeiraRodada(1);
                    }
                }
            }

            if (gameState.isWinnerRound2()) {
                query.setGanhadorSegundaRodada(who);
            } else {
                if (gameState.isEmpateRound2()) {
                    query.setGanhadorSegundaRodada(0);
                } else {
                    if (who == 1) {
                        query.setGanhadorSegundaRodada(2);
                    } else {
                        query.setGanhadorSegundaRodada(1);
                    }
                }
            }
        }

        query.setQuemGanhouTruco(who);
        query.setQuandoTruco(gameState.getCurrentRound());

        // 1-Robo 2-Humano
        if (who == 1) {
            query.setCartaAltaRobo(gameState.getAgentCards().get(2).getCbrCode());
            query.setCartaMediaRobo(gameState.getAgentCards().get(1).getCbrCode());
            query.setCartaBaixaRobo(gameState.getAgentCards().get(0).getCbrCode());
            query.setTentosAnterioresRobo(gameState.getAgentPoints());
            query.setTentosAnterioresHumano(gameState.getOpponentPoints());
        } else {
            query.setCartaAltaHumano(gameState.getAgentCards().get(2).getCbrCode());
            query.setCartaMediaHumano(gameState.getAgentCards().get(1).getCbrCode());
            query.setCartaBaixaHumano(gameState.getAgentCards().get(0).getCbrCode());
            query.setTentosAnterioresHumano(gameState.getAgentPoints());
            query.setTentosAnterioresRobo(gameState.getOpponentPoints());
        }

        return query;
    }

    @Override
    public TrucoDescription getQueryPlayCard(int who, GameState gameState, StatesDecision statesDecision) {

        TrucoDescription query = new TrucoDescription();

        if (gameState.isHand()) {
            query.setJogadorMao(who);
        } else {
            if (who == 1) {
                query.setJogadorMao(2);
            } else {
                query.setJogadorMao(1);
            }
        }

        query.setQuemGanhouTruco(who);


        // 1-Robo 2-Humano
        if (who == 1) {

          /*  if (gameState.getAgentPlayedCards().contains(gameState.getAgentCards().get(2))) {
                query.setCartaAltaRobo(null);
            } else {*/
            query.setCartaAltaRobo(gameState.getAgentCards().get(2).getCbrCode());
            /*}*/

            /*if (gameState.getAgentPlayedCards().contains(gameState.getAgentCards().get(1))) {
                query.setCartaMediaRobo(null);
            } else {*/
            query.setCartaMediaRobo(gameState.getAgentCards().get(1).getCbrCode());
            /*}*/

            /*if (gameState.getAgentPlayedCards().contains(gameState.getAgentCards().get(0))) {
                query.setCartaBaixaRobo(null);
            } else {*/
            query.setCartaBaixaRobo(gameState.getAgentCards().get(0).getCbrCode());
            /*}*/

            query.setTentosAnterioresRobo(gameState.getAgentPoints());
            query.setTentosAnterioresHumano(gameState.getOpponentPoints());

        } else {

           /* if (gameState.getAgentPlayedCards().contains(gameState.getAgentCards().get(2))) {
                query.setCartaAltaHumano(null);
            } else {*/
            query.setCartaAltaHumano(gameState.getAgentCards().get(2).getCbrCode());
            /*}*/

            /*if (gameState.getAgentPlayedCards().contains(gameState.getAgentCards().get(1))) {
                query.setCartaMediaHumano(null);
            } else {*/
            query.setCartaMediaHumano(gameState.getAgentCards().get(1).getCbrCode());
            /*}*/

            /*if (gameState.getAgentPlayedCards().contains(gameState.getAgentCards().get(0))) {
                query.setCartaBaixaHumano(null);
            } else {*/
            query.setCartaBaixaHumano(gameState.getAgentCards().get(0).getCbrCode());
            /*}*/

            query.setTentosAnterioresHumano(gameState.getAgentPoints());
            query.setTentosAnterioresRobo(gameState.getOpponentPoints());
        }

        switch (statesDecision) {
            case PLAY_CARD_2:
                if (gameState.getOpponentCartaVirada() == 0) {
                    if (who == 1) {
                        query.setPrimeiraCartaHumano(gameState.getDealtCards().get(0).getCbrCode());
                    } else {
                        query.setPrimeiraCartaRobo(gameState.getDealtCards().get(0).getCbrCode());
                    }
                } else if (gameState.getOpponentCartaVirada() == 1){
                    if (who == 1) {
                        query.setHumanoCartaVirada(gameState.getOpponentCartaVirada());
                    } else {
                        query.setRoboCartaVirada(gameState.getOpponentCartaVirada());
                    }
                }
                break;
            case PLAY_CARD_3:

                if (gameState.isWinnerRound1()) {
                    query.setGanhadorPrimeiraRodada(who);
                } else {
                    if (gameState.isEmpateRound1()) {
                        query.setGanhadorPrimeiraRodada(0);
                    } else {
                        if (who == 1) {
                            query.setGanhadorPrimeiraRodada(2);
                        } else {
                            query.setGanhadorPrimeiraRodada(1);
                        }
                    }
                }

                if (gameState.getAgentCartaVirada() == 0) {
                    if (who == 2) {
                        query.setPrimeiraCartaHumano(gameState.getAgentPlayedCards().get(0).getCbrCode());
                    } else {
                        query.setPrimeiraCartaRobo(gameState.getAgentPlayedCards().get(0).getCbrCode());
                    }
                } else if (gameState.getOpponentCartaVirada() == 1){
                    if (who == 2) {
                        query.setHumanoCartaVirada(gameState.getAgentCartaVirada());
                    } else {
                        query.setRoboCartaVirada(gameState.getAgentCartaVirada());
                    }
                }

                if (gameState.getOpponentCartaVirada() == 0) {
                    if (who == 1) {
                        query.setPrimeiraCartaHumano(gameState.getOpponentPlayedCards().get(0).getCbrCode());
                    } else {
                        query.setPrimeiraCartaRobo(gameState.getOpponentPlayedCards().get(0).getCbrCode());
                    }
                } else if (gameState.getOpponentCartaVirada() == 1){
                    if (who == 1) {
                        query.setHumanoCartaVirada(gameState.getOpponentCartaVirada());
                    } else {
                        query.setRoboCartaVirada(gameState.getOpponentCartaVirada());
                    }
                }
                break;
            case PLAY_CARD_4:

                if (gameState.isWinnerRound1()) {
                    query.setGanhadorPrimeiraRodada(who);
                } else {
                    if (gameState.isEmpateRound1()) {
                        query.setGanhadorPrimeiraRodada(0);
                    } else {
                        if (who == 1) {
                            query.setGanhadorPrimeiraRodada(2);
                        } else {
                            query.setGanhadorPrimeiraRodada(1);
                        }
                    }
                }

                if (gameState.getAgentCartaVirada() == 0) {
                    if (who == 2) {
                        query.setPrimeiraCartaHumano(gameState.getAgentPlayedCards().get(0).getCbrCode());
                    } else {
                        query.setPrimeiraCartaRobo(gameState.getAgentPlayedCards().get(0).getCbrCode());
                    }
                } else if (gameState.getOpponentCartaVirada() == 1){
                    if (who == 2) {
                        query.setHumanoCartaVirada(gameState.getAgentCartaVirada());
                    } else {
                        query.setRoboCartaVirada(gameState.getAgentCartaVirada());
                    }
                }

                if (gameState.getOpponentCartaVirada() == 0) {
                    if (who == 1) {
                        query.setPrimeiraCartaHumano(gameState.getOpponentPlayedCards().get(0).getCbrCode());
                        query.setSegundaCartaHumano(gameState.getOpponentPlayedCards().get(1).getCbrCode());
                    } else {
                        query.setPrimeiraCartaRobo(gameState.getOpponentPlayedCards().get(0).getCbrCode());
                        query.setSegundaCartaRobo(gameState.getOpponentPlayedCards().get(1).getCbrCode());
                    }
                } else if (gameState.getOpponentCartaVirada() == 1 || gameState.getOpponentCartaVirada() == 2){
                    if (who == 1) {
                        query.setHumanoCartaVirada(gameState.getOpponentCartaVirada());
                    } else {
                        query.setRoboCartaVirada(gameState.getOpponentCartaVirada());
                    }
                }
                break;
            case PLAY_CARD_5:
                if (gameState.isWinnerRound1()) {
                    query.setGanhadorPrimeiraRodada(who);
                } else {
                    if (gameState.isEmpateRound1()) {
                        query.setGanhadorPrimeiraRodada(0);
                    } else {
                        if (who == 1) {
                            query.setGanhadorPrimeiraRodada(2);
                        } else {
                            query.setGanhadorPrimeiraRodada(1);
                        }
                    }
                }

                if (gameState.isWinnerRound2()) {
                    query.setGanhadorSegundaRodada(who);
                } else {
                    if (gameState.isEmpateRound2()) {
                        query.setGanhadorSegundaRodada(0);
                    } else {
                        if (who == 1) {
                            query.setGanhadorSegundaRodada(2);
                        } else {
                            query.setGanhadorSegundaRodada(1);
                        }
                    }
                }

                if (gameState.getAgentCartaVirada() == 0) {
                    if (who == 2) {
                        query.setPrimeiraCartaHumano(gameState.getAgentPlayedCards().get(0).getCbrCode());
                        query.setSegundaCartaHumano(gameState.getAgentPlayedCards().get(1).getCbrCode());
                    } else {
                        query.setPrimeiraCartaRobo(gameState.getAgentPlayedCards().get(0).getCbrCode());
                        query.setSegundaCartaRobo(gameState.getAgentPlayedCards().get(1).getCbrCode());
                    }
                } else if (gameState.getAgentCartaVirada() == 1 || gameState.getAgentCartaVirada() == 2){
                    if (who == 2) {
                        query.setHumanoCartaVirada(gameState.getAgentCartaVirada());
                    } else {
                        query.setRoboCartaVirada(gameState.getAgentCartaVirada());
                    }
                }

                if (gameState.getOpponentCartaVirada() == 0) {
                    if (who == 1) {
                        query.setPrimeiraCartaHumano(gameState.getOpponentPlayedCards().get(0).getCbrCode());
                        query.setSegundaCartaHumano(gameState.getOpponentPlayedCards().get(1).getCbrCode());
                    } else {
                        query.setPrimeiraCartaRobo(gameState.getOpponentPlayedCards().get(0).getCbrCode());
                        query.setSegundaCartaRobo(gameState.getOpponentPlayedCards().get(1).getCbrCode());
                    }
                } else if (gameState.getOpponentCartaVirada() == 1 || gameState.getOpponentCartaVirada() == 2){
                    if (who == 1) {
                        query.setHumanoCartaVirada(gameState.getOpponentCartaVirada());
                    } else {
                        query.setRoboCartaVirada(gameState.getOpponentCartaVirada());
                    }
                }

                break;
            case PLAY_CARD_6:
                if (gameState.isWinnerRound1()) {
                    query.setGanhadorPrimeiraRodada(who);
                } else {
                    if (gameState.isEmpateRound1()) {
                        query.setGanhadorPrimeiraRodada(0);
                    } else {
                        if (who == 1) {
                            query.setGanhadorPrimeiraRodada(2);
                        } else {
                            query.setGanhadorPrimeiraRodada(1);
                        }
                    }
                }

                if (gameState.isWinnerRound2()) {
                    query.setGanhadorSegundaRodada(who);
                } else {
                    if (gameState.isEmpateRound2()) {
                        query.setGanhadorSegundaRodada(0);
                    } else {
                        if (who == 1) {
                            query.setGanhadorSegundaRodada(2);
                        } else {
                            query.setGanhadorSegundaRodada(1);
                        }
                    }
                }

                if (gameState.getAgentCartaVirada() == 0) {
                    if (who == 2) {
                        query.setPrimeiraCartaHumano(gameState.getAgentPlayedCards().get(0).getCbrCode());
                        query.setSegundaCartaHumano(gameState.getAgentPlayedCards().get(1).getCbrCode());
                    } else {
                        query.setPrimeiraCartaRobo(gameState.getAgentPlayedCards().get(0).getCbrCode());
                        query.setSegundaCartaRobo(gameState.getAgentPlayedCards().get(1).getCbrCode());
                    }
                } else {
                    if (who == 2) {
                        query.setHumanoCartaVirada(gameState.getAgentCartaVirada());
                    } else {
                        query.setRoboCartaVirada(gameState.getAgentCartaVirada());
                    }
                }

                if (gameState.getOpponentCartaVirada() == 0) {
                    if (who == 1) {
                        query.setPrimeiraCartaHumano(gameState.getOpponentPlayedCards().get(0).getCbrCode());
                        query.setSegundaCartaHumano(gameState.getOpponentPlayedCards().get(1).getCbrCode());
                        query.setTerceiraCartaHumano(gameState.getOpponentPlayedCards().get(2).getCbrCode());
                    } else {
                        query.setPrimeiraCartaRobo(gameState.getOpponentPlayedCards().get(0).getCbrCode());
                        query.setSegundaCartaRobo(gameState.getOpponentPlayedCards().get(1).getCbrCode());
                        query.setTerceiraCartaRobo(gameState.getOpponentPlayedCards().get(1).getCbrCode());
                    }
                } else {
                    if (who == 1) {
                        query.setHumanoCartaVirada(gameState.getOpponentCartaVirada());
                    } else {
                        query.setRoboCartaVirada(gameState.getOpponentCartaVirada());
                    }
                }
                break;
        }

        return query;
    }

    @Override
    public TrucoDescription getQueryFlor(int who, GameState gameState, StatesDecision statesDecision) {

        TrucoDescription query = new TrucoDescription();

        if (gameState.isHand()) {
            query.setJogadorMao(who);
        } else {
            if (who == 1) {
                query.setJogadorMao(2);
            } else {
                query.setJogadorMao(1);
            }
        }

        switch (statesDecision) {
            case FLOR:
                if (who == 1) {
                    query.setQuemFlor(2);
                } else {
                    query.setQuemFlor(1);
                }
                break;
            case FLOR_FLOR:
                if (who == 1) {
                    query.setQuemFlorFlor(2);
                } else {
                    query.setQuemFlorFlor(1);
                }
                break;
            case CONTRA_FLOR:
                if (who == 1) {
                    query.setQuemContraFlor(2);
                } else {
                    query.setQuemContraFlor(1);
                }
                break;
            case CONTRA_FLOR_FALTA:
                if (who == 1) {
                    query.setQuemContraFlorFalta(2);
                } else {
                    query.setQuemContraFlorFalta(1);
                }
                break;
            case CONTRA_FLOR_RESTO:
                if (who == 1) {
                    query.setQuemContraFlorResto(2);
                } else {
                    query.setQuemContraFlorResto(1);
                }
                break;
        }

        query.setQuemGanhouFlor(who);

        // 1-Robo 2-Humano
        if (who == 1) {
            query.setPontosFlorRobo(gameState.getEnvidoPoints());
            query.setTentosAnterioresRobo(gameState.getAgentPoints());
            query.setTentosAnterioresHumano(gameState.getOpponentPoints());
        } else {
            query.setPontosFlorHumano(gameState.getEnvidoPoints());
            query.setTentosAnterioresHumano(gameState.getAgentPoints());
            query.setTentosAnterioresRobo(gameState.getOpponentPoints());
        }

        return query;
    }

    public String callEnvido(GameState gameState, StatesDecision statesDecision) {


        String action = "0";

        Collection<RetrievalResult> casesRetrivedRobo = null;
        Collection<RetrievalResult> casesRetrivedHumano = null;

        TrucoDescription queryRobo = getQueryPoints(1, gameState, statesDecision);
        TrucoDescription queryHumano = getQueryPoints(2, gameState, statesDecision);

        casesRetrivedRobo = getMostSimilarCases(_caseBase, queryRobo, getSimConfigPoints(queryRobo));
        casesRetrivedHumano = getMostSimilarCases(_caseBase, queryHumano, getSimConfigPoints(queryHumano));

        printRetrievedCases(1, casesRetrivedRobo, "CALL_ENVIDO");
        printRetrievedCases(2, casesRetrivedHumano, "CALL_ENVIDO");


        action = decisionMakingEnvido(selectMostSimilar(casesRetrivedRobo, casesRetrivedHumano), statesDecision);

        return action;

    }

    public boolean callTruco(GameState gameState, StatesDecision statesDecision) {

        boolean call = false;

        Collection<RetrievalResult> casesRetrivedRobo = null;
        Collection<RetrievalResult> casesRetrivedHumano = null;

        TrucoDescription queryRobo = getQueryTruco(1, gameState, statesDecision);
        TrucoDescription queryHumano = getQueryTruco(2, gameState, statesDecision);

        casesRetrivedRobo = getMostSimilarCases(_caseBase, queryRobo, getSimConfigTruco(queryRobo));
        casesRetrivedHumano = getMostSimilarCases(_caseBase, queryHumano, getSimConfigTruco(queryHumano));

        printRetrievedCases(1, casesRetrivedRobo, "CALL_TRUCO");
        printRetrievedCases(2, casesRetrivedHumano, "CALL_TRUCO");

        call = decisionMakingTruco(selectMostSimilar(casesRetrivedRobo, casesRetrivedHumano), statesDecision);

        return call;
    }

    public String callTrucoReply(GameState gameState, StatesDecision statesDecision) {

        String action = "0";

        Collection<RetrievalResult> casesRetrivedRobo = null;
        Collection<RetrievalResult> casesRetrivedHumano = null;

        TrucoDescription queryRobo = getQueryTruco(1, gameState, statesDecision);
        TrucoDescription queryHumano = getQueryTruco(2, gameState, statesDecision);

        casesRetrivedRobo = getMostSimilarCases(_caseBase, queryRobo, getSimConfigTruco(queryRobo));
        casesRetrivedHumano = getMostSimilarCases(_caseBase, queryHumano, getSimConfigTruco(queryHumano));

        printRetrievedCases(1, casesRetrivedRobo, "CALL_TRUCO_REPLY");
        printRetrievedCases(2, casesRetrivedHumano, "CALL_TRUCO_REPLY");

        action = decisionMakingTrucoReply(selectMostSimilar(casesRetrivedRobo, casesRetrivedHumano), statesDecision);

        return action;
    }

    public Card playCard(GameState gameState, StatesDecision statesDecision) {

        Card card = null;

        Collection<RetrievalResult> casesRetrivedRobo = null;
        Collection<RetrievalResult> casesRetrivedHumano = null;

        TrucoDescription queryRobo = getQueryPlayCard(1, gameState, statesDecision);
        TrucoDescription queryHumano = getQueryPlayCard(2, gameState, statesDecision);

        casesRetrivedRobo = getMostSimilarCases(_caseBase, queryRobo, getSimConfigPlayCard(queryRobo));
        casesRetrivedHumano = getMostSimilarCases(_caseBase, queryHumano, getSimConfigPlayCard(queryHumano));

        printRetrievedCases(1, casesRetrivedRobo, "" + statesDecision);
        printRetrievedCases(2, casesRetrivedHumano, "" + statesDecision);

        card = decisionMakingPlayCard(selectMostSimilar(casesRetrivedRobo, casesRetrivedHumano), statesDecision, gameState);

        return card;
    }

    public boolean showPoints(GameState gameState) {

        boolean showPoints = false;

        Collection<RetrievalResult> casesRetrivedRobo = null;
        Collection<RetrievalResult> casesRetrivedHumano = null;

        TrucoDescription queryRobo = getQueryShowPoints(1, gameState);
        TrucoDescription queryHumano = getQueryShowPoints(2, gameState);

        casesRetrivedRobo = getMostSimilarCases(_caseBase, queryRobo, getSimConfigPoints(queryRobo));
        casesRetrivedHumano = getMostSimilarCases(_caseBase, queryHumano, getSimConfigPoints(queryHumano));

        printRetrievedCases(1, casesRetrivedRobo, "SHOW_POINTS");
        printRetrievedCases(2, casesRetrivedHumano, "SHOW_POINTS");

        showPoints = decisionMakingShowPoints(selectMostSimilar(casesRetrivedRobo, casesRetrivedHumano));

        return showPoints;
    }


    public String callFlor(GameState gameState, StatesDecision statesDecision) {


        String action = "0";

        Collection<RetrievalResult> casesRetrivedRobo = null;
        Collection<RetrievalResult> casesRetrivedHumano = null;

        TrucoDescription queryRobo = getQueryFlor(1, gameState, statesDecision);
        TrucoDescription queryHumano = getQueryFlor(2, gameState, statesDecision);

        casesRetrivedRobo = getMostSimilarCases(_caseBase, queryRobo, getSimConfigFlor(queryRobo));
        casesRetrivedHumano = getMostSimilarCases(_caseBase, queryHumano, getSimConfigFlor(queryHumano));

        printRetrievedCases(1, casesRetrivedRobo, "CALL_FLOR");
        printRetrievedCases(2, casesRetrivedHumano, "CALL_FLOR");


        action = decisionMakingFlor(selectMostSimilar(casesRetrivedRobo, casesRetrivedHumano), statesDecision);

        return action;

    }

    private RetrievalResult selectMostSimilar(Collection<RetrievalResult> casesRetrivedRobo,
                                              Collection<RetrievalResult> casesRetrivedHumano) {

        RetrievalResult mostSimilarCase = null;

        RetrievalResult roboCase = null;
        RetrievalResult humanoCase = null;

        Iterator i = casesRetrivedRobo.iterator();
        if (i.hasNext()) {
            roboCase = (RetrievalResult) i.next();
        }

        i = casesRetrivedHumano.iterator();
        if (i.hasNext()) {
            humanoCase = (RetrievalResult) i.next();
        }

        if (roboCase != null && humanoCase != null){
            if (roboCase.getEval() > humanoCase.getEval()) {
                mostSimilarCase = roboCase;
                chosenCase = 1;
            } else {
                mostSimilarCase = humanoCase;
                chosenCase = 2;
            }
        }

        System.out.println("Caso escolhido: " + chosenCase);
        return mostSimilarCase;
    }

    private String decisionMakingEnvido(RetrievalResult retrievalResult, StatesDecision statesDecision) {


        String action = "0";

        TrucoDescription selectedCaso = (TrucoDescription) retrievalResult.get_case().getDescription();

        boolean isNoCallEnvido = false;
        boolean isCallEnvido = false;
        boolean isCallEnvidoEnvido = false;
        boolean isCallRealEnvido = false;
        boolean isCallFaltaEnvido = false;


        switch (statesDecision) {
            case START_HAND:
                // 0: NÃO CHAMAR 1: ENVIDO; 2: ENVIDO_ENVIDO; 3: REAL_ENVIDO; 4: FALTA_ENVIDO
                isCallEnvido = selectedCaso.getQuemPediuEnvido() != null && selectedCaso.getQuemPediuEnvido() ==
                        selectedCaso.getQuemGanhouEnvido();
                isCallEnvidoEnvido = selectedCaso.getQuemEnvidoEnvido() != null && selectedCaso.getQuemEnvidoEnvido() ==
                        selectedCaso.getQuemGanhouEnvido();
                isCallRealEnvido = selectedCaso.getQuemPediuRealEnvido() != null && selectedCaso.getQuemPediuRealEnvido() ==
                        selectedCaso.getQuemGanhouEnvido();
                isCallFaltaEnvido = selectedCaso.getQuemPediuFaltaEnvido() != null && selectedCaso.getQuemPediuFaltaEnvido() ==
                        selectedCaso.getQuemGanhouEnvido();
                isNoCallEnvido = selectedCaso.getQuemPediuEnvido() == null && selectedCaso.getQuemPediuRealEnvido() == null &&
                        selectedCaso.getQuemPediuFaltaEnvido() == null;

                if (isNoCallEnvido) {
                    action = "0";
                } else if (isCallEnvido) {
                    action = "1";
                } else if (isCallRealEnvido) {
                    action = "3";
                } else if (isCallFaltaEnvido) {
                    action = "4";
                }
                break;

            case ENVIDO:

                //0: NÃO ACEITAR 1: ACEITAR; 2: ENVIDO_ENVIDO; 3: REAL_ENVIDO; 4: FALTA_ENVIDO
                if (selectedCaso.getQuemNegouEnvido() != null && selectedCaso.getQuemNegouEnvido() == chosenCase) {
                    action = "0";
                } else {
                    if (selectedCaso.getQuemEnvidoEnvido() != null && selectedCaso.getQuemEnvidoEnvido() == chosenCase) {
                        action = "2";
                    } else if (selectedCaso.getQuemPediuRealEnvido() != null && selectedCaso.getQuemPediuRealEnvido() == chosenCase) {
                        action = "3";
                    } else if (selectedCaso.getQuemPediuFaltaEnvido() != null && selectedCaso.getQuemPediuFaltaEnvido() == chosenCase) {
                        action = "4";
                    } else {
                        action = "1";
                    }
                }
                break;
            case ENVIDO_ENVIDO:
                //0: NÃO ACEITAR 1: ACEITAR; 3: REAL_ENVIDO; 4: FALTA_ENVIDO
                if (selectedCaso.getQuemNegouEnvido() != null && selectedCaso.getQuemNegouEnvido() == chosenCase) {
                    action = "0";
                } else {
                    if (selectedCaso.getQuemPediuRealEnvido() != null && selectedCaso.getQuemPediuRealEnvido() == chosenCase) {
                        action = "3";
                    } else if (selectedCaso.getQuemPediuFaltaEnvido() != null && selectedCaso.getQuemPediuFaltaEnvido() == chosenCase) {
                        action = "4";
                    } else {
                        action = "1";
                    }
                }
                break;
            case REAL_ENVIDO:
                //0: NÃO ACEITAR 1: ACEITAR; 4: FALTA_ENVIDO
                if (selectedCaso.getQuemNegouEnvido() != null && selectedCaso.getQuemNegouEnvido() == chosenCase) {
                    action = "0";
                } else {
                    if (selectedCaso.getQuemPediuFaltaEnvido() != null && selectedCaso.getQuemPediuFaltaEnvido() == chosenCase) {
                        action = "4";
                    } else {
                        action = "1";
                    }
                }
                break;
            case FALTA_ENVIDO:
                //0: NÃO ACEITAR 1: ACEITAR;
                if (selectedCaso.getQuemNegouEnvido() != null && selectedCaso.getQuemNegouEnvido() == chosenCase) {
                    action = "0";
                } else {
                    action = "1";
                }
                break;
        }

        return action;
    }


    private boolean decisionMakingTruco(RetrievalResult retrievalResult, StatesDecision statesDecision) {

        TrucoDescription selectedCaso = (TrucoDescription) retrievalResult.get_case().getDescription();

        boolean call = selectedCaso.getQuemTruco() != null && selectedCaso.getQuemTruco() == selectedCaso.getQuemGanhouTruco();

        return call;
    }

    private String decisionMakingTrucoReply(RetrievalResult retrievalResult, StatesDecision statesDecision) {

        TrucoDescription selectedCaso = (TrucoDescription) retrievalResult.get_case().getDescription();

        String action = "0";

        switch (statesDecision) {
            case TRUCO:
                //0: NÃO ACEITAR 1: ACEITAR; 2: RETRUCO
                if (selectedCaso.getQuemNegouTruco() != null && selectedCaso.getQuemNegouTruco() == chosenCase) {
                    action = "0";
                } else if (selectedCaso.getQuemRetruco() == null) {
                    action = "1";
                } else if (selectedCaso.getQuemRetruco() != null && selectedCaso.getQuemRetruco() == chosenCase) {
                    action = "2";
                }
                break;
            case RETRUCO:
                //0: NÃO ACEITAR 1: ACEITAR; 3: VALE4
                if (selectedCaso.getQuemNegouTruco() != null && selectedCaso.getQuemNegouTruco() == chosenCase) {
                    action = "0";
                } else if (selectedCaso.getQuemValeQuatro() == null) {
                    action = "1";
                } else if (selectedCaso.getQuemValeQuatro() != null && selectedCaso.getQuemValeQuatro() == chosenCase) {
                    action = "3";
                }
                break;
            case VALE4:
                //0: NÃO ACEITAR 1: ACEITAR;
                if (selectedCaso.getQuemNegouTruco() != null && selectedCaso.getQuemNegouTruco() == chosenCase) {
                    action = "0";
                } else {
                    action = "1";
                }
        }

        return action;
    }

    private Card decisionMakingPlayCard(RetrievalResult retrievalResult, StatesDecision statesDecision, GameState gameState) {

        Card card = null;

        cartaVirada = 0;
        irBaralho = false;

        TrucoDescription selectedCaso = (TrucoDescription) retrievalResult.get_case().getDescription();

        if (selectedCaso.getQuemBaralho() != null && selectedCaso.getQuemBaralho() == chosenCase && selectedCaso.getQuandoBaralho() != null &&
                selectedCaso.getQuandoBaralho() == gameState.getCurrentRound()) {
                card = null;
        } else {

            if (chosenCase == 1) {

                if (selectedCaso.getRoboCartaVirada() != null) {
                    if (selectedCaso.getRoboCartaVirada() == gameState.getCurrentRound()) {
                        cartaVirada = gameState.getCurrentRound();
                    }
                } else {

                    if (statesDecision == StatesDecision.START_HAND || statesDecision == StatesDecision.PLAY_CARD_1) {
                        if (selectedCaso.getPrimeiraCartaRobo() != null) {
                            if (selectedCaso.getPrimeiraCartaRobo() == selectedCaso.getCartaAltaRobo()) {
                                card = gameState.getAgentCards().get(2);
                            } else if (selectedCaso.getPrimeiraCartaRobo() == selectedCaso.getCartaMediaRobo()) {
                                card = gameState.getAgentCards().get(1);
                            } else {
                                card = gameState.getAgentCards().get(0);
                            }
                        }
                    } else if (statesDecision == StatesDecision.PLAY_CARD_2) {
                        card = playFirstCardFooter(gameState, chosenCase, selectedCaso);
                    } else if (statesDecision == StatesDecision.PLAY_CARD_3 || statesDecision == StatesDecision.PLAY_CARD_4) {

                        if (gameState.isWinnerRound1()) {

                            if (selectedCaso.getGanhadorPrimeiraRodada() == chosenCase) {

                                if (selectedCaso.getPrimeiraCartaRobo() == selectedCaso.getCartaAltaRobo()) {

                                    if (selectedCaso.getSegundaCartaRobo() == selectedCaso.getCartaBaixaRobo()) {
                                        card = getPreferCartaBaixa(gameState);

                                    } else if (selectedCaso.getSegundaCartaRobo() == selectedCaso.getCartaMediaRobo()) {
                                        card = getPreferCartaMedia(gameState);
                                    } else {
                                        card = getPreferCartaBaixa(gameState);
                                    }
                                } else if (selectedCaso.getPrimeiraCartaRobo() == selectedCaso.getCartaMediaRobo()) {

                                    if (selectedCaso.getSegundaCartaRobo() == selectedCaso.getCartaBaixaRobo()) {
                                        card = getPreferCartaBaixa(gameState);

                                    } else if (selectedCaso.getSegundaCartaRobo() == selectedCaso.getCartaAltaRobo()) {
                                        card = getPreferCartaAlta(gameState);
                                    } else {
                                        card = getPreferCartaBaixa(gameState);
                                    }

                                } else {

                                    if (selectedCaso.getSegundaCartaRobo() == selectedCaso.getCartaAltaRobo()) {
                                        card = getPreferCartaAlta(gameState);

                                    } else if (selectedCaso.getSegundaCartaRobo() == selectedCaso.getCartaMediaRobo()) {
                                        card = getPreferCartaMedia(gameState);
                                    } else {
                                        card = getPreferCartaBaixa(gameState);
                                    }
                                }
                            } else {
                                card = getPreferCartaBaixa(gameState);
                            }

                        } else {
                            if (statesDecision == StatesDecision.PLAY_CARD_3) {
                                card = playSecondCardHandLooserRound1(gameState);
                            }
                            if (statesDecision == StatesDecision.PLAY_CARD_4) {
                                card = playSecondCardFooter(gameState);
                            }
                        }
                    } else if (statesDecision == StatesDecision.PLAY_CARD_5) {
                        card = playHandCardDesc(gameState);
                    } else if (statesDecision == StatesDecision.PLAY_CARD_6) {
                        card = playLastCard(gameState);
                    }
                }

            // 2 - Humano
            } else {

                if (selectedCaso.getHumanoCartaVirada() != null) {
                    if (selectedCaso.getHumanoCartaVirada() == gameState.getCurrentRound()) {
                        cartaVirada = gameState.getCurrentRound();
                    }
                } else {

                    if (statesDecision == StatesDecision.START_HAND || statesDecision == StatesDecision.PLAY_CARD_1) {

                        if (selectedCaso.getPrimeiraCartaHumano() != null) {
                            if (selectedCaso.getPrimeiraCartaHumano() == selectedCaso.getCartaAltaHumano()) {
                                card = gameState.getAgentCards().get(2);
                            } else if (selectedCaso.getPrimeiraCartaHumano() == selectedCaso.getCartaMediaHumano()) {
                                card = gameState.getAgentCards().get(1);
                            } else {
                                card = gameState.getAgentCards().get(0);
                            }
                        }
                    } else if (statesDecision == StatesDecision.PLAY_CARD_2) {
                        card = playFirstCardFooter(gameState, chosenCase, selectedCaso);
                    } else if (statesDecision == StatesDecision.PLAY_CARD_3 || statesDecision == StatesDecision.PLAY_CARD_4) {

                        if (gameState.isWinnerRound1()) {

                            if (selectedCaso.getGanhadorPrimeiraRodada() == chosenCase) {

                                if (selectedCaso.getPrimeiraCartaHumano() == selectedCaso.getCartaAltaHumano()) {

                                    if (selectedCaso.getSegundaCartaHumano() == selectedCaso.getCartaBaixaHumano()) {
                                        card = getPreferCartaBaixa(gameState);

                                    } else if (selectedCaso.getSegundaCartaHumano() == selectedCaso.getCartaMediaHumano()) {
                                        card = getPreferCartaMedia(gameState);
                                    } else {
                                        card = getPreferCartaBaixa(gameState);
                                    }
                                } else if (selectedCaso.getPrimeiraCartaHumano() == selectedCaso.getCartaMediaHumano()) {

                                    if (selectedCaso.getSegundaCartaHumano() == selectedCaso.getCartaBaixaHumano()) {
                                        card = getPreferCartaBaixa(gameState);

                                    } else if (selectedCaso.getSegundaCartaHumano() == selectedCaso.getCartaAltaHumano()) {
                                        card = getPreferCartaAlta(gameState);
                                    } else {
                                        card = getPreferCartaBaixa(gameState);
                                    }

                                } else {

                                    if (selectedCaso.getSegundaCartaHumano() == selectedCaso.getCartaAltaHumano()) {
                                        card = getPreferCartaAlta(gameState);

                                    } else if (selectedCaso.getSegundaCartaHumano() == selectedCaso.getCartaMediaHumano()) {
                                        card = getPreferCartaMedia(gameState);
                                    } else {
                                        card = getPreferCartaBaixa(gameState);
                                    }
                                }
                            } else {
                                card = getPreferCartaBaixa(gameState);
                            }

                        } else {
                            if (statesDecision == StatesDecision.PLAY_CARD_3) {
                               card = playSecondCardHandLooserRound1(gameState);
                            }
                            if (statesDecision == StatesDecision.PLAY_CARD_4) {
                                card = playSecondCardFooter(gameState);
                            }
                        }
                    } else if (statesDecision == StatesDecision.PLAY_CARD_5) {
                        card = playHandCardDesc(gameState);
                    } else if (statesDecision == StatesDecision.PLAY_CARD_6) {
                        card = playLastCard(gameState);
                    }
                }

            }

        }

        return card;

    }

    private boolean decisionMakingShowPoints(RetrievalResult retrievalResult) {

        boolean showPoints = false;

        TrucoDescription selectedCaso = (TrucoDescription) retrievalResult.get_case().getDescription();

        if (selectedCaso.getQuemEscondeuPontosEnvido() != null && selectedCaso.getQuemEscondeuPontosEnvido() == chosenCase) {
            showPoints = false;
        } else {
            showPoints = true;
        }

        return showPoints;
    }

    private String decisionMakingFlor(RetrievalResult retrievalResult, StatesDecision statesDecision) {


        String action = "0";

        TrucoDescription selectedCaso = (TrucoDescription) retrievalResult.get_case().getDescription();

        switch (statesDecision) {
            case FLOR:
                //0: NADA 2: FLOR_FLOR; 3: CONTRA_FLOR;
                if (selectedCaso.getQuemFlorFlor() != null && selectedCaso.getQuemFlorFlor() == chosenCase) {
                    action = "2";
                } else if (selectedCaso.getQuemContraFlor() != null && selectedCaso.getQuemContraFlor() == chosenCase) {
                    action = "3";
                }
                break;
            case FLOR_FLOR:
                //0: NÃO ACEITAR 1: ACEITAR; 3: CONTRA_FLOR;
                if (selectedCaso.getQuemNegouFlor() != null && selectedCaso.getQuemNegouFlor() == chosenCase) {
                    action = "0";
                } else {
                    if (selectedCaso.getQuemContraFlor() != null && selectedCaso.getQuemContraFlor() == chosenCase) {
                        action = "3";
                    } else {
                        action = "1";
                    }
                }
                break;
            case CONTRA_FLOR:
                //0: NÃO ACEITAR 1: ACEITAR; 4: CONTRA_FLOR_FALTA; 5: CONTRA_FLOR_RESTO;
                if (selectedCaso.getQuemNegouFlor() != null && selectedCaso.getQuemNegouFlor() == chosenCase) {
                    action = "0";
                } else {
                    if (selectedCaso.getQuemContraFlorFalta() != null && selectedCaso.getQuemContraFlorFalta() == chosenCase) {
                        action = "4";
                    } else if(selectedCaso.getQuemContraFlorResto() != null && selectedCaso.getQuemContraFlorResto() == chosenCase) {
                        action = "5";
                    } else {
                        action = "1";
                    }
                }
                break;
            case CONTRA_FLOR_FALTA:
                //0: NÃO ACEITAR 1: ACEITAR;  5: CONTRA_FLOR_RESTO;
                if (selectedCaso.getQuemNegouFlor() != null && selectedCaso.getQuemNegouFlor() == chosenCase) {
                    action = "0";
                } else {
                    if(selectedCaso.getQuemContraFlorResto() != null && selectedCaso.getQuemContraFlorResto() == chosenCase) {
                        action = "5";
                    } else {
                        action = "1";
                    }
                }
                break;
            case CONTRA_FLOR_RESTO:
                //0: NÃO ACEITAR 1: ACEITAR;
                if (selectedCaso.getQuemNegouFlor() != null && selectedCaso.getQuemNegouFlor() == chosenCase) {
                    action = "0";
                } else {
                    action = "1";
                }
                break;
        }

        switch (statesDecision) {
            case FLOR_FLOR:

                //0: NÃO ACEITAR 1: ACEITAR; 2: ENVIDO_ENVIDO; 3: REAL_ENVIDO; 4: FALTA_ENVIDO
                if (selectedCaso.getQuemNegouEnvido() != null && selectedCaso.getQuemNegouEnvido() == chosenCase) {
                    action = "0";
                } else {
                    if (selectedCaso.getQuemEnvidoEnvido() != null && selectedCaso.getQuemEnvidoEnvido() == chosenCase) {
                        action = "2";
                    } else if (selectedCaso.getQuemPediuRealEnvido() != null && selectedCaso.getQuemPediuRealEnvido() == chosenCase) {
                        action = "3";
                    } else if (selectedCaso.getQuemPediuFaltaEnvido() != null && selectedCaso.getQuemPediuFaltaEnvido() == chosenCase) {
                        action = "4";
                    } else {
                        action = "1";
                    }
                }
                break;
            case ENVIDO_ENVIDO:
                //0: NÃO ACEITAR 1: ACEITAR; 3: REAL_ENVIDO; 4: FALTA_ENVIDO
                if (selectedCaso.getQuemNegouEnvido() != null && selectedCaso.getQuemNegouEnvido() == chosenCase) {
                    action = "0";
                } else {
                    if (selectedCaso.getQuemPediuRealEnvido() != null && selectedCaso.getQuemPediuRealEnvido() == chosenCase) {
                        action = "3";
                    } else if (selectedCaso.getQuemPediuFaltaEnvido() != null && selectedCaso.getQuemPediuFaltaEnvido() == chosenCase) {
                        action = "4";
                    } else {
                        action = "1";
                    }
                }
                break;
            case REAL_ENVIDO:
                //0: NÃO ACEITAR 1: ACEITAR; 4: FALTA_ENVIDO
                if (selectedCaso.getQuemNegouEnvido() != null && selectedCaso.getQuemNegouEnvido() == chosenCase) {
                    action = "0";
                } else {
                    if (selectedCaso.getQuemPediuFaltaEnvido() != null && selectedCaso.getQuemPediuFaltaEnvido() == chosenCase) {
                        action = "4";
                    } else {
                        action = "1";
                    }
                }
                break;
            case FALTA_ENVIDO:
                //0: NÃO ACEITAR 1: ACEITAR;
                if (selectedCaso.getQuemNegouEnvido() != null && selectedCaso.getQuemNegouEnvido() == chosenCase) {
                    action = "0";
                } else {
                    action = "1";
                }
                break;
        }

        return action;
    }

    private Card playHandCardDesc(GameState gameState) {

        Card card;

        if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(2))) {
            card = gameState.getAgentCards().get(2);
        } else if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(1)))  {
            card = gameState.getAgentCards().get(1);
        } else {
            card = gameState.getAgentCards().get(0);
        }

        return card;

    }

    private Card getPreferCartaBaixa(GameState gameState) {

        Card card;

        if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(0))) {
            card =  gameState.getAgentCards().get(0);
        } else if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(1))) {
            card = gameState.getAgentCards().get(1);
        } else {
            card = gameState.getAgentCards().get(2);
        }

        return card;

    }

    private Card getPreferCartaMedia(GameState gameState) {

        Card card;

        if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(1))) {
            card =  gameState.getAgentCards().get(1);
        } else if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(2))) {
            card = gameState.getAgentCards().get(2);
        } else {
            card = gameState.getAgentCards().get(0);
        }

        return card;

    }

    private Card getPreferCartaAlta(GameState gameState) {

        Card card;

        if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(2))) {
            card =  gameState.getAgentCards().get(2);
        } else if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(1))) {
            card = gameState.getAgentCards().get(1);
        } else {
            card = gameState.getAgentCards().get(0);
        }

        return card;

    }

    private Card playFirstCardFooter(GameState gameState, int chooseCase, TrucoDescription selectedCaso) {

        Card card = null;

        if (selectedCaso.getGanhadorPrimeiraRodada() != null && selectedCaso.getGanhadorPrimeiraRodada() == chosenCase) {

            if (gameState.getAgentCards().get(0).getCbrCode() >
                    gameState.getOpponentPlayedCards().get(0).getCbrCode()) {

                card = gameState.getAgentCards().get(0);

            } else if (gameState.getAgentCards().get(1).getCbrCode() >
                    gameState.getOpponentPlayedCards().get(0).getCbrCode()) {
                card = gameState.getAgentCards().get(1);

            } else {
                card = gameState.getAgentCards().get(2);
            }

        } else {

            if (chooseCase == 1) {

                if (selectedCaso.getPrimeiraCartaRobo() != null) {
                    if (selectedCaso.getPrimeiraCartaRobo() == selectedCaso.getCartaBaixaRobo()) {
                        card = gameState.getAgentCards().get(0);
                    } else if (selectedCaso.getPrimeiraCartaRobo() == selectedCaso.getCartaMediaRobo()) {
                        card = gameState.getAgentCards().get(1);
                    } else {
                        card = gameState.getAgentCards().get(2);
                    }
                } else {
                    card = playHandCardDesc(gameState);
                }

            } else {
                if (selectedCaso.getPrimeiraCartaHumano() != null) {
                    if (selectedCaso.getPrimeiraCartaHumano() == selectedCaso.getCartaBaixaHumano()) {
                        card = gameState.getAgentCards().get(0);
                    } else if (selectedCaso.getPrimeiraCartaHumano() == selectedCaso.getCartaMediaHumano()) {
                        card = gameState.getAgentCards().get(1);
                    } else {
                        card = gameState.getAgentCards().get(2);
                    }
                } else {
                    card = playHandCardDesc(gameState);
                }
            }
        }

        return card;

    }

    private Card playSecondCardHandLooserRound1(GameState gameState) {
        Card card;

        if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(2))) {
            card = gameState.getAgentCards().get(2);
        } else {
            card = gameState.getAgentCards().get(1);
        }

        return card;
    }

    private Card playSecondCardFooter(GameState gameState) {

        Card card = null;

        if (gameState.getOpponentPlayedCards().get(1) != null) {

            if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(0))) {
                if (gameState.getAgentCards().get(0).getCbrCode() >
                        gameState.getOpponentPlayedCards().get(1).getCbrCode()) {
                    card = gameState.getAgentCards().get(0);
                } else {
                    card = getPreferCartaAlta(gameState);
                    if (card.getCbrCode() < gameState.getOpponentPlayedCards().get(1).getCbrCode()) {
                        card = null;
                    }
                }
            } else if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(1))) {
                if (gameState.getAgentCards().get(1).getCbrCode() >
                        gameState.getOpponentPlayedCards().get(1).getCbrCode()) {
                    card = gameState.getAgentCards().get(1);
                } else {
                    card = getPreferCartaAlta(gameState);
                    if (card.getCbrCode() < gameState.getOpponentPlayedCards().get(1).getCbrCode()) {
                        card = null;
                    }
                }
            } else {
                if (gameState.getAgentCards().get(2).getCbrCode() >
                        gameState.getOpponentPlayedCards().get(1).getCbrCode()) {
                    card = gameState.getAgentCards().get(1);
                } else {
                    card = getPreferCartaAlta(gameState);
                    if (card.getCbrCode() < gameState.getOpponentPlayedCards().get(1).getCbrCode()) {
                        card = null;
                    }
                }
            }

        } else {
                card = getPreferCartaAlta(gameState);
            if (card.getCbrCode() < gameState.getOpponentPlayedCards().get(1).getCbrCode()) {
                card = null;
            }
        }

        return card;

    }

    private Card playLastCard(GameState gameState) {

        Card card = null;

        if (gameState.getOpponentPlayedCards().get(2) != null) {

            if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(0))) {
                if (gameState.getAgentCards().get(0).getCbrCode() >
                        gameState.getOpponentPlayedCards().get(2).getCbrCode()) {
                    card = gameState.getAgentCards().get(0);
                } else if (gameState.getAgentCards().get(0).getCbrCode() == gameState.getOpponentPlayedCards().get(2).getCbrCode()
                        && gameState.isWinnerRound1()) {
                    card = gameState.getAgentCards().get(0);
                }

            } else if (!gameState.isPlayedCard(gameState.getAgentPlayedCards(), gameState.getAgentCards().get(1))) {
                if (gameState.getAgentCards().get(1).getCbrCode() >
                        gameState.getOpponentPlayedCards().get(2).getCbrCode()) {
                    card = gameState.getAgentCards().get(1);
                } else if (gameState.getAgentCards().get(1).getCbrCode() == gameState.getOpponentPlayedCards().get(2).getCbrCode()
                        && gameState.isWinnerRound1()) {
                    card = gameState.getAgentCards().get(0);
                }

            } else {
                if (gameState.getAgentCards().get(2).getCbrCode() >
                        gameState.getOpponentPlayedCards().get(2).getCbrCode()) {
                    card = gameState.getAgentCards().get(1);

                } else if (gameState.getAgentCards().get(2).getCbrCode() == gameState.getOpponentPlayedCards().get(2).getCbrCode()
                        && gameState.isWinnerRound1()) {
                    card = gameState.getAgentCards().get(2);
                }
            }

        } else {
            card = playHandCardDesc(gameState);
        }

        return card;

    }

}
