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
        System.out.println("Intentando login para: " + loginRequest.getEmail());
        Optional<Teacher> teacherOpt = teacherRepository.findByEmail(loginRequest.getEmail());
        System.out.println("¿Es profesor? " + teacherOpt.isPresent());

        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            System.out.println("Contraseña almacenada: " + teacher.getPassword());
            System.out.println("Contraseña recibida: " + loginRequest.getPassword());


            if (loginRequest.getPassword().equals(teacher.getPassword())) {
                if (teacher.getRole() == null) {
                    System.out.println("Rol de profesor es null");
                    throw new CustomException(ErrorCode.ROLE_NOT_FOUND);
                }

                String token = jwtUtils.generateJwtToken(
                        teacher.getEmail(),
                        teacher.getRole().getName(),
                        teacher.getId());
                return Optional.of(authMapper.teacherToJwtResponseDTO(teacher, token));
            } else {
                System.out.println("Contraseña de profesor incorrecta");
            }
        }
        Optional<Student> studentOpt = studentRepository.findByEmail(loginRequest.getEmail());
        System.out.println("¿Es estudiante? " + studentOpt.isPresent());

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            System.out.println("Contraseña almacenada: " + student.getPassword());
            System.out.println("Contraseña recibida: " + loginRequest.getPassword());

            // Comparar contraseñas directamente
            if (loginRequest.getPassword().equals(student.getPassword())) {
                // Verificar si el rol es null
                if (student.getRole() == null) {
                    System.out.println("Rol de estudiante es null");
                    throw new CustomException( ErrorCode.ROLE_NOT_FOUND);
                }

                String token = jwtUtils.generateJwtToken(
                        student.getEmail(),
                        student.getRole().getName(),
                        student.getId());
                return Optional.of(authMapper.studentToJwtResponseDTO(student, token));
            } else {
                System.out.println("Contraseña de estudiante incorrecta");
            }
        }
        System.out.println("Usuario no encontrado o contraseña incorrecta");
        throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
    }
}