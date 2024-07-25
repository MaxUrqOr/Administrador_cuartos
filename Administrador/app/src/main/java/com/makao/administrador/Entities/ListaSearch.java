package com.makao.administrador.Entities;

public class ListaSearch {
    private int IDC;
    private String Cuarto;

    public ListaSearch() {
    }

    public ListaSearch(int IDC, String cuarto) {
        this.IDC = IDC;
        Cuarto = cuarto;
    }

    public int getIDC() {
        return IDC;
    }

    public void setIDC(int IDC) {
        this.IDC = IDC;
    }

    public String getCuarto() {
        return Cuarto;
    }

    public void setCuarto(String cuarto) {
        Cuarto = cuarto;
    }

    @Override
    public String toString() {
        return "ListaSearch{" +
                "IDC=" + IDC +
                ", Cuarto='" + Cuarto + '\'' +
                '}';
    }
}
