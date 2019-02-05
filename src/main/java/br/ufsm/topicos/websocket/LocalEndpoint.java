package br.ufsm.topicos.websocket;

import br.ufsm.topicos.agentecbr.Agent;
import br.ufsm.topicos.enuns.StatesDecision;
import br.ufsm.topicos.model.Card;
import com.google.gson.Gson;
import javafx.application.Platform;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;
import java.io.StringReader;


/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Modelagem de Software - Design Patterns
 * Daniel Pinheiro Vargas
 * Criado em 16/10/2018.
 */


public class LocalEndpoint extends Endpoint implements MessageHandler.Whole<String> {

    public static AgentUI agentUI;

    private Agent agent;
    private Session session;

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("Endpoint opened, session = " + session + ", config = " + config);
        this.session = session;
        session.addMessageHandler(this);
        agentUI.setEndpoint(this);
        agent = agentUI.getAgent();
        agent.setEndpoint(this);
    }

    //Mensagens recebidas do server
    @Override
    public void onMessage(String message) {

        Platform.runLater(new Runnable() {
            public void run() {
                JsonReader reader = Json.createReader(new StringReader(message));
                JsonObject messageJson = reader.readObject();
                System.out.println("MSG RECEBIDA: " + messageJson.toString());
                switch (messageJson.getString("action")) {
                    case "WAIT":
                        agentUI.setInfo("Aguardando oponente!");
                        break;
                    case "START":

                        agent.getGameState().initHand();
                        agent.getGameState().setHand(messageJson.getBoolean("isHand"));
                        agent.getGameState().setTurno(messageJson.getBoolean("isTurn"));
                        agent.getGameState().setToken(messageJson.getBoolean("isToken"));
                        agent.getGameState().setHasFlor(messageJson.getBoolean("hasFlor"));
                        agent.setReceiptCards(messageJson);
                        agent.getGameState().setCurrentRound(1);
                        if (agent.getGameState().isTurno()) {
                            agent.receivedTurn(StatesDecision.START_HAND);
                        }
                        break;
                    case "SHIFT_TURN":
                        agent.getGameState().setTurno(messageJson.getBoolean("isTurn"));
                        agent.getGameState().setToken(messageJson.getBoolean("isToken"));
                        break;
                    case "SHIFT_TOKEN":
                        agent.getGameState().setToken(messageJson.getBoolean("isToken"));
                        break;
                    case "FLOR":
                        //TODO: mensagem enviada para os dois jogadores, quem pediu aguarda resposta, caso o adversário tenha flor.
                        /**
                         *  Informações vindas do servidor:
                         *  content - "Aguardando resposta de: ..." / "... cantou ..."
                         *  tipoFlor - (1: FLOR; 2: FLOR_FLOR; 3: CONTRA_FLOR; 4: CONTRA_FLOR_FALTA; 5: CONTRA_FLOR_RESTO);
                         *  isPediu - true/false
                         *  hasFlor - true/false
                         *  florSize - Tamanho da cadeia de Flor, por exemplo FLOR + CONTRA_FLOR, florSize = 2
                         */

                        int tipoFlor = messageJson.getInt("tipoFlor");
                        agent.getGameState().setHasFlor(messageJson.getBoolean("hasFlor"));
                        agent.getGameState().setFlorLevel(messageJson.getInt("florSize"));
                        agent.getGameState().setFlor(true);
                        if (!messageJson.getBoolean("isPediu") && agent.getGameState().isHasFlor()) {

                            switch (tipoFlor) {
                                case 1:
                                    agent.receivedToken(StatesDecision.FLOR);
                                    break;
                                case 2:
                                    agent.receivedToken(StatesDecision.FLOR_FLOR);
                                    break;
                                case 3:
                                    agent.receivedToken(StatesDecision.CONTRA_FLOR);
                                    break;
                                case 4:
                                    agent.receivedToken(StatesDecision.CONTRA_FLOR_FALTA);
                                    break;
                                case 5:
                                    agent.receivedToken(StatesDecision.CONTRA_FLOR_RESTO);
                                    break;
                            }

                        }

                        if (tipoFlor == 1 && agent.getGameState().isTurno()) {
                            agent.receivedTurn(StatesDecision.START_HAND);
                        }
                        break;

                    case "ENVIDO":

                        int tipoEnvido = messageJson.getInt("tipoEnvido");
                        agent.getGameState().setHasFlor(messageJson.getBoolean("hasFlor"));
                        agent.getGameState().setEnvidoLevel(messageJson.getInt("envidoSize"));
                        agent.getGameState().setEnvido(true);

                        if (!messageJson.getBoolean("isPediu") ){

                            switch (tipoEnvido) {
                                case 1:
                                    agent.receivedToken(StatesDecision.ENVIDO);
                                    break;
                                case 2:
                                    agent.receivedToken(StatesDecision.ENVIDO_ENVIDO);
                                    break;
                                case 3:
                                    agent.receivedToken(StatesDecision.REAL_ENVIDO);
                                    break;
                                case 4:
                                    agent.receivedToken(StatesDecision.FALTA_ENVIDO);
                                    break;
                            }
                        }

                        break;
                    case "TRUCO":

                        int tipoTruco = messageJson.getInt("tipoTruco");
                        agent.getGameState().setHasFlor(messageJson.getBoolean("hasFlor"));
                        agent.getGameState().setTruco(true);

                        if (!messageJson.getBoolean("isPediu") ){

                            switch (tipoTruco) {
                                case 1:
                                    agent.receivedToken(StatesDecision.TRUCO);
                                    break;
                                case 2:
                                    agent.receivedToken(StatesDecision.RETRUCO);
                                    break;
                                case 3:
                                    agent.receivedToken(StatesDecision.VALE4);
                                    break;
                            }
                        }

                        break;
                    case "ENVIDO_ERROR":
                        //TODO: caso de exception no envido essa mensagem apenas é enviada para quem pediu
                        /**
                         * Informações vindas do servidor:
                         * content - "Exceção"
                         * tipoEnvido - (1: ENVIDO; 2: ENVIDO_ENVIDO; 3: REAL_ENVIDO; 4: FALTA_ENVIDO)
                         * isPediu = false
                         * hasFlor = false
                         * envidoSize = 0
                         */
                        break;
                    case "PLAY_CARD":
                        //TODO: mensagem enviada para os dois jogadores
                        /**
                         * Informações vindas do servidor:
                         * isPlayed - true/false
                         * card - json
                         * isHand - true/false
                         * hasFlor - true/false
                         * trucoSize - Tamanho da cadeia de Truco
                         * envidoSize - Tamanho da cadeia de Envido
                         * florSize - Tamanho da cadeia de Flor
                         */
                        JsonReader jsonReader = Json.createReader(new StringReader(messageJson.getString("card")));
                        JsonObject card = jsonReader.readObject();
                        String face = card.getString("face");
                        String suit = card.getString("suit");
                        int cbrCode = card.getInt("cbrCode");
                        Card carta = new Card(agent.getFaceByString(face), agent.getSuitByString(suit), cbrCode);
                        agent.addCardTable(carta);
                        if (messageJson.getBoolean("isPlayed")) {
                            agent.getGameState().getAgentPlayedCards().add(carta);
                        } else {
                            agent.getGameState().getOpponentPlayedCards().add(carta);
                        }


                            switch (agent.getGameState().getDealtCards().size()) {
                               /* case 0:
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_1);
                                    break;*/
                                case 1:
                                    if (agent.getGameState().isTurno()) {
                                        agent.receivedTurn(StatesDecision.PLAY_CARD_2);
                                    }

                                    break;
                                case 2:
                                    if (agent.getGameState().isTurno()) {
                                        agent.receivedTurn(StatesDecision.PLAY_CARD_3);
                                    }
                                    int agentCardRound1 = 0;
                                    int opponentCardRound1 = 0;
                                    if (agent.getGameState().getAgentPlayedCards().size() > 0 &&
                                            agent.getGameState().getAgentPlayedCards().get(0) != null) {
                                        agentCardRound1 = agent.getGameState().getAgentPlayedCards().get(0).getCbrCode();
                                    }

                                    if (agent.getGameState().getOpponentPlayedCards().size() > 0 &&
                                            agent.getGameState().getOpponentPlayedCards().get(0) != null) {
                                        opponentCardRound1 = agent.getGameState().getOpponentPlayedCards().get(0).getCbrCode();
                                    }

                                    if (agentCardRound1 > opponentCardRound1) {
                                        agent.getGameState().setWinnerRound1(true);
                                        agent.getGameState().setEmpateRound1(false);
                                    } else if (agentCardRound1 == opponentCardRound1) {
                                        agent.getGameState().setWinnerRound1(false);
                                        agent.getGameState().setEmpateRound1(true);
                                    } else {
                                        agent.getGameState().setWinnerRound1(false);
                                        agent.getGameState().setEmpateRound1(false);
                                    }
                                    break;
                                case 3:
                                    if (agent.getGameState().isTurno()) {
                                        agent.receivedTurn(StatesDecision.PLAY_CARD_4);
                                    }

                                    break;
                                case 4:
                                    if (agent.getGameState().isTurno()) {
                                        agent.receivedTurn(StatesDecision.PLAY_CARD_5);
                                    }
                                    int agentCardRound2 = 0;
                                    int opponentCardRound2 = 0;
                                    if (agent.getGameState().getAgentPlayedCards().size() > 0 &&
                                            agent.getGameState().getAgentPlayedCards().get(0) != null) {
                                        agentCardRound2 = agent.getGameState().getAgentPlayedCards().get(0).getCbrCode();
                                    }

                                    if (agent.getGameState().getOpponentPlayedCards().size() > 0 &&
                                            agent.getGameState().getOpponentPlayedCards().get(0) != null) {
                                        opponentCardRound2 = agent.getGameState().getOpponentPlayedCards().get(0).getCbrCode();
                                    }

                                    if (agentCardRound2 > opponentCardRound2) {
                                        agent.getGameState().setWinnerRound2(true);
                                        agent.getGameState().setEmpateRound2(false);
                                    } else if (agentCardRound2 == opponentCardRound2) {
                                        agent.getGameState().setWinnerRound2(false);
                                        agent.getGameState().setEmpateRound2(true);
                                    } else {
                                        agent.getGameState().setWinnerRound2(false);
                                        agent.getGameState().setEmpateRound2(false);
                                    }
                                    break;
                                case 5:
                                    if (agent.getGameState().isTurno()) {
                                        agent.receivedTurn(StatesDecision.PLAY_CARD_6);
                                    }

                                    break;
                            }

                        break;
                    case "FACE_DOWN_CARD":
                        //TODO: mensagem enviada para os dois jogadores
                        /**
                         * Informações vindas do servidor:
                         * isPlayed - true/false
                         * round - int
                         * isHand - true/false
                         * hasFlor - true/false
                         * trucoSize - Tamanho da cadeia de Truco
                         * envidoSize - Tamanho da cadeia de Envido
                         * florSize - Tamanho da cadeia de Flor
                         */
                        Card faceDowncarta = null;
                        agent.addCardTable(faceDowncarta);
                        if (!messageJson.getBoolean("isPlayed")) {
                            agent.getGameState().getOpponentPlayedCards().add(faceDowncarta);
                        }

                        switch (agent.getGameState().getDealtCards().size()) {
                               /* case 0:
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_1);
                                    break;*/
                            case 1:
                                if (agent.getGameState().isTurno()) {
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_2);
                                }

                                break;
                            case 2:
                                if (agent.getGameState().isTurno()) {
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_3);
                                }
                                int agentCardRound1 = 0;
                                int opponentCardRound1 = 0;
                                if (agent.getGameState().getAgentPlayedCards().size() > 0 &&
                                        agent.getGameState().getAgentPlayedCards().get(0) != null) {
                                    agentCardRound1 = agent.getGameState().getAgentPlayedCards().get(0).getCbrCode();
                                }

                                if (agent.getGameState().getOpponentPlayedCards().size() > 0 &&
                                        agent.getGameState().getOpponentPlayedCards().get(0) != null) {
                                    opponentCardRound1 = agent.getGameState().getOpponentPlayedCards().get(0).getCbrCode();
                                }

                                if (agentCardRound1 > opponentCardRound1) {
                                    agent.getGameState().setWinnerRound1(true);
                                    agent.getGameState().setEmpateRound1(false);
                                } else if (agentCardRound1 == opponentCardRound1) {
                                    agent.getGameState().setWinnerRound1(false);
                                    agent.getGameState().setEmpateRound1(true);
                                } else {
                                    agent.getGameState().setWinnerRound1(false);
                                    agent.getGameState().setEmpateRound1(false);
                                }
                                break;
                            case 3:
                                if (agent.getGameState().isTurno()) {
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_4);
                                }

                                break;
                            case 4:
                                if (agent.getGameState().isTurno()) {
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_5);
                                }
                                int agentCardRound2 = 0;
                                int opponentCardRound2 = 0;
                                if (agent.getGameState().getAgentPlayedCards().size() > 0 &&
                                        agent.getGameState().getAgentPlayedCards().get(0) != null) {
                                    agentCardRound2 = agent.getGameState().getAgentPlayedCards().get(0).getCbrCode();
                                }

                                if (agent.getGameState().getOpponentPlayedCards().size() > 0 &&
                                        agent.getGameState().getOpponentPlayedCards().get(0) != null) {
                                    opponentCardRound2 = agent.getGameState().getOpponentPlayedCards().get(0).getCbrCode();
                                }

                                if (agentCardRound2 > opponentCardRound2) {
                                    agent.getGameState().setWinnerRound2(true);
                                    agent.getGameState().setEmpateRound2(false);
                                } else if (agentCardRound2 == opponentCardRound2) {
                                    agent.getGameState().setWinnerRound2(false);
                                    agent.getGameState().setEmpateRound2(true);
                                } else {
                                    agent.getGameState().setWinnerRound2(false);
                                    agent.getGameState().setEmpateRound2(false);
                                }
                                break;
                            case 5:
                                if (agent.getGameState().isTurno()) {
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_6);
                                }

                                break;
                        }

                        break;
                    case "RESULT_ROUND":
                        /*if (messageJson.getInt("round") == 1 ) {
                            agent.getGameState().setWinnerRound1(messageJson.getBoolean("isWinner"));
                            agent.getGameState().setEmpateRound1(messageJson.getBoolean("isEmpate"));
                        } else if (messageJson.getInt("round") == 2 ) {
                            agent.getGameState().setWinnerRound2(messageJson.getBoolean("isWinner"));
                            agent.getGameState().setEmpateRound2(messageJson.getBoolean("isEmpate"));
                        } else if (messageJson.getInt("round") == 3 ) {
                            agent.getGameState().setWinnerRound3(messageJson.getBoolean("isWinner"));
                            agent.getGameState().setEmpateRound3(messageJson.getBoolean("isEmpate"));
                        }*/
                        break;

                    case "IR_BARALHO":
                        //TODO: mensagem enviada para os dois jogadores. Apenas para informar quem foi ao baralho
                        /**
                         * Informações vindas do servidor:
                         * content - player.getName() + " Foi ao Baralho"
                         */
                        break;
                    case "RESULT_ENVIDO":
                        //TODO: mensagem enviada para os dois jogadores. Apenas para informar o resultado do envido
                        /**
                         * Informações vindas do servidor:
                         * result - fulano 32 x 27 ciclano / player.getName() + " não quis."
                         */
                        if (agent.getGameState().isTurno()) {
                            agent.receivedTurn(StatesDecision.START_HAND);
                        }

                        break;
                    case "RESULT_FLOR":
                        //TODO: mensagem enviada para os dois jogadores. Apenas para informar o resultado da flor
                        /**
                         * Informações vindas do servidor:
                         * result - fulano 32 x 27 ciclano / player.getName() + " se achicou."
                         */
                        if (agent.getGameState().isTurno()) {
                            agent.receivedTurn(StatesDecision.START_HAND);
                        }
                        break;
                    case "UPDATE_PLACAR":
                        agent.getGameState().setAgentPoints(messageJson.getInt("myPoints"));
                        agent.getGameState().setOpponentPoints(messageJson.getInt("otherPoints"));
                        break;
                    case "FINISH_HAND":
                        //TODO: mensagem enviada para os dois jogadores. Distribui novas cartas
                        /**
                         * Informações vindas do servidor:
                         * isHand - true/false
                         * isTurn - true/false
                         * isToken - true/false
                         * hasFlor - true/false
                         * cartas - Array<Card>
                         */

                        agent.getGameState().initHand();
                        agent.getGameState().setHand(messageJson.getBoolean("isHand"));
                        agent.getGameState().setTurno(messageJson.getBoolean("isTurn"));
                        agent.getGameState().setToken(messageJson.getBoolean("isToken"));
                        agent.getGameState().setHasFlor(messageJson.getBoolean("hasFlor"));
                        agent.setReceiptCards(messageJson);
                        agent.getGameState().setCurrentRound(1);
                        if (agent.getGameState().isTurno()) {
                            agent.receivedTurn(StatesDecision.START_HAND);
                        }
                        break;
                    case "FINISH_GAME":
                        //TODO: mensagem enviada para os dois jogadores. Apenas para informar que nova partida esta iniciando
                        /**
                         * Informações vindas do servidor:
                         *
                         */
                        break;
                    case "RESPONSE_TRUCO":
                        //TODO: mensagem enviada para os dois jogadores, quem pediu aguarda resposta do adversário. Serve apenas para atualizar info na tela
                        /**
                         * Informações vindas do servidor:
                         * content", "Aceitou"
                         * tipoTruco - (1: TRUCO; 2: RETRUCO; 3: VALE4;)
                         * isPediu - true/false
                         * round - 0
                         * isHand - true/false
                         * hasFlor - true/false
                         *
                         */
                        if (agent.getGameState().isTurno()) {
                            switch (agent.getGameState().getDealtCards().size()) {
                                case 0:
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_1);
                                    break;
                                case 1:
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_2);
                                    break;
                                case 2:
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_3);
                                    break;
                                case 3:
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_4);
                                    break;
                                case 4:
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_5);
                                    break;
                                case 5:
                                    agent.receivedTurn(StatesDecision.PLAY_CARD_6);
                                    break;
                            }
                        }
                        break;

                    case "ENVIDO_POINTS":
                        agent.getGameState().setOpponentEnvidoPoints(messageJson.getInt("opponentEnvidoPoints"));
                        if (agent.getGameState().getOpponentEnvidoPoints() < agent.getGameState().getEnvidoPoints()) {

                            showPoints(true);
                        } else {
                            showPoints(false);
                            //agent.showPoints();
                        }
                        break;
                }
            }
        });

    }

    /** TODO: chamar esses métodos para enviar mensagens ao servidor */

    /** :::: Métodos para enviar mensagens ao servidor ::::*/

    /** tipo 1-FLOR; 2-FLOR_FLOR; 3-CONTRA_FLOR; 4-CONTRA_FLOR_FALTA; 5-CONTRA_FLOR_RESTO*/
    public void callFlor(String tipo) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "FLOR")
                .add("tipo", tipo)
                .build();
        sendMessage(jsonMessage.toString());
    }

    /** tipo 1-ENVIDO; 2-ENVIDO_ENVIDO; 3-REAL_ENVIDO; 4-FALTA_ENVIDO; */
    public void callEnvido(String tipo) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "ENVIDO")
                .add("tipo", tipo)
                .build();
        sendMessage(jsonMessage.toString());
    }

    /** tipo 1-TRUCO; 2-RETRUCO; 3-VALE4;*/
    public void callTruco(String tipo) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "TRUCO")
                .add("tipo", tipo)
                .build();
        sendMessage(jsonMessage.toString());
    }

    public void irBaralho() {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "IR_BARALHO")
                .build();
        sendMessage(jsonMessage.toString());
    }

    /** tipo 1-ENVIDO; 2-TRUCO; 3-FLOR;*/
    public void accept(String tipo) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "QUERO")
                .add("tipo", tipo)
                .build();
        sendMessage(jsonMessage.toString());
    }

    /** tipo 1-ENVIDO; 2-TRUCO; 3-FLOR;*/
    public void decline(String tipo) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "NAO_QUERO")
                .add("tipo", tipo)
                .build();
        sendMessage(jsonMessage.toString());
    }


    /** TODO: mapear a carta a ser jogada para essa classe Card*/
    public void playCard(Card card) {
        Gson gson = new Gson();
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "PLAY_CARD")
                .add("card", gson.toJson(card))
                .build();
        sendMessage(jsonMessage.toString());
    }

    /** TODO: mapear a carta a ser jogada para essa classe Card*/
    public void playFacedDownCard(Card card) {
        Gson gson = new Gson();
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "FACE_DOWN_CARD")
                .add("card", gson.toJson(card))
                .build();
        sendMessage(jsonMessage.toString());
    }

    public void showPoints(boolean isShowPoints) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "SHOW_POINTS")
                .add("isShowPoints", isShowPoints)
                .build();
        sendMessage(jsonMessage.toString());
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
            System.out.println(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
