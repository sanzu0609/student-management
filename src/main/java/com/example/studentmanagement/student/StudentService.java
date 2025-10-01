package com.example.studentmanagement.student;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StudentService {

    private final Map<Long, Student> students = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @PostConstruct
    void seedInitialData() {
        students.put(1L, new Student(1L, "Alice Nguyen", "alice@example.com", "Computer Science"));
        students.put(2L, new Student(2L, "Bob Tran", "bob@example.com", "Information Systems"));
        students.put(3L, new Student(3L, "Charlie Pham", "charlie@example.com", "Software Engineering"));
        idGenerator.set(students.keySet().stream().mapToLong(Long::longValue).max().orElse(0L));
    }

    public List<Student> findAll() {
        return students.values().stream()
                .sorted(Comparator.comparingLong(Student::id))
                .toList();
    }

    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(students.get(id));
    }

    public Student create(StudentRequest request) {
        long nextId = idGenerator.incrementAndGet();
        Student student = new Student(nextId, request.fullName(), request.email(), request.major());
        students.put(nextId, student);
        return student;
    }

    public Student update(Long id, StudentRequest request) {
        if (!students.containsKey(id)) {
            throw new NoSuchElementException("Student not found");
        }
        Student updated = new Student(id, request.fullName(), request.email(), request.major());
        students.put(id, updated);
        return updated;
    }

    public void delete(Long id) {
        if (students.remove(id) == null) {
            throw new NoSuchElementException("Student not found");
        }
    }
}
