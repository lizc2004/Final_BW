package noemicoppotelli.finalbuildweek.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ruolo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    public Ruolo(String nome) {
        this.nome = nome;
    }
}