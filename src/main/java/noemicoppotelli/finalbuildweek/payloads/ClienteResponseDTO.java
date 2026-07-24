package noemicoppotelli.finalbuildweek.payloads;

import noemicoppotelli.finalbuildweek.enums.TipoCliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;



public record ClienteResponseDTO(

        Long id,
        String ragioneSociale,
        String email,
        String partitaIva,
        String codiceFiscale,
        LocalDateTime dataInserimento,
        LocalDateTime dataUltimoContatto,
        BigDecimal fatturatoAnnuale,
        String pec,
        String telefono,
        String emailContatto,
        String nomeContatto,
        String cognomeContatto,
        String telefonoContatto,
        String logoAziendale,
        TipoCliente tipoCliente,
        String provincia
) {
}