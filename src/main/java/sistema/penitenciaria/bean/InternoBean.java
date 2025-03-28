package sistema.penitenciaria.bean;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.primefaces.event.FileUploadEvent;
import sistema.penitenciaria.dao.InternoDAO;
import sistema.penitenciaria.enums.NumeroCela;
import sistema.penitenciaria.enums.Pavilhao;
import sistema.penitenciaria.enums.Sexo;
import sistema.penitenciaria.modelo.Interno;
import sistema.penitenciaria.util.Mensagem;

@Named
@ViewScoped
public class InternoBean implements Serializable {

    private Interno interno = new Interno();
    private String nome;
    private String nomeMae;
    private List<Interno> internos;
    private String btnType;
    private byte[] foto;
    private String tituloPage;
    private String textoPage;

    @EJB
    private InternoDAO internoDAO;


    @PostConstruct
    public void init() {
        try {
            internos = internoDAO.listarTodos();
            carregarParametro();
            System.out.println("Método init() executado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace(); // Exibe o erro no console
            System.err.println("Erro ao executar init(): " + e.getMessage());
        }
    }

    public void salvar() {
        if (interno.getId() == null) {
            internoDAO.salvar(interno);
            System.out.println("Interno salvo");
            Mensagem.MensagemSucesso("Cadastro realizado com sucesso!");
        } else {
            internoDAO.atualizar(interno);
            System.out.println("Atualizando");
            Mensagem.MensagemSucesso("Os dados foram atualizados corretamente!");
        }
        interno = new Interno();
    }

    public void carregarParametro() {
        Map<String, String> parametro = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String visualizar = parametro.get("visualizar");
        String editar = parametro.get("editar");

        if (visualizar != null) {
            interno = internoDAO.buscarPorId(Long.valueOf(visualizar));
        } else if (editar != null) {
            interno = internoDAO.buscarPorId(Long.valueOf(editar));
            tituloPage = "Editar dados da interno";
            textoPage = "Preencha os novos dados que deseja atualizar e depois clique no botão 𝘀𝗮𝗹𝘃𝗮𝗿 para salvar os novos dados";
            btnType = "Salvar alterações";
        } else {
            tituloPage = "Cadastrar de internos";
            textoPage = "Preencha os campos abaixo e após isso, clique no botão 𝗰𝗮𝗱𝗮𝘀𝘁𝗿𝗮𝗿 para inserir o interno no sistema";
            btnType = "Cadastrar";
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            // Converte a imagem para byte[]
            byte[] fileContent = event.getFile().getContent();

            // Armazena a imagem no objeto interno
            interno.setFoto(fileContent);
            
            System.out.println("Imagem carregada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar imagem!");
        }
    }

    public String getImagemBase64() {
        if (interno.getFoto() != null) {
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(interno.getFoto());
        }
        return null; // Se não houver imagem
    }

    public void remover(Interno interno) {
        internoDAO.remover(interno);
        internos = internoDAO.listarTodos();
    }

    public void buscarInternos() {
        internos = internoDAO.buscarInternos(nome, nomeMae);
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

    public List<Interno> getInternos() {
        return internos;
    }

    public void setInternos(List<Interno> internos) {
        this.internos = internos;
    }

    public InternoDAO getInternoDAO() {
        return internoDAO;
    }

    public void setInternoDAO(InternoDAO internoDAO) {
        this.internoDAO = internoDAO;
    }

    public List<Interno> listarTodos() {
        return internoDAO.listarTodos();
    }
    
    public List<Interno> listarPorSexoMasculino(){
        return internoDAO.listarPorSexo(Sexo.M);
    }
    
    public List<Interno> listarPorSexoFeminino(){
        return internoDAO.listarPorSexo(Sexo.F);
    }

    public List<Pavilhao> getPavilhoes() {
        return Arrays.asList(Pavilhao.values()); // Retorna todos os valores do enum
    }

    public List<NumeroCela> getNumerosCela() {
        return Arrays.asList(NumeroCela.values()); // Retorna todos os valores do enum
    }

    public List<Sexo> getSexos() {
        return Arrays.asList(Sexo.values());
    }

    public Interno getInterno() {
        return interno;
    }

    public void setInterno(Interno interno) {
        this.interno = interno;
    }

    public String getBtnType() {
        return btnType;
    }

    public void setBtnType(String btnType) {
        this.btnType = btnType;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getTituloPage() {
        return tituloPage;
    }

    public String getTextoPage() {
        return textoPage;
    }

    public void setTextoPage(String textoPage) {
        this.textoPage = textoPage;
    }
 
    
}
