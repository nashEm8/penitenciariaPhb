
package sistema.penitenciaria.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "foto_documentos")
public class FotoDocumento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    private byte[] imagens;
    
    @ManyToOne
    @JoinColumn(name = "visita_id")
    private Visita visita;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImagens() {
        return imagens;
    }

    public void setImagens(byte[] imagensDocumentos) {
        this.imagens = imagensDocumentos;
    }

    public Visita getVisita() {
        return visita;
    }

    public void setVisita(Visita visita) {
        this.visita = visita;
    }
    
}
