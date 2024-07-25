package com.makao.administrador.Entities;

public class Solicitudes {
    private int idSolicitud;
    private int idInquilino;
    private String nombreSolicitud;
    private String descripcionSolicitud;
    private String fechaSolicitud;
    private String estadoSolicitud;
    private int montoSolicitud;
    //DATOS USUARIO
    private String nombreUsu;
    //DATOS CUARTO
    private String codigoCua;
    private String habitacionCua;
    private int costoCua;

    public Solicitudes() {
    }

    public Solicitudes(int idSolicitud, int idInquilino, String nombreSolicitud,
                       String descripcionSolicitud, String fechaSolicitud,
                       String estadoSolicitud, int montoSolicitud, String nombreUsu,
                       String codigoCua, String habitacionCua, int costoCua) {
        this.idSolicitud = idSolicitud;
        this.idInquilino = idInquilino;
        this.nombreSolicitud = nombreSolicitud;
        this.descripcionSolicitud = descripcionSolicitud;
        this.fechaSolicitud = fechaSolicitud;
        this.estadoSolicitud = estadoSolicitud;
        this.montoSolicitud = montoSolicitud;
        this.nombreUsu = nombreUsu;
        this.codigoCua = codigoCua;
        this.habitacionCua = habitacionCua;
        this.costoCua = costoCua;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public String getNombreSolicitud() {
        return nombreSolicitud;
    }

    public void setNombreSolicitud(String nombreSolicitud) {
        this.nombreSolicitud = nombreSolicitud;
    }

    public String getDescripcionSolicitud() {
        return descripcionSolicitud;
    }

    public void setDescripcionSolicitud(String descripcionSolicitud) {
        this.descripcionSolicitud = descripcionSolicitud;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public int getMontoSolicitud() {
        return montoSolicitud;
    }

    public void setMontoSolicitud(int montoSolicitud) {
        this.montoSolicitud = montoSolicitud;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public String getCodigoCua() {
        return codigoCua;
    }

    public void setCodigoCua(String codigoCua) {
        this.codigoCua = codigoCua;
    }

    public String getHabitacionCua() {
        return habitacionCua;
    }

    public void setHabitacionCua(String habitacionCua) {
        this.habitacionCua = habitacionCua;
    }

    public int getCostoCua() {
        return costoCua;
    }

    public void setCostoCua(int costoCua) {
        this.costoCua = costoCua;
    }

    @Override
    public String toString() {
        return "Solicitudes{" +
                "idSolicitud=" + idSolicitud +
                ", idInquilino=" + idInquilino +
                ", nombreSolicitud='" + nombreSolicitud + '\'' +
                ", descripcionSolicitud='" + descripcionSolicitud + '\'' +
                ", fechaSolicitud='" + fechaSolicitud + '\'' +
                ", estadoSolicitud='" + estadoSolicitud + '\'' +
                ", montoSolicitud=" + montoSolicitud +
                ", nombreUsu='" + nombreUsu + '\'' +
                ", codigoCua='" + codigoCua + '\'' +
                ", habitacionCua='" + habitacionCua + '\'' +
                ", costoCua=" + costoCua +
                '}';
    }
}
