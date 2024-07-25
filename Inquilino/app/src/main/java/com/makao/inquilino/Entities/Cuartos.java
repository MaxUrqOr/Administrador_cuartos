package com.makao.inquilino.Entities;

public class Cuartos {
    private int idCuarto;
    private int idInquilino;
    private String codigoCua;
    private String tamanoCua;
    private String pisoCua;
    private int costoCua;
    private String Estado_Cua;

    //DATOS INQUILINOS
    private String DniInq;
    private String NombreInq;
    private String TelefonoInq;
    private String FechaIngresoInq;
    public Cuartos() {
    }

    public Cuartos(int idCuarto, int idInquilino, String codigoCua, String tamanoCua,
                   String pisoCua, int costoCua, String estado_Cua) {
        this.idCuarto = idCuarto;
        this.idInquilino = idInquilino;
        this.codigoCua = codigoCua;
        this.tamanoCua = tamanoCua;
        this.pisoCua = pisoCua;
        this.costoCua = costoCua;
        Estado_Cua = estado_Cua;
    }

    public Cuartos(int idCuarto, int idInquilino, String codigoCua, String tamanoCua,
                   String pisoCua, int costoCua, String estado_Cua, String dniInq,
                   String nombreInq, String telefonoInq, String fechaIngresoInq) {
        this.idCuarto = idCuarto;
        this.idInquilino = idInquilino;
        this.codigoCua = codigoCua;
        this.tamanoCua = tamanoCua;
        this.pisoCua = pisoCua;
        this.costoCua = costoCua;
        Estado_Cua = estado_Cua;
        DniInq = dniInq;
        NombreInq = nombreInq;
        TelefonoInq = telefonoInq;
        FechaIngresoInq = fechaIngresoInq;
    }

    public int getIdCuarto() {
        return idCuarto;
    }

    public void setIdCuarto(int idCuarto) {
        this.idCuarto = idCuarto;
    }

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public String getCodigoCua() {
        return codigoCua;
    }

    public void setCodigoCua(String codigoCua) {
        this.codigoCua = codigoCua;
    }

    public String getTamanoCua() {
        return tamanoCua;
    }

    public void setTamanoCua(String tamanoCua) {
        this.tamanoCua = tamanoCua;
    }

    public String getPisoCua() {
        return pisoCua;
    }

    public void setPisoCua(String pisoCua) {
        this.pisoCua = pisoCua;
    }

    public int getCostoCua() {
        return costoCua;
    }

    public void setCostoCua(int costoCua) {
        this.costoCua = costoCua;
    }

    public String getEstado_Cua() {
        return Estado_Cua;
    }

    public void setEstado_Cua(String estado_Cua) {
        Estado_Cua = estado_Cua;
    }

    public String getDniInq() {
        return DniInq;
    }

    public void setDniInq(String dniInq) {
        DniInq = dniInq;
    }

    public String getNombreInq() {
        return NombreInq;
    }

    public void setNombreInq(String nombreInq) {
        NombreInq = nombreInq;
    }

    public String getTelefonoInq() {
        return TelefonoInq;
    }

    public void setTelefonoInq(String telefonoInq) {
        TelefonoInq = telefonoInq;
    }

    public String getFechaIngresoInq() {
        return FechaIngresoInq;
    }

    public void setFechaIngresoInq(String fechaIngresoInq) {
        FechaIngresoInq = fechaIngresoInq;
    }

    @Override
    public String toString() {
        return "Cuartos{" +
                "idCuarto=" + idCuarto +
                ", idInquilino=" + idInquilino +
                ", codigoCua='" + codigoCua + '\'' +
                ", tamanoCua='" + tamanoCua + '\'' +
                ", pisoCua='" + pisoCua + '\'' +
                ", costoCua=" + costoCua +
                ", Estado_Cua='" + Estado_Cua + '\'' +
                ", DniInq='" + DniInq + '\'' +
                ", NombreInq='" + NombreInq + '\'' +
                ", TelefonoInq='" + TelefonoInq + '\'' +
                ", FechaIngresoInq='" + FechaIngresoInq + '\'' +
                '}';
    }
}
