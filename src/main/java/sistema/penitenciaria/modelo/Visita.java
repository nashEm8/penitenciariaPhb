
package sistema.penitenciaria.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sistema.penitenciaria.enums.Carteirinha;
import sistema.penitenciaria.enums.StatusVisitas;
import sistema.penitenciaria.enums.TipoVisita;
import sistema.penitenciaria.util.Identifiable;


@Entity
@Table(name = "visita")
public class Visita implements Serializable, Identifiable{
    
    @Id
    @SequenceGenerator(name = "seq_visitas", sequenceName = "seq_visitas", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", length = 100)
    private String nome;
    @Column(name = "nome_interno", length = 100)
    private String nomeInterno;
    @Column(name = "parentesco", length = 50)
    private String parentesco;
    @Enumerated(EnumType.STRING)
    private Carteirinha Entregacarteirinha;
    @Lob 
    private byte[] foto3x4;
    @Column(name = "telefone", length = 15)
    private String telefone;
    @Enumerated(EnumType.STRING)
    private TipoVisita tipoVisita;
    @Enumerated(EnumType.STRING)
    private StatusVisitas statusVisitas;
    
    @OneToMany(mappedBy = "visita", cascade = CascadeType.ALL)
    private List<FotoDocumento> fotosDocumentos = new ArrayList<>();
     

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoVisita getTipoVisita() {
        return tipoVisita;
    }

    public void setTipoVisita(TipoVisita tipoVisita) {
        this.tipoVisita = tipoVisita;
    }
  
    public List<FotoDocumento> getFotosDocumentos() {
        return fotosDocumentos;
    }

    public void setFotosDocumentos(List<FotoDocumento> fotosDocumentos) {
        this.fotosDocumentos = fotosDocumentos;
    }
    
    public void addFoto(FotoDocumento foto){
        this.fotosDocumentos.add(foto);
        foto.setVisita(this);
    }

    public StatusVisitas getStatusVisitas() {
        return statusVisitas;
    }

    public void setStatusVisitas(StatusVisitas statusVisitas) {
        this.statusVisitas = statusVisitas;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public byte[] getFoto3x4() {
        return foto3x4;
    }

    public void setFoto3x4(byte[] foto3x4) {
        this.foto3x4 = foto3x4;
    }

    public String getNomeInterno() {
        return nomeInterno;
    }

    public void setNomeInterno(String nomeInterno) {
        this.nomeInterno = nomeInterno;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public Carteirinha getEntregacarteirinha() {
        return Entregacarteirinha;
    }

    public void setEntregacarteirinha(Carteirinha Entregacarteirinha) {
        this.Entregacarteirinha = Entregacarteirinha;
    }

    @Override
    public String toString() {
        return "Visita{" + "id=" + id + ", nome=" + nome + ", foto3x4=" + foto3x4 + ", telefone=" + telefone + ", tipoVisita=" + tipoVisita + ", statusVisitas=" + statusVisitas + ", fotosDocumentos=" + fotosDocumentos + '}';
    }
    
    
    
}
