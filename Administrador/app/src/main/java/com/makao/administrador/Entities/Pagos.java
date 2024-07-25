package com.makao.administrador.Entities;

public class Pagos {
    private int idPago;
    private int idCuarto;
    private String fechaPago;
    private int montoPago;
    private String rutaFoto;
    private String estadoPago;

    //DATOS NESESARIOS DEL CUARTO
    private String codigoCua;
    private String pisoCua;
    private int costoCua;

    //NESESARIOS DE LA CUOTA
    private String Fecha_Vencimiento;
    private String Estado_Cuo;
    private String Monto_Cuo;

    //NESESARIOS DE LA INQUILIDO
    private int idInquilino;
    private String NombreInqui;

    public Pagos() {
    }

    public Pagos(int idPago, int idCuarto, String fechaPago,
                 int montoPago, String rutaFoto, String estadoPago, String codigoCua,
                 String pisoCua, int costoCua, String fecha_Vencimiento) {
        this.idPago = idPago;
        this.idCuarto = idCuarto;
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
        this.rutaFoto = rutaFoto;
        this.estadoPago = estadoPago;
        this.codigoCua = codigoCua;
        this.pisoCua = pisoCua;
        this.costoCua = costoCua;
        this.Fecha_Vencimiento = fecha_Vencimiento;
    }

    public Pagos(int idPago, int idCuarto, String fechaPago, int montoPago, String rutaFoto,
                 String estadoPago, String codigoCua, String pisoCua, int costoCua,
                 String fecha_Vencimiento, String estado_Cuo) {
        this.idPago = idPago;
        this.idCuarto = idCuarto;
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
        this.rutaFoto = rutaFoto;
        this.estadoPago = estadoPago;
        this.codigoCua = codigoCua;
        this.pisoCua = pisoCua;
        this.costoCua = costoCua;
        Fecha_Vencimiento = fecha_Vencimiento;
        Estado_Cuo = estado_Cuo;
    }

    public Pagos(int idPago, int idCuarto, String fechaPago, int montoPago, String rutaFoto,
                 String estadoPago, String codigoCua, String pisoCua, int costoCua,
                 String fecha_Vencimiento, String estado_Cuo, int idInquilino) {
        this.idPago = idPago;
        this.idCuarto = idCuarto;
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
        this.rutaFoto = rutaFoto;
        this.estadoPago = estadoPago;
        this.codigoCua = codigoCua;
        this.pisoCua = pisoCua;
        this.costoCua = costoCua;
        Fecha_Vencimiento = fecha_Vencimiento;
        Estado_Cuo = estado_Cuo;
        this.idInquilino = idInquilino;
    }

    public Pagos(int idPago, int idCuarto, String fechaPago, int montoPago, String rutaFoto,
                 String estadoPago, String codigoCua, String pisoCua, int costoCua,
                 String fecha_Vencimiento, String estado_Cuo, int idInquilino, String nombreInqui) {
        this.idPago = idPago;
        this.idCuarto = idCuarto;
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
        this.rutaFoto = rutaFoto;
        this.estadoPago = estadoPago;
        this.codigoCua = codigoCua;
        this.pisoCua = pisoCua;
        this.costoCua = costoCua;
        Fecha_Vencimiento = fecha_Vencimiento;
        Estado_Cuo = estado_Cuo;
        this.idInquilino = idInquilino;
        NombreInqui = nombreInqui;
    }

    public Pagos(int idPago, int idCuarto, String fechaPago, int montoPago, String rutaFoto,
                 String estadoPago, String codigoCua, String pisoCua, int costoCua,
                 String fecha_Vencimiento, String estado_Cuo, String monto_Cuo,
                 int idInquilino, String nombreInqui) {
        this.idPago = idPago;
        this.idCuarto = idCuarto;
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
        this.rutaFoto = rutaFoto;
        this.estadoPago = estadoPago;
        this.codigoCua = codigoCua;
        this.pisoCua = pisoCua;
        this.costoCua = costoCua;
        Fecha_Vencimiento = fecha_Vencimiento;
        Estado_Cuo = estado_Cuo;
        Monto_Cuo = monto_Cuo;
        this.idInquilino = idInquilino;
        NombreInqui = nombreInqui;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdCuarto() {
        return idCuarto;
    }

    public void setIdCuarto(int idCuarto) {
        this.idCuarto = idCuarto;
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

    public String getFecha_Vencimiento() {
        return Fecha_Vencimiento;
    }

    public void setFecha_Vencimiento(String fecha_Vencimiento) {
        Fecha_Vencimiento = fecha_Vencimiento;
    }

    public String getEstado_Cuo() {
        return Estado_Cuo;
    }

    public void setEstado_Cuo(String estado_Cuo) {
        Estado_Cuo = estado_Cuo;
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

    public String getMonto_Cuo() {
        return Monto_Cuo;
    }

    public void setMonto_Cuo(String monto_Cuo) {
        Monto_Cuo = monto_Cuo;
    }

    @Override
    public String toString() {
        return "Pagos{" +
                "idPago=" + idPago +
                ", idCuarto=" + idCuarto +
                ", fechaPago='" + fechaPago + '\'' +
                ", montoPago=" + montoPago +
                ", rutaFoto='" + rutaFoto + '\'' +
                ", estadoPago='" + estadoPago + '\'' +
                ", codigoCua='" + codigoCua + '\'' +
                ", pisoCua='" + pisoCua + '\'' +
                ", costoCua=" + costoCua +
                ", Fecha_Vencimiento='" + Fecha_Vencimiento + '\'' +
                ", Estado_Cuo='" + Estado_Cuo + '\'' +
                ", Monto_Cuo='" + Monto_Cuo + '\'' +
                ", idInquilino=" + idInquilino +
                ", NombreInqui='" + NombreInqui + '\'' +
                '}';
    }
}
