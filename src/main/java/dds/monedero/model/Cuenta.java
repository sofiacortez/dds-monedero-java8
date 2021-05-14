package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {}

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void depositarDinero(double cuanto) {
    validarDeposito(cuanto);
    Movimiento movimiento = new Movimiento(LocalDate.now(), cuanto, true);
    agregarMovimiento(movimiento);
  }

  public void extraerDinero(double cuanto) {
    validarExtraccion(cuanto);
    Movimiento movimiento = new Movimiento(LocalDate.now(), cuanto, false);
    agregarMovimiento(movimiento);
  }

  public void validarDeposito(double cuanto) {
    validarMonto(cuanto);
    verificarCantidadDeDepositos();
  }

  public void validarMonto(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void verificarCantidadDeDepositos() {
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  public void validarExtraccion(double cuanto) {
    validarMonto(cuanto);
    verificarSaldoDisponible(cuanto);
    verificarMaximaExtraccionDiaria(cuanto);
  }

  public void verificarSaldoDisponible(double cuanto) {
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  public void verificarMaximaExtraccionDiaria(double cuanto) {
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
  }

  public void agregarMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  // No se si es necesario este metodo pero si lo fuera haria uno para monto depositado en cierta fecha tambien.
  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

}
