package com.makao.inquilino.Entities;

public class Cuotas {
    private int idCuota;
    private int idCuarto;
    private String fechaVencimiento;
    private String estadoCuota;
    private String MontoCuo;

    //DATOS PAGOS
    private int idPago;
    private String fechaPago;
    private int montoPago;
    private String rutaFoto;
    private String estadoPago;

    //DATOS NESESARIOS DEL CUARTO
    private String codigoCua;
    private String pisoCua;
    private int costoCua;

    //NESESARIOS DE LA INQUILIDO
    private int idInquilino;
    private String NombreInqui;

    public Cuotas() {
    }

    public Cuotas(int idCuota, int idCuarto, String fechaVencimiento, String estadoCuota,
                  int idPago, String fechaPago, int montoPago, String rutaFoto,
                  String estadoPago, String codigoCua, String pisoCua, int costoCua,
                  int idInquilino, String nombreInqui) {
        this.idCuota = idCuota;
        this.idCuarto = idCuarto;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoCuota = estadoCuota;
        this.idPago = idPago;
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
        this.rutaFoto = rutaFoto;
        this.estadoPago = estadoPago;
        this.codigoCua = codigoCua;
        this.pisoCua = pisoCua;
        this.costoCua = costoCua;
        this.idInquilino = idInquilino;
        NombreInqui = nombreInqui;
    }

    public Cuotas(int idCuota, int idCuarto, String fechaVencimiento, String estadoCuota,
                  String montoCuo, int idPago, String fechaPago, int montoPago, String rutaFoto,
                  String estadoPago, String codigoCua, String pisoCua, int costoCua,
                  int idInquilino, String nombreInqui) {
        this.idCuota = idCuota;
        this.idCuarto = idCuarto;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoCuota = estadoCuota;
        MontoCuo = montoCuo;
        this.idPago = idPago;
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
        this.rutaFoto = rutaFoto;
        this.estadoPago = estadoPago;
        this.codigoCua = codigoCua;
        this.pisoCua = pisoCua;
        this.costoCua = costoCua;
        this.idInquilino = idInquilino;
        NombreInqui = nombreInqui;
    }

    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public int getIdCuarto() {
        return idCuarto;
    }

    public void setIdCuarto(int idCuarto) {
        this.idCuarto = idCuarto;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstadoCuota() {
        return estadoCuota;
    }

    public void setEstadoCuota(String estadoCuota) {
        this.estadoCuota = estadoCuota;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(int montoPago) {
        this.montoPago = montoPago;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getCodigoCua() {
        return codigoCua;
    }

    public void setCodigoCua(String codigoCua) {
        this.codigoCua = codigoCua;
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

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public String getNombreInqui() {
        return NombreInqui;
    }

    public void setNombreInqui(String nombreInqui) {
        NombreInqui = nombreInqui;
    }

    public String getMontoCuo() {
        return MontoCuo;
    }

    public void setMontoCuo(String montoCuo) {
        MontoCuo = montoCuo;
    }

    @Override
    public String toString() {
        return "Cuotas{" +
                "idCuota=" + idCuota +
                ", idCuarto=" + idCuarto +
                ", fechaVencimiento='" + fechaVencimiento + '\'' +
                ", estadoCuota='" + estadoCuota + '\'' +
                ", MontoCuo='" + MontoCuo + '\'' +
                ", idPago=" + idPago +
                ", fechaPago='" + fechaPago + '\'' +
                ", montoPago=" + montoPago +
                ", rutaFoto='" + rutaFoto + '\'' +
                ", estadoPago='" + estadoPago + '\'' +
                ", codigoCua='" + codigoCua + '\'' +
                ", pisoCua='" + pisoCua + '\'' +
                ", costoCua=" + costoCua +
                ", idInquilino=" + idInquilino +
                ", NombreInqui='" + NombreInqui + '\'' +
                '}';
    }
}
