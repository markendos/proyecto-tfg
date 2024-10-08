package es.upo.witzl.proyectotfg.projects.service;

import es.upo.witzl.proyectotfg.projects.dto.SubjectDto;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.model.Subject;

public interface ISubjectService {

    Subject saveSubject(SubjectDto subjectDto, Project project);

    void deleteSubject(Subject subject);
}
