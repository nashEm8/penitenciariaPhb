
package sistema.penitenciaria.dao;

import jakarta.ejb.Stateless;
import sistema.penitenciaria.modelo.Visita;


@Stateless
public class VisitaDAO extends GenericDAO<Visita> {
    
    public VisitaDAO(){
        super(Visita.class);
    }
    
}
