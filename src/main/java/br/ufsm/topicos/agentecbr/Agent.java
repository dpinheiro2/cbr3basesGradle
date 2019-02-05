package br.ufsm.topicos.agentecbr;

import br.ufsm.topicos.enuns.Face;
import br.ufsm.topicos.enuns.StatesDecision;
import br.ufsm.topicos.enuns.Suit;
import br.ufsm.topicos.model.Card;
import br.ufsm.topicos.websocket.LocalEndpoint;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.*;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 16/12/2018.
 */


public class Agent {

    private LocalEndpoint endpoint;

    private AdultBrain adultBrain;
    private BabyBrain babyBrain;

    private GameState gameState;

    public Agent() throws InitializingException {
        this.gameState = GameState.getInstance();
        this.adultBrain = new AdultBrain();
        this.babyBrain = new BabyBrain();
    }

    public LocalEndpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(LocalEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setReceiptCards(JsonObject messageJson) {
        JsonReader reader = Json.createReader(new StringReader(messageJson.getString("cartas")));
        JsonObject cartas = reader.readObject();
        JsonArray ListCartas = cartas.getJsonArray("cards");

        for(int i=0; i<ListCartas.size();i++) {
            JsonObject temp = ListCartas.getJsonObject(i);
            String face = temp.getString("face");
            String suit = temp.getString("suit");
            int cbrCode = temp.getInt("cbrCode");
            Card card = new Card(getFaceByString(face), getSuitByString(suit), cbrCode);
            gameState.getAgentCards().add(card);
            gameState.getAgentHandCards().add(card);
            System.out.println(card.toString());
        }
        gameState.setEnvidoPoints(getEnvidoPoints(gameState.getAgentCards()));
        Collections.sort(gameState.getAgentCards(), Card.compareCards()); //Alta-2 Media-1 Baixa-0
    }

    public void addCardTable(Card card){

        gameState.getDealtCards().add(card);

        if (gameState.getDealtCards().size() < 2) {
            gameState.setCurrentRound(1);
        } else if (gameState.getDealtCards().size() >= 2 && gameState.getDealtCards().size() < 4) {
            gameState.setCurrentRound(2);
        } else  {
            gameState.setCurrentRound(3);
        }
    }

    private void wait(int i) {
        int time = i*1000;
        try {
            Thread.currentThread().sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void receivedToken(StatesDecision statesDecision) {

        String action = "0";

        wait(3);

        switch (statesDecision) {
            case FLOR:
                //0: NADA 2: FLOR_FLOR; 3: CONTRA_FLOR;
                if (gameState.isHasFlor()) {
                    action = babyBrain.callFlor(gameState, statesDecision);
                    if (action == "2") {
                        endpoint.callFlor("2");
                        return;
                    } else if (action == "3") {
                        endpoint.callFlor("3");
                        return;
                    }
                } else {
                    action = "0";
                }
                break;
            case FLOR_FLOR:
                //0: NÃO ACEITAR 1: ACEITAR; 3: CONTRA_FLOR;
                action = babyBrain.callFlor(gameState, statesDecision);
                if(action == "0") {
                    endpoint.decline("3");
                    return;
                } else if (action == "1") {
                    endpoint.accept("3");
                    return;
                } else if (action == "3") {
                    endpoint.callFlor("3");
                    return;
                }
                break;
            case CONTRA_FLOR:
                //0: NÃO ACEITAR 1: ACEITAR; 4: CONTRA_FLOR_FALTA; 5: CONTRA_FLOR_RESTO;
                action = babyBrain.callFlor(gameState, statesDecision);
                if(action == "0") {
                    endpoint.decline("3");
                    return;
                } else if (action == "1") {
                    endpoint.accept("3");
                    return;
                } else if (action == "4") {
                    endpoint.callFlor("4");
                    return;
                } else if (action == "5") {
                    endpoint.callFlor("5");
                    return;
                }
                break;
            case CONTRA_FLOR_FALTA:
                //0: NÃO ACEITAR 1: ACEITAR; 5: CONTRA_FLOR_RESTO;
                action = babyBrain.callFlor(gameState, statesDecision);
                if(action == "0") {
                    endpoint.decline("3");
                    return;
                } else if (action == "1") {
                    endpoint.accept("3");
                    return;
                } else if (action == "5") {
                    endpoint.callFlor("5");
                    return;
                }
                break;
            case CONTRA_FLOR_RESTO:
                //0: NÃO ACEITAR 1: ACEITAR;
                action = babyBrain.callFlor(gameState, statesDecision);
                if(action == "0") {
                    endpoint.decline("3");
                    return;
                } else if (action == "1") {
                    endpoint.accept("3");
                    return;
                }
                break;
            case ENVIDO:
                if (gameState.isHasFlor() && !gameState.isFlor()) {
                    endpoint.callFlor("1");
                    return;
                } else {
                    //0: NÃO ACEITAR 1: ACEITAR; 2: ENVIDO_ENVIDO; 3: REAL_ENVIDO; 4: FALTA_ENVIDO
                    action = babyBrain.callEnvido(gameState, statesDecision);
                    if(action == "0") {
                        endpoint.decline("1");
                        return;
                    } else if (action == "1") {
                        endpoint.accept("1");
                        return;
                    } else if (action == "2") {
                        endpoint.callEnvido("2");
                        return;
                    } else if (action == "3") {
                        endpoint.callEnvido("3");
                        return;
                    } else if (action == "4") {
                        endpoint.callEnvido("4");
                        return;
                    }
                }
                break;
            case ENVIDO_ENVIDO:
                //0: NÃO ACEITAR 1: ACEITAR; 3: REAL_ENVIDO; 4: FALTA_ENVIDO
                action = babyBrain.callEnvido(gameState, statesDecision);
                if(action == "0") {
                    endpoint.decline("1");
                    return;
                } else if (action == "1") {
                    endpoint.accept("1");
                    return;
                } else if (action == "3") {
                    endpoint.callEnvido("3");
                    return;
                } else if (action == "4") {
                    endpoint.callEnvido("4");
                    return;
                }
                break;
            case REAL_ENVIDO:
                //0: NÃO ACEITAR 1: ACEITAR; 4: FALTA_ENVIDO
                action = babyBrain.callEnvido(gameState, statesDecision);
                if(action == "0") {
                    endpoint.decline("1");
                    return;
                } else if (action == "1") {
                    endpoint.accept("1");
                    return;
                } else if (action == "4") {
                    endpoint.callEnvido("4");
                    return;
                }
                break;
            case FALTA_ENVIDO:
                //0: NÃO ACEITAR 1: ACEITAR;
                action = babyBrain.callEnvido(gameState, statesDecision);
                if(action == "0") {
                    endpoint.decline("1");
                    return;
                } else if (action == "1") {
                    endpoint.accept("1");
                    return;
                }
                break;
            case TRUCO:
                //0: NÃO ACEITAR 1: ACEITAR; 2: RETRUCO
                action = babyBrain.callTrucoReply(gameState, statesDecision);
                if (action == "0") {
                    endpoint.decline("2");
                    return;
                } else if (action == "1") {
                    endpoint.accept("2");
                    return;
                } else if (action == "2") {
                    endpoint.callTruco("2");
                }
                break;
            case RETRUCO:
                //0: NÃO ACEITAR 1: ACEITAR; 3: VALE4
                action = babyBrain.callTrucoReply(gameState, statesDecision);
                if (action == "0") {
                    endpoint.decline("2");
                    return;
                } else if (action == "1") {
                    endpoint.accept("2");
                    return;
                } else if (action == "3") {
                    endpoint.callTruco("3");
                }
                break;
            case VALE4:
                //0: NÃO ACEITAR 1: ACEITAR;
                action = babyBrain.callTrucoReply(gameState, statesDecision);
                if (action == "0") {
                    endpoint.decline("2");
                    return;
                } else if (action == "1") {
                    endpoint.accept("2");
                    return;
                }
                break;
        }

    }


    public void receivedTurn(StatesDecision statesDecision) {

        wait(10);

        if (gameState.getDealtCards().size() < 2) {
            if (!gameState.isFlor() && !gameState.isEnvido()) {
                if (gameState.isHasFlor()) {
                    endpoint.callFlor("1");
                    return;
                } else {
                    String action = babyBrain.callEnvido(gameState, statesDecision);
                    if (action == "1") {
                        endpoint.callEnvido("1");
                        return;
                    } else if (action == "3") {
                        endpoint.callEnvido("3");
                        return;
                    } else if (action == "4") {
                        endpoint.callEnvido("4");
                        return;
                    }
                }
            }
        }

        wait(3);
        if (!gameState.isTruco()) {

            if (babyBrain.callTruco(gameState, statesDecision)) {
                endpoint.callTruco("1");
                return;
            }

        } /*else {
            //TODO: implementar quando ja pediram truco
        }*/

        wait(3);
        Card card = babyBrain.playCard(gameState, statesDecision);


        if (card != null) {
            if (babyBrain.getCartaVirada() != 0 && babyBrain.getCartaVirada() == gameState.getCurrentRound()) {
                gameState.getAgentPlayedCards().add(card);
                endpoint.playFacedDownCard(card);
                return;
            } else {

                gameState.getAgentHandCards().remove(card);

                Collections.sort( gameState.getAgentHandCards(), Card.compareCards());
                endpoint.playCard(card);
                return;
            }
        } else {
            endpoint.irBaralho();
            return;
        }

    }

    public void showPoints() {

        boolean showPoints = babyBrain.showPoints(gameState);
        endpoint.showPoints(showPoints);
        return;
    }

    public Suit getSuitByString(String suitString) {
        Suit suit = null;

        switch (suitString) {
            case "ESPADAS":
                suit = Suit.ESPADAS;
                break;
            case "BASTOS":
                suit = Suit.BASTOS;
                break;
            case "OURO":
                suit = Suit.OURO;
                break;
            case "COPAS":
                suit = Suit.COPAS;
                break;
        }

        return suit;
    }

    public Face getFaceByString(String faceString) {
        Face face = null;

        switch (faceString) {
            case "AS":
                face = Face.AS;
                break;
            case "DOIS":
                face = Face.DOIS;
                break;
            case "TRES":
                face = Face.TRES;
                break;
            case "QUATRO":
                face = Face.QUATRO;
                break;
            case "CINCO":
                face = Face.CINCO;
                break;
            case "SEIS":
                face = Face.SEIS;
                break;
            case "SETE":
                face = Face.SETE;
                break;
            case "DEZ":
                face = Face.DEZ;
                break;
            case "VALETE":
                face = Face.VALETE;
                break;
            case "REI":
                face = Face.REI;
                break;
        }

        return face;
    }

    public int getEnvidoPoints(ArrayList<Card> cards) {
        HashMap<Suit, ArrayList<Card>> cardsBySuit = new HashMap<>();

        cards.forEach(card -> {
            if (!cardsBySuit.containsKey(card.getSuit())) {
                cardsBySuit.put(card.getSuit(), new ArrayList<>());
            }
            cardsBySuit.get(card.getSuit()).add(card);
        });

        int modeLength = 0;
        Suit modeSuit = Suit.ESPADAS;

        for(Suit suit : cardsBySuit.keySet()){
            if(cardsBySuit.get(suit).size() > modeLength){
                modeLength = cardsBySuit.get(suit).size();
                modeSuit = suit;
            }
        }
        int envidoPoints = 0;

        if(modeLength > 1) {
            ArrayList<Card> envidoCards = cardsBySuit.get(modeSuit);
            envidoPoints = 20;

            for(Card card : envidoCards){
                envidoPoints += card.getFace().getValue();
            }
            return envidoPoints;
        } else {
            for(Card card : cards) {
                if(card.getFace().getValue() > envidoPoints)
                {
                    envidoPoints = card.getFace().getValue();
                }
            }
            return envidoPoints;
        }
    }

}
