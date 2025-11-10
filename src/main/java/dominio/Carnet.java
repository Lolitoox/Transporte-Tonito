package dominio;

import java.time.Year;

public class Carnet {
    public enum Tipo { B, C, E }

    private final Tipo tipo;
    private final String letra;
    private final int numero;
    private final Year vencimiento;

    public Carnet(Tipo tipo, String letra, int numero, Year vencimiento) {
        this.tipo = tipo;
        this.letra = letra == null ? "" : letra.trim().toUpperCase();
        this.numero = numero;
        this.vencimiento = vencimiento;
    }

    public Tipo getTipo() { return tipo; }
    public String getLetra() { return letra; }
    public int getNumero() { return numero; }
    public Year getVencimiento() { return vencimiento; }

    public boolean vigente(Year ahora) {
        return vencimiento.compareTo(ahora) >= 0;
    }

    @Override
    public String toString() {
        return "Carnet{" + tipo + "-" + letra + "-" + numero + "-" + vencimiento + "}";
    }
}