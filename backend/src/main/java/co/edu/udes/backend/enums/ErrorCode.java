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
    INVALID_PREREQUISITE_SEMESTER("Prerequisite invalido, o materia en mismo semestre"),
    STUDENT_ALREADY_EXISTS("El estudiante ya existe."),
    STUDENT_NOT_ENROLLED_IN_CAREER("El estudiante no se encuentra matriculado en la carrera."),
    SUBJECT_NOT_IN_CAREER("La materia no se encuentra en la carrera"),
    GROUP_FULL("El grupo esta lleno."),
    STUDENT_ALREADY_ENROLLED("El estudiante ya se encuentra matriculado."),
    SCHEDULE_NOT_FOUND("Horario no encontrado"),
    SUBJECT_ALREADY_ASSIGNED("La materia ya esta asignada a un semestre."),
    PREREQUISITE_NOT_COMPLETED("No ha cursado la materia de pre-requisito"),
    NO_ENROLLED_GROUPS("No hay grupos matriculados."),
    TEACHER_ALREADY_EXISTS("El profesor ya existe"),
    GROUP_WITHOUT_SCHEDULE("El grupo no tiene horarios asignados."),
    TEACHER_WORKLOAD_EXCEEDED("La carga horaria del profesor esta en su limite."),
    GROUP_ALREADY_ASSIGNED_TO_ANOTHER_TEACHER("El grupo ya está asignado a otro profesor."),
    TEACHER_SCHEDULE_CONFLICT("El profesor tiene asignada una clase durante, o a la misma hora."),
    GROUP_SCHEDULE_CONFLICT("El grupo ya tiene un horario asignado a la misma hora, o durante esa hora."),
    STUDENT_NOT_ENROLLED("El estudiante no esta matriculado en ese grupo."),
    NOT_CREATED_EVALUATION("La evaluacion no pudo ser creada."),
    EXIST_RESERVE("");
    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}