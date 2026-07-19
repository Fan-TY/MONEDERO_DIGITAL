package com.utp.AppBanco.pattern.factory;


public interface OperacionFinanciera {

    String ejecutar(double monto, String referencia);

    String getTipo();
}
