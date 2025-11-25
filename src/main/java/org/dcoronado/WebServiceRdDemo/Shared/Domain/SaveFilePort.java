package org.dcoronado.WebServiceRdDemo.Shared.Domain;

public interface SaveFilePort {

    String getBasePath();

    void save(String path, byte[] content);

}
