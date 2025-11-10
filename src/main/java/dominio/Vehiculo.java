package dominio;

import java.time.LocalDate;

public abstract class Vehiculo {
    protected String patente;
    protected String marca;
    protected String modelo;
    protected int anio;
    protected LocalDate ultimaRtv; // fecha
    protected int kilometraje;
    protected double capacidadCargaKg;
    protected int ultimoServiceKm; // km al que se hizo el último service de 10.000

    protected Vehiculo(String patente, String marca, String modelo, int anio, LocalDate ultimaRtv, int kilometraje, double capacidadCargaKg) {
        this.patente = patente.toUpperCase();
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.ultimaRtv = ultimaRtv;
        this.kilometraje = kilometraje;
        this.capacidadCargaKg = capacidadCargaKg;
        this.ultimoServiceKm = kilometraje; // asume service al km de ingreso
    }

    public abstract String tipo();
    public abstract boolean validar();

    public String getPatente() { return patente; }
    public int getKilometraje() { return kilometraje; }
    public void sumarKm(int km) { this.kilometraje += km; }
    public LocalDate getUltimaRtv() { return ultimaRtv; }
    public int getUltimoServiceKm() { return ultimoServiceKm; }
    public void registrarService() { this.ultimoServiceKm = this.kilometraje; }

    public boolean requiereService() {
        int proximo = ultimoServiceKm + 10_000;
        return kilometraje >= proximo;
    }

    public boolean requiereRTV(LocalDate hoy) {
        // RTV anual (criterio razonable)
        return ultimaRtv == null || ultimaRtv.plusYears(1).isBefore(hoy) || ultimaRtv.plusYears(1).isEqual(hoy);
    }

    @Override
    public String toString() {
        return tipo() + "{patente=" + patente + ", marca=" + marca + ", modelo=" + modelo + ", anio=" + anio +
                ", ultimaRtv=" + ultimaRtv + ", km=" + kilometraje + ", cargaKg=" + capacidadCargaKg +
                ", ultimoServiceKm=" + ultimoServiceKm + "}";
    }
}