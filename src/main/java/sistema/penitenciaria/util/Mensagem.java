
package sistema.penitenciaria.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public class Mensagem {
    
    public static void MensagemSucesso(String mensagem){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem));
    }
    
    public static void MensagemErro(String mensagem){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", mensagem));
    }  
  
}
