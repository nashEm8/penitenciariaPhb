package sistema.penitenciaria.bean;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;

@Named
@RequestScoped
public class LoginBean {

    private String username;
    private String password;

    // Injeção de DataSource para a conexão com o banco
    @Resource(lookup = "jdbc/penitenciaria")
    private DataSource dataSource;

    public String login() {
        try {
            // Obter a conexão com o banco de dados
            Connection conn = dataSource.getConnection();
            System.out.println("Conexão com o banco de dados estabelecida.");

            // Certifique-se de limpar espaços em branco no username
            username = username.trim();
            System.out.println("Username fornecido: " + username); // Exibe o valor de username para depuração

            // Consulta para obter a senha hashada do usuário
            String query = "SELECT password FROM users WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username); // Define o parâmetro username na consulta
            ResultSet rs = ps.executeQuery();

            // Verifica se um usuário foi encontrado no banco de dados
            if (rs.next()) {
                // Recupera a senha armazenada como hash no banco de dados
                String storedPasswordHash = rs.getString("password");

                // Exibe o nome de usuário, a senha digitada e a senha armazenada para depuração
                System.out.println("Usuário: " + username);
                System.out.println("Senha digitada: " + password);
                System.out.println("Senha armazenada no banco (hash): " + storedPasswordHash);

                // Comparar a senha fornecida (hash) com a senha armazenada no banco
                if (storedPasswordHash.equals(hashPassword(password))) {
                    System.out.println("Senha certa");
                    // Sucesso no login
                    FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
                    return null;
                } else {
                    // Se a senha estiver incorreta
                    System.out.println("Senha errada");
                }
            } else {
                // Se o usuário não for encontrado
                System.out.println("Usuário não encontrado.");
            }

            // Senha ou usuário inválidos
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login falhou!", "Usuário ou senha inválidos."));
            return null; // Fica na página de login

        } catch (Exception e) {
            // Se ocorrer um erro na autenticação
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de autenticação!", "Ocorreu um erro ao tentar autenticar."));
            e.printStackTrace(); // Exibe o erro no console
            return null;
        }
    }

    // Método para gerar o hash SHA-256 da senha fornecida
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b)); // Converte o byte para hexadecimal
        }
        return hexString.toString(); // Retorna o hash da senha como uma string hexadecimal
    }

    // Getters e Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
