package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.login.JwtResponseDTO;
import co.edu.udes.backend.dto.login.LoginRequestDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.auth.AuthMapper;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Teacher;
import co.edu.udes.backend.repositories.StudentRepository;
import co.edu.udes.backend.repositories.TeacherRepository;
import co.edu.udes.backend.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<JwtResponseDTO> login(LoginRequestDTO loginRequest) {

        Optional<Teacher> teacherOpt = teacherRepository.findByEmail(loginRequest.getEmail());
        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            if (loginRequest.getPassword().equals(teacher.getPassword())) {
                String token = jwtUtils.generateJwtToken(
                        teacher.getEmail(),
                        teacher.getRole().getName(),
                        teacher.getId());
                return Optional.of(authMapper.teacherToJwtResponseDTO(teacher, token));
            }
        }

        Optional<Student> studentOpt = studentRepository.findByEmail(loginRequest.getEmail());
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (loginRequest.getPassword().equals(student.getPassword())) {
                String token = jwtUtils.generateJwtToken(
                        student.getEmail(),
                        student.getRole().getName(),
                        student.getId());
                return Optional.of(authMapper.studentToJwtResponseDTO(student, token));
            }
        }


        return Optional.empty();
    }
}