package noemicoppotelli.finalbuildweek.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import noemicoppotelli.finalbuildweek.enums.TipoCliente;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRequestDTO {

    @NotBlank
    private String ragioneSociale;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String partitaIva;

    @NotBlank
    private String codiceFiscale;

    private LocalDate dataInserimento;

    private LocalDate dataUltimoContatto;

    @NotNull
    private BigDecimal fatturatoAnnuale;

    private String pec;

    private String telefono;

    private String emailContatto;

    private String nomeContatto;

    private String cognomeContatto;

    private String telefonoContatto;

    @NotNull
    private TipoCliente tipoCliente;

    @NotBlank
    private String via;

    @NotBlank
    private String civico;

    @NotBlank
    private String cap;

    @NotNull
    private Long comuneId;
}