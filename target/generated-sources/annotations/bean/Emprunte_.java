package bean;

import bean.Etudiant;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-30T16:27:54")
@StaticMetamodel(Emprunte.class)
public class Emprunte_ { 

    public static volatile SingularAttribute<Emprunte, Date> dateRemise;
    public static volatile SingularAttribute<Emprunte, Long> id;
    public static volatile SingularAttribute<Emprunte, Date> dateDemande;
    public static volatile SingularAttribute<Emprunte, Etudiant> etudiant;

}