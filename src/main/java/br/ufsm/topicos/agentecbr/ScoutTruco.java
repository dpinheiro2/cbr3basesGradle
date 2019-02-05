package br.ufsm.topicos.agentecbr;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 22/01/2019.
 */


public class ScoutTruco {

    private int countCallTrucoRodada1Won;
    private int countCallTrucoRodada1Lost;
    private int countNoCallTrucoRodada1Won;
    private int countNoCallTrucoRodada1Lost;

    private int countPlayedCartaAltaWon;
    private int countPlayedCartaAltaLost;
    private int countPlayedCartaMediaWon;
    private int countPlayedCartaMediaLost;
    private int countPlayedCartaBaixaWon;
    private int countPlayedCartaBaixaLost;
    private int countWentDeck;

    public ScoutTruco(){

        countCallTrucoRodada1Won = 0;
        countCallTrucoRodada1Lost = 0;
        countNoCallTrucoRodada1Won = 0;
        countNoCallTrucoRodada1Lost = 0;
        countPlayedCartaAltaWon = 0;
        countPlayedCartaAltaLost = 0;
        countPlayedCartaMediaWon = 0;
        countPlayedCartaMediaLost = 0;
        countPlayedCartaBaixaWon = 0;
        countPlayedCartaBaixaLost = 0;
        countWentDeck = 0;
    }

    public int getCountCallTrucoRodada1Won() {
        return countCallTrucoRodada1Won;
    }

    public void setCountCallTrucoRodada1Won(int countCallTrucoRodada1Won) {
        this.countCallTrucoRodada1Won = countCallTrucoRodada1Won;
    }

    public int getCountCallTrucoRodada1Lost() {
        return countCallTrucoRodada1Lost;
    }

    public void setCountCallTrucoRodada1Lost(int countCallTrucoRodada1Lost) {
        this.countCallTrucoRodada1Lost = countCallTrucoRodada1Lost;
    }

    public int getCountNoCallTrucoRodada1Won() {
        return countNoCallTrucoRodada1Won;
    }

    public void setCountNoCallTrucoRodada1Won(int countNoCallTrucoRodada1Won) {
        this.countNoCallTrucoRodada1Won = countNoCallTrucoRodada1Won;
    }

    public int getCountNoCallTrucoRodada1Lost() {
        return countNoCallTrucoRodada1Lost;
    }

    public void setCountNoCallTrucoRodada1Lost(int countNoCallTrucoRodada1Lost) {
        this.countNoCallTrucoRodada1Lost = countNoCallTrucoRodada1Lost;
    }

    public int getCountPlayedCartaAltaWon() {
        return countPlayedCartaAltaWon;
    }

    public void setCountPlayedCartaAltaWon(int countPlayedCartaAltaWon) {
        this.countPlayedCartaAltaWon = countPlayedCartaAltaWon;
    }

    public int getCountPlayedCartaAltaLost() {
        return countPlayedCartaAltaLost;
    }

    public void setCountPlayedCartaAltaLost(int countPlayedCartaAltaLost) {
        this.countPlayedCartaAltaLost = countPlayedCartaAltaLost;
    }

    public int getCountPlayedCartaMediaWon() {
        return countPlayedCartaMediaWon;
    }

    public void setCountPlayedCartaMediaWon(int countPlayedCartaMediaWon) {
        this.countPlayedCartaMediaWon = countPlayedCartaMediaWon;
    }

    public int getCountPlayedCartaMediaLost() {
        return countPlayedCartaMediaLost;
    }

    public void setCountPlayedCartaMediaLost(int countPlayedCartaMediaLost) {
        this.countPlayedCartaMediaLost = countPlayedCartaMediaLost;
    }

    public int getCountPlayedCartaBaixaWon() {
        return countPlayedCartaBaixaWon;
    }

    public void setCountPlayedCartaBaixaWon(int countPlayedCartaBaixaWon) {
        this.countPlayedCartaBaixaWon = countPlayedCartaBaixaWon;
    }

    public int getCountPlayedCartaBaixaLost() {
        return countPlayedCartaBaixaLost;
    }

    public void setCountPlayedCartaBaixaLost(int countPlayedCartaBaixaLost) {
        this.countPlayedCartaBaixaLost = countPlayedCartaBaixaLost;
    }

    public int getCountWentDeck() {
        return countWentDeck;
    }

    public void setCountWentDeck(int countWentDeck) {
        this.countWentDeck = countWentDeck;
    }
}
