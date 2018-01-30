package bean;

import bean.Auteur;
import bean.Categorie;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-30T16:27:54")
@StaticMetamodel(Oeuvre.class)
public class Oeuvre_ { 

    public static volatile SingularAttribute<Oeuvre, String> image;
    public static volatile SingularAttribute<Oeuvre, Date> dateCreation;
    public static volatile SingularAttribute<Oeuvre, String> Description;
    public static volatile SingularAttribute<Oeuvre, Categorie> categorie;
    public static volatile SingularAttribute<Oeuvre, Integer> qte;
    public static volatile SingularAttribute<Oeuvre, String> edition;
    public static volatile SingularAttribute<Oeuvre, Integer> nbtotalemprute;
    public static volatile SingularAttribute<Oeuvre, String> type;
    public static volatile SingularAttribute<Oeuvre, String> nom;
    public static volatile SingularAttribute<Oeuvre, Auteur> auteur;
    public static volatile SingularAttribute<Oeuvre, Integer> rate;
    public static volatile SingularAttribute<Oeuvre, Integer> nbemprute;
    public static volatile SingularAttribute<Oeuvre, Long> id;
    public static volatile SingularAttribute<Oeuvre, String> lang;

}