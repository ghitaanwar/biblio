package bean;

import bean.Etudiant;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-30T16:27:54")
@StaticMetamodel(Reservation.class)
public class Reservation_ { 

    public static volatile SingularAttribute<Reservation, Date> dateRemise;
    public static volatile SingularAttribute<Reservation, Boolean> traiter;
    public static volatile SingularAttribute<Reservation, Long> id;
    public static volatile SingularAttribute<Reservation, Date> dateDemande;
    public static volatile SingularAttribute<Reservation, Etudiant> etudiant;
    public static volatile SingularAttribute<Reservation, Boolean> confirmer;

}