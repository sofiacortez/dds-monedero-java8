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

  // Getters sin necesidad (poca proteccion, mas facil de romper el encapsulamiento)
  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  // REVISAR
  public boolean fueDepositado(LocalDate fecha) {
    return isDeposito() && esDeLaFecha(fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return isExtraccion() && esDeLaFecha(fecha);
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  // Redundancia (se pueden obtener a partir del mismo atributo)
  public boolean isDeposito() {
    return esDeposito;
  }

  public boolean isExtraccion() {
    return !esDeposito;
  }

  // Nombre muy poco expresivo
  // Esta rompiento el encapsulamiento calculando el saldo de la cuenta cuando ese dato se puede obtener directamente de ella
  public double calcularValor(Cuenta cuenta) {
    if (esDeposito) {
      return cuenta.getSaldo() + getMonto();
    } else {
      return cuenta.getSaldo() - getMonto();
    }
  }
}
