package dominio;

public class Viaje {
    private Chofer chofer;
    private String origen;
    private String destino;
    private int km;
    private Vehiculo vehiculo;

    public Viaje(Chofer chofer, String origen, String destino, int km, Vehiculo vehiculo) {
        this.chofer = chofer;
        this.origen = origen;
        this.destino = destino;
        this.km = km;
        this.vehiculo = vehiculo;
    }

    public Chofer getChofer() { return chofer; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public int getKm() { return km; }
    public Vehiculo getVehiculo() { return vehiculo; }

    @Override
    public String toString() {
        return "Viaje{patente=" + vehiculo.getPatente() + ", legajo=" + chofer.getLegajo() + ", desde='" + origen + "', hasta='" + destino + "', km=" + km + "}";
    }
}