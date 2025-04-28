package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.loan.LoanDTO;
import co.edu.udes.backend.dto.loan.LoanResponseDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
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
                .orElseThrow(()-> new CustomException(ErrorCode.LOAN_NOT_FOUND));
        return loanMapper.toResponseDTO(loanExist);
    }

    public LoanResponseDTO createLoan(LoanDTO loanDTO) {


        if (loanDTO.getCode() == null || loanDTO.getCode().trim().isEmpty()) {
            throw new CustomException(ErrorCode.LOAN_CODE_IS_NULL);
        }
        if (loanRepository.existsByCode(loanDTO.getCode())) {
            throw new CustomException(ErrorCode.LOAN_EXISTS);
        }

        Loan loan = new Loan();
        loan.setCode(loanDTO.getCode());


        if (loanDTO.getMaterialId() == null) {
            throw new CustomException(ErrorCode.CODE_IS_NULL);
        }
        Material material = materialRepository.findById(loanDTO.getMaterialId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MATERIAL));
        loan.setMaterial(material);


        if (loanDTO.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(loanDTO.getTeacherId())
                    .orElseThrow(() -> new  CustomException(ErrorCode.TEACHER_NOT_FOUND));
            loan.setTeacher(teacher);
        }


        if (loanDTO.getStudentId() != null) {
            Student student = studentRepository.findById(loanDTO.getStudentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));
            loan.setStudent(student);
        }


        if (loanDTO.getLoanDate() == null) {
            loan.setLoanDate(LocalDateTime.now());
        } else {
            loan.setLoanDate(loanDTO.getLoanDate());
        }


        loan.setDeadline(loan.getLoanDate().plusDays(15));


        loan.setActualReturnDate(loanDTO.getActualReturnDate());
        loan.setReturnState(loanDTO.getReturnState());
        loan.setStatus(loanDTO.getStatus());


        Loan loanSaved = loanRepository.save(loan);


        return loanMapper.toResponseDTO(loanSaved);
    }


    public LoanResponseDTO modifyLoan(long id, LoanDTO loanDTO) {

        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LOAN_NOT_FOUND));


        if (loanDTO.getCode() != null && loanDTO.getCode().equals(existingLoan.getCode())) {
            throw new CustomException(ErrorCode.LOAN_CODE_IS_SAME);
        }


        Material material = null;
        if (loanDTO.getMaterialId() != null) {
            material = materialRepository.findById(loanDTO.getMaterialId())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MATERIAL));
        }

        Teacher teacher = null;
        if (loanDTO.getTeacherId() != null) {
            teacher = teacherRepository.findById(loanDTO.getTeacherId())
                    .orElseThrow(() -> new  CustomException(ErrorCode.TEACHER_NOT_FOUND));
        }

        Student student = null;
        if (loanDTO.getStudentId() != null) {
            student = studentRepository.findById(loanDTO.getStudentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));
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
                .orElseThrow(() -> new CustomException(ErrorCode.LOAN_NOT_FOUND));
        loanRepository.deleteById(id);
    }
}
