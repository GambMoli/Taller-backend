package co.edu.udes.backend.mappers.academic;

import co.edu.udes.backend.dto.student.*;
import co.edu.udes.backend.models.*;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AcademicMapper {

    public StudentSemesterDTO mapStudentSemesters(Student student, List<Semester> semesters, Map<Long, List<Grade>> allGrades) {
        StudentSemesterDTO dto = new StudentSemesterDTO();
        dto.setStudentId(student.getId());
        dto.setStudentName(student.getName());

        List<SemesterInfoDTO> semesterInfoList = new ArrayList<>();
        double totalSemesterGrades = 0;

        for (Semester semester : semesters) {
            SemesterInfoDTO semesterInfo = mapSemesterInfo(student, semester, allGrades);
            semesterInfoList.add(semesterInfo);

            if (semesterInfo.getFinalGrade() != null) {
                totalSemesterGrades += semesterInfo.getFinalGrade();
            }
        }

        dto.setSemesters(semesterInfoList);

        // Calculate average grade across all semesters
        if (!semesterInfoList.isEmpty()) {
            dto.setAverageGrade(totalSemesterGrades / semesterInfoList.size());
        }

        return dto;
    }

    public SemesterInfoDTO mapSemesterInfo(Student student, Semester semester, Map<Long, List<Grade>> allGrades) {
        SemesterInfoDTO semesterInfo = new SemesterInfoDTO();
        semesterInfo.setSemesterId(semester.getId());
        semesterInfo.setSemesterNumber(semester.getNumber());
        semesterInfo.setSemesterName(semester.getNumber() + "° Semestre");

        List<PeriodInfoDTO> periodInfoList = new ArrayList<>();

        // Get all student grades for subjects in this semester
        Set<Subject> semesterSubjects = new HashSet<>(semester.getSubjects());

        // Collect total credits for calculating weighted average
        int totalCredits = semesterSubjects.stream()
                .mapToInt(Subject::getCredits)
                .sum();

        double weightedSum = 0;

        for (Period period : semester.getPeriods()) {
            PeriodInfoDTO periodInfo = mapPeriodInfo(student, period, semesterSubjects, allGrades);
            periodInfoList.add(periodInfo);
        }

        semesterInfo.setPeriods(periodInfoList);

        // Calculate final semester grade based on period weights and subject credits
        double finalGrade = calculateFinalSemesterGrade(periodInfoList, semesterSubjects);
        semesterInfo.setFinalGrade(finalGrade);
        semesterInfo.setApproved(finalGrade >= 3.0); // Assuming passing grade is 3.0

        return semesterInfo;
    }

    public PeriodInfoDTO mapPeriodInfo(Student student, Period period, Set<Subject> semesterSubjects, Map<Long, List<Grade>> allGrades) {
        PeriodInfoDTO periodInfo = new PeriodInfoDTO();
        periodInfo.setPeriodId(period.getId());
        periodInfo.setPeriodName(period.getName());
        periodInfo.setWeight(period.getWeight());

        Map<String, SubjectGradeInfo> subjectGrades = new HashMap<>();

        for (Subject subject : semesterSubjects) {
            // Find grades for this student, subject, and period
            List<Grade> subjectGradesList = allGrades.getOrDefault(period.getId(), Collections.emptyList())
                    .stream()
                    .filter(g -> g.getSubject().getId().equals(subject.getId()) &&
                            g.getStudent().getId().equals(student.getId()))
                    .collect(Collectors.toList());

            // Calculate average grade for this subject in this period
            double avgGrade = 0.0;
            if (!subjectGradesList.isEmpty()) {
                avgGrade = subjectGradesList.stream()
                        .mapToDouble(Grade::getValue)
                        .average()
                        .orElse(0.0);
            }

            SubjectGradeInfo subjectInfo = new SubjectGradeInfo();
            subjectInfo.setSubjectId(subject.getId());
            subjectInfo.setSubjectName(subject.getName());
            subjectInfo.setCredits(subject.getCredits());
            subjectInfo.setGrade(avgGrade);
            subjectInfo.setApproved(avgGrade >= 3.0); // Assuming passing grade is 3.0

            subjectGrades.put(subject.getName(), subjectInfo);
        }

        periodInfo.setSubjects(subjectGrades);

        return periodInfo;
    }

    private double calculateFinalSemesterGrade(List<PeriodInfoDTO> periods, Set<Subject> subjects) {
        // Create map of subject to weighted average grade across periods
        Map<Long, WeightedGrade> subjectFinalGrades = new HashMap<>();

        // Initialize with all subjects
        for (Subject subject : subjects) {
            subjectFinalGrades.put(subject.getId(), new WeightedGrade(subject.getCredits()));
        }

        // Add period grades with weights
        for (PeriodInfoDTO period : periods) {
            Double periodWeight = period.getWeight();

            for (SubjectGradeInfo subjectGrade : period.getSubjects().values()) {
                WeightedGrade wg = subjectFinalGrades.get(subjectGrade.getSubjectId());
                if (wg != null && subjectGrade.getGrade() != null) {
                    wg.addGrade(subjectGrade.getGrade(), periodWeight);
                }
            }
        }

        // Calculate final weighted average across all subjects
        double totalCredits = 0;
        double totalWeightedGrade = 0;

        for (WeightedGrade wg : subjectFinalGrades.values()) {
            if (wg.hasFinalGrade()) {
                totalWeightedGrade += wg.getFinalGrade() * wg.getCredits();
                totalCredits += wg.getCredits();
            }
        }

        return totalCredits > 0 ? totalWeightedGrade / totalCredits : 0;
    }

    // Helper class for calculating weighted grades
    private static class WeightedGrade {
        private double totalWeight = 0;
        private double weightedSum = 0;
        private final int credits;

        public WeightedGrade(int credits) {
            this.credits = credits;
        }

        public void addGrade(double grade, double weight) {
            weightedSum += grade * weight;
            totalWeight += weight;
        }

        public boolean hasFinalGrade() {
            return totalWeight > 0;
        }

        public double getFinalGrade() {
            return totalWeight > 0 ? weightedSum / totalWeight : 0;
        }

        public int getCredits() {
            return credits;
        }
    }
}