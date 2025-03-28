package sistema.penitenciaria.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import sistema.penitenciaria.enums.Sexo;
import sistema.penitenciaria.enums.StatusVisitas;
import sistema.penitenciaria.util.Identifiable;

@Stateless
public class GenericDAO<T extends Identifiable> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> entityClass;
    
    public GenericDAO() {
        // Construtor sem parâmetros exigido pelo EJB
    }

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void salvar(T entity) {
        if (entity.getId() == null) {
            // Novo, insere
            entityManager.persist(entity);
            entityManager.flush();
        } else {
            // Existente, atualiza
            entityManager.merge(entity);
        }
    }

    public void atualizar(T entity) {
        entityManager.merge(entity);
    }

    public void remover(T entity) {
        entityManager.remove(entity);
    }

    public List<T> buscarInternos(String nome, String nomeMae) {
        String jpql = "SELECT i FROM Interno i WHERE 1=1";
        if (nome != null && !nome.isEmpty()) {
            jpql += " AND LOWER(i.nome) LIKE LOWER(:nome)";
        }
        if (nomeMae != null && !nomeMae.isEmpty()) {
            jpql += " AND LOWER(i.nomeMae) LIKE LOWER(:nomeMae)";
        }

        TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
        if (nome != null && !nome.isEmpty()) {
            query.setParameter("nome", "%" + nome + "%");
        }
        if (nomeMae != null && !nomeMae.isEmpty()) {
            query.setParameter("nomeMae", "%" + nomeMae + "%");
        }

        return query.getResultList();
    }

    public T buscarPorId(Long id) {
        return entityManager.find(entityClass, id);
    }

    public List<T> listarTodos() {
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e ORDER BY e.nome ASC", entityClass)
                .getResultList();
    }
    
    public List<T> listarTodosPorStatusPendente(){
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.statusVisitas = :status ORDER BY e.nome ASC", entityClass)
                .setParameter("status", StatusVisitas.PENDENTE)
                .getResultList();
    }
    
    public List<T> listarTodosPorStatusAprovado(){
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.statusVisitas = :status ORDER BY e.nome ASC", entityClass)
                .setParameter("status", StatusVisitas.APROVADA)
                .getResultList();
    }
    
    public List<T> listarPorSexo(Sexo sexo) {
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.sexo = :sexo", entityClass)
                .setParameter("sexo", sexo)
                .getResultList();
    }
}
