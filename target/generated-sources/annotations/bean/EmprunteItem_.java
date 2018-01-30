package bean;

import bean.Emprunte;
import bean.Oeuvre;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-30T16:27:54")
@StaticMetamodel(EmprunteItem.class)
public class EmprunteItem_ { 

    public static volatile SingularAttribute<EmprunteItem, Date> dateDepotPrevue;
    public static volatile SingularAttribute<EmprunteItem, Emprunte> emprute;
    public static volatile SingularAttribute<EmprunteItem, Oeuvre> oeuvre;
    public static volatile SingularAttribute<EmprunteItem, Long> id;
    public static volatile SingularAttribute<EmprunteItem, Date> datedepoteffectif;

}