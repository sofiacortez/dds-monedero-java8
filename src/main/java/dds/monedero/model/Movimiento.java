package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
  private LocalDate fecha;
  //En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
  //siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
  private double monto;
  // Primitive obsession: se representa con un boolean algo que puede ser un objeto con comportamiento.
  private boolean esDeposito; // Si quiero agregar mas tipos de movimientos en el futuro tengo que cambiar todo (poco extensible)

  public Movimiento(LocalDate fecha, double monto, boolean esDeposito) {
    this.fecha = fecha;
    this.monto = monto;
    this.esDeposito = esDeposito;
  }

  // CUANTO CORRIJA EL METODO "CALCULARVALOR" LO SACO
  public double getMonto() {
    return monto;
  }

  public boolean fueDepositado(LocalDate fecha) {
    return esDeposito && esDeLaFecha(fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return isExtraccion() && esDeLaFecha(fecha);
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public boolean isExtraccion() {
    return !esDeposito;
  }

  // Nombre muy poco expresivo
  // Esta rompiento el encapsulamiento calculando el saldo de la cuenta cuando ese dato se puede obtener directamente de ella
  // Type Tests: se le pregunta al movimiento si es deposito o no todo el tiempo
  public double calcularValor(Cuenta cuenta) {
    if (esDeposito) {
      return cuenta.getSaldo() + getMonto();
    } else {
      return cuenta.getSaldo() - getMonto();
    }
  }
}
