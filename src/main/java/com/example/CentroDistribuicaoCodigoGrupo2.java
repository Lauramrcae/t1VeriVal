package com.example;

public class CentroDistribuicaoCodigoGrupo2 {
    public enum SITUACAO {
        NORMAL, SOBRAVISO, EMERGENCIA
    }

    public enum TIPOPOSTO {
        COMUM, ESTRATEGICO
    }

    public static final int MAX_ADITIVO = 500;
    public static final int MAX_ALCOOL = 2500;
    public static final int MAX_GASOLINA = 10000;
    private int tAditivo;
    private int tGasolina;
    private int tAlcool1;
    private int tAlcool2;
    private SITUACAO sit;

    public CentroDistribuicaoCodigoGrupo2(int tAditivo, int tGasolina, int tAlcool1, int tAlcool2) {
        if (tAditivo < 0 || tAlcool1 < 0 || tAlcool2 < 0 || tGasolina < 0 || tAlcool1 != tAlcool2) {
            throw new IllegalArgumentException();
        } else {
            this.tAditivo = tAditivo;
            this.tGasolina = tGasolina;
            this.tAlcool1 = tAlcool1;
            this.tAlcool2 = tAlcool2;
        }

    }

    public void defineSituacao() {
        if (tAditivo < MAX_ADITIVO * 0.25 || tGasolina < MAX_GASOLINA * 0.25 || tAlcool1 < (MAX_ALCOOL / 2) * 0.25
                || tAlcool2 < (MAX_ALCOOL / 2) * 0.25) {
            sit = SITUACAO.EMERGENCIA;
        } else if ((tAditivo >= MAX_ADITIVO * 0.25 && tAditivo < MAX_ADITIVO * 0.50)
                || (tGasolina >= MAX_GASOLINA * 0.25 && tGasolina < MAX_GASOLINA * 0.50)
                || (tAlcool1 + tAlcool2 >= MAX_ALCOOL * 0.25 && tAlcool1 + tAlcool2 < MAX_ALCOOL * 0.50)) {
            sit = SITUACAO.SOBRAVISO;
        } else
            sit = SITUACAO.NORMAL;
    }

    public SITUACAO getSituacao() {
        defineSituacao();
        return sit;

    }

    public int gettGasolina() {
        return tGasolina;
    }

    public int gettAditivo() {
        return tAditivo;
    }

    public int gettAlcool1() {
        return tAlcool1;
    }

    public int gettAlcool2() {
        return tAlcool2;
    }

    public int recebeAditivo(int qtdade) {
        if (qtdade < 0) {
            return -1;
        }
        if (qtdade + tAditivo >= MAX_ADITIVO) {
            int valorAbastecido = MAX_ADITIVO - tAditivo;
            tAditivo = MAX_ADITIVO;
            return valorAbastecido;
        } else {
            tAditivo = tAditivo + qtdade;
            return qtdade;
        }
    }

    public int recebeGasolina(int qtdade) {
        if (qtdade < 0) {
            return -1;
        }
        if (qtdade + tGasolina >= MAX_GASOLINA) {
            int valorAbastecido = MAX_GASOLINA - tGasolina;
            tGasolina = MAX_GASOLINA;
            return valorAbastecido;
        } else {
            tGasolina = tGasolina + qtdade;
            return qtdade;
        }
    }

    public int recebeAlcool(int qtdade) {
        if (qtdade < 0) {
            return -1;
        }
        if (qtdade + tAlcool1 + tAlcool2 >= MAX_ALCOOL) {
            int valorAbastecido = MAX_ALCOOL - tAlcool1 - tAlcool2;
            tAlcool1 = MAX_ALCOOL / 2;
            tAlcool2 = MAX_ALCOOL / 2;
            return valorAbastecido;
        } else {
            tAlcool1 = tAlcool1 + (qtdade / 2);
            tAlcool2 = tAlcool1;
            return qtdade;
        }
    }

    public int[] encomendaCombustivel(int qtdade, TIPOPOSTO tipoPosto) {
        int retorno[] = new int[4];
        double qtdG;
        double qtdAd;
        double qtdAl;
        if (qtdade <= 0) {
            retorno[0] = -7;
            return retorno;
        } else {
            if (TIPOPOSTO.COMUM == tipoPosto) {
                switch (getSituacao()) {
                    case EMERGENCIA:
                        retorno[0] = -14;
                        return retorno;
                    case SOBRAVISO:
                        qtdade = qtdade / 2;
                        qtdG = qtdade * 0.7;
                        qtdAd = qtdade * 0.05;
                        qtdAl = qtdade * 0.25;
                        if (qtdG > gettGasolina() || qtdAd > gettAditivo() || qtdAl > gettAlcool1() + gettAlcool2()) {
                            retorno[0] = -21;
                            return retorno;
                        } else {
                            tGasolina = tGasolina - (int) Math.round(qtdG);
                            tAditivo = tAditivo - (int) Math.round(qtdAd);
                            tAlcool1 = tAlcool1 - (int) Math.round(qtdAl / 2);
                            tAlcool2 = tAlcool2 - (int) Math.round(qtdAl / 2);
                            retorno[0] = tAditivo;
                            retorno[1] = tGasolina;
                            retorno[2] = tAlcool1;
                            retorno[3] = tAlcool2;
                            return retorno;
                        }
                    case NORMAL:
                        qtdG = qtdade * 0.7;
                        qtdAd = qtdade * 0.05;
                        qtdAl = qtdade * 0.25;
                        if (qtdG > gettGasolina() || qtdAd > gettAditivo() || qtdAl > gettAlcool1() + gettAlcool2()) {
                            retorno[0] = -21;
                            return retorno;
                        } else {
                            tGasolina = tGasolina - (int) Math.round(qtdG);
                            tAditivo = tAditivo - (int) Math.round(qtdAd);
                            tAlcool1 = tAlcool1 - (int) Math.round(qtdAl / 2);
                            tAlcool2 = tAlcool2 - (int) Math.round(qtdAl / 2);
                            retorno[0] = tAditivo;
                            retorno[1] = tGasolina;
                            retorno[2] = tAlcool1;
                            retorno[3] = tAlcool2;
                            return retorno;

                        }
                }
            } else {
                if (getSituacao() == SITUACAO.EMERGENCIA) {
                    qtdade = qtdade / 2;
                    qtdG = qtdade * 0.7;
                    qtdAd = qtdade * 0.05;
                    qtdAl = qtdade * 0.25;
                    if (qtdG > gettGasolina() || qtdAd > gettAditivo() || qtdAl > gettAlcool1() + gettAlcool2()) {
                        retorno[0] = -21;
                        return retorno;
                    } else {
                        tGasolina = tGasolina - (int) Math.round(qtdG);
                        tAditivo = tAditivo - (int) Math.round(qtdAd);
                        tAlcool1 = tAlcool1 - (int) Math.round(qtdAl / 2);
                        tAlcool2 = tAlcool2 - (int) Math.round(qtdAl / 2);
                        retorno[0] = tAditivo;
                        retorno[1] = tGasolina;
                        retorno[2] = tAlcool1;
                        retorno[3] = tAlcool2;
                        return retorno;
                    }
                } else {
                    qtdG = qtdade * 0.7;
                    qtdAd = qtdade * 0.05;
                    qtdAl = qtdade * 0.25;
                    if (qtdG > gettGasolina() || qtdAd > gettAditivo() || qtdAl > gettAlcool1() + gettAlcool2()) {
                        retorno[0] = -21;
                        return retorno;
                    } else {
                        tGasolina = tGasolina - (int) Math.round(qtdG);
                        tAditivo = tAditivo - (int) Math.round(qtdAd);
                        tAlcool1 = tAlcool1 - (int) Math.round(qtdAl / 2);
                        tAlcool2 = tAlcool2 - (int) Math.round(qtdAl / 2);
                        retorno[0] = tAditivo;
                        retorno[1] = tGasolina;
                        retorno[2] = tAlcool1;
                        retorno[3] = tAlcool2;
                        return retorno;

                    }
                }

            }
        }
        return retorno;

    }
}
