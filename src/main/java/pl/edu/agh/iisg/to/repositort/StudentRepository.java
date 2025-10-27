package pl.edu.agh.iisg.to.repositort;

import pl.edu.agh.iisg.to.model.Student;

import java.util.List;
import java.util.Optional;

public class StudentRepository implements Repository<Student>{

    @Override
    public Optional<Student> add(Student student) {
        return Optional.empty();
    }

    @Override
    public Optional<Student> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Student> findAll() {
        return List.of();
    }

    @Override
    public void remove(Student student) {

    }
}
