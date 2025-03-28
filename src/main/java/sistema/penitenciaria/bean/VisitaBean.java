/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema.penitenciaria.bean;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.model.file.UploadedFile;
import sistema.penitenciaria.dao.VisitaDAO;
import sistema.penitenciaria.enums.Carteirinha;
import sistema.penitenciaria.enums.StatusVisitas;
import static sistema.penitenciaria.enums.StatusVisitas.APROVADA;
import static sistema.penitenciaria.enums.StatusVisitas.PENDENTE;
import static sistema.penitenciaria.enums.TipoVisita.*;
import sistema.penitenciaria.modelo.FotoDocumento;
import sistema.penitenciaria.modelo.Visita;
import sistema.penitenciaria.util.Mensagem;

@Named
@ViewScoped
public class VisitaBean implements Serializable {

    private Visita visita = new Visita();
    private List<Byte[]> fotos = new ArrayList<>();
    private List<Visita> visitas = new ArrayList<>();
    private List<Visita> visitasPendentes = new ArrayList<>();
    private List<Visita> visitasAprovadas = new ArrayList<>();
    private List<String> imagensBase64;
    private String textoDocumentos;
    private String tituloPage;
    private String textoDocumentosVisitante;
    private String pdfPath;

    @EJB
    private VisitaDAO visitaDAO;

    @PostConstruct
    public void init() {
        try {
            visitasPendentes = visitaDAO.listarTodosPorStatusPendente();
            visitasAprovadas = visitaDAO.listarTodosPorStatusAprovado();
            carregarParametro();
            getEstiloStatus();
            getTextoTipoVisita();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao exibir dados: " + e.getMessage());
        }
    }

    public void salvar() {
        if (visita.getId() == null) {
            visitaDAO.salvar(visita); // Isso persiste a visita E as fotos associadas
            Mensagem.MensagemSucesso("Cadastro feito com sucesso!");
        } else {
            visitaDAO.atualizar(visita);
            Mensagem.MensagemSucesso("Cadastro atualizado com sucesso!");
        }
        visita = new Visita(); // Limpa o objeto para evitar dados antigos
    }

    public void carregarParametro() {
        Map<String, String> parametro = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String tipoVisita = parametro.get("tipo");
        String visualizar = parametro.get("visualizar");
        String editar = parametro.get("editar");

        if (tipoVisita != null) {
            if (tipoVisita.equals("social")) {
                System.out.println("Visita Social");
                textoDocumentos = "RG e CPF, Comprovante de residência, Nada consta (Estadual e Federal), Documento do interno com foto (RG, Carteira de trabalho ou Carteira de motorista)";
                tituloPage = "Cadastro - Carteirinha de Visita Social";
                visita.setTipoVisita(SOCIAL);
                visita.setStatusVisitas(PENDENTE);
                visita.setEntregacarteirinha(Carteirinha.PENDENTE);
            } else if (tipoVisita.equals("infantil")) {
                System.out.println("Visita Infantil");
                textoDocumentos = "Certidão de nascimento da criança, documento do PAI ou MÃE (que esteja reclusa), documento da pessoa que ficará responsável por trazer a criança (RG e CPF)";
                tituloPage = "Cadastro - Carteirinha de Visita Infantil";
                visita.setTipoVisita(INFANTIL);
                visita.setStatusVisitas(PENDENTE);
                visita.setEntregacarteirinha(Carteirinha.PENDENTE);
            } else if (tipoVisita.equals("virtual")) {
                System.out.println("Visita Virtual");
                textoDocumentos = "RG e CPF, Comprovante de residência, Nada consta (Estadual e Federal), Documento do interno com foto (RG, Carteira de trabalho ou Carteira de motorista)";
                tituloPage = "Cadastro - Carteirinha de Visita Virtual";
                visita.setTipoVisita(VIRTUAL);
                visita.setStatusVisitas(PENDENTE);
                visita.setEntregacarteirinha(Carteirinha.PENDENTE);
            } else if (tipoVisita.equals("intima")) {
                System.out.println("Visita Intima");
                textoDocumentos = "União estável (Escritura pública no cartório) e exames de teste rápido de sífilis, hepatite B, C e de HIV";
                tituloPage = "Cadastro - Carteirinha de Visita Íntima";
                visita.setTipoVisita(INTIMA);
                visita.setStatusVisitas(PENDENTE);
                visita.setEntregacarteirinha(Carteirinha.PENDENTE);
            }
        }

        if (visualizar != null) {
            visita = visitaDAO.buscarPorId(Long.parseLong(visualizar));
        } else if (editar != null) {
            visita = visitaDAO.buscarPorId(Long.parseLong(editar));
        }
    }
    
    public String getCarregarParametroVisita(){
        Map<String, String> parametro = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String statusVisita = parametro.get("visita");
        String status = null;
        
        if(statusVisita.equals("aprovada")){
            status = "aprovada";
        } else if(statusVisita.equals("pendente")){
            status = "pendente";
        }
        
        return status;
    }
    
    public void carregarVisita(){
        visitas = visitaDAO.listarTodos();
    }

    public void handleFileUpload2(FileUploadEvent event) {
        try {
            // Converte a imagem para byte[]
            byte[] fileContent = event.getFile().getContent();

            // Armazena a imagem no objeto interno
            visita.setFoto3x4(fileContent);

            System.out.println("Imagem carregada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar imagem!");
        }
    }

    public String getImagemBase64() {
        if (visita.getFoto3x4() != null) {
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(visita.getFoto3x4());
        }
        return null; // Se não houver imagem
    }
    
    public void executarAcao(){
        if(visita.getStatusVisitas() == visita.getStatusVisitas().PENDENTE){
            modificarStatusVisita();
        } else {
            visita.setEntregacarteirinha(Carteirinha.CONCLUIDO);
            visitaDAO.atualizar(visita);
            PrimeFaces.current().executeScript("window.open('carteirinha?visualizar=" + visita.getId() + "', '_blank');");
        }
    }

    public void modificarStatusVisita() {
        visita.setStatusVisitas(APROVADA);
        visitaDAO.atualizar(visita);
    }

    public void handleFileUpload(FilesUploadEvent event) {
        try {
            for (UploadedFile file : event.getFiles().getFiles()) { // Obtém a lista de arquivos
                if (file != null) {
                    byte[] fileContent = file.getContent();

                    FotoDocumento fotoDocumento = new FotoDocumento();
                    fotoDocumento.setImagens(fileContent);
                    visita.addFoto(fotoDocumento);

                    System.out.println("Foto adicionada: " + file.getFileName());
                }
            }
            System.out.println("Todas as fotos foram adicionadas!");
            Mensagem.MensagemSucesso("Imagens enviadas com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao adicionar as fotos: " + e.getMessage());
        }
    }

    public List<String> getImagensBase64() {
        if (imagensBase64 == null) {
            imagensBase64 = new ArrayList<>();
            for (FotoDocumento foto : visita.getFotosDocumentos()) {
                if (foto.getImagens() != null && foto.getImagens().length > 0) {
                    imagensBase64.add("data:image/png;base64," + Base64.getEncoder().encodeToString(foto.getImagens()));
                } else {
                    System.out.println("Imagem nula ou vazia para " + foto);
                }
            }
        }
        return imagensBase64;
    }

    public StatusVisitas getEstiloStatus() {
        if (visita.getStatusVisitas().APROVADA.equals("APROVADA")) {
            System.out.println("Verdadeiro, imprimindo correto");
            return StatusVisitas.APROVADA;
        } else {
            System.out.println("Falso, imprimindo pendente");
            return StatusVisitas.PENDENTE;
        }
    }

    public void getTextoTipoVisita() {
        if (visita.getTipoVisita() == visita.getTipoVisita().INFANTIL) {
            System.out.println("É uma visita infantil!!");
            textoDocumentosVisitante = "Certidão de nascimento da criança, documento do PAI ou MÃE (que esteja reclusa), documento da pessoa responsável por trazer a criança (RG e CPF)";
        } else if (visita.getTipoVisita() == visita.getTipoVisita().SOCIAL) {
            System.out.println("É uma visita social");
            textoDocumentosVisitante = "RG e CPF, Comprovante de residência, Nada consta (Estadual e Federal), Documento do interno com foto (RG, Carteira de trabalho ou Carteira de motorista)";
        } else if (visita.getTipoVisita() == visita.getTipoVisita().INTIMA) {
            System.out.println("É uma visita íntima");
            textoDocumentosVisitante = "União estável (Escritura pública no cartório) e exames de teste rápido de sífilis, hepatite B, C e de HIV";
        } else if (visita.getTipoVisita() == visita.getTipoVisita().SOCIAL) {
            System.out.println("É uma visita virtual");
            textoDocumentosVisitante = "RG e CPF, Comprovante de residência, Nada consta (Estadual e Federal), Documento do interno com foto (RG, Carteira de trabalho ou Carteira de motorista)";
        }
    }

    public Visita getVisita() {
        return visita;
    }

    public List<Byte[]> getFotos() {
        return fotos;
    }

    public void setFotos(List<Byte[]> fotos) {
        this.fotos = fotos;
    }

    public String getTextoDocumentos() {
        return textoDocumentos;
    }

    public void setTextoExemplo(String textoDocumentos) {
        this.textoDocumentos = textoDocumentos;
    }

    public VisitaDAO getVisitaDAO() {
        return visitaDAO;
    }

    public void setVisitaDAO(VisitaDAO visitaDAO) {
        this.visitaDAO = visitaDAO;
    }

    public String getTituloPage() {
        return tituloPage;
    }

    public void setTituloPage(String tituloPage) {
        this.tituloPage = tituloPage;
    }

    public List<Visita> getVisitas() {
        return visitas;
    }

    public void setVisitas(List<Visita> visitas) {
        this.visitas = visitas;
    }

    public List<StatusVisitas> getStatusVisita() {
        return Arrays.asList(StatusVisitas.values());
    }

    public String getTextoDocumentosVisitante() {
        return textoDocumentosVisitante;
    }

    public void setTextoDocumentosVisitante(String textoDocumentosVisitante) {
        this.textoDocumentosVisitante = textoDocumentosVisitante;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public List<Visita> getVisitasPendentes() {
        return visitasPendentes;
    }

    public void setVisitasPendentes(List<Visita> visitasPendentes) {
        this.visitasPendentes = visitasPendentes;
    }

    public List<Visita> getVisitasAprovadas() {
        return visitasAprovadas;
    }

    public void setVisitasAprovadas(List<Visita> visitasAprovadas) {
        this.visitasAprovadas = visitasAprovadas;
    }

    
    
}
