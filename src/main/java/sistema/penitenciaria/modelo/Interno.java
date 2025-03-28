/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema.penitenciaria.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import sistema.penitenciaria.enums.NumeroCela;
import sistema.penitenciaria.enums.Pavilhao;
import sistema.penitenciaria.enums.Sexo;
import sistema.penitenciaria.util.Identifiable;


@Entity
@Table(name = "internos")
public class Interno implements Serializable, Identifiable{
    
    @Id
    @SequenceGenerator(name = "seq_internos", sequenceName = "seq_internos", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_internos")
    private Long id;
    
    @Lob 
    private byte[] foto;
    
    @Column(length = 200)
    private String nome;
    
    private String cpf;

    @Column(length = 200)
    private String nomeMae;
    
    @Column(nullable = false)
    private Date dataEntrada;
    
    @Column(length = 100)
    private String naturalidade;
   
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    
    @Enumerated(EnumType.STRING)
    private Pavilhao pavilhao;
    
    @Enumerated(EnumType.STRING)
    private NumeroCela numeroCela;
    
    

    public Interno() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
    
    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }
    
    public Pavilhao getPavilhao() {
        return pavilhao;
    }

    public void setPavilhao(Pavilhao pavilhao) {
        this.pavilhao = pavilhao;
    }

    public NumeroCela getNumeroCela() {
        return numeroCela;
    }

    public void setNumeroCela(NumeroCela numeroCela) {
        this.numeroCela = numeroCela;
    }
    
    public String getImagemBase64() {
        if (foto != null) {
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(getFoto());
        }
        return null; // Se não houver imagem
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Arrays.hashCode(this.foto);
        hash = 59 * hash + Objects.hashCode(this.nome);
        hash = 59 * hash + Objects.hashCode(this.cpf);
        hash = 59 * hash + Objects.hashCode(this.nomeMae);
        hash = 59 * hash + Objects.hashCode(this.dataEntrada);
        hash = 59 * hash + Objects.hashCode(this.naturalidade);
        hash = 59 * hash + Objects.hashCode(this.pavilhao);
        hash = 59 * hash + Objects.hashCode(this.numeroCela);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Interno other = (Interno) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.cpf, other.cpf)) {
            return false;
        }
        if (!Objects.equals(this.nomeMae, other.nomeMae)) {
            return false;
        }
        if (!Objects.equals(this.naturalidade, other.naturalidade)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Arrays.equals(this.foto, other.foto)) {
            return false;
        }
        if (!Objects.equals(this.dataEntrada, other.dataEntrada)) {
            return false;
        }
        if (this.pavilhao != other.pavilhao) {
            return false;
        }
        return this.numeroCela == other.numeroCela;
    }

}
