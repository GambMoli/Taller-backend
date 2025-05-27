package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.loan.LoanDTO;
import co.edu.udes.backend.dto.loan.LoanDateDTO;
import co.edu.udes.backend.dto.loan.LoanResponseDTO;
import co.edu.udes.backend.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService){
        this.loanService=loanService;
    }


    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAlls(){
        return ResponseEntity.ok(loanService.getAlls());
    }
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getOne(@PathVariable long id){
        return ResponseEntity.ok(loanService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanDTO loanDTO){
        return ResponseEntity.ok(loanService.createLoan(loanDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> modifyLoan(@PathVariable long id, @RequestBody LoanDateDTO dto){
        return ResponseEntity.ok(loanService.modifyLoan(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LoanResponseDTO>deleteLoan(@PathVariable long id){
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}
