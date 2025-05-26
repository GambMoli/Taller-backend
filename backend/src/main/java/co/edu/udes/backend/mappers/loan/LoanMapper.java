package co.edu.udes.backend.mappers.loan;

import co.edu.udes.backend.dto.loan.LoanDTO;
import co.edu.udes.backend.dto.loan.LoanResponseDTO;
import co.edu.udes.backend.models.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanMapper {


    @Mapping(target = "material.id", source = "materialId")
    @Mapping(target = "teacher.id", source = "teacherId")
    @Mapping(target = "student.id", source = "studentId")
    Loan toEntity(LoanDTO loanDTO);


    LoanResponseDTO toResponseDTO(Loan loan);
}
