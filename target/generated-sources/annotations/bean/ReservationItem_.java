package bean;

import bean.Oeuvre;
import bean.Reservation;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-30T16:27:54")
@StaticMetamodel(ReservationItem.class)
public class ReservationItem_ { 

    public static volatile SingularAttribute<ReservationItem, Reservation> reservation;
    public static volatile SingularAttribute<ReservationItem, Oeuvre> oeuvre;
    public static volatile SingularAttribute<ReservationItem, Long> id;

}