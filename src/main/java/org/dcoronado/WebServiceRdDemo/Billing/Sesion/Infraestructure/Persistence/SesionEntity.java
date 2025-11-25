package org.dcoronado.WebServiceRdDemo.Billing.Sesion.Infraestructure.Persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dcoronado.WebServiceRdDemo.Shared.Domain.Enum.AmbienteEnum;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "SesionEntity")
@Table(name = "sesion")
public class SesionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_rnc")
    private String tenantRnc;

    @Enumerated(EnumType.STRING)
    @Column(name = "ambiente")
    private AmbienteEnum ambiente;

    @Column(name = "token_dgii", columnDefinition = "TEXT")
    private String tokenDgii;

    /**
     * Guardado siempre en UTC.
     */
    @Column(name = "fecha_tokenexpedido_dgii")
    private LocalDateTime fechaTokenExpedidoDgii;

    /**
     * Guardado siempre en UTC.
     */
    @Column(name = "fecha_tokenexpira_dgii")
    private LocalDateTime fechaTokenExpiraDgii;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegitro;

    @PrePersist
    public void prePersist() {
        fechaRegitro = LocalDateTime.now();
    }
}
