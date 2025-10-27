package pl.edu.agh.iisg.to.repository;

import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.session.TransactionService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class StudentRepository implements Repository<Student>{

    private StudentDao studentDao;
    private TransactionService transactionService;

    public StudentRepository(StudentDao studentDao, TransactionService transactionService){
        this.studentDao = studentDao;
        this.transactionService = transactionService;
    }
    @Override
    public Optional<Student> add(Student student) {
        return studentDao.save(student);
    }

    @Override
    public Optional<Student> getById(int id) {
        return studentDao.findById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public void remove(Student student) {
        Set<Course> courseSet = student.courseSet();
        transactionService.doAsTransaction(() -> {
            for (Course course : courseSet) {
                course.studentSet().remove(student);
            }
            student.courseSet().clear();
            this.studentDao.remove(student);
            return true;
        });
    }
    public List<Student> findAllByCourseName(String courseName){
        return studentDao.currentSession().createQuery(
                "SELECT s FROM Student s JOIN s.courseSet c WHERE c.name = :courseName",
                Student.class)
                .setParameter("courseName", courseName)
                .getResultList();
    }
}
