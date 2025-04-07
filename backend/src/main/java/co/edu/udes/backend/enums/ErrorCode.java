package co.edu.udes.backend.enums;

public enum ErrorCode {
    STUDENT_NOT_FOUND("Estudiante no encontrado"),
    TEACHER_NOT_FOUND("Profesor no encontrado"),
    SUBJECT_NOT_FOUND("Asignatura no encontrada"),
    GROUP_NOT_FOUND("Grupo no encontrado"),
    GROUP_ALREADY_EXISTS("Ya existe un grupo con este nombre para esta materia"),
    ALREADY_ENROLLED("Ya estás inscrito en esta materia"),
    PREREQUISITE_NOT_FOUND("No tienes registro del prerrequisito"),
    PREREQUISITE_NOT_APPROVED("No has aprobado el prerrequisito"),
    CAREER_NOT_FOUND("Carrera no encontrada"),
    CAREER_ALREADY_EXISTS("La carrera ya existe."),
    SUBJECT_ALREADY_EXISTS("La materia ya existe."),
    SEMESTER_NOT_FOUND("Semestre no encontrado"),
    SEMESTER_HAS_SUBJECTS("El semestre contiene materias"),
    SEMESTER_ALREADY_EXISTS("El semestre ya existe"),
    INVALID_PREREQUISITE_SEMESTER("Prerequisite invalido, o materia en mismo semestre");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}