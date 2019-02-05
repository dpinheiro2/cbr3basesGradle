package br.ufsm.topicos.agentecbr;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 15/01/2019.
 */


public class ScoutPoints {


    private int countCallEnvidoWon;
    private int countCallEnvidoLost;
    private int countNoCallEnvidoWon;
    private int countNoCallEnvidoLost;

    private int countCallEnvidoHonestWon;
    private int countCallEnvidoHonestLost;
    private int countNoCallEnvidoHonestWon;
    private int countNoCallEnvidoHonestLost;

    private int countCallEnvidoDeceptiveWon;
    private int countCallEnvidoDeceptiveLost;
    private int countNoCallEnvidoDeceptiveWon;
    private int countNoCallEnvidoDeceptiveLost;

    private int countTypeEnvidoDeception1;
    private int countTypeEnvidoDeception2;
    private int countTypeEnvidoDeception3;
    private int countTypeEnvidoDeception4;
    private int countTypeEnvidoDeception5;

    private int countCallEnvido;
    private int countCallRealEnvido;
    private int countCallFaltaEnvido;



    public ScoutPoints() {


        countCallEnvidoWon = 0;
        countCallEnvidoLost = 0;
        countNoCallEnvidoWon = 0;
        countNoCallEnvidoLost = 0;

        countCallEnvidoHonestWon = 0;
        countCallEnvidoHonestLost = 0;
        countNoCallEnvidoHonestWon = 0;
        countNoCallEnvidoHonestLost = 0;
        countCallEnvidoDeceptiveWon = 0;
        countCallEnvidoDeceptiveLost = 0;
        countNoCallEnvidoDeceptiveWon = 0;
        countNoCallEnvidoDeceptiveLost = 0;

        countTypeEnvidoDeception1 = 0;
        countTypeEnvidoDeception2 = 0;
        countTypeEnvidoDeception3 = 0;
        countTypeEnvidoDeception4 = 0;
        countTypeEnvidoDeception5 = 0;

        countCallEnvido = 0;
        countCallRealEnvido = 0;
        countCallFaltaEnvido = 0;
    }

    public int getCountCallEnvidoHonestWon() {
        return countCallEnvidoHonestWon;
    }

    public void setCountCallEnvidoHonestWon(int countCallEnvidoHonestWon) {
        this.countCallEnvidoHonestWon = countCallEnvidoHonestWon;
    }

    public int getCountCallEnvidoHonestLost() {
        return countCallEnvidoHonestLost;
    }

    public void setCountCallEnvidoHonestLost(int countCallEnvidoHonestLost) {
        this.countCallEnvidoHonestLost = countCallEnvidoHonestLost;
    }

    public int getCountNoCallEnvidoHonestWon() {
        return countNoCallEnvidoHonestWon;
    }

    public void setCountNoCallEnvidoHonestWon(int countNoCallEnvidoHonestWon) {
        this.countNoCallEnvidoHonestWon = countNoCallEnvidoHonestWon;
    }

    public int getCountNoCallEnvidoHonestLost() {
        return countNoCallEnvidoHonestLost;
    }

    public void setCountNoCallEnvidoHonestLost(int countNoCallEnvidoHonestLost) {
        this.countNoCallEnvidoHonestLost = countNoCallEnvidoHonestLost;
    }

    public int getCountCallEnvidoDeceptiveWon() {
        return countCallEnvidoDeceptiveWon;
    }

    public void setCountCallEnvidoDeceptiveWon(int countCallEnvidoDeceptiveWon) {
        this.countCallEnvidoDeceptiveWon = countCallEnvidoDeceptiveWon;
    }

    public int getCountCallEnvidoDeceptiveLost() {
        return countCallEnvidoDeceptiveLost;
    }

    public void setCountCallEnvidoDeceptiveLost(int countCallEnvidoDeceptiveLost) {
        this.countCallEnvidoDeceptiveLost = countCallEnvidoDeceptiveLost;
    }

    public int getCountNoCallEnvidoDeceptiveWon() {
        return countNoCallEnvidoDeceptiveWon;
    }

    public void setCountNoCallEnvidoDeceptiveWon(int countNoCallEnvidoDeceptiveWon) {
        this.countNoCallEnvidoDeceptiveWon = countNoCallEnvidoDeceptiveWon;
    }

    public int getCountNoCallEnvidoDeceptiveLost() {
        return countNoCallEnvidoDeceptiveLost;
    }

    public void setCountNoCallEnvidoDeceptiveLost(int countNoCallEnvidoDeceptiveLost) {
        this.countNoCallEnvidoDeceptiveLost = countNoCallEnvidoDeceptiveLost;
    }

    public int getCountTypeEnvidoDeception1() {
        return countTypeEnvidoDeception1;
    }

    public void setCountTypeEnvidoDeception1(int countTypeEnvidoDeception1) {
        this.countTypeEnvidoDeception1 = countTypeEnvidoDeception1;
    }

    public int getCountTypeEnvidoDeception2() {
        return countTypeEnvidoDeception2;
    }

    public void setCountTypeEnvidoDeception2(int countTypeEnvidoDeception2) {
        this.countTypeEnvidoDeception2 = countTypeEnvidoDeception2;
    }

    public int getCountTypeEnvidoDeception3() {
        return countTypeEnvidoDeception3;
    }

    public void setCountTypeEnvidoDeception3(int countTypeEnvidoDeception3) {
        this.countTypeEnvidoDeception3 = countTypeEnvidoDeception3;
    }

    public int getCountTypeEnvidoDeception4() {
        return countTypeEnvidoDeception4;
    }

    public void setCountTypeEnvidoDeception4(int countTypeEnvidoDeception4) {
        this.countTypeEnvidoDeception4 = countTypeEnvidoDeception4;
    }

    public int getCountTypeEnvidoDeception5() {
        return countTypeEnvidoDeception5;
    }

    public void setCountTypeEnvidoDeception5(int countTypeEnvidoDeception5) {
        this.countTypeEnvidoDeception5 = countTypeEnvidoDeception5;
    }

    public int getCountCallEnvido() {
        return countCallEnvido;
    }

    public void setCountCallEnvido(int countCallEnvido) {
        this.countCallEnvido = countCallEnvido;
    }

    public int getCountCallRealEnvido() {
        return countCallRealEnvido;
    }

    public void setCountCallRealEnvido(int countCallRealEnvido) {
        this.countCallRealEnvido = countCallRealEnvido;
    }

    public int getCountCallFaltaEnvido() {
        return countCallFaltaEnvido;
    }

    public void setCountCallFaltaEnvido(int countCallFaltaEnvido) {
        this.countCallFaltaEnvido = countCallFaltaEnvido;
    }

    public int getCountCallEnvidoWon() {
        return countCallEnvidoWon;
    }

    public void setCountCallEnvidoWon(int countCallEnvidoWon) {
        this.countCallEnvidoWon = countCallEnvidoWon;
    }

    public int getCountCallEnvidoLost() {
        return countCallEnvidoLost;
    }

    public void setCountCallEnvidoLost(int countCallEnvidoLost) {
        this.countCallEnvidoLost = countCallEnvidoLost;
    }

    public int getCountNoCallEnvidoWon() {
        return countNoCallEnvidoWon;
    }

    public void setCountNoCallEnvidoWon(int countNoCallEnvidoWon) {
        this.countNoCallEnvidoWon = countNoCallEnvidoWon;
    }

    public int getCountNoCallEnvidoLost() {
        return countNoCallEnvidoLost;
    }

    public void setCountNoCallEnvidoLost(int countNoCallEnvidoLost) {
        this.countNoCallEnvidoLost = countNoCallEnvidoLost;
    }
}
