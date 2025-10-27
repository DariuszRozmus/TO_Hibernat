package pl.edu.agh.iisg.to.dao;

import org.hibernate.Session;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.session.SessionService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentDao extends GenericDao<Student> {

    public StudentDao(SessionService sessionService) {
        super(sessionService, Student.class);
    }

    public Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        // TODO - implement ask
//        if (this.findByIndexNumber(indexNumber).isPresent()){
//            return Optional.empty();
//        }
//        Student student = new Student(firstName, lastName, indexNumber);
        return save(new Student(firstName, lastName, indexNumber));
    }

    public List<Student> findAll() {
        // TODO - implement
        Session session = currentSession();
        return session.createQuery("Select s from Student s order by s.lastName").getResultList();
    }

    public Optional<Student> findByIndexNumber(final int indexNumber) {
        // TODO - implement
        try {
            Session session = currentSession();
            return session.createQuery(
                            "SELECT s FROM Student s WHERE s.indexNumber = :indexNumber",
                            Student.class
                    )
                    .setParameter("indexNumber", indexNumber)
                    .uniqueResultOptional();
        } catch (jakarta.persistence.PersistenceException e) {
            System.err.println("Błąd podczas wykonywania zapytania findByIndexNumber: " + e.getMessage());
            return Optional.empty();
        }
    }

}
