package noemicoppotelli.finalbuildweek.payloads;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;
    private String nome;
    private String cognome;
}