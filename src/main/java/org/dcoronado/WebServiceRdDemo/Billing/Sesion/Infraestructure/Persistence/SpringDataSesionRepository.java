package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Persistence;

import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SpringDataSesionRepository extends JpaRepository<SesionEntity, Long> {

    String FIND_SESION_ACTIVA = """
            SELECT s
            FROM SesionEntity s
            WHERE s.tenantRnc = :rnc
              AND s.ambiente = :ambiente
              AND s.fechaTokenExpiraDgii > :ahora
            ORDER BY s.fechaTokenExpedidoDgii DESC
            """;

    @Query(FIND_SESION_ACTIVA)
    Optional<SesionEntity> findSesionActivaByRnc(
            @Param("rnc") String rnc,
            @Param("ambiente") AmbienteEnum ambiente,
            @Param("ahora") LocalDateTime ahora
    );
}
