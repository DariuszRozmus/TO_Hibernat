package pl.edu.agh.iisg.to.service;

import pl.edu.agh.iisg.to.dao.CourseDao;
import pl.edu.agh.iisg.to.dao.GradeDao;
import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.repository.StudentRepository;
import pl.edu.agh.iisg.to.session.TransactionService;

import java.util.*;

public class SchoolService {

    private final TransactionService transactionService;

    private final StudentDao studentDao;

    private final CourseDao courseDao;

    private final GradeDao gradeDao;

    private final StudentRepository studentRepository;

    public SchoolService(TransactionService transactionService, StudentDao studentDao, CourseDao courseDao, GradeDao gradeDao) {
        this.transactionService = transactionService;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.gradeDao = gradeDao;
        this.studentRepository = new StudentRepository(studentDao, transactionService);
    }

    public boolean enrollStudent(final Course course, final Student student) {
        // TODO - implement
        return transactionService.doAsTransaction(() -> {
            if(course.studentSet().contains(student)) {
                return false;
            }
            course.studentSet().add(student);
            student.courseSet().add(course);
            return true;
        }).orElse(false);
    }

    public boolean removeStudent(int indexNumber) {
        studentRepository.remove(studentDao.findByIndexNumber(indexNumber).get());
        return true;
    }
//        // TODO - implement
//        Optional<Student> optionalStudent = this.studentDao.findByIndexNumber(indexNumber);
//        if(optionalStudent.isEmpty()) {
//            return false;
//        }
//        Student student = optionalStudent.get();
//        Set<Course> courseSet = student.courseSet();
//        return transactionService.doAsTransaction(() -> {
//            for (Course course : courseSet) {
//                if (!course.studentSet().contains(student)) {
//                    return false;
//                }
//                course.studentSet().remove(student);
//            }
//            student.courseSet().clear();
//            this.studentDao.remove(student);
//            return true;
//        }).orElse(false);
//    }

    public boolean gradeStudent(final Student student, final Course course, final float gradeValue) {
        // TODO - implement
        if(student == null || course == null){
            return false;
        }
        return transactionService.doAsTransaction(() -> {
                Grade grade = new Grade(student, course, gradeValue);
                student.gradeSet().add(grade);
                course.gradeSet().add(grade);
                this.gradeDao.save(grade);
                return true;
        }).orElse(false);
    }

    public Map<String, List<Float>> getStudentGrades(String courseName) {
        Map<String, List<Float>> map = new HashMap<>();

        Optional<Course> optionalCourse = courseDao.findByName(courseName);
        if (optionalCourse.isEmpty()) {
            return map;
        }

        Course course = optionalCourse.get();
        List<Student> studentList = studentDao.findAll();

        for (Student student : studentList) {
            List<Float> grades = student.gradeSet().stream().map(Grade::grade).toList();
            if (!grades.isEmpty()) {
                map.put(student.fullName(), grades);
            }
        }

        return map;
    }

}
