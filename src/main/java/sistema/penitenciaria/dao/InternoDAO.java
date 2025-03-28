
package sistema.penitenciaria.dao;

import jakarta.ejb.Stateless;
import sistema.penitenciaria.modelo.Interno;


@Stateless
public class InternoDAO extends GenericDAO<Interno> {

    public InternoDAO() {
        super(Interno.class);
    }
}