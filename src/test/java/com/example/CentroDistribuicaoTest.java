package com.example;

import com.example.CentroDistribuicao.SITUACAO;
import com.example.CentroDistribuicao.TIPOPOSTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CentroDistribuicaoTest {

    private CentroDistribuicao cd = null;

    @ParameterizedTest
    @CsvSource({ "-10,10,10,10",
            "10,-10,10,10",
            "10,10,10,-10",
            "10,10,-10,10",
            "10, 10, 10, 5",
            "10, 10, 5, 10",
            "0, 10, 5, 10",
            "10, 0, 5, 10",
            "10, 10, 0, 10",
            "0, 10, 5, 0",
            "501,10,10,10",
            "10,10001,10,10",
            "10,10,1251,10",
            "10,10,10,1251"
    })
    public void testeControllerValoresInvalidos(int adt, int gas, int al1, int al2) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new CentroDistribuicao(adt, gas, al1, al2);
        });
    }

    @ParameterizedTest
    @CsvSource({ "500,10000,1250,1250",
            "250,5000,625,625"
    })
    public void testeSituacaoNormal(int adt, int gas, int al1, int al2) {
        cd = new CentroDistribuicao(adt, gas, al1, al2);
        SITUACAO expectedResult = cd.getSituacao();
        Assertions.assertEquals(expectedResult, SITUACAO.NORMAL);
    }

    @ParameterizedTest
    @CsvSource({ "249,4999,624,624",
            "125,2500,313,313"
    })
    public void testeSituacaoSobraviso(int adt, int gas, int al1, int al2) {
        cd = new CentroDistribuicao(adt, gas, al1, al2);
        SITUACAO expectedResult = cd.getSituacao();
        Assertions.assertEquals(expectedResult, SITUACAO.SOBRAVISO);
    }

    @ParameterizedTest
    @CsvSource({ "124,2499,312,312",
            "1,2,4,4"
    })
    public void testeSituacaoEmergencia(int adt, int gas, int al1, int al2) {
        cd = new CentroDistribuicao(adt, gas, al1, al2);
        SITUACAO expectedResult = cd.getSituacao();
        Assertions.assertEquals(expectedResult, SITUACAO.EMERGENCIA);
    }

    @Test
    public void testeRecebeAditivoPositivo() {
        cd = new CentroDistribuicao(1, 10000, 1250, 1250);
        int result = cd.recebeAditivo(500);
        Assertions.assertEquals(result, 499);
    }

    @Test
    public void testeRecebeGasolinaPositivo() {
        cd = new CentroDistribuicao(1, 1, 1250, 1250);
        int result = cd.recebeGasolina(10000);
        Assertions.assertEquals(result, 9999);
    }

    @Test
    public void testeRecebeAlcoolPositivo() {
        cd = new CentroDistribuicao(1, 1, 1, 1);
        int result = cd.recebeAlcool(900);
        Assertions.assertEquals(result, 900);
    }

    @ParameterizedTest
    @CsvSource({ "501",
    "0",
    "-2"
    })
    public void testeRecebeAditivoValoresInvalidos(int adt) {
        cd = new CentroDistribuicao(1, 10000, 1250, 1250);
        int result = cd.recebeAditivo(adt);
        Assertions.assertEquals(result, -1);
    }

    @ParameterizedTest
    @CsvSource({ "10001",
    "0",
    "-2"
    })
    public void testeRecebeGasolinaValoresInvalidos(int gas) {
        cd = new CentroDistribuicao(1, 1,1, 1);
        int result = cd.recebeGasolina(gas);
        Assertions.assertEquals(result, -1);
    }

    @ParameterizedTest
    @CsvSource({ "2501",
    "0",
    "-2"
    })
    public void testeRecebeAlcoolValoresInvalidos(int al) {
        cd = new CentroDistribuicao(1, 1, 1, 1);
        int result = cd.recebeAlcool(al);
        Assertions.assertEquals(result, -1);
    }

    @ParameterizedTest
    @CsvSource({ "500,10000,1250,1250,0",
    "500,10000,1250,1250,-10"
    })
    public void testeEncomendaComValorInvalido(int adt, int gas, int al1, int al2, int qtd) {
        cd = new CentroDistribuicao(adt, gas, al1, al2);
        int expectedResult[] = new int[4];
        expectedResult[0] = -7;
        Assertions.assertArrayEquals(cd.encomendaCombustivel(qtd, TIPOPOSTO.COMUM), expectedResult);
    }

    @Test
    public void testePostoComumSituacaoEmergencia() {
        cd = new CentroDistribuicao(1, 10000, 1250, 1250);
        int expectedResult[] = new int[4];
        expectedResult[0] = -14;
        Assertions.assertArrayEquals(cd.encomendaCombustivel(250, TIPOPOSTO.COMUM), expectedResult);
    }

    @ParameterizedTest
    @CsvSource({ "500,10000,1250,1250,100"
    })
    public void testePostoComumSituacaoNormalComMisturaSuficiente(int adt, int gas, int al1, int al2, int qtd) {
        cd = new CentroDistribuicao(adt, gas, al1, al2);
        double qtdAd = adt - qtd * 0.05;
        double qtdG = gas - qtd * 0.7;
        double qtdAl = al1 - (qtd / 2) * 0.25;

        double expectedResult[] = new double[] { qtdAd, qtdG, qtdAl, qtdAl };
        int[] result = new int[4];
        result = cd.encomendaCombustivel(qtd, TIPOPOSTO.COMUM);
        double[] resultConverted = new double[4];
        for (int i = 0; i < 4; i++) {
            resultConverted[i] = result[i];
        }
        Assertions.assertArrayEquals(resultConverted, expectedResult, 1);
    }

    @Test
    public void testePostoComumSituacaoSobreavisoComMisturaSuficiente() {
        cd = new CentroDistribuicao(249, 5000, 625, 625);

        double expectedResult[] = new double[] { 244, 4930, 613, 613 };
        int[] result = new int[4];
        result = cd.encomendaCombustivel(200, TIPOPOSTO.COMUM);
        double[] resultConverted = new double[4];
        for (int i = 0; i < 4; i++) {
            resultConverted[i] = result[i];
        }
        Assertions.assertArrayEquals(resultConverted, expectedResult, 1);
    }

    @ParameterizedTest
    @CsvSource({ "250,5000,625,625,7500",
            "230,2500,330,330,7200"
    })
    public void testePostoComumSemMisturaSuficiente(int adt, int gas, int al1, int al2, int qtd) {
        cd = new CentroDistribuicao(adt, gas, al1, al2);
        int expectedResult[] = new int[4];
        expectedResult[0] = -21;
        Assertions.assertArrayEquals(cd.encomendaCombustivel(qtd, TIPOPOSTO.COMUM), expectedResult);
    }

    @Test
    public void testePostoEstrategicoSituacaoEmergenciaComMisturaSuficiente() {
        cd = new CentroDistribuicao(124, 2599, 324, 324);
        int expectedResult[] = new int[] { 119, 2529, 311, 311 };
        Assertions.assertArrayEquals(cd.encomendaCombustivel(200, TIPOPOSTO.ESTRATEGICO), expectedResult);
    }

    @Test
    public void testePostoEstrategicoSituacaoNormalComMisturaSuficiente() {
        cd = new CentroDistribuicao(500, 10000, 1250, 1250);
        int expectedResult[] = new int[] { 495, 9930, 1237, 1237 };
        Assertions.assertArrayEquals(cd.encomendaCombustivel(100, TIPOPOSTO.ESTRATEGICO), expectedResult);
    }

    @Test
    public void testePostoEstrategicoSituacaoSobreavisoComMisturaSuficiente() {
        cd = new CentroDistribuicao(249, 4999, 649, 649);
        int expectedResult[] = new int[] { 244, 4929, 636, 636 };
        Assertions.assertArrayEquals(cd.encomendaCombustivel(100, TIPOPOSTO.ESTRATEGICO), expectedResult);
    }

    @Test
    public void testePostoEstrategicoSituacaoSobreavisoSemMisturaSuficiente() {
        cd = new CentroDistribuicao(249, 4999, 649, 649);
        int expectedResult[] = new int[] { 244, 4929, 636, 636 };
        Assertions.assertArrayEquals(cd.encomendaCombustivel(100, TIPOPOSTO.ESTRATEGICO), expectedResult);
    }

}