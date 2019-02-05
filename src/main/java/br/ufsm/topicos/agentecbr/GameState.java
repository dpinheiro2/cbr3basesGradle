package br.ufsm.topicos.agentecbr;

import br.ufsm.topicos.model.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 28/01/2019.
 */


public class GameState implements Serializable {

    private static GameState _instance = null;

    public static GameState getInstance()
    {
        if(_instance == null)
            _instance = new GameState();
        return _instance;
    }

    private ArrayList<Card> agentCards;
    private ArrayList<Card> agentHandCards;
    private LinkedList<Card> agentPlayedCards;
    private LinkedList<Card> opponentPlayedCards;
    private LinkedList<Card> dealtCards;

    private int agentPoints = 0;
    private int opponentPoints = 0;

    private int envidoPoints = 0;
    private int opponentEnvidoPoints = 0;

    private boolean isWinnerRound1 = false;
    private boolean isEmpateRound1 = false;

    private boolean isWinnerRound2 = false;
    private boolean isEmpateRound2 = false;

    private boolean isWinnerRound3 = false;
    private boolean isEmpateRound3 = false;

    private int opponentCartaVirada = 0;
    private int agentCartaVirada = 0;

    private boolean isHand;
    private boolean turno;
    private boolean token;
    private boolean isPlaying;
    private boolean hasFlor;
    private boolean envido;
    private boolean flor;
    private boolean truco;
    private boolean isLastRaise;
    private int trucoLevel = 0;
    private int florLevel = 0;
    private int envidoLevel = 0;
    private boolean isLastFlorRaise;
    private int currentRound = 1;


    public ArrayList<Card> getAgentCards() {
        return agentCards;
    }

    public void setAgentCards(ArrayList<Card> agentCards) {
        this.agentCards = agentCards;
    }

    public LinkedList<Card> getAgentPlayedCards() {
        return agentPlayedCards;
    }

    public void setAgentPlayedCards(LinkedList<Card> agentPlayedCards) {
        this.agentPlayedCards = agentPlayedCards;
    }

    public LinkedList<Card> getOpponentPlayedCards() {
        return opponentPlayedCards;
    }

    public void setOpponentPlayedCards(LinkedList<Card> opponentPlayedCards) {
        this.opponentPlayedCards = opponentPlayedCards;
    }

    public LinkedList<Card> getDealtCards() {
        return dealtCards;
    }

    public void setDealtCards(LinkedList<Card> dealtCards) {
        this.dealtCards = dealtCards;
    }

    public int getAgentPoints() {
        return agentPoints;
    }

    public void setAgentPoints(int agentPoints) {
        this.agentPoints = agentPoints;
    }

    public int getOpponentPoints() {
        return opponentPoints;
    }

    public void setOpponentPoints(int opponentPoints) {
        this.opponentPoints = opponentPoints;
    }

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    public boolean isToken() {
        return token;
    }

    public void setToken(boolean token) {
        this.token = token;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isHasFlor() {
        return hasFlor;
    }

    public void setHasFlor(boolean hasFlor) {
        this.hasFlor = hasFlor;
    }

    public boolean isEnvido() {
        return envido;
    }

    public void setEnvido(boolean envido) {
        this.envido = envido;
    }

    public boolean isFlor() {
        return flor;
    }

    public void setFlor(boolean flor) {
        this.flor = flor;
    }

    public boolean isTruco() {
        return truco;
    }

    public void setTruco(boolean truco) {
        this.truco = truco;
    }

    public boolean isLastRaise() {
        return isLastRaise;
    }

    public void setLastRaise(boolean lastRaise) {
        isLastRaise = lastRaise;
    }

    public int getTrucoLevel() {
        return trucoLevel;
    }

    public void setTrucoLevel(int trucoLevel) {
        this.trucoLevel = trucoLevel;
    }

    public int getFlorLevel() {
        return florLevel;
    }

    public void setFlorLevel(int florLevel) {
        this.florLevel = florLevel;
    }

    public int getEnvidoLevel() {
        return envidoLevel;
    }

    public void setEnvidoLevel(int envidoLevel) {
        this.envidoLevel = envidoLevel;
    }

    public boolean isLastFlorRaise() {
        return isLastFlorRaise;
    }

    public void setLastFlorRaise(boolean lastFlorRaise) {
        isLastFlorRaise = lastFlorRaise;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public int getEnvidoPoints() {
        return envidoPoints;
    }

    public void setEnvidoPoints(int envidoPoints) {
        this.envidoPoints = envidoPoints;
    }

    public boolean isHand() {
        return isHand;
    }

    public void setHand(boolean hand) {
        isHand = hand;
    }

    public int getOpponentEnvidoPoints() {
        return opponentEnvidoPoints;
    }

    public void setOpponentEnvidoPoints(int opponentEnvidoPoints) {
        this.opponentEnvidoPoints = opponentEnvidoPoints;
    }

    public boolean isWinnerRound1() {
        return isWinnerRound1;
    }

    public void setWinnerRound1(boolean winnerRound1) {
        isWinnerRound1 = winnerRound1;
    }

    public boolean isEmpateRound1() {
        return isEmpateRound1;
    }

    public void setEmpateRound1(boolean empateRound1) {
        isEmpateRound1 = empateRound1;
    }

    public boolean isWinnerRound2() {
        return isWinnerRound2;
    }

    public void setWinnerRound2(boolean winnerRound2) {
        isWinnerRound2 = winnerRound2;
    }

    public boolean isEmpateRound2() {
        return isEmpateRound2;
    }

    public void setEmpateRound2(boolean empateRound2) {
        isEmpateRound2 = empateRound2;
    }

    public boolean isWinnerRound3() {
        return isWinnerRound3;
    }

    public void setWinnerRound3(boolean winnerRound3) {
        isWinnerRound3 = winnerRound3;
    }

    public boolean isEmpateRound3() {
        return isEmpateRound3;
    }

    public void setEmpateRound3(boolean empateRound3) {
        isEmpateRound3 = empateRound3;
    }

    public int getOpponentCartaVirada() {
        return opponentCartaVirada;
    }

    public void setOpponentCartaVirada(int opponentCartaVirada) {
        this.opponentCartaVirada = opponentCartaVirada;
    }

    public ArrayList<Card> getAgentHandCards() {
        return agentHandCards;
    }

    public void setAgentHandCards(ArrayList<Card> agentHandCards) {
        this.agentHandCards = agentHandCards;
    }

    public int getAgentCartaVirada() {
        return agentCartaVirada;
    }

    public void setAgentCartaVirada(int agentCartaVirada) {
        this.agentCartaVirada = agentCartaVirada;
    }

    public void initHand() {

        agentCards = new ArrayList<>();
        agentHandCards = new ArrayList<>();
        agentPlayedCards = new LinkedList<>();
        opponentPlayedCards = new LinkedList<>();
        dealtCards = new LinkedList<>();

        agentPoints = 0;
        opponentPoints = 0;

        envidoPoints = 0;
        opponentEnvidoPoints = 0;

        isWinnerRound1 = false;
        isEmpateRound1 = false;

        isWinnerRound2 = false;
        isEmpateRound2 = false;

        isWinnerRound3 = false;
        isEmpateRound3 = false;

        opponentCartaVirada = 0;
        agentCartaVirada = 0;

        turno  = false;
        token  = false;
        isPlaying = false;
        envido = false;
        flor = false;
        truco = false;
        isLastRaise = false;
        trucoLevel = 0;
        florLevel = 0;
        envidoLevel = 0;
        isLastFlorRaise = false;
        currentRound = 1;
    }

    public boolean isPlayedCard(LinkedList<Card> list, Card card) {

        for (Card c : list) {
            if (c.getFace() == card.getFace() && c.getSuit()==card.getSuit()) {
                return true;
            }
            /*if (c.equals(card)) {
                return true;
            }*/
        }


        return false;
    }



}
