package sistema.penitenciaria.servlet;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import sistema.penitenciaria.dao.VisitaDAO;
import sistema.penitenciaria.modelo.Visita;

@WebServlet("/carteirinha")
public class PDFConector extends HttpServlet {

    @EJB
    VisitaDAO visitaDAO;

    Visita visita = new Visita();

    // Método doGet que será acionado ao acessar a URL /carteirinha
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verifica a ação para continuar o processo
        String action = request.getServletPath();
        System.out.println("Caminho do servlet: " + action);

        // Se o caminho for "/carteirinha", chama o método gerarCarteirinha
        if (action.equals("/carteirinha")) {
            gerarCarteirinha(request, response);
            System.out.println("Gerar Carteirinha chamada com sucesso");
        }
    }

    // Método para gerar a carteirinha (ainda não é necessário para exibir o ID)
    protected void gerarCarteirinha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Document documento = new Document();

        try {
            // Captura o parâmetro "visualizar" da URL
            String visualizar = request.getParameter("visualizar");

            // Exibe o parâmetro no console
            System.out.println("ID do visitante: " + visualizar);  // Aqui está sendo exibido no console

            Visita visitaBuscada = visitaDAO.buscarPorId(Long.parseLong(visualizar));
            System.out.println(visitaBuscada);
            
            // Tipo de conteúdo
            response.setContentType("application/pdf");
            // Nome do documento
            response.addHeader("Content-Disposition", "inline; filename=" + "carteirinha.pdf");
            // Criar o documento PDF
            PdfWriter.getInstance(documento, response.getOutputStream());
            // Abrir o documento para gerar o conteúdo
            documento.open();
            
            Font fonteNegrito = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            
            PdfPTable tabelaImagem = new PdfPTable(2);
            tabelaImagem.setWidthPercentage(60);
            tabelaImagem.setWidths(new float[]{1f, 2f}); 
            
            Image image = Image.getInstance(visitaBuscada.getFoto3x4());
            image.scaleToFit(200, 200);         
            PdfPCell imagemCell = new PdfPCell(image, true);
            imagemCell.setBorder(Rectangle.BOX);
            imagemCell.setPadding(4); 
            imagemCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            imagemCell.setBorderWidth(2f);
            tabelaImagem.addCell(imagemCell);
            
            String caminhoImagemLogo = request.getServletContext().getRealPath("/assets/imagens/logoGovPi.png");
            
            if (caminhoImagemLogo != null) {
                Image imageLogo = Image.getInstance(caminhoImagemLogo);
                imageLogo.scaleToFit(100, 100);
                PdfPCell imagemCell2 = new PdfPCell(imageLogo, true);
                imagemCell2.setBorder(Rectangle.BOX);
                imagemCell2.setPadding(2);
                imagemCell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                imagemCell2.setBorderWidth(2f);
                
                tabelaImagem.addCell(imagemCell2);

                documento.add(tabelaImagem);
            }
            
            PdfPTable tabela = new PdfPTable(2);
            tabela.setWidthPercentage(60);
            tabela.setWidths(new float[]{1f, 2f}); // A segunda coluna será maior
            
            PdfPCell col1 = new PdfPCell(new Paragraph("Visitante: ", fonteNegrito));
            col1.setPadding(5);
            col1.setBorderWidth(2f);
            PdfPCell col2 = new PdfPCell(new Paragraph(visitaBuscada.getNome()));
            col2.setPadding(5);
            col2.setBorderWidth(2f);
            
            tabela.addCell(col1);
            tabela.addCell(col2);
            documento.add(tabela);

            PdfPTable tabela2 = new PdfPTable(2);
            tabela2.setWidthPercentage(60);
            tabela2.setWidths(new float[]{1f, 2f}); // Segunda coluna maior
            PdfPCell col5 = new PdfPCell(new Paragraph("Interno", fonteNegrito));
            col5.setPadding(5);
            col5.setBorderWidth(2f);
            PdfPCell col6 = new PdfPCell(new Paragraph(visitaBuscada.getNomeInterno()));
            col6.setPadding(5);
            col6.setBorderWidth(2f);
            PdfPCell col7 = new PdfPCell(new Paragraph("Parentesco", fonteNegrito));
            col7.setPadding(5);
            col7.setBorderWidth(2f);
            PdfPCell col8 = new PdfPCell(new Paragraph(" " + visitaBuscada.getParentesco()));
            col8.setBorderWidth(2f); 
            PdfPCell col9 = new PdfPCell(new Paragraph("Autorização", fonteNegrito));
            col9.setPadding(5);
            col9.setBorderWidth(2f);
            PdfPCell col10 = new PdfPCell(new Paragraph("Assinatura do diretor"));
            col10.setPadding(5);
            col10.setBorderWidth(2f);

            tabela2.addCell(col5);
            tabela2.addCell(col6);
            tabela2.addCell(col7);
            tabela2.addCell(col8);
            tabela2.addCell(col9);
            tabela2.addCell(col10);

            documento.add(tabela2);

            documento.close();
        } catch (Exception e) {
            System.out.println("Erro ao gerar o PDF: " + e.getMessage());
            documento.close();
        }
    }
}
