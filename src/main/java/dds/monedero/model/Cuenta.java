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

    new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
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

  // Creo que termina habiendo repeticion de codigo entre sacar y poner (validaciones + new movimiento + agregarlo a la lista de movimientos)
  public void extraerDinero(double cuanto) {

    validarExtraccion(cuanto);

    new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
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

  // Podria ser considerado un metodo con una long parameter list
  // Es responsabilidad de la cuenta agregar movimientos a su lista no del movimiento
  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  // REVISAR, mas abstracciones?
  // No usa abstracciones presentes en movimiento como esDeLaFecha, fueExtraido(fecha), fueDepositado(fecha)
  public double getMontoExtraidoA(LocalDate fecha) {

    // Type Tests: se le pregunta al movimiento si es deposito o no todo el tiempo
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  //No hace falta este getter, solo accede a su lista la cuenta y puede hacerlo a traves del atributo directamente
  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  //No es necesario, el saldo se le setea con el constructor y luego se maneja con los movimientos.
  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
