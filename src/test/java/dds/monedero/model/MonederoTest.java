package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Poner() {
    cuenta.depositarDinero(1500);

    assertEquals(cuenta.getSaldo(), 1500);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.depositarDinero(-1500));
  }

  @Test
  void TresDepositos() {
    cuenta.depositarDinero(1500);
    cuenta.depositarDinero(456);
    cuenta.depositarDinero(1900);

    assertEquals(cuenta.getSaldo(), 3856);
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.depositarDinero(1500);
          cuenta.depositarDinero(456);
          cuenta.depositarDinero(1900);
          cuenta.depositarDinero(245);
    });
  }

  @Test
  void sacarDinero() {
    cuenta.depositarDinero(200);
    cuenta.extraerDinero(100);

    assertEquals(cuenta.getSaldo(), 100);
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.extraerDinero(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.extraerDinero(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.extraerDinero(-500));
  }

}