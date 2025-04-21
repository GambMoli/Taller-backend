package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.loan.LoanDTO;
import co.edu.udes.backend.dto.loan.LoanResponseDTO;
import co.edu.udes.backend.mappers.loan.LoanMapper;
import co.edu.udes.backend.models.Loan;

import co.edu.udes.backend.models.Material;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Teacher;
import co.edu.udes.backend.repositories.LoanRepository;
import co.edu.udes.backend.repositories.MaterialRepository;
import co.edu.udes.backend.repositories.StudentRepository;
import co.edu.udes.backend.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final MaterialRepository materialRepository;
    private final LoanMapper loanMapper;

    public LoanService( MaterialRepository materialRepository,StudentRepository studentRepository,TeacherRepository teacherRepository,LoanMapper loanMapper,LoanRepository loanRepository){
        this.loanRepository=loanRepository;
        this.loanMapper=loanMapper;
        this.studentRepository= studentRepository;
        this.materialRepository =materialRepository;
        this.teacherRepository=teacherRepository;
    }

    public List<LoanResponseDTO> getAlls(){
        return loanRepository.findAll().stream()
                .map(loanMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public LoanResponseDTO getOne(long id){
        Loan loanExist= loanRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("EL PRESTAMO NO EXISTE"));
        return loanMapper.toResponseDTO(loanExist);
    }

    public LoanResponseDTO createLoan(LoanDTO loanDTO) {

        // Validación del código de préstamo
        if (loanDTO.getCode() == null || loanDTO.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de préstamo no puede ser nulo o vacío");
        }
        if (loanRepository.existsByCode(loanDTO.getCode())) {
            throw new RuntimeException("Ya existe un préstamo con ese código");
        }

        Loan loan = new Loan();
        loan.setCode(loanDTO.getCode());

        // Validación del ID de material
        if (loanDTO.getMaterialId() == null) {
            throw new IllegalArgumentException("El ID del material no puede ser nulo");
        }
        Material material = materialRepository.findById(loanDTO.getMaterialId())
                .orElseThrow(() -> new RuntimeException("No existe el material con ID: " + loanDTO.getMaterialId()));
        loan.setMaterial(material);

        // Validación del ID de profesor (si existe)
        if (loanDTO.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(loanDTO.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("No existe el profesor con ID: " + loanDTO.getTeacherId()));
            loan.setTeacher(teacher);
        }

        // Validación del ID de estudiante (si existe)
        if (loanDTO.getStudentId() != null) {
            Student student = studentRepository.findById(loanDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("No existe el estudiante con ID: " + loanDTO.getStudentId()));
            loan.setStudent(student);
        }

        // Establecer loanDate (fecha del préstamo) si no está presente
        if (loanDTO.getLoanDate() == null) {
            loan.setLoanDate(LocalDateTime.now()); // Si no se especifica, se usa la fecha actual
        } else {
            loan.setLoanDate(loanDTO.getLoanDate());
        }

        // Calcular el deadline (15 días después del loanDate)
        loan.setDeadline(loan.getLoanDate().plusDays(15));

        // Establecer otros campos como actualReturnDate, returnState y status
        loan.setActualReturnDate(loanDTO.getActualReturnDate());
        loan.setReturnState(loanDTO.getReturnState());
        loan.setStatus(loanDTO.getStatus());

        // Guardar el préstamo en la base de datos
        Loan loanSaved = loanRepository.save(loan);

        // Mapear el préstamo guardado a un DTO de respuesta
        return loanMapper.toResponseDTO(loanSaved);
    }


    public LoanResponseDTO modifyLoan(long id, LoanDTO loanDTO) {

        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el préstamo con ese ID"));


        if (loanDTO.getCode() != null && loanDTO.getCode().equals(existingLoan.getCode())) {
            throw new IllegalArgumentException("El código nuevo no puede ser igual al código actual");
        }


        Material material = null;
        if (loanDTO.getMaterialId() != null) {
            material = materialRepository.findById(loanDTO.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("No existe el material con ID: " + loanDTO.getMaterialId()));
        }

        Teacher teacher = null;
        if (loanDTO.getTeacherId() != null) {
            teacher = teacherRepository.findById(loanDTO.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("No existe el profesor con ID: " + loanDTO.getTeacherId()));
        }

        Student student = null;
        if (loanDTO.getStudentId() != null) {
            student = studentRepository.findById(loanDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("No existe el estudiante con ID: " + loanDTO.getStudentId()));
        }


        existingLoan.setCode(loanDTO.getCode());

        if (material != null) {
            existingLoan.setMaterial(material);
        }

        if (teacher != null) {
            existingLoan.setTeacher(teacher);
        }

        if (student != null) {
            existingLoan.setStudent(student);
        }

        existingLoan.setLoanDate(loanDTO.getLoanDate());
        existingLoan.setDeadline(loanDTO.getDeadline());
        existingLoan.setActualReturnDate(loanDTO.getActualReturnDate());
        existingLoan.setReturnState(loanDTO.getReturnState());
        existingLoan.setStatus(loanDTO.getStatus());

        // Guardar y devolver
        Loan loanModified = loanRepository.save(existingLoan);
        return loanMapper.toResponseDTO(loanModified);
    }

    public void deleteLoan(long id){
         loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el préstamo con ese ID"));
        loanRepository.deleteById(id);
    }
}
