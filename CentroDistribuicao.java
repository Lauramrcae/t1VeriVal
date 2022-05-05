package com.example;

public class CentroDistribuicao {
    public enum SITUACAO {
        NORMAL, SOBRAVISO, EMERGENCIA
    }

    public enum TIPOPOSTO {
        COMUM, ESTRATEGICO
    }

    public static final int MAX_ADITIVO = 500;
    public static final int MAX_ALCOOL = 2500;
    public static final int MAX_GASOLINA = 10000;

    // tanques e situação
    private int tAditivo;
    private int tGasolina;
    private int tAlcool1;
    private int tAlcool2;
    private SITUACAO situacao;

    public CentroDistribuicao(int tAditivo, int tGasolina, int tAlcool1, int tAlcool2) throws IllegalArgumentException {
        if (tAditivo <= 0 || tGasolina <= 0 || tAlcool1 <= 0 || tAlcool2 <= 0 || tAlcool1 != tAlcool2)
            throw new IllegalArgumentException("ILLEGAL_ARGUMENT_EXCEPTION");
        if (tAditivo > MAX_ADITIVO)
            throw new IllegalArgumentException("ILLEGAL_ARGUMENT_EXCEPTION");
        else
            this.tAditivo = tAditivo;

        if (tGasolina > MAX_GASOLINA)
            throw new IllegalArgumentException("ILLEGAL_ARGUMENT_EXCEPTION");
        else
            this.tGasolina = tGasolina;

        if (tAlcool1 + tAlcool2 > MAX_ALCOOL)
            throw new IllegalArgumentException("ILLEGAL_ARGUMENT_EXCEPTION");
        else {
            this.tAlcool1 = tAlcool1;
            this.tAlcool2 = tAlcool2;
        }

        defineSituacao();
    }

    public void defineSituacao() {
        if (tGasolina >= (MAX_GASOLINA / 2) && tAditivo >= (MAX_ADITIVO / 2)
                && (tAlcool1 + tAlcool2) >= (MAX_ALCOOL / 2)) {
            situacao = SITUACAO.NORMAL;
        } else if (tGasolina >= (MAX_GASOLINA / 4) && tAditivo >= (MAX_ADITIVO / 4)
                && (tAlcool1 + tAlcool2) >= (MAX_ALCOOL / 4)) {
            situacao = SITUACAO.SOBRAVISO;
        } else
            situacao = SITUACAO.EMERGENCIA;
    }

    public SITUACAO getSituacao() {
        return situacao;
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
        if (qtdade <= 0 || qtdade > MAX_ADITIVO)
            return -1;
        if (qtdade + tAditivo > MAX_ADITIVO) {
            int aux = tAditivo;
            tAditivo = MAX_ADITIVO;
            return (tAditivo - aux);
        }
        tAditivo += qtdade;
        return qtdade;
    }

    public int recebeGasolina(int qtdade) {
        if (qtdade <= 0 || qtdade > MAX_GASOLINA)
            return -1;
        if (qtdade + tGasolina > MAX_GASOLINA) {
            int aux = tGasolina;
            tGasolina = MAX_GASOLINA;
            return (tGasolina - aux);
        }
        tGasolina += qtdade;
        return qtdade;
    }

    public int recebeAlcool(int qtdade) {
        if (qtdade <= 0 || qtdade > MAX_ALCOOL)
            return -1;
        if (qtdade + tAlcool1 + tAlcool2 > MAX_ALCOOL) {
            int aux = tAlcool1 + tAlcool2;
            tAlcool1 = MAX_ALCOOL / 2;
            tAlcool2 = MAX_ALCOOL / 2;
            return ((tAlcool1 + tAlcool2) - aux);
        }
        tAlcool1 = tAlcool1 + (qtdade / 2);
        tAlcool2 = tAlcool2 + (qtdade / 2);
        return qtdade;
    }

    public int[] encomendaCombustivel(int qtdade, TIPOPOSTO tipoPosto) {
        if (qtdade <= 0)
            return new int[] { -7, 0, 0, 0 };

        double gasolina = qtdade * 0.7;
        double aditivo = qtdade * 0.05;
        double alcool1 = qtdade * 0.25 * 0.5;
        double alcool2 = qtdade * 0.25 * 0.5;

        // Se a situacao for normal ou a situacao for sobreaviso e o posto
        // estrategico(tem o mesmo comportamento)
        if (getSituacao() == SITUACAO.NORMAL
                || (getSituacao() == SITUACAO.SOBRAVISO && tipoPosto == TIPOPOSTO.ESTRATEGICO)) {
            if (tGasolina >= gasolina && tAditivo >= aditivo && tAlcool1 >= alcool1 && tAlcool2 >= alcool2) {
                tGasolina -= gasolina;
                tAditivo -= aditivo;
                tAlcool1 -= alcool1;
                tAlcool2 -= alcool2;
                return new int[] { (int) tAditivo, (int) tGasolina, (int) tAlcool1, (int) tAlcool2 };
            } else
                return new int[] { -21, 0, 0, 0 };
        }

        if (getSituacao() == SITUACAO.SOBRAVISO && tipoPosto == TIPOPOSTO.COMUM || tipoPosto == TIPOPOSTO.ESTRATEGICO && getSituacao() == SITUACAO.EMERGENCIA) {
            if (tGasolina >= gasolina && tAditivo >= aditivo && tAlcool1 >= alcool1 && tAlcool2 >= alcool2) {

                    tGasolina -= (gasolina / 2);
                    tAditivo -= (aditivo / 2);
                    tAlcool1 -= (alcool1 / 2);
                    tAlcool2 -= (alcool2 / 2);
                    return new int[] { (int) (tAditivo), (int) (tGasolina ), (int) (tAlcool1),
                            (int) (tAlcool2) };
                
            } else
                return new int[] { -21, 0, 0, 0 };
        }

        if (tipoPosto == TIPOPOSTO.COMUM && getSituacao() == SITUACAO.EMERGENCIA)
            return new int[] { -14, 0, 0, 0 };

        return new int[] { -21, 0, 0, 0 };
    }
}