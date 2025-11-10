package dominio;

import java.time.LocalDate;

public class Camioneta extends Vehiculo {
    public enum TipoTransporte { PASAJEROS, MERCADERIA }

    private TipoTransporte tipoTransporte;

    public Camioneta(String patente, String marca, String modelo, int anio, LocalDate ultimaRtv, int kilometraje, double capacidadCargaKg, TipoTransporte tipo) {
        super(patente, marca, modelo, anio, ultimaRtv, kilometraje, capacidadCargaKg);
        this.tipoTransporte = tipo;
    }

    public TipoTransporte getTipoTransporte() { return tipoTransporte; }

    @Override
    public String tipo() { return "Camioneta"; }

    @Override
    public boolean validar() {
        System.out.println("Validando Camioneta: " + this);
        if (patente.isBlank() || marca.isBlank() || modelo.isBlank()) {
            System.out.println(" - Datos básicos inválidos");
            return false;
        }
        System.out.println(" - OK (" + tipoTransporte + ")");
        return true;
    }
}